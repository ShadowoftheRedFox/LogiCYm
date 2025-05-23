
package com.pjava.src.UI.components.input;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.input.Ground;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class UIGround extends UIGate {
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
        setLogic(new Ground());
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
    public Ground getLogic() {
        return (Ground) super.getLogic();
    }

    /**
     * set the logic of a Ground Gate
     *
     * @param ground a ground gate
     */

    private void setLogic(Ground ground) {
        super.setLogic(ground);
    }

    @Override
    public Pin getPinInput(int index) {
        return null;
    }
}
