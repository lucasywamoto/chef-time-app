package com.cheftime.cheftimeapp.controllers;

import com.cheftime.cheftimeapp.entities.Recipe;
import com.cheftime.cheftimeapp.services.RecipeSearchService;
import com.cheftime.cheftimeapp.services.ViewNavigator;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipeViewController implements Initializable {

    @FXML
    private AnchorPane recipeViewPane;

    @FXML
    private Button backBtn;

    @FXML
    private Text recipeTitle;

    @FXML
    private Text healthScoreText;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private WebView contentWebView;

    private Recipe recipe;

    @FXML
    public void showRecipe(Recipe recipe) {
        this.recipe = recipe;
        updateRecipeView();
    }

    @FXML
    private void updateRecipeView() {
        recipeTitle.setText(recipe.getTitle());
        healthScoreText.setText(recipe.getHealthScore());

        RecipeSearchService.getRecipeInfo(recipe);

        String htmlContent = getHtmlContent();
        contentWebView.getEngine().loadContent(htmlContent);
    }

    private String getHtmlContent() {
        String htmlContent = "<html><head><style>body { font-family: 'Poppins Light'; } a { pointer-events: none; cursor: default; color: inherit; text-decoration: none; }</style></head><body>" + recipe.getSummary();
        htmlContent += "<br><h2>Ingredients</h2><ul>";
        for (String ingredient : recipe.getIngredients()) {
            htmlContent += "<li> " + ingredient + "</li>";
        }
        htmlContent += "</ul><br><h2>Instructions</h2>" + recipe.getInstructions();
        htmlContent += "</body></html>";
        return htmlContent;
    }

    @FXML
    private void handleBackButtonClick() throws IOException {
        ViewNavigator.loadMainView();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine webEngine = contentWebView.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                int contentHeight = (int) webEngine.executeScript("document.body.scrollHeight");
                contentWebView.setPrefHeight(contentHeight);
                scrollPane.layout();
            }
            contentWebView.setMouseTransparent(true);
        });
    }
}