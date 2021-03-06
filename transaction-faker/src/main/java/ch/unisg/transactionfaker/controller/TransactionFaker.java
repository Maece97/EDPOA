package ch.unisg.transactionfaker.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import com.github.javafaker.Faker;
import com.google.gson.Gson;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.unisg.transactionfaker.Transaction;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/faker")
@RequiredArgsConstructor
public class TransactionFaker {
    

    @PutMapping("/transactions")
    public String startMessageProcess(@RequestBody String body){
        System.out.println(body);

        int fakeTransactions = Integer.parseInt(body);

        if (fakeTransactions < 1) {
            return "Please provide a number greater than 0";
        }

        Faker faker = new Faker();
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();

        for (int i = 0; i < fakeTransactions; i++) {
            Transaction t = new Transaction();
            //determines whether or not the pin is correct (70% correct 30% wrong)
            double randNr = Math.random();
            //The app currently knows these currencies
            String[] currencies = {"EUR", "CHF", "JPY"};
            String[] cardNumbers = {"123456789", "849203850", "302849553"};
            //t.setAmount(faker.commerce().price(0, 1000));
            t.setId(UUID.randomUUID().toString());
            t.setAmount(String.valueOf(faker.random().nextInt(1000)));
            // t.setCardNumber(faker.finance().creditCard());
            t.setCardNumber(cardNumbers[faker.random().nextInt(cardNumbers.length)]);
            t.setCountry(faker.address().country());
            //t.setCurrency(faker.currency().code());
            //Randomly select one of those three currencies
            t.setCurrency(currencies[faker.random().nextInt(currencies.length)]);
            t.setMerchant(faker.company().name());
            t.setMerchantCategory(faker.company().industry());

            if (randNr > 0.7) {
                //wrong pin
                t.setPin(faker.number().digits(4));
            } else {
                //correct pin
                t.setPin(t.getCardNumber().substring(0, 4));
            }
            t.setTries("0");

            System.out.println(t);
            transactions.add(t);
        }


        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        Properties props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG,"faker");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:29092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        for (Transaction transaction : transactions) {
            //20% of cards are invalid
            String status = Math.random()>0.2?"open":"invalid";
            String requestBody = "{\"cardNumber\":\"" + transaction.getCardNumber() + "\", \"limit\":\"" + faker.number().numberBetween(450, 100000) + "\",\"status\": \""+status+"\"}";

            HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8109/limit/update"))
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .setHeader("Content-Type", "application/json")
            .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("-->" + response.body() + "<--" + response.statusCode());
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Producer<String, String> producer = new KafkaProducer<String, String>(props);
            producer.send(new ProducerRecord<String, String>("incoming-transactions", null, gson.toJson(transaction)));
            
        }

        // create a request
        

        return "Hello World";
    }

}
