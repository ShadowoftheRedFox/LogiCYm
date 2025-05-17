package com.pjava.src.UI.components.gates;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.gates.And;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;

public class UIAnd extends UIGate {
    @FXML
    private Pin input1Controller;
    @FXML
    private Pin input2Controller;
    @FXML
    private Pin output1Controller;

    @FXML
    private VBox input1;
    @FXML
    private VBox input2;
    @FXML
    private VBox output1;
    @FXML
    private Arc body1;
    @FXML
    private Rectangle body2;

    @FXML
    private AnchorPane self;

    public static UIAnd create() {
        return (UIAnd) UIElement.create("UIAnd");
    }

    @FXML
    private void initialize() {
        System.out.println("Initialisation!");
        setLogic(new And());
        output1Controller.setAsInput(false);

        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));
        body2.setOnMousePressed(event -> pressed(event));
        body2.setOnMouseReleased(event -> released(event));
        body2.setOnMouseDragged(event -> dragged(event));
    }

    @Override
    public And getLogic() {
        return (And) super.getLogic();
    }

    private void setLogic(And and) {
        super.setLogic(and);
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
