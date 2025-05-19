
package com.pjava.src.UI.components.input;


import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.input.Lever;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.image.ImageView;

public class UILever extends UIGate{
    @FXML
    private Pin outputController;
    @FXML
    private Rectangle body1;
    @FXML
    private VBox output;
    @FXML
    private ImageView body2;
    @FXML
    private AnchorPane self;

    @FXML
    private void initialize(){
        System.out.println("Initialisation!");
        setLogic(new Lever());
        outputController.setAsInput(false);
        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));
        body2.setOnMousePressed(event -> pressed(event));
        body2.setOnMouseReleased(event -> released(event));
        body2.setOnMouseDragged(event -> dragged(event));
    }

    @Override
    public Lever getLogic() {
        return (Lever) super.getLogic();
    }

    private void setLogic(Lever lever) {
        super.setLogic(lever);
    }
}