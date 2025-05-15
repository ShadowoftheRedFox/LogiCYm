package com.pjava;

import java.io.IOException;

import com.pjava.controllers.Editor;
import com.pjava.controllers.MainController;
import com.pjava.controllers.PloofController;
import com.pjava.controllers.WorkBenchController;
import com.pjava.src.UI.SceneManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * JavaFX entry point.
 */
public class App extends Application {

    /**
     * Create a new App.
     */
    public App() {

    }

    /**
     * The scene manager of the application. Remember and provide all scenes. It
     * should be this one given when creating a new scene.
     */
    private SceneManager manager;

    /**
     * Setup the application here with every scene.
     * Might be modified later.
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new BorderPane(), 400, 200);

        this.manager = new SceneManager(scene);
        this.manager.addScreen("main", new MainController(this.manager));
        this.manager.addScreen("ploof", new PloofController(this.manager));
        this.manager.addScreen("workbench", new WorkBenchController(this.manager));
        this.manager.addScreen("editor", new Editor(this.manager));

        Image haerin = new Image("/img/haerin.jpg"); // silly
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());
        stage.setTitle("Hello JavaFX and Maven");
        stage.setScene(scene);
        stage.getIcons().add(haerin);
        stage.show();

        manager.activate("editor");
    }

    /**
     * Entry point of the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
