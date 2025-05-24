package com.pjava.src.UI.components;

import java.util.ArrayList;

import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.components.gates.Schema;

import java.io.IOException;
import java.nio.file.Path;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * Controller for UISchema.fxml
 * Handles the visual representation of a Schema gate
 */
public class UISchema extends UIGate {

    @FXML
    private Rectangle mainRectangle;

    @FXML
    private Label schemaNameLabel;

    @FXML
    private Pane inputPinsContainer;

    @FXML
    private Pane outputPinsContainer;

    /**
     * Constructor with schema file path
     *
     * @param schemaFilePath path to the schema file
     */
    public static UISchema create(Path schemaPath) {
        try {
            // load the gate's fxml
            FXMLLoader loader = new FXMLLoader(UIElement.class.getResource("/fxml/components/UISchema.fxml"));
            Node node = loader.load();
            // and get the controller instance
            UISchema controller = loader.getController();
            node.setUserData(controller);
            // TODO javadoc + check info pour compatibilité avec schéma
            // create a gate infos instance
            FXMLLoader infosLoader = new FXMLLoader(UIElement.class.getResource("/fxml/ComponentInfos.fxml"));
            infosLoader.load();
            // and add it to the gate controller
            controller.infos = infosLoader.getController();
            controller.infos.setOrigin(controller);

            // return the controller
            try {
                controller.setLogic(new Schema(schemaPath.toString()));
                System.out.println(controller.getLogic().getInnerCircuit().getAllGates());
            } catch (Exception e) {
                throw new Error(e);
            }
            return controller;
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @FXML
    private void initialize() {
        System.out.println("Schema Initialisation!");
        // Initialize pins based on the schema's input/output requirements
        initializePinsFromLogic();
        // Set mouse event handlers for dragging
        mainRectangle.setOnMousePressed(event -> pressed(event));
        mainRectangle.setOnMouseReleased(event -> released(event));
        mainRectangle.setOnMouseDragged(event -> dragged(event));
    }

    /**
     * Initialize pins based on the logic gate's requirements
     */
    private void initializePinsFromLogic() {
        Schema schema = getLogic();
        if (schema == null)
            return;

        // Clear existing pins
        getInputPins().clear();
        getOutputPins().clear();
        inputPinsContainer.getChildren().clear();
        outputPinsContainer.getChildren().clear();

        // Create input pins
        int inputCount = schema.getInputNumber();
        for (int i = 0; i < inputCount; i++) {
            Pin inputPin = new Pin(false);
            inputPin.setAsInput(true);
            inputPin.originController = this;

            // Position the pin
            double yPos = (60.0 / (inputCount + 1)) * (i + 1) - 10;
            inputPin.setLayoutY(yPos);
            inputPin.setLayoutX(-10);

            inputPinsContainer.getChildren().add(inputPin);
            getInputPins().add(inputPin);
        }

        // Create output pins
        int outputCount = schema.getOutputNumber();
        for (int i = 0; i < outputCount; i++) {
            Pin outputPin = new Pin(false);
            outputPin.setAsInput(false);
            outputPin.originController = this;

            // Position the pin
            double yPos = (60.0 / (outputCount + 1)) * (i + 1) - 10;
            outputPin.setLayoutY(yPos);
            outputPin.setLayoutX(10);

            outputPinsContainer.getChildren().add(outputPin);
            getOutputPins().add(outputPin);
        }

        // Adjust size based on pin count
        adjustSizeForPins(inputCount, outputCount);

        // Update schema name label
        if (schema.getName() != null) {
            schemaNameLabel.setText(schema.getName());
        }
    }

    /**
     * Adjust the size of the schema component based on the number of pins
     *
     * @param inputCount  number of input pins
     * @param outputCount number of output pins
     */
    private void adjustSizeForPins(int inputCount, int outputCount) {
        int maxPins = Math.max(inputCount, outputCount);

        // Minimum height to accommodate pins with some spacing
        double minHeight = Math.max(80.0, maxPins * 20.0 + 20.0);

        // Update the main rectangle
        mainRectangle.setHeight(minHeight - 20.0);

        // Update the container heights
        inputPinsContainer.setPrefHeight(minHeight - 20.0);
        outputPinsContainer.setPrefHeight(minHeight - 20.0);
    }

    /**
     * Set the schema name displayed on the component
     *
     * @param name the name to display
     */
    public void setSchemaName(String name) {
        schemaNameLabel.setText(name);
        Schema schema = getLogic();
        if (schema != null) {
            try {
                schema.setName(name);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid schema name: " + e.getMessage());
            }
        }
    }

    /**
     * Get the schema name
     *
     * @return the current schema name
     */
    public String getSchemaName() {
        return schemaNameLabel.getText();
    }

    @Override
    public Schema getLogic() {
        return (Schema) super.getLogic();
    }

    /**
     * Set the logic to the schema gate it's supposed to have
     *
     * @param schema a Schema gate
     */
    protected void setLogic(Schema schema) {
        super.setLogic(schema);
    }

    /**
     * Get a specific input pin (using Gate's getter pattern)
     *
     * @param index which input pin (0 to N-1)
     * @return the selected input pin
     */
    @Override
    public Pin getPinInput(int index) {
        if (index >= 0 && index < getInputPins().size()) {
            return getInputPins().get(index);
        }
        return null;
    }

    /**
     * Get a specific output pin (using Gate's getter pattern)
     *
     * @param index which output pin (0 to N-1)
     * @return the selected output pin
     */
    @Override
    public Pin getPinOutput(int index) {
        if (index >= 0 && index < getOutputPins().size()) {
            return getOutputPins().get(index);
        }
        return null;
    }
}
