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
    /**
     * name of the Gate
     */
    private String name = "";
    /**
     * logic of the Gate
     */
    private Element logic = null;
    /**
     * position of the Gate
     */
    private Point2D position = Point2D.ZERO;
    /**
     * rotation of the Gate
     */
    private Rotation rotation = Rotation.EAST;
    /**
     * color of the Gate
     */
    private Color color = Color.AQUA;

    /**
     * size of the Gate
     */
    public static final int baseSize = 50;

    @FXML
    protected AnchorPane self;

    /**
     * info of the Controller
     */
    private InfosController infos = null;

    /**
     * create a Gate with all the infos, gate will be stocked in the pc
     *
     * @param fxml name of the element
     * @return the Gate
     */
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
            controller.infos.setNode(infosNode);
            controller.infos.setOrigin(controller);

            // return the controller
            return controller;
        } catch (IOException e) {
            throw new Error(e);
        }
    };

    /**
     * detect when a element is pressed
     *
     * @param event mouseEvent
     */
    protected void pressed(MouseEvent event) {
        System.out.println(getClass().getSimpleName() + " pressed");
    }

    /**
     * detect when the element is released , when u stop pressing the element
     *
     * @param event mouseEvent
     */
    protected void released(MouseEvent event) {
        System.out.println(getClass().getSimpleName() + " released");
    }

    /**
     * detect when you drag the element AND mouves the gate on the mouse's position
     *
     * @param event mouseEvent
     */
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
    /**
     * give the name of the Gate
     *
     * @return name of the gate (string)
     */
    public String getName() {
        return name;
    }

    /**
     * give the logic of the Gate
     *
     * @return logic of the gate (Element)
     */
    public Element getLogic() {
        return logic;
    }

    /**
     * give the position of the Gate
     *
     * @return position of the gate (point2D)
     */
    public Point2D getPosition() {
        return position;
    }

    /**
     * give the rotation of the Gate
     *
     * @return angle of the gate (Rotation)
     */
    public Rotation getRotation() {
        return rotation;
    }

    /**
     * give the color of the Gate
     *
     * @return color of the gate
     */
    public Color getColor() {
        return color;
    }

    /**
     * give the Gate(FXML) of the Gate
     *
     * @return The main node body of the gate
     */
    public AnchorPane getNode() {
        return self;
    }

    /**
     * give the Controller of the Gate
     *
     * @return Controller of the gate (info)
     */
    public InfosController getInfos() {
        return infos;
    }
    // #endregion

    // #region Setters
    /**
     * to modify the name of an element
     *
     * @param name enter the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * it changes the logic of an element
     *
     * @param logic the new logic of the gate (component)
     * @throws NullPointerException if you dont send no logic
     */
    protected void setLogic(Element logic) throws NullPointerException {
        if (logic == null) {
            throw new NullPointerException("Expected logic to be an instance of Element, received null");
        }
        this.logic = logic;
    }

    /**
     * changes the position of an element
     *
     * @param position the new position of the element
     */
    public void setPosition(Point2D position) {
        this.position = position;
    }

    /**
     * changes the rotation of the element
     *
     * @param rotation the new angle of the element
     */
    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    /**
     * changes the color of the element
     *
     * @param color the new color of the element
     */
    public void setColor(Color color) {
        this.color = color;
    }
    // #endregion
}
