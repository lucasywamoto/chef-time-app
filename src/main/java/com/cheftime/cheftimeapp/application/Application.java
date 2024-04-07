package com.cheftime.cheftimeapp.application;

import com.cheftime.cheftimeapp.services.ViewNavigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/cheftime/cheftimeapp/MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        //custom fonts
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Light.ttf"), -1);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Bold.ttf"), -1);

        //load css
        scene.getStylesheets().add("style.css");

        stage.setTitle("Chef Time");

        //add app icon
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png")));
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();

        ViewNavigator.setMainStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
