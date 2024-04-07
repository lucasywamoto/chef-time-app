package com.cheftime.cheftimeapp.services;

import com.cheftime.cheftimeapp.entities.Recipe;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeSearchService {

    //api key
    private static final String APP_KEY = "3c80d4ac04a945ef9da3ea819a5ffe22";

    //perform query in api using form data from search form
    public static List<Recipe> fetchSearchResults(String searchQuery, String mealType, int calories, boolean isVegan) throws UnsupportedEncodingException {
        List<Recipe> searchResults = new ArrayList<>();
        //encode search text to handle spaces
        String encodedSearchQuery = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);

        //build api url
        StringBuilder apiUrlBuilder = new StringBuilder("https://api.spoonacular.com/recipes/complexSearch?");
        //add api key
        apiUrlBuilder.append("apiKey=").append(APP_KEY);
        //add search text
        apiUrlBuilder.append("&query=").append(encodedSearchQuery);
        //add meal type if chosen
        if (mealType != null && !mealType.isEmpty()) {
            mealType = URLEncoder.encode(mealType, "UTF-8");
            apiUrlBuilder.append("&mealType=").append(mealType);
        }
        //add calories if set > 0
        if (calories > 0) {
            apiUrlBuilder.append("&maxCalories=").append(calories);
        }
        //add vegan is checkbox is true
        if (isVegan) {
            apiUrlBuilder.append("&diet=vegan");
        }
        //request nutrition information and a maximum of 12 results
        apiUrlBuilder.append("&addRecipeNutrition=true");
        apiUrlBuilder.append("&number=12");

        //string builder to string
        String apiUrl = apiUrlBuilder.toString();
        System.out.println(apiUrl);

        //call http client to perform request in api
        ApiHttpClient apiHttpClient = new ApiHttpClient();
        String responseBody = apiHttpClient.fetchData(apiUrl);
        if (responseBody != null) {
            //parse response
            parseSearchResults(responseBody, searchResults);
        }
        return searchResults;
    }

    private static void parseSearchResults(String responseBody, List<Recipe> searchResults) {
        Gson gson = new Gson();
        //JSON string to JSON object
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);

        if (jsonObject.has("results")) {
            JsonArray resultsArray = jsonObject.getAsJsonArray("results");

            //accessing each element and generating a recipe object
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

    //method to call api and request individual recipe ingredients and instruction
    public static void getRecipeInfo(Recipe recipe) {
        String id = recipe.getId();

        String apiUrl = "https://api.spoonacular.com/recipes/" + id + "/information?" +
                "apiKey=" + APP_KEY +
                "&includeNutrition=false&addWinePairing=false&addTasteData=false";

        System.out.println(apiUrl);

        ApiHttpClient apiHttpClient = new ApiHttpClient();
        String responseBody = apiHttpClient.fetchData(apiUrl);
        if (responseBody != null) {
            parseRecipeInfo(responseBody, recipe);
        }
    }

    //parsing response from getRecipeInfo
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