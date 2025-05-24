
package com.pjava.src.UI.components.input;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UICable;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.Element;
import com.pjava.src.components.Element.ElementEvent;
import com.pjava.src.components.input.Lever;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class UILever extends UIGate {
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
    private void initialize() {
        System.out.println("Initialisation!");
        setLogic(new Lever());
        outputController.setAsInput(false);
        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));
        body2.setOnMousePressed(event -> pressed(event));
        body2.setOnMouseReleased(event -> released(event));
        body2.setOnMouseDragged(event -> dragged(event));

        outputController.originController = this;

        outputPins.add(outputController);

        getLogic().getUpdateEvents().add(new ElementEvent() {
            public void updateState(Element element) {
                updateVisuals();
            };

            public void updatePower(Element element) {
                updateVisuals();
            };
        });
    }

    @Override
    public Lever getLogic() {
        return (Lever) super.getLogic();
    }

    /**
     * set the logic of a Lever Gate
     *
     * @param lever a lever gate
     */

    private void setLogic(Lever lever) {
        super.setLogic(lever);
    }

    @Override
    public Pin getPinInput(int index) {
        return null;
    }

    @Override
    protected void released(MouseEvent event) {
        getLogic().flip();
        updateVisuals();
        super.pressed(event);
    }

    @Override
    public void updateVisuals() {
        for (UICable connectedCables : getConnectedCables()) {
            connectedCables.updateVisuals();
        }
    }
}
