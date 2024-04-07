package com.cheftime.cheftimeapp.application;

import com.cheftime.cheftimeapp.services.ViewNavigator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/cheftime/cheftimeapp/MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Light.ttf"), -1);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Bold.ttf"), -1);

        scene.getStylesheets().add("style.css");
        stage.setTitle("Chef Time");

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png")));
        if (Taskbar.isTaskbarSupported()) {
            var taskbar = Taskbar.getTaskbar();

            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                var dockIcon = defaultToolkit.getImage(getClass().getResource("/images/icon.png"));
                taskbar.setIconImage(dockIcon);
            }

        }
        stage.getIcons().add(icon);

        stage.setScene(scene);
        stage.show();

        ViewNavigator.setMainStage(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
