package ch.unisg.transactionpostprocessing;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

import ch.unisg.model.PostTransaction;
import ch.unisg.model.Transaction;
import ch.unisg.transactionpostprocessing.serialization.avro.AvroSerdes;

// import com.mitchseymour.kafka.serialization.avro.AvroSerdes;

public class TransactionPostprocessingApplication {

	public static void main(String[] args) {
		
		Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "transaction-postprocessing");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		StreamsBuilder builder = new StreamsBuilder();

		// KStream<String, Transaction> originalStream =
        //     builder.stream("transaction-postprocessing", Consumed.with(Serdes.String(), 
        //         AvroSerdes.get(Transaction.class)));
		KStream<String, Transaction> originalStream =
            builder.stream("transaction-postprocessing", Consumed.with(Serdes.String(), 
                AvroSerdes.Transaction("http://localhost:8081", false)));
        
        originalStream.print(Printed.<String, Transaction>toSysOut().withLabel("transaction-postprocessing"));

        KStream<String, PostTransaction> filteredStream = originalStream.mapValues(
            (reading) -> {
                PostTransaction postTransaction = new PostTransaction();
                postTransaction.setAmount(reading.getAmount());
                postTransaction.setCardNumber(reading.getCardNumber());
                postTransaction.setCurrency(reading.getCurrency());
            return postTransaction;
        });
            

        // filteredStream.to("transaction-filtered", Produced.with(Serdes.String(), AvroSerdes.Transaction("http://localhost:8081", false)));

        // build the topology and start streaming
        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.start();

        // close Kafka Streams when the JVM shuts down (e.g. SIGTERM)
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
	}

}
