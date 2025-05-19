package com.pjava.src.UI.components.output;


import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.output.Display;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class UIDisplay extends UIGate{
    @FXML
    private Pin outputController;
    @FXML
    private Rectangle body1;
    @FXML
    private VBox output;
    @FXML
    private Text text;
    @FXML
    private AnchorPane self;

    @FXML
    private void initialize() {
        System.out.println("Initialisation!");
        try {
            setLogic(new Display());
        } catch (Exception e) {
        }
        outputController.setAsInput(false);
        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));
        text.setOnMousePressed(event -> pressed(event));
        text.setOnMouseReleased(event -> released(event));
        text.setOnMouseDragged(event -> dragged(event));
    }

    @Override
    public Display getLogic() {
        return (Display) super.getLogic();
    }
    private void setLogic(Display display) {
        super.setLogic(display);
    }
}