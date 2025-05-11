package com.pjava.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import com.pjava.src.UI.SceneManager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class MainController extends AnchorPane {
    @FXML
    public ListView<String> itemList;

    @FXML
    public ChoiceBox<String> typeChoiceBox;

    private SceneManager manager;

    public MainController(SceneManager manager) {
        this.manager = manager;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
            System.err.println(MainController.class.getName() + " " + ex.toString());
        }
    }

    // include a fxml file into another with:
    // <fx:include source="Child.fxml"/>

    public void initialize() {
        // initialization here, if needed...
        itemList.setItems(FXCollections.observableArrayList("Ploof", "Glooof", "Mloof", "Floof", "Floof", "Floof"));

        typeChoiceBox.setItems(FXCollections.observableArrayList("Lettre", "Colis"));
        typeChoiceBox.getSelectionModel().select(0);
    }

    @FXML
    public void clickPloof(ActionEvent event) {
        this.manager.activate("ploof");
        System.out.println("Changement à ploof!");
    }

    @FXML
    public void clickWorkbench(ActionEvent event) {
        this.manager.activate("workbench");
        System.out.println("Changement à workbench!");
    }

    @FXML
    public void quit(ActionEvent event) {
        System.out.println("Fermeture...");
        Platform.exit();
    }
}
