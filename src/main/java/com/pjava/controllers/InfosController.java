package com.pjava.controllers;

import com.pjava.src.UI.Rotation;
import com.pjava.src.UI.components.UICable;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.cables.UIMerger;
import com.pjava.src.UI.components.cables.UINodeSplitter;
import com.pjava.src.UI.components.cables.UISplitter;
import com.pjava.src.UI.components.gates.UIAnd;
import com.pjava.src.UI.components.gates.UINot;
import com.pjava.src.UI.components.gates.UIOr;
import com.pjava.src.UI.components.input.UIButton;
import com.pjava.src.UI.components.input.UIClock;
import com.pjava.src.UI.components.input.UIGround;
import com.pjava.src.UI.components.input.UILever;
import com.pjava.src.UI.components.input.UIPower;
import com.pjava.src.UI.components.output.UIDisplay;
import com.pjava.src.errors.BusSizeException;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class InfosController {
    @FXML
    private ScrollPane self;
    @FXML
    private ChoiceBox<Integer> baseChoice;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField delayInput;
    @FXML
    private ToggleButton enableToggle;
    @FXML
    private TextField inputSize;
    @FXML
    private TextField labelField;
    @FXML
    private TextField outputSize;
    @FXML
    private TextField positionText;
    @FXML
    private ChoiceBox<Rotation> rotationChoice;

    @FXML
    private VBox container;

    private UIElement origin = null;

    @FXML
    private void initialize() {
        // event listeners and fill choice boxes
        if (baseChoice != null) {
            baseChoice.setItems(FXCollections.observableArrayList(2, 8, 10, 16));
            baseChoice.getSelectionModel().select(0);
            baseChoice.setOnAction(event -> {
                setBase(baseChoice.getValue());
            });
        }
        if (colorPicker != null) {
            colorPicker.setOnAction(event -> {
                setColor(colorPicker.getValue());
            });
        }
        if (delayInput != null) {
            delayInput.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    int value = 0;
                    try {
                        value = Integer.parseInt(newValue);
                        setDelay(value);
                    } catch (Exception e) {
                        delayInput.setText(oldValue);
                    }
                }
            });
        }
        if (enableToggle != null) {
            enableToggle.setOnAction(event -> {
                setState(enableToggle.isSelected());
            });
        }
        // BUG looping
        if (inputSize != null) {
            // create a kind of timer to wait before checking value
            final PauseTransition delay = new PauseTransition(Duration.millis(250));
            // create a buffer to look into whithout triggering the original textfield
            final StringProperty buffer = new SimpleStringProperty();
            // when delay finished, fire the buffer
            delay.setOnFinished(event -> buffer.set(inputSize.getText()));
            // listen to change on original field, if timer is not done, restart it
            inputSize.textProperty().addListener((obs, oldValue, newValue) -> {
                delay.playFromStart();
            });

            // when the buffer fires, check value and replace
            buffer.addListener((obs, oldValue, newValue) -> {
                if (oldValue == newValue) {
                    return;
                }
                if (newValue == null || newValue.isBlank()) {
                    inputSize.setText("1");
                }
                int value = 0;
                try {
                    value = Integer.parseInt(newValue);
                    if (!setInputSize(value)) {
                        inputSize.setText(oldValue);
                    }
                } catch (Exception e) {
                    inputSize.setText(oldValue);
                }
                // TODO update bus size in the back
            });
        }
        if (outputSize != null) {
            // create a kind of timer to wait before checking value
            final PauseTransition delay = new PauseTransition(Duration.millis(250));
            // create a buffer to look into whithout triggering the original textfield
            final StringProperty buffer = new SimpleStringProperty();
            // when delay finished, fire the buffer
            delay.setOnFinished(event -> buffer.set(inputSize.getText()));
            // listen to change on original field, if timer is not done, restart it
            inputSize.textProperty().addListener((obs, oldValue, newValue) -> {
                delay.playFromStart();
            });

            // when the buffer fires, check value and replace
            buffer.addListener((obs, oldValue, newValue) -> {
                if (oldValue == newValue) {
                    return;
                }
                if (newValue.isBlank()) {
                    outputSize.setText("1");
                }
                int value = 0;
                try {
                    value = Integer.parseInt(newValue);
                    if (!setOutputSize(value)) {
                        outputSize.setText(oldValue);
                    }
                } catch (Exception e) {
                    outputSize.setText(oldValue);
                }
                // TODO update bus size in the back
            });
        }
        if (labelField != null) {
            labelField.setOnAction(event -> {
                setLabel(labelField.getText());
            });
        }
        // can't receive event from position since it's disabled, only change from code
        if (rotationChoice != null) {
            rotationChoice.setItems(
                    FXCollections.observableArrayList(Rotation.EAST, Rotation.WEST, Rotation.NORTH, Rotation.SOUTH));
            rotationChoice.getSelectionModel().select(0);
            rotationChoice.setOnAction(event -> {
                setRotation(rotationChoice.getValue());
            });
        }
    }

    public void setOrigin(UIElement origin) {
        if (this.origin != null) {
            throw new Error("Infos origin set twice");
        }

        this.origin = origin;
        // remove the useless line for this gate infos
        if (origin instanceof UICable) {
            container.getChildren().removeAll(labelField.getParent());
            container.getChildren().removeAll(rotationChoice.getParent());
            container.getChildren().removeAll(positionText.getParent());
            container.getChildren().removeAll(outputSize.getParent());
            container.getChildren().removeAll(enableToggle.getParent());
            container.getChildren().removeAll(delayInput.getParent());
            container.getChildren().removeAll(baseChoice.getParent());
        } else if (origin instanceof UIClock) {
            container.getChildren().removeAll(outputSize.getParent());
            container.getChildren().removeAll(inputSize.getParent());
            container.getChildren().removeAll(baseChoice.getParent());
        } else if (origin instanceof UIDisplay) {
            container.getChildren().removeAll(enableToggle.getParent());
            container.getChildren().removeAll(outputSize.getParent());
            container.getChildren().removeAll(delayInput.getParent());
        } else if (origin instanceof UIButton) {
            container.getChildren().removeAll(enableToggle.getParent());
            container.getChildren().removeAll(baseChoice.getParent());
        } else if (origin instanceof UILever) {
            container.getChildren().removeAll(inputSize.getParent());
            container.getChildren().removeAll(delayInput.getParent());
            container.getChildren().removeAll(baseChoice.getParent());
        } else if (origin instanceof UIAnd || origin instanceof UIOr || origin instanceof UINot) {
            container.getChildren().removeAll(enableToggle.getParent());
            container.getChildren().removeAll(delayInput.getParent());
            container.getChildren().removeAll(baseChoice.getParent());
        } else if (origin instanceof UIMerger) {
            container.getChildren().removeAll(enableToggle.getParent());
            container.getChildren().removeAll(delayInput.getParent());
            container.getChildren().removeAll(baseChoice.getParent());
        } else if (origin instanceof UISplitter) {
            container.getChildren().removeAll(enableToggle.getParent());
            container.getChildren().removeAll(delayInput.getParent());
            container.getChildren().removeAll(baseChoice.getParent());
        } else if (origin instanceof UINodeSplitter) {
            container.getChildren().removeAll(enableToggle.getParent());
            container.getChildren().removeAll(delayInput.getParent());
            container.getChildren().removeAll(baseChoice.getParent());
        } else if (origin instanceof UIGround || origin instanceof UIPower) {
            container.getChildren().removeAll(enableToggle.getParent());
            container.getChildren().removeAll(inputSize.getParent());
            container.getChildren().removeAll(delayInput.getParent());
            container.getChildren().removeAll(baseChoice.getParent());
        }
        // TODO numeric and schema
    }

    public UIElement getOrigin() {
        return origin;
    }

    public Node getNode() {
        return self;
    }

    public boolean setColor(Color color) {
        return setColor(color, false);
    }

    public boolean setColor(Color color, boolean fromOrigin) {
        if (color == null) {
            return false;
        }
        if (!fromOrigin) {
            origin.setColor(color);
        }
        origin.updateVisuals();
        colorPicker.setValue(color);
        return true;
    }

    public boolean setLabel(String label) {
        return setLabel(label, false);
    }

    public boolean setLabel(String label, boolean fromOrigin) {
        if (label == null) {
            return false;
        }
        if (!fromOrigin) {
            origin.setName(label);
        }
        labelField.setText(label);
        return true;
    }

    public boolean setDelay(int delay) {
        return setDelay(delay, false);
    }

    public boolean setDelay(int delay, boolean fromOrigin) {
        if (delay <= 0) {
            return false;
        }
        if (origin instanceof UIButton) {
            try {
                ((UIButton) origin).getLogic().setDelay(Long.parseLong("" + delay));
                delayInput.setText("" + delay);
                return true;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        if (origin instanceof UIClock) {
            ((UIClock) origin).getLogic().setCycleSpeed(Long.parseLong("" + delay));
            delayInput.setText("" + delay);
            return true;
        }
        return false;
    }

    public boolean setPosition(Point2D point) {
        return setPosition(point, false);
    }

    public boolean setPosition(Point2D point, boolean fromOrigin) {
        if (point == null ||
        // check non negative position (out of bound of the grid)
                point.getX() < 0 || point.getY() < 0) {
            return false;
        }
        if (!fromOrigin) {
            origin.setPosition(point);
        }
        positionText.setText("x: " + point.getX() + ", y:" + point.getY());
        return true;
    }

    public boolean setRotation(Rotation rotation) {
        return setRotation(rotation, false);
    }

    public boolean setRotation(Rotation rotation, boolean fromOrigin) {
        if (rotation == null) {
            return false;
        }
        if (!(origin instanceof UICable)) {
            if (!fromOrigin) {
                origin.setRotation(rotation);
            }
            rotationChoice.getSelectionModel().select(rotation);
            return true;
        }
        return false;
    }

    public boolean setState(boolean state) {
        return setState(state, false);
    }

    public boolean setState(boolean state, boolean fromOrigin) {
        if (origin instanceof UIClock) {
            ((UIClock) origin).getLogic().setEnabled(state);
            enableToggle.setSelected(state);
            enableToggle.setText(state ? "Enabled" : "Enable");
            return true;
        }
        if (origin instanceof UILever && ((UILever) origin).getLogic().getState(0) != state) {
            ((UILever) origin).getLogic().flip();
            enableToggle.setSelected(state);
            enableToggle.setText(state ? "Enabled" : "Enable");
            return true;
        }
        return false;
    }

    public boolean setInputSize(int size) {
        return setInputSize(size, false);
    }

    public boolean setInputSize(int size, boolean fromOrigin) {
        if (BusSizeException.isBusSizeException(size)) {
            return false;
        }

        // TODO method in the back
        inputSize.setText("" + size);
        return false;
    }

    public boolean setOutputSize(int size) {
        return setOutputSize(size, false);
    }

    public boolean setOutputSize(int size, boolean fromOrigin) {
        if (BusSizeException.isBusSizeException(size)) {
            return false;
        }

        // TODO method in the back
        outputSize.setText("" + size);
        return false;
    }

    public boolean setBase(int base) {
        return setBase(base, false);
    }

    public boolean setBase(int base, boolean fromOrigin) {
        if (base != 2 && base != 8 && base != 10 && base != 16) {
            return false;
        }
        if (!(origin instanceof UIDisplay)) {
            try {
                ((UIDisplay) origin).getLogic().setBaseOutput(base);
                baseChoice.getSelectionModel().select(base);
                return true;
            } catch (Exception e) {
                throw new Error(e);
            }
        }
        // TODO numeric gate
        return false;
    }
}
