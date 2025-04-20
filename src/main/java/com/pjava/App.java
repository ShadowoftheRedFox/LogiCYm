package com.pjava;

import java.io.IOException;

import com.pjava.controllers.MainController;
import com.pjava.controllers.PloofController;
import com.pjava.src.UI.SceneManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * JavaFX entry point
 *
 * Launch the Scneemanager and the main scene here
 */
public class App extends Application {

    SceneManager manager;

    /**
     * Setup the application here with every scene.
     * Might be modified later.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new BorderPane(), 400, 200);

        this.manager = new SceneManager(scene);
        this.manager.addScreen("main", new MainController(this.manager));
        this.manager.addScreen("ploof", new PloofController(this.manager));

        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
        stage.setTitle("Hello JavaFX and Maven");
        stage.setScene(scene);
        stage.show();

        manager.activate("main");
    }

    public static void main(String[] args) {
        launch();
    }
}
