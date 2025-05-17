package com.pjava.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.pjava.src.UI.SceneManager;
import com.pjava.src.UI.components.Pin;
import com.pjava.src.UI.components.gates.UIAnd;
import com.pjava.src.UI.components.UICable;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import com.pjava.src.UI.components.gates.UINot;
import com.pjava.src.UI.components.gates.UIOr;
import com.pjava.src.UI.components.Pin.CableConnectionListener;

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
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;


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
     * Rectangle
     */
    private Rectangle selectionRectangle = null;
    private Point2D selectionStart = null;

    /**
     * list of nodes selected
     */
    private ArrayList<Node> selectedNodes = new ArrayList<Node>();

    /**
     * the cabling mode is when you can link gates
     */
    private boolean cablingMode = false;
    private Map<Pin, UIGate> pinToGateMap = new HashMap<>();

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
    public void initialize() {
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

        container.setOnMouseReleased(event -> endSelection(event));
        container.setOnMouseDragged(event -> dragSelection(event));

        unselectAllButton.setOnAction(event -> {
            clearSelection();
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
               // Définir l'écouteur de connexions de câbles
               Pin.setCableConnectionListener(new CableConnectionListener() {
                @Override
                public void onCableConnection(Pin source, Pin target) {
                    createCableBetweenPins(source, target);
                }
            });
        }

        /**
         * Crée un câble entre deux pins
         */
        private void createCableBetweenPins(Pin source, Pin target) {
            // Vérifier si les pins sont associés à des gates
            UIGate sourceGate = (UIGate)source.originController;
            UIGate targetGate = (UIGate)target.originController;

            if (sourceGate == null || targetGate == null) {
                System.out.println("Pin non associé à une gate");
                return;
            }

            // Créer le câble en tant que Node
            UICable cableController = UICable.create();
            Node cableNode = cableController.getNode();

            // Ajouter le câble au conteneur
            container.getChildren().add(cableNode);

            // Connecter le câble
            cableController.connect(source, target, sourceGate, targetGate);

            // Ajouter le câble aux gates connectées
            sourceGate.addConnectedCable(cableController);
            targetGate.addConnectedCable(cableController);
            Line cable= new Line();
            cable.setLayoutX(source.getCenter().getX());
            cable.setLayoutY(source.getCenter().getY());
            cable.setEndX(target.getCenter().getX());
            cable.setEndY(target.getCenter().getY());

            cable.strokeWidthProperty().set(5);
            cable.setFill(Color.RED);
            container.getChildren().add(cable);
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

    private void dragSelection(MouseEvent event) {
        if (selectionRectangle == null) {
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
    }

    private void replaceInfos(Node content) {
        // clear container
        infosContainer.getChildren().clear();
        if (content != null) {
            // add content to the container
            infosContainer.getChildren().add(content);
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

        // Ajouter les pins de cette gate à la map pour le cablage
        registerGatePins(andController);
        andController.getNode().setOnMousePressed(mouseEvent -> {
            replaceInfos(andController.getInfos().getSelf());
        });
    }

    @FXML
    public void clickOr(ActionEvent event) {
        System.out.println("Click Or!");
        UIOr orController = (UIOr) UIElement.create("UIOr");
        Node or = orController.getNode();
        container.getChildren().add(or);

        // Ajouter les pins de cette gate à la map pour le cablage
        registerGatePins(orController);
    }

    @FXML
    public void clickNot(ActionEvent event) {
        System.out.println("Click Not!");
        UINot notController = (UINot) UIElement.create("UINot");
        Node not = notController.getNode();
        container.getChildren().add(not);

        // Ajouter les pins de cette gate à la map pour le cablage
        registerGatePins(notController);
    }

    /**
     * Enregistre les pins d'une porte logique dans la map pour faciliter la création de câbles
     */
    private void registerGatePins(UIGate gate) {
        // Supposant que UIGate a des méthodes pour obtenir ses pins d'entrée et de sortie
        for (Pin pin : gate.getInputPins()) {
            pinToGateMap.put(pin, gate);
        }
        for (Pin pin : gate.getOutputPins()) {
            pinToGateMap.put(pin, gate);
        }
    }



    @FXML
    public void clickClock(ActionEvent event) {
        System.out.println("Click Clock!");
    }

    @FXML
    public void clickDisplay(ActionEvent event) {
        System.out.println("Click Display!");
    }

    @FXML
    public void clickCable(ActionEvent event) {
        System.out.println("Click Cable!");
        // Activer/désactiver le mode câblage
        cablingMode = !cablingMode;
        Pin.setCablingMode(cablingMode);

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
