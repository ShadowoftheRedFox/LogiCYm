package org.openjfx;

import java.io.IOException;

import org.openjfx.src.Main;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // new IHMCompteur(stage);
        new Main(stage);
    }

    public static void main(String[] args) {
        launch();
    }

}