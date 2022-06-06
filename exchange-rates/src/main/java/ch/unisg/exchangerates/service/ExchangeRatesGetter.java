package ch.unisg.exchangerates.service;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ExchangeRatesGetter {
    HttpClient client = HttpClient.newHttpClient();

    public void getExchangeRates(){

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://open.er-api.com/v6/latest/USD"))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ExchangeRatesUpdater exchangeRatesUpdater = new ExchangeRatesUpdater();
            //map with all ers to get and define currencies to get
            HashMap<String,Double> exchangeRates = new HashMap<>();
            String [] currencies = {"EUR","CHF","JPY"};
            //get from API
            String data = response.body();
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(data) ;
            JSONObject rates = (JSONObject) json.get("rates");
            //get interesting currencies
            for (String currency:currencies){
                Double er = (Double) rates.get(currency);
                exchangeRates.put(currency,er);
            }
            //send ers to preprocessing
            exchangeRatesUpdater.updateExchangeRates(exchangeRates);


        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
