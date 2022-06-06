package ch.unisg.transaction.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${kafka.bootstrap-address}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }


    //Pin
    @Bean
    public NewTopic checkPinTopic(){return new NewTopic("check-pin",1,(short) 1);};
    @Bean
    public NewTopic checkPinResultTopic(){return new NewTopic("check-pin-result",1,(short) 1);};
     // blocking rules
     @Bean
     public NewTopic checkBlockingTopic(){return new NewTopic("check-blocking",1,(short) 1);};
    @Bean
    public NewTopic checkBlockingResultTopic(){return new NewTopic("check-blocking-result",1,(short) 1);};

    //limit update
    @Bean
    public NewTopic updateLimitTopic(){return new NewTopic("update-limit",1,(short) 1);};

    //forward transaction
    @Bean
    public NewTopic sendTransactionTopic(){return new NewTopic("transaction-postprocessing",1,(short) 1);};
}