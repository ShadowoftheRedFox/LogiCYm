package com.pjava.src.UI.components.gates;

import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.cables.NodeSplitter;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class UINodeSplitter extends UIGate {
    @FXML
    private Rectangle body;
    @FXML
    private AnchorPane self;

    private Line inputLine;
    private List<Line> outputLines = new ArrayList<>();
    private int outputCount = 2;
    private int busSize = 1;

    public static UINodeSplitter create() {
        return (UINodeSplitter) UIElement.create("UINodeSplitter");
    }

    @FXML
    private void initialize() {
        try {
            setLogic(new NodeSplitter(outputCount, busSize));
            createInputLine();
            createOutputLines();

            body.setOnMousePressed(event -> pressed(event));
            body.setOnMouseReleased(event -> released(event));
            body.setOnMouseDragged(event -> dragged(event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createInputLine() {
        inputLine = new Line();
        inputLine.setVisible(false);
        inputLine.setStrokeWidth(2);
        self.getChildren().add(inputLine);
    }

    private void createOutputLines() {
        for (int i = 0; i < 32; i++) {
            Line line = new Line();
            line.setVisible(i < outputCount);
            line.setStrokeWidth(2);
            self.getChildren().add(line);
            outputLines.add(line);
        }
    }

    @Override
    public NodeSplitter getLogic() {
        return (NodeSplitter) super.getLogic();
    }

    public void changeBusSize(int newBusSize) {
        try {
            busSize = newBusSize;
            getLogic().changeBusSize(newBusSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOutputCount(int count) {
        if (count < 1 || count > 32) {
            throw new IllegalArgumentException("Output count must be between 1 and 32");
        }

        try {
            if (count < outputCount) {
                NodeSplitter newNodeSplitter = new NodeSplitter(count, busSize);
                setLogic(newNodeSplitter);
            } else if (count > outputCount) {
                getLogic().addOutput(count - outputCount);
            }

            outputCount = count;
            updateOutputVisibility();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOutputVisibility() {
        for (int i = 0; i < outputLines.size(); i++) {
            outputLines.get(i).setVisible(i < outputCount);
        }
    }

    public Line getInputLine() {
        return inputLine;
    }

    public Line getOutputLine(int index) {
        if (index >= 0 && index < outputLines.size()) {
            return outputLines.get(index);
        }
        return null;
    }
}
