package ch.unisg.preprocessing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//@RestController
@RequiredArgsConstructor
public class ForwardTransactionRestController {

    //Construct the body
    private String assembleBody(String businessKey,String amount, String pin, String cardNumber, String country,
                                String merchant, String merchantCategory, String currency, String tries,
                                String status, String exchangeRate ){
        String body = "{\n" +
                "  \"messageName\" : \"Transaction\",\n" +
                "  \"businessKey\" : \""+businessKey+"\",\n" +
                " \n" +
                "  \"processVariables\" : {\n" +
                "    \"amount\" : {\"value\" : \""+amount+"\", \"type\": \"String\"},\n" +
                "    \"pin\": {\"value\":\""+pin+"\", \"type\":\"String\"},\n" +
                "    \"cardNumber\": {\"value\":\""+cardNumber+"\", \"type\":\"String\"},\n" +
                "    \"country\":{\"value\":\""+country+"\",\"type\":\"String\"},\n" +
                "    \"merchant\":{\"value\":\""+merchant+"\",\"type\":\"String\"},\n" +
                "    \"merchantCategory\":{\""+merchantCategory+"\":\"Bitcoin\",\"type\":\"String\"},\n" +
                "    \"currency\":{\"value\":\""+currency+"\",\"type\":\"String\"},\n" +
                "    \"tries\":{\"value\":\""+tries+"\",\"type\":\"String\"},\n" +
                "    \"status\":{\"value\":\""+status+"\",\"type\":\"String\"},\n" +
                "    \"exchangeRate\":{\"value\":\""+exchangeRate+"\",\"type\":\"String\"}\n" +
                "\n" +
                "  }\n" +
                "}";
        return body;

    }

    //method to send out the transaction to the transaction service
    public void forwardTransaction(String businessKey,String amount, String pin, String cardNumber, String country,
                                  String merchant, String merchantCategory, String currency, String tries,
                                   String status, String exchangeRate){
        URI uri = URI.create("http://localhost:8080/engine-rest/message");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(this.assembleBody(businessKey,amount,pin,cardNumber,country,
                        merchant,merchantCategory,currency,tries,status,exchangeRate) ))
                .header(HttpHeaders.CONTENT_TYPE,"application/json")
                .build();

        //send
        try {
            HttpResponse response = httpClient.send(httpRequest,HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());
            if(response.statusCode()==204){
                System.out.println("Forwarding the transaction was successful");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
