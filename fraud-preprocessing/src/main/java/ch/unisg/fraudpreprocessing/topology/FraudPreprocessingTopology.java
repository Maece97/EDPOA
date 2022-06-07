package ch.unisg.fraudpreprocessing.topology;

import java.time.Duration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.Suppressed.BufferConfig;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;

import ch.unisg.fraudpreprocessing.json.TransactionTimestampExtractor;
import ch.unisg.fraudpreprocessing.serialization.avro.AvroSerdes;
import ch.unisg.model.FilteredTransaction;

public class FraudPreprocessingTopology {

    public static Topology build(){
        //construct the topology
        var builder = new StreamsBuilder();

        

        KStream<String, FilteredTransaction> incomingTransactionStream =
            builder.stream("transaction-filtered", Consumed.with(Serdes.String(), 
                AvroSerdes.FilteredTransaction("http://localhost:8081", false))
                    .withTimestampExtractor(new TransactionTimestampExtractor()))
                         .map((key, value) -> new KeyValue<>(
                                 value.getCardNumber().toString(),
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

        filteredTransactionCounts.toStream().to("transaction-fraud-counts");

        return builder.build();
    }
}
