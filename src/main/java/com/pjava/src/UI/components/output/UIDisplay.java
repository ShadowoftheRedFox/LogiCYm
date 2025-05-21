package com.pjava.src.UI.components.output;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.output.Display;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
    private Text display;
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
        display.setOnMousePressed(event -> pressed(event));
        display.setOnMouseReleased(event -> released(event));
        display.setOnMouseDragged(event -> dragged(event));

        inputController.originController = this;

        inputPins.add(inputController);

        display.fillProperty().set(Color.RED);
        updateVisuals();
        // TODO the width of the gate need to be (1+bussize) * 50, because each number
        // is 50px wide
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

    @Override
    public void updateVisuals() {
        if (!getLogic().getPowered()) {
            display.setText("X");
        } else {
            display.setText(getLogic().getOutput());
        }
    }
}
