package com.pjava.src.UI.components.gates;

import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.cables.NodeSplitter;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class UINodeSplitter extends UIGate {
    @FXML
    private Rectangle body;
    @FXML
    private AnchorPane self;
    @FXML
    private VBox input1;
    @FXML
    private VBox output1;
    @FXML
    private VBox output2;

    @FXML
    private Pin input1Controller;
    @FXML
    private Pin output1Controller;
    @FXML
    private Pin output2Controller;

    public static UINodeSplitter create() {
        return (UINodeSplitter) UIElement.create("UINodeSplitter");
    }

    @FXML
    private void initialize() {
        System.out.println("NodeSplitter Initialisation!");
        try{
        setLogic(new NodeSplitter());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        output1Controller.setAsInput(false);
        output2Controller.setAsInput(false);

        body.setOnMousePressed(event -> pressed(event));
        body.setOnMouseReleased(event -> released(event));
        body.setOnMouseDragged(event -> dragged(event));

        input1Controller.originController = this;
        output1Controller.originController = this;
        output2Controller.originController = this;

        inputPins.add(input1Controller);
        outputPins.add(output1Controller);
        outputPins.add(output2Controller);
    }

    @Override
    public NodeSplitter getLogic() {
        return (NodeSplitter) super.getLogic();
    }

    protected void setLogic(NodeSplitter nodeSplitter) {
        super.setLogic(nodeSplitter);
    }
}
