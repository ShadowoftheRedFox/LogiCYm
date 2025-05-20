package com.pjava.controllers;

import java.io.IOException;
import java.util.ArrayList;

import com.pjava.src.UI.SceneManager;
import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.gates.UIAnd;
import com.pjava.src.UI.components.UICable;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.UI.components.gates.UINot;
import com.pjava.src.UI.components.gates.UIOr;
import com.pjava.src.UI.components.input.*;
import com.pjava.src.UI.components.output.UIDisplay;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
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

public class Editor extends VBox {
    @FXML
    public GridPane gridPane;
    @FXML
    public ScrollPane viewScroll;
    @FXML
    private AnchorPane container;
    @FXML
    private VBox infosContainer;
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
    // #endregion

    // #region Menu items
    @FXML
    public Button cableBtn;
    /**
     * scenemanager
     */
    private SceneManager manager;
/**
     * Element Selected
     */
    private UIElement currentlySelectedElement = null;

    /**
     * Rectangle
     */
    private Rectangle selectionRectangle = null;
    private Point2D selectionStart = null;

    /**
     * list of nodes selected
     */
    private ArrayList<Node> selectedNodes = new ArrayList<Node>();
    private ArrayList<UICable> cableLines = new ArrayList<UICable>();

    /**
     * the cabling mode is when you can link gates
     */
    private boolean cablingMode = false;
    private Pin lastInputPinPressed = null;
    private Pin lastOutputPinPressed = null;

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

        quitButton.setOnAction(event -> {
            // TODO popup to ask to save?
            Platform.exit();
        });

        deleteButton.setOnAction(event -> {
            deleteSelectedElement();
        });
        manager.getScene().setOnKeyPressed(event -> {
            System.out.println("Touche pressée: " + event.getCode());
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
    }

    /**
     * Crée un câble entre deux pins
     */
    private void createCableBetweenPins(Pin source, Pin target) {
        if (source == null || target == null) {
            System.out.println("no pin pair selected");
            return;
        }

        // Vérifier si les pins sont associés à des gates
        UIGate sourceGate = (UIGate) source.originController;
        UIGate targetGate = (UIGate) target.originController;

        if (sourceGate == null || targetGate == null) {
            throw new Error("Stand alone pin");
        }

        // TODO case if either gate already has a one sided cable connected
        // create a new cable controller
        UICable cableController = UICable.create();
        // connect cable
        cableController.connect(source, target, sourceGate, targetGate);
        cableLines.add(cableController);
        container.getChildren().add(cableController.getNode());

        lastInputPinPressed = null;
        lastOutputPinPressed = null;
    }

    // #region Functions
    private void resizeGrid() {
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

    private void endSelection(MouseEvent event) {
        // TODO look for selected elements with selectedNodes
        container.getChildren().remove(selectionRectangle);
        selectionRectangle = null;
    }
    private void clearSelection() {
        selectedNodes.clear();
        currentlySelectedElement = null;
        deleteButton.setDisable(true);
    }

    private void replaceInfos(Node content) {
        // clear container
        infosContainer.getChildren().clear();
        if (content != null) {
            // add content to the container
            infosContainer.getChildren().add(content);
        }
    }
    private void selectElement(UIElement element) {
        currentlySelectedElement = element;
        deleteButton.setDisable(false);
    }

    private void deleteSelectedElement() {
        if (currentlySelectedElement == null) {
            System.out.println("No item selected");
            return;
        }
        String elementName = currentlySelectedElement.getName();
        System.out.println(elementName + " delete");

        container.getChildren().remove(currentlySelectedElement.getNode());

        replaceInfos(null);
        currentlySelectedElement = null;
        clearSelection();
        deleteButton.setDisable(true);
        // TODO Supp in BAck
        // SUpp Cable connect to the gate
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
            replaceInfos(andController.getInfos().getNode());
            selectElement(andController);
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
            replaceInfos(orController.getInfos().getNode());
            selectElement(orController);
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
            replaceInfos(notController.getInfos().getNode());
            selectElement(notController);
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
            replaceInfos(buttonController.getInfos().getNode());
            selectElement(buttonController);
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
            replaceInfos(clockController.getInfos().getNode());
            selectElement(clockController);
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
            replaceInfos(leverController.getInfos().getNode());
            selectElement(leverController);
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
            replaceInfos(powerController.getInfos().getNode());
            selectElement(powerController);
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
            replaceInfos(groundController.getInfos().getNode());
            selectElement(groundController);
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
            replaceInfos(displayController.getInfos().getNode());
            selectElement(displayController);
        });
    }

    /**
     * register the pins for later connection
     *
     * @param gate the gate related to the pins
     */
    private void pinsListener(UIGate gate) {
        for (Pin pin : gate.getInputPins()) {
            pin.setOnPressed(event -> {
                lastInputPinPressed = pin;
                createCableBetweenPins(lastOutputPinPressed, lastInputPinPressed);
            });
        }
        for (Pin pin : gate.getOutputPins()) {
            pin.setOnPressed(event -> {
                lastOutputPinPressed = pin;
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
}
