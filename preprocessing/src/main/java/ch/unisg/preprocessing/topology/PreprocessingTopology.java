package ch.unisg.preprocessing.topology;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.tomcat.jni.Global;

public class PreprocessingTopology {

    public static Topology build(){

        //construct the topology
        StreamsBuilder builder = new StreamsBuilder();
        //create global currency exchange table
        System.out.println("Incoming transactions");
     KStream<String,String> kStream = builder.stream("incoming-transactions");
        kStream.foreach((k,v)-> System.out.println("Key: "+k+" Value: "+v));

        System.out.println("incoming exachnge rates");
      KTable<String, String> exchangeRates =
                builder.table("exchange-rates",Consumed.with(Serdes.String(),Serdes.String()));
      KStream<String,String> stream = exchangeRates.toStream();
      stream.foreach((k,v)-> System.out.println("Key: "+ k + "Value: "+v));

      //Joining stuff together


        return builder.build();


    }
}
