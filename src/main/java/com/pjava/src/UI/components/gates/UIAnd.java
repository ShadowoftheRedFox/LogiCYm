package com.pjava.src.UI.components.gates;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.gates.And;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;

public class UIAnd extends UIGate {
    @FXML
    private Pin input1Controller;
    @FXML
    private Pin input2Controller;
    @FXML
    private Pin output1Controller;

    @FXML
    private VBox input1;
    @FXML
    private VBox input2;
    @FXML
    private VBox output1;
    @FXML
    private Arc body1;
    @FXML
    private Rectangle body2;

    public static UIAnd create() {
        return (UIAnd) UIElement.create("UIAnd");
    }

    @FXML
    private void initialize() {
        System.out.println("Initialisation!");
        setLogic(new And());
        output1Controller.setAsInput(false);

        body1.setOnMousePressed(event -> pressed(event));
        body1.setOnMouseReleased(event -> released(event));
        body1.setOnMouseDragged(event -> dragged(event));
        body2.setOnMousePressed(event -> pressed(event));
        body2.setOnMouseReleased(event -> released(event));
        body2.setOnMouseDragged(event -> dragged(event));

        input1Controller.originController = this;
        input2Controller.originController = this;
        output1Controller.originController = this;
    }

    @Override
    public And getLogic() {
        return (And) super.getLogic();
    }

    private void setLogic(And and) {
        super.setLogic(and);
    }
<<<<<<< Updated upstream
=======

    private void pressed(MouseEvent event) {
        System.out.println(getClass().getSimpleName() + " pressed");
    }

    private void released(MouseEvent event) {
        System.out.println(getClass().getSimpleName() + " released");
    }
    private void dragged(MouseEvent event) {
        double posX = event.getX() + self.getLayoutX();
        double posY = event.getY() + self.getLayoutY();

        // prevent out of bound
        if (posX < 0) {
            posX = 0;
        }
        if (posY < 0) {
            posY = 0;
        }

        // Stocker la position précédente
        double oldX = self.getLayoutX();
        double oldY = self.getLayoutY();

        // move
        self.setLayoutX(posX - (posX % UIElement.baseSize));
        self.setLayoutY(posY - (posY % UIElement.baseSize));

        setPosition(new Point2D(posX / 50, posY / 50));

        // Si le déplacement est significatif, vérifier et déconnecter les câbles qui s'étirent trop
        double deltaX = Math.abs(self.getLayoutX() - oldX);
        double deltaY = Math.abs(self.getLayoutY() - oldY);



        // Mettre à jour les câbles connectés qui restent
        updateConnectedCables();
    }
    @Override
    public Pin getPinInput(int index) {
        switch(index){
            case 0: return input1Controller;
            case 1: return input2Controller;
            default : return null;
        }
    }
    @Override
    public Pin getPinOutput(int index) {
        switch(index){
            case 0: return output1Controller;
            default : return null;
        }
    }

>>>>>>> Stashed changes
}
