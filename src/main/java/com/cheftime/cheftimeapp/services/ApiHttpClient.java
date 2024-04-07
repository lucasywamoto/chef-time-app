package com.cheftime.cheftimeapp.services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiHttpClient {
    public String fetchData(String url) {
        try {
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (URISyntaxException e) {
            System.err.println("Invalid URL: " + url);
            return null;
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to fetch data due to network issues.");
            return null;
        }
    }
}
