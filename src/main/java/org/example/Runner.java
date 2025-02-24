package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Component
public class Runner implements ApplicationRunner {

    @Autowired
    private HttpClient httpClient;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://bdu.fstec.ru/vul/2017-00791"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
       // System.out.println(response.body());
    }
}
