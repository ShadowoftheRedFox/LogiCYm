package com.pjava.controllers;

import java.io.IOException;
import java.util.ArrayList;

import com.pjava.src.UI.SceneManager;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.gates.UIAnd;
import com.pjava.src.UI.components.gates.UINot;
import com.pjava.src.UI.components.gates.UIOr;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
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
import javafx.scene.text.Text;

public class Editor extends VBox {
    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane viewScroll;
    @FXML
    private AnchorPane container;

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

    private SceneManager manager;

    private Rectangle selectionRectangle = null;
    private Point2D selectionStart = null;

    private ArrayList<Node> selectedNodes = new ArrayList<Node>();

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
        // press is not on a blank space
        if (!gridPane.equals(event.getTarget())) {
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
    }

    // #region Gate spawn
    @FXML
    public void clickAnd(ActionEvent event) {
        System.out.println("Click And!");
        UIAnd and = UIAnd.create();
        container.getChildren().add(and.getNode());
    }

    @FXML
    public void clickOr(ActionEvent event) {
        System.out.println("Click Or!");
        UIOr or = UIOr.create();
        container.getChildren().add(or.getNode());
    }

    @FXML
    public void clickNot(ActionEvent event) {
        System.out.println("Click Not!");
        UINot not = UINot.create();
        container.getChildren().add(not.getNode());
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
    }

    @FXML
    public void clickMerger(ActionEvent event) {
        System.out.println("Click Merger!");
    }

    @FXML
    public void clickSplitter(ActionEvent event) {
        System.out.println("Click Splitter!");
    }
    // #endregion
}
