package com.pjava.src.UI.components.input;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.Element;
import com.pjava.src.components.Element.ElementEvent;
import com.pjava.src.components.input.Button;
import com.pjava.src.utils.Utils;

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
    public Button getLogic() {
        return (Button) super.getLogic();
    }


    /**
     * set the logic of a Button Gate
     *
     * @param button a  button gate
     */

    private void setLogic(Button button) {
        super.setLogic(button);
    }

    @Override
    public Pin getPinInput(int index) {
        return null;
    }

    @Override
    protected void released(MouseEvent event) {
        getLogic().press();
        updateVisuals();
        super.released(event);
        Utils.timeout(() -> {
            // BUG it does not release the button somehow
            getLogic().release();
            updateVisuals();
        }, getLogic().getDelay(), null);
    }
}
