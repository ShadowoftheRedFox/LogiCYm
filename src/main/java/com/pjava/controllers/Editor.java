package com.pjava.controllers;

import java.io.IOException;
import java.util.ArrayList;

import com.pjava.src.UI.SceneManager;
import com.pjava.src.UI.components.UIAnd;
import com.pjava.src.UI.components.UIElement;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
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
    public AnchorPane container;

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

    @FXML
    public void clickAnd(ActionEvent event) {
        System.out.println("Click And!");
        Node and = UIAnd.create(getClass());
        // UIAnd uiand = UIAnd.getController(and);
        container.getChildren().add(and);
    }

    @FXML
    public void clickOr(ActionEvent event) {
        System.out.println("Click Or!");
    }

    @FXML
    public void clickNot(ActionEvent event) {
        System.out.println("Click Not!");
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
}
