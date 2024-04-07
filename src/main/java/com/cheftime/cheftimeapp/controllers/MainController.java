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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public StackPane messageContainer;

    @FXML
    private Text message;

    @FXML
    public VBox resultVBox;

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

        //show calories if user set value > 0
        caloriesSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > 0)
                maxCaloriesLabel.setText("Maximum calories: " + newValue.intValue());
            else
                maxCaloriesLabel.setText("Maximum calories: ");
        });
    }

    @FXML
    private void searchRecipes() throws IOException {
        //get data from seach form
        String searchQuery = searchTextField.getText();
        String mealType = typeCombobox.getValue();
        int calories = (int) caloriesSlider.getValue();
        boolean isVegan = checkVegan.isSelected();

        //clear previous search
        clearSearch();

        //perform query in api and generate search cards UI
        searchResults = RecipeSearchService.fetchSearchResults(searchQuery, mealType, calories, isVegan);
        generateResulCards(searchResults);
    }

    //insert result cards in search results pane (keep it as a separate method to simplify showing results when back from recipe view)
    public void displaySearchResults() throws IOException {
        resultVBox.getChildren().remove(messageContainer);
        resultVBox.getChildren().addAll(ViewNavigator.getResultCards());
    }

    private void generateResulCards(List<Recipe> searchResults) throws IOException {
        //populate resultCards list for each instance of Recipe from search
        List<AnchorPane> resultCards = searchResults.parallelStream()
                .map(recipe -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(Application.class.getResource("/com/cheftime/cheftimeapp/SearchCard.fxml"));
                        AnchorPane resultCard = loader.load();
                        SearchCardController searchCardController = loader.getController();
                        searchCardController.setRecipe(recipe);
                        return resultCard;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        ViewNavigator.getResultCards().addAll(resultCards);
        displaySearchResults();

        //animation to load search result cards
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

    public void clearSearch() {
        //clear form
        searchTextField.clear();
        typeCombobox.getSelectionModel().clearSelection();
        caloriesSlider.setValue(0);
        checkVegan.setSelected(false);
        //clear search content
        resultVBox.getChildren().clear();
        ViewNavigator.getResultCards().clear();
        //return to top of scroll pane
        resultScrollPane.setVvalue(0.0);
    }
}