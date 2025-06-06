package com.pjava.src.UI.components.input;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.Element;
import com.pjava.src.components.Element.ElementEvent;
import com.pjava.src.components.input.Power;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class UIPower extends UIGate {
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
        setLogic(new Power());
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
    public Power getLogic() {
        return (Power) super.getLogic();
    }
    /**
     * set the logic of a Power Gate
     *
     * @param power a power gate
     */

    private void setLogic(Power power) {
        super.setLogic(power);
    }

    @Override
    public Pin getPinInput(int index) {
        return null;
    }
}
