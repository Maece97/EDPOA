package ch.unisg.preprocessing.topology;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.tomcat.jni.Global;

public class PreprocessingTopology {

    public static Topology build(){

        //construct the topology
        StreamsBuilder builder = new StreamsBuilder();
        //create global currency exchange table
        GlobalKTable<String, Double> exchangeRates =
                builder.globalTable("exchange-rates", Consumed.with(Serdes.String(), Serdes.Double()));
        System.out.println(exchangeRates.queryableStoreName());

        return builder.build();


    }
}
