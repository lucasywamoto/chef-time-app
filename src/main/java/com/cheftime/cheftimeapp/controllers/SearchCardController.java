package com.cheftime.cheftimeapp.controllers;

import com.cheftime.cheftimeapp.entities.Recipe;
import com.cheftime.cheftimeapp.services.ViewNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;

public class SearchCardController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Text titleText;

    @FXML
    private Text infoText;

    @FXML
    private Text caloriesText;

    @FXML
    private Text proteinText;

    @FXML
    private Text carbText;

    @FXML
    private Text fatText;

    @FXML
    private ImageView imageView;

    @FXML
    private Button viewRecipeButton;

    private Recipe recipe;

    //assign data for each recipe in search result for its respective card for UI
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;

        String tempTitle = recipe.getTitle();
        titleText.setText(tempTitle.length() > 36 ? tempTitle.substring(0, 36) + "..." : tempTitle);

        Image img = new Image(recipe.getImageLink(), false);
        imageView.setImage(img);

        Rectangle clip = new Rectangle();
        clip.setWidth(180.0f);
        clip.setHeight(200.0f);
        clip.setArcWidth(24);
        clip.setArcHeight(24);
        imageView.setClip(clip);

        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(clip.widthProperty());
        imageView.fitHeightProperty().bind(clip.heightProperty());

        infoText.setText(String.format("%s servings • Ready in %s minutes", recipe.getServings(), recipe.getPrepTime()));

        caloriesText.setText(recipe.getCalories() + " Kcal");
        int indexOfDot = recipe.getProtein().indexOf(".");
        proteinText.setText("• Protein " + recipe.getProtein().substring(0, indexOfDot));
        indexOfDot = recipe.getCarb().indexOf(".");
        carbText.setText("• Carb " + recipe.getCarb().substring(0, indexOfDot));
        indexOfDot = recipe.getFat().indexOf(".");
        fatText.setText("• Fat " + recipe.getFat().substring(0, indexOfDot));
    }

    //handle action for switching to recipe view and see recipe instructions
    @FXML
    private void handleButtonClick() throws IOException {
        ViewNavigator.loadRecipeView(recipe);
    }
}