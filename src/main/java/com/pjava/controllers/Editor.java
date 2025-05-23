package com.pjava.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JFileChooser;

import org.json.JSONArray;

import com.pjava.src.UI.SceneManager;
import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UICable;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
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
import com.pjava.src.components.Circuit;
import com.pjava.src.components.Gate;
import com.pjava.src.document.SimulationFileLoader;
import com.pjava.src.utils.UIUtils;
import com.pjava.src.utils.UIUtils.ValidationAnwser;
import com.pjava.src.utils.UtilsSave;
import com.pjava.src.utils.SaveData;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Editor extends VBox {
    @FXML
    public GridPane gridPane;
    @FXML
    public ScrollPane viewScroll;
    @FXML
    private AnchorPane container;
    @FXML
    private VBox infosContainer;
    @FXML
    private VBox schemaContainer;

    // #region Menu items
    @FXML
    private MenuItem copyButton;
    @FXML
    private MenuItem cutButton;
    @FXML
    private MenuItem deleteButton;
    @FXML
    private MenuItem disableSimulationButton;
    @FXML
    private MenuItem enableSimulationButton;
    @FXML
    private Text loadFromInputHelp;
    @FXML
    private MenuItem loadInputsButton;
    @FXML
    private MenuItem newFileButton;
    @FXML
    private MenuItem openFileButton;
    @FXML
    private Menu openRecentButton;
    @FXML
    private MenuItem pasteButton;
    @FXML
    private MenuItem preferencesButton;
    @FXML
    private MenuItem quitButton;
    @FXML
    private MenuItem redoButton;
    @FXML
    private MenuItem saveAsButton;
    @FXML
    private MenuItem saveButton;
    @FXML
    private MenuItem selectAllButton;
    @FXML
    private MenuItem undoButton;
    @FXML
    private MenuItem unselectAllButton;
    @FXML
    private RadioMenuItem simulationSpeed1;
    @FXML
    private RadioMenuItem simulationSpeed10;
    @FXML
    private RadioMenuItem simulationSpeed20;
    @FXML
    private RadioMenuItem simulationSpeed5;
    @FXML
    private RadioMenuItem simulationSpeed60;
    @FXML
    private RadioMenuItem simulationSpeed1k;
    @FXML
    private RadioMenuItem simulationSpeedUnlimited;
    // #endregion

    /**
     * scenemanager if we ever want to change scene
     */
    private SceneManager manager;

    /**
     * Rectangle that show the current selectionned area
     */
    private Rectangle selectionRectangle = null;
    /**
     * The point where the selection started.
     */
    private Point2D selectionStart = null;

    /**
     * list of nodes selected
     */
    private ArrayList<Node> selectedNodes = new ArrayList<Node>();

    /**
     * Copied elements are stored here.
     */
    private ArrayList<UIElement> clipboardElements = new ArrayList<>();

    /**
     * The last intput pin pressed. See {@link #createCableBetweenPins(Pin, Pin)}
     */
    private Pin lastInputPinPressed = null;
    /**
     * The last output pin pressed. See {@link #createCableBetweenPins(Pin, Pin)}
     */
    private Pin lastOutputPinPressed = null;

    /**
     * list of element in the page
     */
    private Circuit editedCircuit = new Circuit("Unamed circuit");

    /**
     * verification if they don'save
     */
    private boolean unsavedChanges = false;

    /**
     * it setup the view section
     *
     * @param manager
     */
    public Editor(SceneManager manager) {
        this.manager = manager;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Editor.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    @FXML
    private void initialize() {
        manager.getScene().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
                    Number newSceneWidth) {
                resizeGrid();
            }
        });
        manager.getScene().heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
                    Number newSceneHeight) {
                resizeGrid();
            }
        });
        viewScroll.setOnScroll(event -> resizeGrid());

        setupElementSelection();
        resizeGrid();

        // #region Listeners
        container.setOnMousePressed(event -> pressSelection(event));
        container.setOnMouseReleased(event -> endSelection(event));
        container.setOnMouseDragged(event -> dragSelection(event));

        unselectAllButton.setOnAction(event -> {
            clearSelection();
        });

        manager.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                closeEditor();
            };
        });
        quitButton.setOnAction(event -> {
            closeEditor();
        });

        deleteButton.setOnAction(event -> {
            deleteSelectedElements(event);
        });
        manager.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DELETE:
                    deleteSelectedElements(null);
                    break;
                case BACK_SPACE:
                    deleteSelectedElements(null);
                    break;
                case S:
                    if (event.isControlDown())
                        saveEditor(false);
                    break;
                case A:
                    if (event.isControlDown())
                        selectAllElements(null);
                default:
                    break;
            }
        });

        newFileButton.setOnAction(event -> {
            saveEditor(true);
            resetEditor();
        });

        saveButton.setOnAction(event -> {
            saveEditor(false);
        });

        saveAsButton.setOnAction(event -> {
            saveEditor(true);

        });

        openFileButton.setOnAction(event -> {
            try {
                File file = UtilsSave.openSaveFolder().showOpenDialog(manager.getStage());
                resetEditor();

                editedCircuit.loadGatesFromFile(file.getPath());

                addGates(editedCircuit.getAllGates(), editedCircuit.getAllGatesData());

            } catch (Exception e) {
                UIUtils.errorPopup(e.getMessage());
            }
        });

        selectAllButton.setOnAction(event -> {
        });

        unselectAllButton.setOnAction(event -> {
            clearSelection();
        });

        // set a listener that will automaticaly change unselect when deleteButton
        // disable state is changed
        deleteButton.disableProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                    Boolean newValue) {
                unselectAllButton.setDisable(newValue);
            };
        });

        initializeSchema();

        loadInputsButton.setOnAction(event -> {
            loadSimulationFile();
        });

        enableSimulationButton.setOnAction(event -> {
            toggleSimulation(true);
        });
        disableSimulationButton.setOnAction(event -> {
            toggleSimulation(false);
        });

        simulationSpeed1.setOnAction(event -> {
            setSimulationSpeed(1);
        });
        simulationSpeed10.setOnAction(event -> {
            setSimulationSpeed(10);
        });
        simulationSpeed20.setOnAction(event -> {
            setSimulationSpeed(20);
        });
        simulationSpeed5.setOnAction(event -> {
            setSimulationSpeed(5);
        });
        simulationSpeed60.setOnAction(event -> {
            setSimulationSpeed(60);
        });
        simulationSpeed1k.setOnAction(event -> {
            setSimulationSpeed(1000);
        });
        simulationSpeedUnlimited.setOnAction(event -> {
            setSimulationSpeed(-1);
        });
        // #endregion

        // #region Help
        loadFromInputHelp.setText(
                "Load from inputs button enable the user to give a CSV file containing a list of inputs to emulate.\n" +
                        "The file should have the first row as the name of each input, separated by a comma \";\".\n" +
                        "Each line contains either \"1\" or \"0\" for each columns, also separated by a comma.\n" +
                        "The file should have the first row as the name of each input, separated by a comma \";\".\n" +
                        "Each line contains either \"1\" or \"0\" for each columns, also separated by a comma. It should look something like this:\n\n"
                        +
                        "A;B;C\n" +
                        "1;0;0\n" +
                        "1;0;1\n" +
                        "0;1;0\n");
        // #endregion

        initializeSchema();
        setUnsavedChanges(false);
    }

    private void initializeSchema() {
        // TODO get all schema and add the buttons here with event listeners of the said
        // schema

        ArrayList<Path> paths = UtilsSave.list(UtilsSave.saveFolder);
        if (paths == null) {
            throw new Error("Can't find save folder");
        }

        ArrayList<Path> files = new ArrayList<Path>();
        ArrayList<Path> folders = new ArrayList<Path>();

        for (Path path : paths) {
            if (path.toString().endsWith(UtilsSave.saveExtension)) {
                System.out.println("schema file: " + path);
                files.add(path);
            } else {
                System.out.println("schema sub folder: " + path);
                folders.add(path);
            }
        }

        // TODO now, create panels and add buttons inside depending on subfolders

        // creating buttons and adding them to the schema container
        for (Path file : files) {
            Button button = new Button(file.getFileName().toString().replaceFirst("[.][^.]+$", ""));
            button.setTextAlignment(TextAlignment.CENTER);
            button.setAlignment(Pos.CENTER);
            button.setMaxSize(Double.MAX_VALUE, USE_COMPUTED_SIZE);
            button.setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
            button.setMinSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
            schemaContainer.getChildren().add(button);

            button.setOnAction(event -> {
                System.out.println("Schema " + button.getText());
                // TODO ui schema and add to container
            });
        }
    }

    /**
     * Connect gate wit the two selected pins
     */
    private void createCableBetweenPins(Pin source, Pin target) {
        if (source == null || target == null) {
            System.out.println("no pin pair selected");
            return;
        }

        // check pins are connected to gates controller
        UIGate sourceGate = (UIGate) source.originController;
        UIGate targetGate = (UIGate) target.originController;

        if (sourceGate == null || targetGate == null) {
            throw new Error("Stand alone pin");
        }

        // TODO case if either gate already has a one sided cable connected

        UICable sourceCable = sourceGate.getCableFromPin(source);
        UICable targetCable = targetGate.getCableFromPin(target);

        // check if either gate are full at the given pins
        if (sourceCable != null && targetCable != null) {
            System.out.println("Pins already connected");
        } else
        // check if source has a spot
        if (sourceCable != null && targetCable == null) {
            if (sourceCable.getOutputGate() != null) {
                System.out.println("Cable not able to connect");
                return;
            }

            sourceCable.connect(source, target, sourceGate, targetGate);
        } else
        // check if target has a spot
        if (sourceCable == null && targetCable != null) {
            if (targetCable.getOutputGate() != null) {
                System.out.println("Cable not able to connect");
                return;
            }

            targetCable.connect(source, target, sourceGate, targetGate);
            return;
        } else {
            // create a new cable controller
            UICable cableController = UICable.create();
            // connect cable
            cableController.connect(source, target, sourceGate, targetGate);
            container.getChildren().add(cableController.getNode());
            cableController.getNode().toBack();

            // FIXME selection doesn't work
            selectElement(cableController);
            cableController.getLine().setOnMousePressed(event -> {
                replaceInfos(cableController.getInfos().getNode());
            });
        }

        // remove color if yellow and forget the pins
        if (lastInputPinPressed.getColor() == Color.YELLOW) {
            lastInputPinPressed.setColor(Color.BLUE);
        }
        if (lastOutputPinPressed.getColor() == Color.YELLOW) {
            lastOutputPinPressed.setColor(Color.RED);
        }

        lastInputPinPressed = null;
        lastOutputPinPressed = null;
    }

    // #region Functions

    private void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
        manager.getStage()
                .setTitle("LogiCYm: " + (unsavedChanges ? "Unsaved changes - " : "") + editedCircuit.getName());
        saveButton.setDisable(!unsavedChanges);
    }

    /**
     * Change the speed simulation
     *
     * @param value the value of hz
     */
    private void setSimulationSpeed(int value) {
        // TODO edit simulation speed somewhere
    }

    private void saveEditor(Boolean defaultSaving) {
        selectAllElements(null);
        try {
            JSONArray data = new JSONArray();
            selectedNodes.forEach(node -> {
                UIElement element = (UIElement) node.getUserData();
                data.put(new SaveData(element.getLogic().uuid(), element.getName(), element.getColor(),
                        element.getPosition(), element.getRotation()).toJson());
            });

            if (defaultSaving) {
                JFileChooser c = UtilsSave.openAndSaveInFolder();
                int result = c.showOpenDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File saveFile = c.getSelectedFile();
                    editedCircuit.save(saveFile.getPath(), data);
                }
            } else
                editedCircuit.save(data);
            setUnsavedChanges(false);
        } catch (Exception e) {
            UIUtils.errorPopup(e.getMessage());
        }
    }

    public void resetEditor() {
        selectAllElements(null);
        deleteSelectedElements(null);
    }

    public void addGate(Gate gate, String label, SaveData data) throws Exception {
        addGate(gate.getClass().getSimpleName(), label, data);
    }

    public void addGate(String type, SaveData data) throws Exception {
        addGate(type, data);
    }

    public void addGate(String type, String label, SaveData data) throws Exception {
        UIGate elementController;
        switch (type) {
            case "Power":
                elementController = (UIPower) UIGate.create("UIPower");
                if (label != "")
                    elementController.setName(label);
                break;
            case "Ground":
                elementController = (UIGround) UIGate.create("UIGround");
                if (label != "")
                    elementController.setName(label);
                break;
            case "Lever":
                elementController = (UILever) UIGate.create("UILever");
                if (label != "")
                    elementController.setName(label);
                break;
            case "Button":
                elementController = (UIButton) UIGate.create("UIButton");
                if (label != "")
                    elementController.setName(label);
                break;
            case "Clock":
                elementController = (UIClock) UIGate.create("UIClock");
                if (label != "")
                    elementController.setName(label);
                break;
            case "Display":
                elementController = (UIDisplay) UIGate.create("UIDisplay");
                if (label != "")
                    elementController.setName(label);
                break;
            // case "Numeric":
            // elementController = (UINumeric) UIGate.create("UINumeric");
            // if (label != "")
            // elementController.setName(label);
            // break;
            case "Not":
                elementController = (UINot) UIGate.create("UINot");
                if (label != "")
                    elementController.setName(label);
                break;
            case "And":
                elementController = (UIAnd) UIGate.create("UIAnd");
                if (label != "")
                    elementController.setName(label);
                break;
            case "Or":
                elementController = (UIOr) UIGate.create("UIOr");
                if (label != "")
                    elementController.setName(label);
                break;
            case "NodeSplitter":
                elementController = (UINodeSplitter) UIGate.create("UINodeSplitter");
                if (label != "")
                    elementController.setName(label);
                break;
            case "Splitter":
                elementController = (UISplitter) UIGate.create("UISplitter");
                if (label != "")
                    elementController.setName(label);
                break;
            case "Merger":
                elementController = (UIMerger) UIGate.create("UIMerger");
                if (label != "")
                    elementController.setName(label);
                break;

            default:
                throw new Exception("ElementController of type " + type + " not found");
        }

        // elementController.setPosition(data.position);
        // elementController.setRotation(data.rotation);
        // elementController.setColor(data.color);
        container.getChildren().add(elementController.getNode());
        pinsListener(elementController);
        elementController.getNode().setOnMousePressed(mouseEvent -> {
            selectElement(elementController);
            replaceInfos(elementController.getInfos().getNode());
        });
    }

    public void addGates(HashMap<String, Gate> gates, ArrayList<SaveData> data) {
        Collection<UIElement> uiGates = new ArrayList<>();

        gates.forEach((label, gate) -> {
            try {
                data.forEach(e -> System.out.println(e.label));
                SaveData gateData = data.stream().filter(e -> Integer.parseInt(e.label) == gate.uuid()).findFirst().get();
                addGate(gate, label, gateData);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

    }

    public void resizeGrid() {
        final double paneWidth = viewScroll.getWidth();
        final double paneHeight = viewScroll.getHeight();

        // calculate the wanted amounf of rows and columns
        int wantedColumns = (int) Math.round((paneWidth / UIElement.baseSize) + 0.5d);
        int wantedRows = (int) Math.round((paneHeight / UIElement.baseSize) + 0.5d);

        // add more cols/rows if we scroll to the max
        if (viewScroll.getVvalue() > 0.9) {
            wantedRows = gridPane.getRowCount() + 5;
        }

        if (viewScroll.getHvalue() > 0.9) {
            wantedColumns = gridPane.getColumnCount() + 5;
        }

        // create the constraints
        final ColumnConstraints col = new ColumnConstraints(UIElement.baseSize);
        col.setHalignment(HPos.CENTER);
        final RowConstraints row = new RowConstraints(UIElement.baseSize);
        row.setValignment(VPos.CENTER);

        for (int x = gridPane.getRowCount(); x < wantedRows; x++) {
            gridPane.getRowConstraints().add(row);
        }
        for (int y = gridPane.getColumnCount(); y < wantedColumns; y++) {
            gridPane.getColumnConstraints().add(col);
        }
    }
    // #endregion

    private void replaceInfos(Node content) {
        // clear container
        infosContainer.getChildren().clear();
        if (content != null) {
            // add content to the container
            infosContainer.getChildren().add(content);
        }
    }

    /**
     * when editor is close make propose if they want to save the circuit
     */
    private void closeEditor() {
        if (unsavedChanges) {
            Consumer<ValidationAnwser> callback = (res) -> {
                if (res == ValidationAnwser.APPROVED) {
                    try {
                        saveEditor(true);
                    } catch (Exception e) {
                        UIUtils.errorPopup(e.getMessage());
                    }
                    Platform.exit();
                } else if (res == ValidationAnwser.DENIED) {
                    // don't save
                    Platform.exit();
                }
            };

            UIUtils.validationPopup("There are unsaved changes.\nDo you wish to save them?", callback, "Save changes",
                    "Dismiss changes", "Cancel");
        } else {
            Platform.exit();
        }
    }

    /**
     * open the file of the computer and select a file for simulate input
     */
    private void loadSimulationFile() {
        // Get the primary stage from the scene
        Stage stage = (Stage) this.getScene().getWindow();

        // Open file chooser dialog and get the saved file path
        Path filePath = SimulationFileLoader.loadSimulationFile(stage);

        if (filePath != null) {
            // Display loading message
            System.out.println("Loading simulation data from: " + filePath);

            // Run the simulation
            boolean success = SimulationFileLoader.runSimulation(filePath);

            if (success) {
                System.out.println("Simulation loaded and running successfully!");
                // Enable the disable simulation button
                disableSimulationButton.setDisable(false);
                // Disable the enable simulation button
                enableSimulationButton.setDisable(true);
            } else {
                System.err.println("Failed to run simulation.");
            }
        } else {
            System.out.println("Simulation file selection canceled.");
        }
    }

    /**
     * register the pins for later connection
     *
     * @param gate the gate related to the pins
     */
    private void pinsListener(UIGate gate) {
        for (Pin pin : gate.getInputPins()) {
            pin.setOnPressed(event -> {
                if (lastInputPinPressed != null) {
                    lastInputPinPressed.setColor(Color.BLUE);
                }
                lastInputPinPressed = pin;
                pin.setColor(Color.YELLOW);
                createCableBetweenPins(lastOutputPinPressed, lastInputPinPressed);
            });
        }
        for (Pin pin : gate.getOutputPins()) {
            pin.setOnPressed(event -> {
                if (lastOutputPinPressed != null) {
                    lastOutputPinPressed.setColor(Color.RED);
                }
                lastOutputPinPressed = pin;
                pin.setColor(Color.YELLOW);
                createCableBetweenPins(lastOutputPinPressed, lastInputPinPressed);
            });
        }
    }
    // #endregion

    // #region Gate spawn
    // BUG bad cable selection
    // TODO spawn gate on center of teh scroll pane (with current scroll value)
    // BUG deconnection
    @FXML
    public void clickAnd(ActionEvent event) {
        System.out.println("Click And!");
        UIAnd andController = (UIAnd) UIElement.create("UIAnd");
        Node and = andController.getNode();
        container.getChildren().add(and);

        pinsListener(andController);
        andController.getNode().setOnMousePressed(mouseEvent -> {
            selectElement(andController);
            replaceInfos(andController.getInfos().getNode());
        });
    }

    @FXML
    public void clickOr(ActionEvent event) {
        System.out.println("Click Or!");
        UIOr orController = (UIOr) UIElement.create("UIOr");
        Node or = orController.getNode();
        container.getChildren().add(or);

        pinsListener(orController);
        orController.getNode().setOnMousePressed(mouseEvent -> {
            selectElement(orController);
            replaceInfos(orController.getInfos().getNode());
        });
    }

    @FXML
    public void clickNot(ActionEvent event) {
        System.out.println("Click Not!");
        UINot notController = (UINot) UIElement.create("UINot");
        Node not = notController.getNode();
        container.getChildren().add(not);

        pinsListener(notController);
        notController.getNode().setOnMousePressed(mouseEvent -> {
            selectElement(notController);
            replaceInfos(notController.getInfos().getNode());
        });
    }

    @FXML
    public void clickButton(ActionEvent event) {
        System.out.println("Click Button!");
        UIButton buttonController = (UIButton) UIElement.create("UIButton");
        Node button = buttonController.getNode();
        container.getChildren().add(button);

        pinsListener(buttonController);
        buttonController.getNode().setOnMousePressed(mouseEvent -> {
            selectElement(buttonController);
            replaceInfos(buttonController.getInfos().getNode());
        });
    }

    @FXML
    public void clickClock(ActionEvent event) {
        System.out.println("Click clock!");
        UIClock clockController = (UIClock) UIElement.create("UIClock");
        Node clock = clockController.getNode();
        container.getChildren().add(clock);

        pinsListener(clockController);
        clockController.getNode().setOnMousePressed(mouseEvent -> {
            selectElement(clockController);
            replaceInfos(clockController.getInfos().getNode());
        });
    }

    @FXML
    public void clickLever(ActionEvent event) {
        System.out.println("Click lever!");
        UILever leverController = (UILever) UIElement.create("UILever");
        Node lever = leverController.getNode();
        container.getChildren().add(lever);

        pinsListener(leverController);
        leverController.getNode().setOnMousePressed(mouseEvent -> {
            selectElement(leverController);
            replaceInfos(leverController.getInfos().getNode());
        });
    }

    @FXML
    public void clickPower(ActionEvent event) {
        System.out.println("Click power!");
        UIPower powerController = (UIPower) UIElement.create("UIPower");
        Node power = powerController.getNode();
        container.getChildren().add(power);

        pinsListener(powerController);
        powerController.getNode().setOnMousePressed(mouseEvent -> {
            selectElement(powerController);
            replaceInfos(powerController.getInfos().getNode());
        });
    }

    @FXML
    public void clickGround(ActionEvent event) {
        System.out.println("Click ground!");
        UIGround groundController = (UIGround) UIElement.create("UIGround");
        Node ground = groundController.getNode();
        container.getChildren().add(ground);

        pinsListener(groundController);
        groundController.getNode().setOnMousePressed(mouseEvent -> {
            selectElement(groundController);
            replaceInfos(groundController.getInfos().getNode());
        });
    }

    @FXML
    public void clickDisplay(ActionEvent event) {
        System.out.println("Click display!");
        UIDisplay displayController = (UIDisplay) UIElement.create("UIDisplay");
        Node display = displayController.getNode();
        container.getChildren().add(display);

        pinsListener(displayController);
        displayController.getNode().setOnMousePressed(mouseEvent -> {
            selectElement(displayController);
            replaceInfos(displayController.getInfos().getNode());
        });
    }

    @FXML
    public void clickCable(ActionEvent event) {
        setUnsavedChanges(true);
        System.out.println("Click Cable!");
        UICable cableController = (UICable) UIElement.create("UICable");
        Node and = cableController.getNode();
        container.getChildren().add(and);

        cableController.getLine().setOnMousePressed(mouseEvent -> {
            selectElement(cableController);
            replaceInfos(cableController.getInfos().getNode());
        });
    }

    @FXML
    public void clickMerger(ActionEvent event) {
        setUnsavedChanges(true);
        System.out.println("Click Merger!");
    }

    @FXML
    public void clickSplitter(ActionEvent event) {
        setUnsavedChanges(true);
        System.out.println("Click Splitter!");
    }

    @FXML
    public void clickSchema(ActionEvent event) {
        setUnsavedChanges(true);
        System.out.println("Click Schema!");
    }
    // #endregion

    // #region Selection
    /**
     * change color around the gate when element is selected and canceled the
     * previous selection
     *
     * @param event clic on element
     */
    private void pressSelection(MouseEvent event) {
        // make sure selection always start on the grid
        if (!gridPane.equals(event.getTarget()) &&
                !(event.getTarget() instanceof Pane)) {
            return;
        }

        if (selectionRectangle != null) {
            endSelection(event);
        }

        clearSelection();

        selectionRectangle = new Rectangle();
        selectionStart = new Point2D(event.getX(), event.getY());

        selectionRectangle.setLayoutX(event.getX());
        selectionRectangle.setLayoutY(event.getY());

        selectionRectangle.setFill(Color.BLUE);
        selectionRectangle.setStroke(Color.BLUEVIOLET);
        selectionRectangle.strokeWidthProperty().set(3);
        selectionRectangle.setOpacity(0.3);

        container.getChildren().add(selectionRectangle);
        selectionRectangle.toFront();
    }

    /**
     * change the position and the size when element is selected and slide
     *
     * @param event
     */
    private void dragSelection(MouseEvent event) {
        // drag detected but not a selection
        if (selectionRectangle == null) {
            return;
        }
        double width = event.getX() - selectionStart.getX();
        double height = event.getY() - selectionStart.getY();

        if (width > 0) {
            selectionRectangle.setWidth(width);
        } else if (width < 0) {
            selectionRectangle.setLayoutX(event.getX());
            selectionRectangle.setWidth(-width);
        }

        if (height > 0) {
            selectionRectangle.setHeight(height);
        } else if (height < 0) {
            selectionRectangle.setLayoutY(event.getY());
            selectionRectangle.setHeight(-height);
        }
    }

    /**
     * end the selection of the element when is unselected
     *
     * @param event
     */

    /**
     * grabs the element selected
     *
     * @param element the element selected
     */
    private void selectElement(UIElement element) {
        clearSelection();
        if (element != null) {
            if (element instanceof UIGate) {
                selectedNodes.add(element.getNode());
                highlightSelectedNode(element.getNode());
            } else if (element instanceof UICable) {
                highlightSelectedNode(((UICable) element).getLine());
                selectedNodes.add(((UICable) element).getLine());
            }
        }
        updateSelectionActions();
    }

    /**
     * grabs a group of element selected
     *
     * @param array collection of element selected
     */
    private void selectElement(Collection<UIElement> array) {
        clearSelection();
        if (array != null) {
            for (UIElement element : array) {
                if (element != null) {
                    if (element instanceof UIGate) {
                        selectedNodes.add(element.getNode());
                        highlightSelectedNode(element.getNode());
                    } else if (element instanceof UICable) {
                        highlightSelectedNode(((UICable) element).getLine());
                        selectedNodes.add(((UICable) element).getLine());
                    }
                }
            }
        }
        updateSelectionActions();
    }

    private void toggleSimulation(boolean activated) {
        enableSimulationButton.setDisable(activated);
        disableSimulationButton.setDisable(!activated);
    }

    /**
     * Ends the selection process by checking which nodes are inside the selection
     * rectangle,
     * highlights selected nodes, and updates the UI accordingly.
     *
     * @param event the MouseEvent triggering the end of selection
     */
    private void endSelection(MouseEvent event) {
        if (selectionRectangle == null) {
            return;
        }

        // Coordinates of the selection rectangle
        double selectionMinX = selectionRectangle.getLayoutX();
        double selectionMinY = selectionRectangle.getLayoutY();
        double selectionMaxX = selectionMinX + selectionRectangle.getWidth();
        double selectionMaxY = selectionMinY + selectionRectangle.getHeight();

        System.out.println("Selection rectangle: " + selectionMinX + "," + selectionMinY + " -> " + selectionMaxX + ","
                + selectionMaxY);

        // Iterate over all children of the container
        for (Node node : container.getChildren()) {
            // Ignore the selection rectangle itself
            if (node == selectionRectangle) {
                continue;
            }

            // Check if this is a UI element with userData
            if (node.getUserData() instanceof UIElement) {
                UIElement element = (UIElement) node.getUserData();

                // Get coordinates of the element
                double elementMinX = node.getLayoutX();
                double elementMinY = node.getLayoutY();
                double elementMaxX = elementMinX + node.getBoundsInLocal().getWidth();
                double elementMaxY = elementMinY + node.getBoundsInLocal().getHeight();

                System.out.println("Element " + element.getName() + ": " + elementMinX + "," + elementMinY + " -> "
                        + elementMaxX + "," + elementMaxY);

                // Check if the element is inside the selection rectangle
                boolean isInSelection = (elementMinX >= selectionMinX &&
                        elementMaxX <= selectionMaxX &&
                        elementMinY >= selectionMinY &&
                        elementMaxY <= selectionMaxY);

                // Add the element to the selection if it is inside the rectangle
                if (isInSelection) {
                    System.out.println("Selected element: " + element.getName());
                    if (element instanceof UIGate) {
                        selectedNodes.add(element.getNode());
                        highlightSelectedNode(element.getNode());
                    } else if (element instanceof UICable) {
                        highlightSelectedNode(((UICable) element).getLine());
                        selectedNodes.add(((UICable) element).getLine());
                    }
                }
            }
        }

        // Update action buttons based on the selection
        updateSelectionActions();

        // Remove the selection rectangle
        container.getChildren().remove(selectionRectangle);
        selectionRectangle = null;
    }

    /**
     * Highlights a selected node by adding a visual effect.
     *
     * @param node the Node to highlight
     */
    private void highlightSelectedNode(Node node) {
        // Add a visual effect to indicate the node is selected
        node.setEffect(new DropShadow(5, Color.BLUE));
    }

    /**
     * Removes the highlight effect from a deselected node.
     *
     * @param node the Node to unhighlight
     */
    private void unhighlightNode(Node node) {
        node.setEffect(null);
    }

    /**
     * Clears the selection by removing highlights and clearing the selection list.
     */
    private void clearSelection() {
        // Remove highlight from all selected nodes
        for (Node node : selectedNodes) {
            unhighlightNode(node);
        }
        // Clear the selection list
        selectedNodes.clear();
        // Update action buttons
        updateSelectionActions();
    }

    /**
     * Updates the state of action buttons based on whether there is a selection.
     */
    private void updateSelectionActions() {
        boolean hasSelection = !selectedNodes.isEmpty();

        // Enable/disable buttons based on selection state
        deleteButton.setDisable(!hasSelection);
        copyButton.setDisable(!hasSelection);
        cutButton.setDisable(!hasSelection);
        // Paste button is always disabled if clipboard is empty
        // It should be enabled separately when something is copied
    }

    /**
     * Deletes all selected elements from the container and handles disconnection of
     * cables if applicable.
     *
     * @param event the ActionEvent triggering the deletion
     */
    @FXML
    private void deleteSelectedElements(ActionEvent event) {
        // Use a new list to avoid ConcurrentModificationException
        ArrayList<Node> nodesToRemove = new ArrayList<>(selectedNodes);

        for (Node node : nodesToRemove) {
            // If it's a UIElement, handle disconnections
            if (node.getUserData() instanceof UIElement) {
                UIElement element = (UIElement) node.getUserData();

                // If the element is a logic gate, manage its connections
                if (element instanceof UIGate) {
                    UIGate gate = (UIGate) element;
                    gate.disconnect();
                } else if (element instanceof UICable) {
                    UICable cable = (UICable) element;
                    cable.disconnect();
                }
            }

            // Remove the node from the container
            container.getChildren().remove(node);
        }

        // Clear the selection
        clearSelection();
    }

    /**
     * Copies the selected elements to the clipboard.
     *
     * @param event the ActionEvent triggering the copy
     */
    @FXML
    private void copySelectedElements(ActionEvent event) {
        // Clear the clipboard
        clipboardElements.clear();

        // Add selected elements to the clipboard
        for (Node node : selectedNodes) {
            if (node.getUserData() instanceof UIElement) {
                UIElement element = (UIElement) node.getUserData();
                clipboardElements.add(element);
            }
        }

        // Enable the paste button
        pasteButton.setDisable(selectedNodes.size() == 0);
    }

    /**
     * Cuts the selected elements (copy then delete).
     *
     * @param event the ActionEvent triggering the cut
     */
    @FXML
    private void cutSelectedElements(ActionEvent event) {
        // First copy
        copySelectedElements(event);
        // Then delete
        deleteSelectedElements(event);
    }

    /**
     * Pastes elements from the clipboard at a position relative to the container or
     * mouse position.
     *
     * @param event the ActionEvent triggering the paste
     */
    @FXML
    private void pasteElements(ActionEvent event) {
        // nothing copied, so early return
        if (clipboardElements.size() == 0) {
            return;
        }
        // Default paste coordinates (center of the container)
        double pasteX = container.getWidth() / 2;
        double pasteY = container.getHeight() / 2;

        // If the mouse is inside the container, use its position
        if (container.localToScene(container.getBoundsInLocal()).contains(manager.getScene().getX(),
                manager.getScene().getY())) {
            pasteX = manager.getScene().getX() - container.localToScene(container.getBoundsInLocal()).getMinX();
            pasteY = manager.getScene().getY() - container.localToScene(container.getBoundsInLocal()).getMinY();
        }

        // Deselect all currently selected elements
        clearSelection();

        // For each element in the clipboard
        for (UIElement originalElement : clipboardElements) {
            // Create a new instance of the element
            UIElement newElement = UIElement.create(originalElement.getClass().getSimpleName());

            if (newElement != null) {
                // Position the new element
                Node newNode = newElement.getNode();
                newElement.setPosition(new Point2D(pasteX, pasteY));

                // Add the new element to the container
                container.getChildren().add(newNode);

                // Register listeners for the pins if it is a gate
                if (newElement instanceof UIGate) {
                    pinsListener((UIGate) newElement);
                    newElement.getNode().setOnMousePressed(mouseEvent -> {
                        selectElement(newElement);
                        replaceInfos(newElement.getInfos().getNode());
                    });
                    highlightSelectedNode(newNode);
                } else if (newElement instanceof UICable) {
                    ((UICable) newElement).getLine().setOnMousePressed(mouseEvent -> {
                        selectElement(newElement);
                        replaceInfos(newElement.getInfos().getNode());
                    });
                    highlightSelectedNode(((UICable) newElement).getLine());
                }

                // Add to selection
                selectedNodes.add(newNode);

                // Slightly offset position for next element
                pasteX += UIElement.baseSize;
                pasteY += UIElement.baseSize;
            }
        }

        // Update action buttons
        updateSelectionActions();
    }

    /**
     * Sets up selection/deselection of elements by mouse click.
     * Allows multi-selection with Ctrl key.
     */
    private void setupElementSelection() {
        // Add a listener for all existing and future UIElements
        container.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            // If clicking on the background (GridPane), ignore here
            // because it will be handled by pressSelection method
            if (event.getTarget() == gridPane) {
                return;
            }

            // Check if the click is on an element
            if (event.getTarget() instanceof Node) {
                Node targetNode = (Node) event.getTarget();

                // Climb up to a parent that has a UIElement in userData
                while (targetNode != null && targetNode != container) {
                    if (targetNode.getUserData() instanceof UIElement) {
                        UIElement element = (UIElement) targetNode.getUserData();
                        System.out.println("Click on element: " + element.getName());

                        // If Ctrl key is pressed, add or remove from selection
                        if (event.isControlDown()) {
                            if (selectedNodes.contains(targetNode)) {
                                // Deselect
                                selectedNodes.remove(targetNode);
                                unhighlightNode(targetNode);
                            } else {
                                // Select
                                selectedNodes.add(targetNode);
                                highlightSelectedNode(targetNode);
                            }
                        } else {
                            // If Ctrl is not pressed and node is not already selected
                            if (!selectedNodes.contains(targetNode)) {
                                // Deselect all others
                                clearSelection();
                                // Select clicked node
                                selectedNodes.add(targetNode);
                                highlightSelectedNode(targetNode);
                            }
                        }

                        // Update action buttons
                        updateSelectionActions();

                        break;
                    }

                    // Go up to parent
                    targetNode = targetNode.getParent();
                }
            }
        });
    }

    /**
     * Selects all UIElements in the container.
     *
     * @param event the ActionEvent triggering the select all
     */
    @FXML
    private void selectAllElements(ActionEvent event) {
        // First clear selection
        clearSelection();

        // Iterate over all children in the container
        for (Node node : container.getChildren()) {
            // Only consider UIElements
            if (node.getUserData() instanceof UIElement) {
                selectedNodes.add(node);
                if (node.getUserData() instanceof UICable) {
                    highlightSelectedNode(((UICable) node.getUserData()).getLine());
                } else {
                    highlightSelectedNode(node);
                }
            }
        }

        // Update action buttons
        updateSelectionActions();
    }
    // #endregion
}
