package ch.unisg.preprocessing.topology;


import ch.unisg.preprocessing.dto.Transaction;
import ch.unisg.preprocessing.models.StringTest;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.tomcat.jni.Global;

import java.util.Locale;

public class PreprocessingTopology {

    public static Topology build(){

        //construct the topology
        StreamsBuilder builder = new StreamsBuilder();
        //create global currency exchange table
        System.out.println("Incoming transactions");
     KStream<String, Transaction> kStream = builder.stream("incoming-transactions");
        kStream.foreach((k,v)-> System.out.println("Key: "+k+" Value: "+v));


        //Incoming status update
        //KStream<String,String> statusStream = builder.stream("card-status");
        //statusStream.foreach((k,v)-> System.out.println("Key: "+k+" Value: "+v));

        //Create a table with card status
        KTable<String,String> statusTable =
                builder.table("card-status",Consumed.with(Serdes.String(),Serdes.String()));

        //Create Table with exchange rates
        GlobalKTable<String, Double> exchangeRates =
                builder.globalTable("exchange-rates",Consumed.with(Serdes.String(),Serdes.Double()));
        System.out.println(exchangeRates);

        //Router


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


        return builder.build();


    }
}
