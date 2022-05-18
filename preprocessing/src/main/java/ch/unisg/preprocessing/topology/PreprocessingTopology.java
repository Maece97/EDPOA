package ch.unisg.preprocessing.topology;


import ch.unisg.preprocessing.dto.Transaction;
import ch.unisg.preprocessing.dto.TransactionWithExchangeRate;
import ch.unisg.preprocessing.dto.TransactionWithExchangeRateAndStatus;
import ch.unisg.preprocessing.serialization.json.TransactionSerdes;

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

        // Incoming Transactions
        KStream<String, Transaction> incomingTransactionStream = 
                builder.stream("incoming-transactions", Consumed.with(Serdes.String(), new TransactionSerdes()));
                incomingTransactionStream.foreach((k,v)-> System.out.println("--> Incoming Transaction" + v));


        //Incoming status update
        //KStream<String,String> statusStream = builder.stream("card-status");
        //statusStream.foreach((k,v)-> System.out.println("Key: "+k+" Value: "+v));

        // Create a table with card status
        KTable<String,String> statusTable =
                builder.table("card-status",Consumed.with(Serdes.String(),Serdes.String()));
        //KStream<String,String> streamm = statusTable.toStream();
        //streamm.foreach((k,v)-> System.out.println("Status: "+k));

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
        transactionStream = transactionStream.selectKey((k,v)->v.getCurrency().toString());

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



        /**ValueJoiner<Transaction, Double,TransactionWithExchangeRate> exchangeRateJoiner =
                 (transaction, exchangeRate) -> new TransactionWithExchangeRate(transaction,exchangeRate.toString());

         KeyValueMapper<String,Transaction,String> keyMapper =
                 (leftKey,transaction) -> {
                         return String.valueOf(transaction.getCurrency());
                 };

         KStream <String,TransactionWithExchangeRate> withExchangeRate =
                 transactionStream.join(exchangeRates, keyMapper, exchangeRateJoiner);
         withExchangeRate.foreach((k,v)-> System.out.println(v));**/

        //Joining with cardStatus
        /**ValueJoiner<TransactionWithExchangeRate,String, TransactionWithExchangeRateAndStatus> statusJoiner =
                (transactionEr, status)-> new TransactionWithExchangeRateAndStatus(transactionEr, status);
        
        KeyValueMapper<String,TransactionWithExchangeRate,String> keyMapperStatus =
                (leftKey, transactionEr) -> {
                        return String.valueOf(transactionEr.getCardNumber());
                };

        KStream <String, TransactionWithExchangeRateAndStatus> transactionAndStatus =
                withExchangeRate.join(statusTable, keyMapperStatus, statusJoiner);**/

      //Joining stuff together
      //Join params
        /**Joined<String,String,String> joinParams =
        Joined.with(Serdes.String(),Serdes.String(),Serdes.String());
      //Value joiner
        ValueJoiner<String, String, StringTest> testJoiner =
                (s, er)-> new StringTest(s,er);
        //need some key mapper to join on another field then the actual key --> skript 09/slide 48
        /**KeyValueMapper<String,String,String> keyMapper =
                (leftKey,string)->{return }*/
        /**KStream<String, StringTest> newStream =
                kStream.join(exchangeRates,testJoiner,joinParams);**/

        //newStream.foreach((k,v)-> System.out.println("Key: "+ k + "Value: "+v));
        //KStream<String,String> upper = newStream.mapValues((v)->v.toUpperCase());
        //upper.foreach((k,v)-> System.out.println("Key: "+ k + "Value: "+v));
        
        // System.out.println("--> Outgoing Transaction");

        return builder.build();

    }
}
