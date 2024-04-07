package com.cheftime.cheftimeapp.services;

import com.cheftime.cheftimeapp.entities.Recipe;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeSearchService {

    private static final String APP_KEY = "3c80d4ac04a945ef9da3ea819a5ffe22";

    public static List<Recipe> fetchSearchResults(String searchQuery, String mealType, int calories, boolean isVegan) throws UnsupportedEncodingException {
        List<Recipe> searchResults = new ArrayList<>();
        String encodedSearchQuery = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);

        StringBuilder apiUrlBuilder = new StringBuilder("https://api.spoonacular.com/recipes/complexSearch?");
        apiUrlBuilder.append("apiKey=").append(APP_KEY);

        apiUrlBuilder.append("&query=").append(encodedSearchQuery);

        StringBuilder includeIngredientsBuilder = new StringBuilder();

        if (mealType != null && !mealType.isEmpty()) {
            mealType = URLEncoder.encode(mealType, "UTF-8");
            apiUrlBuilder.append("&mealType=").append(mealType);
        }

        if (calories > 0) {
            apiUrlBuilder.append("&maxCalories=").append(calories);
        }

        if (isVegan) {
            apiUrlBuilder.append("&diet=vegan");
        }

        apiUrlBuilder.append("&addRecipeNutrition=true");
        apiUrlBuilder.append("&number=12");

        String apiUrl = apiUrlBuilder.toString();

        System.out.println(apiUrl);

        try {
            URI uri = new URI(apiUrl);
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                String responseBody = httpResponse.body();
                parseSearchResults(responseBody, searchResults);
            } else {
                System.out.println("Error: " + httpResponse.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    private static void parseSearchResults(String responseBody, List<Recipe> searchResults) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

        if (jsonObject.has("results")) {
            JsonArray resultsArray = jsonObject.getAsJsonArray("results");

            for (JsonElement resultElement : resultsArray) {
                JsonObject resultObject = resultElement.getAsJsonObject();
                String id = resultObject.get("id").getAsString();
                String title = resultObject.get("title").getAsString();
                String imageLink = resultObject.get("image").getAsString();
                String servings = resultObject.get("servings").getAsString();
                String prepTime = resultObject.get("readyInMinutes").getAsString();
                JsonObject nutrition = resultObject.get("nutrition").getAsJsonObject();
                JsonArray nutrientsArray = nutrition.get("nutrients").getAsJsonArray();
                String healthScore = resultObject.get("healthScore").getAsString();

                Map<String, String> nutrientValues = new HashMap<>();

                for (JsonElement nutrientElement : nutrientsArray) {
                    JsonObject nutrientObject = nutrientElement.getAsJsonObject();
                    String name = nutrientObject.get("name").getAsString();
                    String amount = nutrientObject.get("amount").getAsString();
                    nutrientValues.put(name, amount);
                }

                String calories = nutrientValues.getOrDefault("Calories", "N/A");
                if (!calories.equals("N/A")) {
                    calories = calories.split("\\.")[0];
                }
                String fat = nutrientValues.getOrDefault("Fat", "N/A");
                String protein = nutrientValues.getOrDefault("Protein", "N/A");
                String carb = nutrientValues.getOrDefault("Carbohydrates", "N/A");

                Recipe r = new Recipe(id, title, calories, imageLink, servings, prepTime, protein, carb, fat, healthScore);
                searchResults.add(r); // Add parsed recipe to the searchResults list
            }
        }
    }

    public static void getRecipeInfo(Recipe recipe) {
        String id = recipe.getId();

        String apiUrl = "https://api.spoonacular.com/recipes/" + id + "/information?" +
                "apiKey=" + APP_KEY +
                "&includeNutrition=false&addWinePairing=false&addTasteData=false";

        System.out.println(apiUrl);

        try {
            URI uri = new URI(apiUrl);
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                String responseBody = httpResponse.body();
                parseRecipeInfo(responseBody, recipe);
            } else {
                System.out.println("Error: " + httpResponse.statusCode());
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void parseRecipeInfo(String responseBody, Recipe recipe) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

        String instructions = jsonObject.get("instructions").getAsString();
        String summary = jsonObject.get("summary").getAsString();
        List<String> ingredients = new ArrayList<>();

        JsonArray ingredientsArray = jsonObject.get("extendedIngredients").getAsJsonArray();

        for (JsonElement ingredientElement : ingredientsArray) {
            JsonObject ingredientObject = ingredientElement.getAsJsonObject();
            String ingredient = ingredientObject.get("original").getAsString();
            ingredients.add(ingredient);
        }
        instructions = instructions.replace("\n", "<br>");
        recipe.setInstructions(instructions);
        recipe.setSummary(summary);
        recipe.setIngredients(ingredients);
    }
}