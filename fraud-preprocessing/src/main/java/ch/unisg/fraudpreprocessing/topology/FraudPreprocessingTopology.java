package ch.unisg.fraudpreprocessing.topology;

import ch.unisg.fraudpreprocessing.json.TransactionTimestampExtractor;
import ch.unisg.fraudpreprocessing.serialization.avro.AvroSerdes;
import ch.unisg.model.FilteredTransaction;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.time.Duration;

public class FraudPreprocessingTopology {

    public static Topology build(){
        //construct the topology
        var builder = new StreamsBuilder();

        KStream<String, FilteredTransaction> incomingTransactionStream =
            builder.stream("transaction-filtered", Consumed.with(Serdes.String(), 
                AvroSerdes.FilteredTransaction("http://localhost:8081", false)))
                    .withTimestampExtractor(new TransactionTimestampExtractor()))
                         .map((key, value) -> new KeyValue<>(
                                 value.getCardNumber(),
                                 value)
                         );

        incomingTransactionStream.print(Printed.<String, FilteredTransaction>toSysOut().withLabel("transaction-filtered"));

        incomingTransactionStream.foreach((k,v)-> System.out.println("--> Incoming Transaction" + " " + k + " : " + v));

        var tumblingWindow = TimeWindows.of(Duration.ofSeconds(60)).grace(Duration.ofSeconds(5));

        var transactionCounts = incomingTransactionStream
                .groupByKey(Grouped.with(Serdes.String(), AvroSerdes.FilteredTransaction("http://localhost:8081", false)))
                .windowedBy(tumblingWindow)
                .count(Materialized.as("transaction-counts"))
                .suppress(Suppressed.untilWindowCloses(BufferConfig.unbounded().shutDownWhenFull()));

        var filteredTransactionCounts = transactionCounts.filter(
                (key, value) ->
                        value != null && value >= 5
        );

        incomingTransactionStream.to("transaction-fraud-preprocessed", Produced.with(Serdes.String(), AvroSerdes.FilteredTransaction("http://localhost:8081", false)));
        
        transactionCounts.toStream().print(Printed.<Windowed<String>, Long>toSysOut().withLabel("transaction-counts"));

        return builder.build();
    }
}
