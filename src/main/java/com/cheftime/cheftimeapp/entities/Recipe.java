package com.cheftime.cheftimeapp.entities;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

    private String id;
    private String title;
    private String calories;
    private String imageLink;
    private String servings;
    private String prepTime;
    private String protein;
    private String carb;
    private String fat;
    private String summary;
    private List<String> ingredients = new ArrayList<>();
    private String instructions;
    private String healthScore;

    public Recipe() {

    }

    //recipe object constructor
    public Recipe(String id, String title, String calories, String imageLink, String servings, String prepTime, String protein, String carb, String fat, String healthScore) {
        this.id = id;
        this.title = title;
        this.calories = calories;
        this.imageLink = imageLink;
        this.servings = servings;
        this.prepTime = prepTime;
        this.protein = protein;
        this.carb = carb;
        this.fat = fat;
        this.healthScore = healthScore;
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarb() {
        return carb;
    }

    public void setCarb(String carb) {
        this.carb = carb;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(String healthScore) {
        this.healthScore = healthScore;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String prepSteps) {
        this.instructions = prepSteps;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    //hash code and equals for good practice purpose
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        return id.equals(recipe.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
