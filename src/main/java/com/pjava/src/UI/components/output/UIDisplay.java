package com.pjava.src.UI.components.output;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.output.Display;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class UIDisplay extends UIGate {
    @FXML
    private Pin inputController;
    @FXML
    private Rectangle body1;
    @FXML
    private VBox input;
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
        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));
        text.setOnMousePressed(event -> pressed(event));
        text.setOnMouseReleased(event -> released(event));
        text.setOnMouseDragged(event -> dragged(event));

        inputController.originController = this;

        inputPins.add(inputController);
    }

    @Override
    public Display getLogic() {
        return (Display) super.getLogic();
    }

    private void setLogic(Display display) {
        super.setLogic(display);
    }

    @Override
    public Pin getPinOutput(int index) {
        return null;
    }
}
