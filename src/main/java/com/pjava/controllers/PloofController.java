package com.pjava.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

import com.pjava.src.UI.SceneManager;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

public class PloofController extends AnchorPane {

    private SceneManager manager;

    public PloofController(SceneManager manager) {
        this.manager = manager;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Ploof.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
            System.err.println(PloofController.class.getName() + " " + ex.toString());
        }
    }

    @FXML
    public void click(ActionEvent event) {
        this.manager.activate("main");
        System.out.println("Changement à main!");
    }
}
