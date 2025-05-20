package com.pjava.src.UI.components.cables;

import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.cables.Merger;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class UIMerger extends UIGate {
    @FXML
    private Rectangle body;
    @FXML
    private AnchorPane self;

    private List<Line> inputLines = new ArrayList<>();
    private Line outputLine;
    private int[] busSizes = new int[32]; // Max 32 inputs

    public static UIMerger create() {
        return (UIMerger) UIElement.create("UIMerger");
    }

    @FXML
    private void initialize() {
        try {
            setLogic(new Merger(busSizes));
            createInputLines();
            createOutputLine();

            body.setOnMousePressed(event -> pressed(event));
            body.setOnMouseReleased(event -> released(event));
            body.setOnMouseDragged(event -> dragged(event));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createInputLines() {
        for (int i = 0; i < 32; i++) {
            Line line = new Line();
            line.setVisible(false);
            line.setStrokeWidth(2);
            self.getChildren().add(line);
            inputLines.add(line);
        }
    }

    private void createOutputLine() {
        outputLine = new Line();
        outputLine.setVisible(false);
        outputLine.setStrokeWidth(2);
        self.getChildren().add(outputLine);
    }

    @Override
    public Merger getLogic() {
        return (Merger) super.getLogic();
    }

    public void changeBusSize(int inputIndex, int busSize) {
        if (inputIndex >= 0 && inputIndex < busSizes.length) {
            busSizes[inputIndex] = busSize;
            try {
                Merger newMerger = new Merger(busSizes);
                setLogic(newMerger);
                updateInputVisibility();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateInputVisibility() {
        for (int i = 0; i < inputLines.size(); i++) {
            inputLines.get(i).setVisible(busSizes[i] > 0);
        }
    }

    public Line getInputLine(int index) {
        if (index >= 0 && index < inputLines.size()) {
            return inputLines.get(index);
        }
        return null;
    }

    public Line getOutputLine() {
        return outputLine;
    }
}
