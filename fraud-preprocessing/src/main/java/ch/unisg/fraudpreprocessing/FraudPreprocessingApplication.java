package ch.unisg.fraudpreprocessing;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.HostInfo;

import java.util.Properties;

import ch.unisg.fraudpreprocessing.topology.FraudPreprocessingTopology;
import ch.unisg.fraudpreprocessing.controller.InteractiveQueries;

public class FraudPreprocessingApplication {

    public static void main(String[] args) {
        var topology = FraudPreprocessingTopology.build();

        var host = "localhost";
        var port = 7069;
        var stateDir = "/tmp/kafka-streams";
        var endpoint = String.format("%s:%s", host, port);

        // set the required properties for running Kafka Streams
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "fraud-preprocessing-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.APPLICATION_SERVER_CONFIG, endpoint);
        props.put(StreamsConfig.STATE_DIR_CONFIG, stateDir);

        //build the topology
        var streams = new KafkaStreams(topology, props);
        streams.start();

        // start the REST service
        HostInfo hostInfo = new HostInfo(host, port);
        var service = new InteractiveQueries(hostInfo, streams);
        service.start();

        // close Kafka Streams when the JVM shuts down (e.g. SIGTERM)
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}
