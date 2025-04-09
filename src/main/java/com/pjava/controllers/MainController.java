package com.pjava.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import com.pjava.src.SceneManager;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class MainController extends BorderPane {
    @FXML
    public ListView<String> itemList;

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
    }

    @FXML
    public void click(ActionEvent event) {
        this.manager.activate("ploof");
        System.out.println("Changement à ploof!");
    }
}
