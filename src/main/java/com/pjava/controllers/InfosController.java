package com.pjava.controllers;

import com.pjava.src.UI.components.UIElement;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

public class InfosController {
    private Node self = null;

    @FXML
    private TextField labelField;

    private UIElement origin = null;

    public void setOrigin(UIElement origin) {
        this.origin = origin;
    }

    public void setSelf(Node node) {
        self = node;
    }

    public UIElement getOrigin() {
        return origin;
    }

    public Node getSelf() {
        return self;
    }
}
