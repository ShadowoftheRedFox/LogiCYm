package com.pjava.src.UI.components.input;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UICable;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.input.Button;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class UIButton extends UIGate {
    @FXML
    private Pin outputController;
    @FXML
    private Rectangle body2;
    @FXML
    private VBox output;
    @FXML
    private ImageView body1;
    @FXML
    private AnchorPane self;

    @FXML
    private void initialize() {
        System.out.println("Initialisation!");
        try {
            setLogic(new Button());
        } catch (Exception e) {
        }
        outputController.setAsInput(false);
        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));
        body2.setOnMousePressed(event -> pressed(event));
        body2.setOnMouseReleased(event -> released(event));
        body2.setOnMouseDragged(event -> dragged(event));

        outputController.originController = this;

        outputPins.add(outputController);
    }

    @Override
    public Button getLogic() {
        return (Button) super.getLogic();
    }

    private void setLogic(Button button) {
        super.setLogic(button);
    }

    @Override
    public Pin getPinInput(int index) {
        return null;
    }

    @Override
    protected void pressed(MouseEvent event) {
        getLogic().press();
        updateVisuals();
        super.pressed(event);
        try {
            wait(getLogic().getDelay());
            getLogic().release();
        } catch (InterruptedException e) {
            e.printStackTrace();
            getLogic().release();
        }
    }

    @Override
    public void updateVisuals() {
        for (UICable connectedCables : getConnectedCables()) {
            connectedCables.updateVisuals();
        }
    }
}
