package com.pjava.src.UI.components.gates;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.gates.And;
import com.pjava.src.components.gates.Not;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class UINot extends UIGate {
    @FXML
    private Pin input1Controller;
    @FXML
    private Pin output1Controller;

    @FXML
    private VBox input1;
    @FXML
    private VBox output1;
    @FXML
    private ImageView body1;

    public static UINot create() {
        return (UINot) UIElement.create("UINot");
    }

    @FXML
    private void initialize() {
        System.out.println("Initialisation!");
        setLogic(new And());
        output1Controller.setAsInput(false);

        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));
    }

    @Override
    public Not getLogic() {
        return (Not) super.getLogic();
    }

    public void setLogic(Not not) {
        super.setLogic(not);
    }
}
