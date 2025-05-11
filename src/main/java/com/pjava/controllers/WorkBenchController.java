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

public class WorkBenchController extends AnchorPane {
    private SceneManager manager;

    public WorkBenchController(SceneManager manager) {
        this.manager = manager;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/WorkBench.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
            System.err.println(MainController.class.getName() + " " + ex.toString());
        }
    }



    @FXML
    public void click(ActionEvent event) {
        this.manager.activate("main");
        System.out.println("Changement à main!");
    }

}
