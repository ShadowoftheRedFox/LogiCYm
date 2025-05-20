package com.pjava.src.UI.components.cables;

import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.cables.Splitter;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class UISplitter extends UIGate {
    @FXML
    private Rectangle body;
    @FXML
    private AnchorPane self;

    private Line inputLine;
    private List<Line> outputLines = new ArrayList<>();
    private int busSize = 4;

    public static UISplitter create() {
        return (UISplitter) UIElement.create("UISplitter");
    }

    @FXML
    private void initialize() {
        try {
            setLogic(new Splitter(busSize));
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
            line.setVisible(i < busSize);
            line.setStrokeWidth(2);
            self.getChildren().add(line);
            outputLines.add(line);
        }
    }

    @Override
    public Splitter getLogic() {
        return (Splitter) super.getLogic();
    }

    public void changeBusSize(int newBusSize) {
        try {
            busSize = newBusSize;
            Splitter newSplitter = new Splitter(newBusSize);
            setLogic(newSplitter);
            updateOutputVisibility();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateOutputVisibility() {
        for (int i = 0; i < outputLines.size(); i++) {
            outputLines.get(i).setVisible(i < busSize);
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
