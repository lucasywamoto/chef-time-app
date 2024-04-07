package com.cheftime.cheftimeapp.controllers;

import com.cheftime.cheftimeapp.application.Application;
import com.cheftime.cheftimeapp.entities.Recipe;
import com.cheftime.cheftimeapp.services.RecipeSearchService;
import com.cheftime.cheftimeapp.services.ViewNavigator;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private VBox sideMenuPane;

    @FXML
    private Label searchLabel;

    @FXML
    private TextField searchTextField;

    @FXML
    private Slider caloriesSlider;

    @FXML
    private Label maxCaloriesLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private ComboBox<String> typeCombobox;

    @FXML
    private CheckBox checkVegan;

    @FXML
    private Button searchButton;

    @FXML
    private ScrollPane resultScrollPane;

    @FXML
    private VBox resultVBox;

    private List<Recipe> searchResults = new ArrayList<>();

    public MainController() {
    }

    //getters
    public AnchorPane getRootPane() {
        return rootPane;
    }

    //setters
    @FXML
    public void setCaloriesSlider() {
        caloriesSlider.setMin(0);
        caloriesSlider.setMax(1200);

        caloriesSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > 0)
                maxCaloriesLabel.setText("Maximum calories: " + newValue.intValue());
            else
                maxCaloriesLabel.setText("Maximum calories: ");
        });
    }

    @FXML
    private void searchRecipes() throws IOException {
        resultVBox.getChildren().clear();
        ViewNavigator.getResultCards().clear();
        resultScrollPane.setVvalue(0.0);

        String searchQuery = searchTextField.getText();
        String mealType = typeCombobox.getValue();
        int calories = (int) caloriesSlider.getValue();
        boolean isVegan = checkVegan.isSelected();

        searchResults = RecipeSearchService.fetchSearchResults(searchQuery, mealType, calories, isVegan);
        clearForm();
        generateResulCards(searchResults);
    }

    public void displaySearchResults() throws IOException {
        resultVBox.getChildren().addAll(ViewNavigator.getResultCards());
    }

    private void generateResulCards(List<Recipe> searchResults) throws IOException {

        for (Recipe r : searchResults) {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("/com/cheftime/cheftimeapp/SearchCard.fxml"));
            AnchorPane resultCard = loader.load();
            SearchCardController searchCardController = loader.getController();
            searchCardController.setRecipe(r);
            ViewNavigator.getResultCards().add(resultCard);
        }

        displaySearchResults();

        int delay = 200;
        for (int i = 0; i < ViewNavigator.getResultCards().size(); i++) {
            final int index = i;
            AnchorPane resultCard = ViewNavigator.getResultCards().get(i);
            resultCard.opacityProperty().setValue(0);

            resultCard.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.getWidth() > 0 && newValue.getHeight() > 0) {
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), resultCard);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);
                    fadeIn.setDelay(Duration.millis(index * delay));
                    fadeIn.play();
                }
            });
        }
    }

    public void clearForm() {
        searchTextField.clear();
        typeCombobox.getSelectionModel().clearSelection();
        caloriesSlider.setValue(0);
        checkVegan.setSelected(false);
    }
}