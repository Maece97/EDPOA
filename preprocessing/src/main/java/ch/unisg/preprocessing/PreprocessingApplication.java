package ch.unisg.preprocessing;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.log4j.BasicConfigurator;

import ch.unisg.preprocessing.topology.PreprocessingTopology;

public class PreprocessingApplication {

	public static void main(String[] args) {
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

		// BasicConfigurator.configure();

		//build the topology
		KafkaStreams streams = new KafkaStreams(topology, props);
		streams.start();

		// close Kafka Streams when the JVM shuts down (e.g. SIGTERM)
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

	}

}
