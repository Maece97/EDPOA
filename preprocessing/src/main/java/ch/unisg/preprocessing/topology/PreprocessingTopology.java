package ch.unisg.preprocessing.topology;


import ch.unisg.preprocessing.controller.ForwardTransactionRestController;
import ch.unisg.preprocessing.dto.Transaction;
import ch.unisg.preprocessing.dto.TransactionWithExchangeRate;
import ch.unisg.preprocessing.dto.TransactionWithExchangeRateAndStatus;
import ch.unisg.preprocessing.serialization.json.TransactionSerdes;

import ch.unisg.preprocessing.serialization.json.TransactionWithErSerdes;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.util.Locale;

public class PreprocessingTopology {

    public static Topology build(){

        //construct the topology
        StreamsBuilder builder = new StreamsBuilder();
        //forwarder
        ForwardTransactionRestController forwardTransactionRestController = new ForwardTransactionRestController();

        // Incoming Transactions
        KStream<String, Transaction> incomingTransactionStream = 
                builder.stream("incoming-transactions", Consumed.with(Serdes.String(), new TransactionSerdes()));
                incomingTransactionStream.foreach((k,v)-> System.out.println("--> Incoming Transaction" + v));


        //Incoming status update
        // Create a table with card status
        KTable<String,String> statusTable =
                builder.table("card-status",Consumed.with(Serdes.String(),Serdes.String()));
        KStream<String,String> streamm = statusTable.toStream();
        streamm.foreach((k,v)-> System.out.println("Status: "+k + "is: "+v));

        // Create global currency exchange table
        KTable<String, Double> exchangeRates =
                builder.table("exchange-rates",Consumed.with(Serdes.String(),Serdes.Double()));
        KStream<String,Double> stream = exchangeRates.toStream();
        stream.foreach((k,v)-> System.out.println(k+v));

        //Router
        // Filter out creditcards starting with a "3", because they don't belong to us.
        final KStream<String, Transaction>[] branches = incomingTransactionStream.branch(
                (id, transaction) -> transaction.getCardNumber().startsWith("3"),
                (id, transaction) -> !transaction.getCardNumber().startsWith("3"));

        branches[0].to("wrong-card-number", Produced.with(Serdes.String(), new TransactionSerdes()));

        KStream<String, Transaction> transactionStream = branches[1];
        transactionStream.foreach((k,v)-> System.out.println("Before: "+ v.getAmount()+ " "+v.getCurrency()));

        //Joining Exchange Rat
        transactionStream = transactionStream.selectKey((k,v)->v.getCurrency());

        Joined<String, Transaction, Double> erJoinParams =
                Joined.with(Serdes.String(), new TransactionSerdes(),Serdes.Double());

        ValueJoiner<Transaction, Double, TransactionWithExchangeRate> erJoiner =
                (transaction, er) -> {
                    return new TransactionWithExchangeRate(transaction, String.valueOf(er));
                };
        KStream<String, TransactionWithExchangeRate> withEr =
                transactionStream.join(exchangeRates,erJoiner,erJoinParams);


        //Translate currency
        KStream<String,TransactionWithExchangeRate> translated = withEr.mapValues(TransactionWithExchangeRate::exchangeMoney);
        translated.foreach((k,v)-> System.out.println("After: "+ v.getAmount()+" "+v.getCurrency()));

        //TODO: joining status here
        translated = translated.selectKey((k,v)->v.getCardNumber());
        Joined<String,TransactionWithExchangeRate,String> statusJoinParams =
                Joined.with(Serdes.String(), new TransactionWithErSerdes(),Serdes.String());

        ValueJoiner<TransactionWithExchangeRate,String, TransactionWithExchangeRateAndStatus> statusJoiner =
                (transactionWithEr, status)->{
                    return new TransactionWithExchangeRateAndStatus(transactionWithEr,status);
                };
        KStream<String, TransactionWithExchangeRateAndStatus> withErAndStatus =
                translated.join(statusTable, statusJoiner,statusJoinParams);

        withErAndStatus.foreach((k,v)-> System.out.println("Key: "+k+ " Value: "+v));
        //Forward to transaction service
        withErAndStatus.foreach((k,v)->{
            String amount = v.getAmount();
            //Camunda wants to have ints thats why we cut off after comma
            amount = amount.split("[.]")[0];
            forwardTransactionRestController.forwardTransaction("123",amount,v.getPin(),v.getCardNumber(),v.getCountry(),
                    v.getMerchant(),v.getMerchantCategory(),v.getCurrency(),v.getTries(),v.getStatus(),v.getExchangeRate());
        });




        return builder.build();

    }
}
