package ch.unisg.preprocessing;

import ch.unisg.preprocessing.topology.PreprocessingTopology;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class PreprocessingApplication {

	public static <StreamBuilder> void main(String[] args) {
		SpringApplication.run(PreprocessingApplication.class, args);

		Topology topology = PreprocessingTopology.build();


		// set the required properties for running Kafka Streams
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "preprocessing-app");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

		/**StreamsBuilder streamBuilder = new StreamsBuilder();
			 GlobalKTable<String, String> exchangeRates =
				streamBuilder.globalTable("exchange-rates", Consumed.with(Serdes.String(),Serdes.String()));
			 exchangeRates.queryableStoreName();


		Topology topology1 = streamBuilder.build();

		KafkaStreams streams = new KafkaStreams(topology1,props);
		streams.start();**/


		//build the topology
		System.out.println("Builing topology");
		KafkaStreams streams = new KafkaStreams(topology, props);
		streams.start();



	}

}
