package com.cheftime.cheftimeapp.services;

import com.cheftime.cheftimeapp.application.Application;
import com.cheftime.cheftimeapp.controllers.MainController;
import com.cheftime.cheftimeapp.controllers.RecipeViewController;
import com.cheftime.cheftimeapp.entities.Recipe;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewNavigator {

    private static Stage mainStage;
    private static List<AnchorPane> resultCards = new ArrayList<>();

    public static void setMainStage(Stage stage) {
        mainStage = stage;
    }

    //load and instance controller Recipe View
    public static void loadRecipeView(Recipe recipe) throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("/com/cheftime/cheftimeapp/RecipeView.fxml"));
        Parent recipeViewPane = loader.load();
        RecipeViewController recipeViewController = loader.getController();
        recipeViewController.showRecipe(recipe);

        Scene recipeViewScene = new Scene(recipeViewPane);
        recipeViewScene.getStylesheets().add("style.css");
        mainStage.setScene(recipeViewScene);
    }

    //load and instance controller Main View
    public static void loadMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("/com/cheftime/cheftimeapp/MainView.fxml"));
        Parent mainViewPane = loader.load();
        MainController mainController = loader.getController();
        mainController.displaySearchResults();

        Scene mainViewScene = new Scene(mainViewPane);
        mainViewScene.getStylesheets().add("style.css");
        mainStage.setScene(mainViewScene);
    }

    //resultCards is maintained by this class so user can see them again after going back from recipe view
    public static List<AnchorPane> getResultCards() {
        return resultCards;
    }
}

