package com.pjava.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

import com.pjava.src.UI.SceneManager;
import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.UICable;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
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
import com.pjava.src.document.SimulationFileLoader;
import com.pjava.src.utils.UIUtils;
import com.pjava.src.utils.UIUtils.ValidationAnwser;
import com.pjava.src.utils.UtilsSave;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Editor extends VBox {
    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane viewScroll;
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

    @FXML
    private Button cableBtn;
    /**
     * scenemanager
     */
    private SceneManager manager;

    /**
     * Rectangle
     */
    private Rectangle selectionRectangle = null;
    private Point2D selectionStart = null;

    /**
     * list of nodes selected
     */
    private ArrayList<UIElement> allController = new ArrayList<UIElement>();
    private ArrayList<UIElement> selectedNodes = new ArrayList<UIElement>();
    private ArrayList<UICable> cableLines = new ArrayList<UICable>();

    /**
     * the cabling mode is when you can link gates
     */
    private boolean cablingMode = false;
    private Pin lastInputPinPressed = null;
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
            deleteSelectedElement();
        });
        manager.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DELETE:
                    deleteSelectedElement();
                    break;
                case BACK_SPACE:
                    deleteSelectedElement();
                    break;
                default:
                    break;
            }
        });
        saveButton.setOnAction(event -> {
            try {
                editedCircuit.save();
            } catch (Exception e) {
                UIUtils.errorPopup(e.getMessage());
            }
        });

        saveAsButton.setOnAction(event -> {
            try {
                FileChooser fileChooser = new FileChooser();
                File intialDirectory = new File("./data");
                fileChooser.setTitle("Select file to save as");
                fileChooser.setInitialDirectory(intialDirectory);
                FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON Files (*.json)",
                        "*.json");
                fileChooser.getExtensionFilters().addAll(jsonFilter);

                File saveFile = fileChooser.showOpenDialog(manager.getStage());

                editedCircuit.save(saveFile.getParent(), saveFile.getName());
            } catch (Exception e) {
                UIUtils.errorPopup(e.getMessage());
            }
        });

        openFileButton.setOnAction(event -> {
            try {
                FileChooser fileChooser = new FileChooser();
                File intialDirectory = new File("./data");
                fileChooser.setTitle("Select file to open");
                fileChooser.setInitialDirectory(intialDirectory);
                FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON Files (*.json)",
                        "*.json");
                fileChooser.getExtensionFilters().addAll(jsonFilter);

                File file = fileChooser.showOpenDialog(manager.getStage());

                editedCircuit.loadGatesFromFile(file.getPath());
            } catch (Exception e) {
                UIUtils.errorPopup(e.getMessage());
            }
        });

        selectElement(allController);
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
            cableLines.add(cableController);
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
     * @param value the value of hz
     */
    private void setSimulationSpeed(int value) {
        // TODO edit simulation speed somewhere
    }

    /**
     * Create the grid in page
     */

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

    /**
     * change color around the gate when element is selected and canceled the previous selection
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
     * @param event
     */

    private void endSelection(MouseEvent event) {
        // TODO look for selected elements with selectedNodes
        container.getChildren().remove(selectionRectangle);
        selectionRectangle = null;
        deleteButton.setDisable(false);
    }
    /**
     * Clears the current selection of elements on the UI.
     */

    private void clearSelection() {
        for (UIElement selectedElement : selectedNodes) {
            if (selectedElement != null) {
                selectedElement.getNode().setStyle("-fx-background-color: #ffffff00");
            }
        }

        selectedNodes.clear();
        replaceInfos(null);
        deleteButton.setDisable(true);
    }
    /**
     * Clears the panel and adds the provided Node if it is not null.
     * @param content The Node to display in the information panel, or null to clear it
     */

    private void replaceInfos(Node content) {
        // clear container
        infosContainer.getChildren().clear();
        if (content != null) {
            // add content to the container
            infosContainer.getChildren().add(content);
        }
    }
    /**
     * grabs the element selected
     * @param element the element selected
     */
    private void selectElement(UIElement element) {
        clearSelection();
        if (element != null) {
            selectedNodes.add(element);
            element.getNode().setStyle("-fx-background-color: #0000ff80");
        }
        deleteButton.setDisable(element == null);
    }
    /**
     * grabs a group of element selected
     * @param array collection of element selected
     */
    private void selectElement(Collection<UIElement> array) {
        clearSelection();
        if (array != null) {
            selectedNodes.addAll(array);
            selectedNodes.forEach(element -> {
                if (element == null) {
                    selectedNodes.remove(element);
                }
                element.getNode().setStyle("-fx-background-color: #0000ff80");
            });
        }
        deleteButton.setDisable(selectedNodes.size() == 0);
    }
    /**
     * delete the element selected 
     */
    private void deleteSelectedElement() {
        deleteButton.setDisable(true);
        if (selectedNodes.size() == 0) {
            return;
        }

        System.out.println("Deleting " + selectedNodes.size() + " elements");
        for (UIElement selectedElement : selectedNodes) {
            String elementName = selectedElement.toString();
            System.out.println("deleting " + elementName);

            // remove from the scene
            container.getChildren().remove(selectedElement.getNode());
            // remove them from the controller list
            allController.remove(selectedElement);

            // disconnect everything
            if (selectedElement instanceof UIGate) {
                ((UIGate) selectedElement).disconnect();
            } else if (selectedElement instanceof UICable) {
                ((UICable) selectedElement).disconnect();
            } else {
                System.out.println("Something unexpected have been selected: " + selectedElement);
            }
        }

        clearSelection();
    }

    private void toggleSimulation(boolean activated) {
        enableSimulationButton.setDisable(activated);
        disableSimulationButton.setDisable(!activated);
    }
    /**
     * when editor is close make propose if they want to save the circuit
     */
    private void closeEditor() {
        if (unsavedChanges) {
            Consumer<ValidationAnwser> callback = (res) -> {
                if (res == ValidationAnwser.APPROVED) {
                    // TODO save here
                    try {
                        editedCircuit.save();
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

    // #endregion

    // #region Gate spawn
    
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
        allController.add(andController);
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
        allController.add(orController);
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
        allController.add(notController);
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
        allController.add(buttonController);
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
        allController.add(clockController);
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
        allController.add(leverController);
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
        allController.add(powerController);
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
        allController.add(groundController);
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
        allController.add(displayController);
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

    @FXML
    public void clickCable(ActionEvent event) {
        System.out.println("Click Cable!");
        // Activer/désactiver le mode câblage
        cablingMode = !cablingMode;

        // Mettre à jour l'apparence du bouton
        Button btn = (Button) event.getSource();
        btn.setStyle(cablingMode ? "-fx-background-color: lightblue;" : "");
    }

    @FXML
    public void clickMerger(ActionEvent event) {
        System.out.println("Click Merger!");
    }

    @FXML
    public void clickSplitter(ActionEvent event) {
        System.out.println("Click Splitter!");
    }

    @FXML
    public void clickSchema(ActionEvent event) {
        System.out.println("Click Schema!");
    }
}
