package com.pjava.src.UI.components.gates;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.gates.And;
import com.pjava.src.components.gates.Not;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class UINot extends UIGate {
    @FXML
    private Pin input1Controller;
    @FXML
    private Pin output1Controller;

    @FXML
    private VBox input1;
    @FXML
    private VBox output1;
    @FXML
    private ImageView body1;

    @FXML
    private AnchorPane self;

    public static UINot create() {
        return (UINot) UIElement.create("UINot");
    }

    public static UIOr getController(Node node) {
        Object controller = null;
        do {
            controller = node.getProperties().get("controller");
            node = node.getParent();
        } while (controller == null && node != null);
        return (UIOr) controller;
    }

    @FXML
    private void initialize() {
        System.out.println("Initialisation!");
        setLogic(new And());
        output1Controller.setAsInput(false);

        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));
    }

    @Override
    public Not getLogic() {
        return (Not) super.getLogic();
    }

    public void setLogic(Not not) {
        super.setLogic(not);
    }

    private void pressed(MouseEvent event) {
        System.out.println(getClass().getSimpleName() + " pressed");
    }

    private void released(MouseEvent event) {
        System.out.println(getClass().getSimpleName() + " released");
    }

    private void dragged(MouseEvent event) {
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
}
