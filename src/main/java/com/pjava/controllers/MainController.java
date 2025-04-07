package com.pjava.controllers;

import javafx.fxml.FXML;

import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

public class MainController {
    @FXML
    public ListView<String> itemList;

    public void initialize() {
        // initialization here, if needed...
        itemList.setItems(FXCollections.observableArrayList("Ploof", "Glooof", "Mloof", "Floof", "Floof", "Floof"));
    }
}
