package com.pjava;

import java.io.IOException;

import com.pjava.controllers.MainController;
import com.pjava.src.SceneManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX entry point
 *
 * Launch the Scneemanager and the main scene here
 */
public class App extends Application {

    SceneManager manager;

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new MainController(manager), 400, 200);

        this.manager = new SceneManager(scene);

        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
        stage.setTitle("Hello JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
