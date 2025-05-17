package com.pjava.src.UI.components;

import java.io.IOException;

import com.pjava.controllers.InfosController;
import com.pjava.src.UI.Rotation;
import com.pjava.src.components.Element;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public abstract class UIElement {
    private String name = "";
    private Element logic = null;
    private Point2D position = Point2D.ZERO;
    private Rotation rotation = Rotation.EAST;
    private Color color = Color.AQUA;

    public static final int baseSize = 50;

    @FXML
    protected AnchorPane self;

    private InfosController infos = null;

    public static UIElement create(String fxml) {
        try {
            // load the gate's fxml
            FXMLLoader loader = new FXMLLoader(UIElement.class.getResource("/fxml/components/" + fxml + ".fxml"));
            loader.load();
            // and get the controller instance
            UIElement controller = loader.getController();

            // create a gate infos instance
            FXMLLoader infosLoader = new FXMLLoader(
                    UIElement.class.getResource("/fxml/components/ComponentInfos.fxml"));
            Node infosNode = infosLoader.load();
            // and add it to the gate controller
            controller.infos = infosLoader.getController();
            controller.infos.setSelf(infosNode);
            controller.infos.setOrigin(controller);

            // return the controller
            return controller;
        } catch (IOException e) {
            throw new Error(e);
        }
    };

    protected void pressed(MouseEvent event) {
        System.out.println(getClass().getSimpleName() + " pressed");
    }

    protected void released(MouseEvent event) {
        System.out.println(getClass().getSimpleName() + " released");
    }

    protected void dragged(MouseEvent event) {
        double posX = event.getX() + self.getLayoutX();
        double posY = event.getY() + self.getLayoutY();

        // prevent out of bound
        if (posX < 0) {
            posX = 0;
        }
        if (posY < 0) {
            posY = 0;
        }

        // move
        self.setLayoutX(posX - (posX % UIElement.baseSize));
        self.setLayoutY(posY - (posY % UIElement.baseSize));

        setPosition(new Point2D(posX / 50, posY / 50));
    }

    // #region Getters
    public String getName() {
        return name;
    }

    public Element getLogic() {
        return logic;
    }

    public Point2D getPosition() {
        return position;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public Color getColor() {
        return color;
    }

    public Node getNode() {
        return self;
    }

    public InfosController getInfos() {
        return infos;
    }
    // #endregion

    // #region Setters
    public void setName(String name) {
        this.name = name;
    }

    protected void setLogic(Element logic) throws NullPointerException {
        if (logic == null) {
            throw new NullPointerException("Expected logic to be an instance of Element, received null");
        }
        this.logic = logic;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    // #endregion
}
