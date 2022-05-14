package ch.unisg.preprocessing;

import ch.unisg.preprocessing.topology.PreprocessingTopology;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class PreprocessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PreprocessingApplication.class, args);

		Topology topology = PreprocessingTopology.build();


		// set the required properties for running Kafka Streams
		Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "preprocessing-app");
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		//build the topology
		System.out.println("Builing topology");
		KafkaStreams streams = new KafkaStreams(topology, props);
		streams.start();



	}

}
