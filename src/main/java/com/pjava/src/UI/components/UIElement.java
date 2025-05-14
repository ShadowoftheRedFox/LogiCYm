package com.pjava.src.UI.components;

import java.io.IOException;

import com.pjava.src.UI.Rotation;
import com.pjava.src.components.Element;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public abstract class UIElement extends AnchorPane {
    private String name = "";
    private Element logic = null;
    private Point2D position = Point2D.ZERO;
    private Rotation rotation = Rotation.EAST;
    private Color color = Color.AQUA;

    protected UIElement(String fxml) {
        loadFXML(fxml);
    }

    protected void loadFXML(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/components/" + name + ".fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new Error(e);
        }
    };

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
