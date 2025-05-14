package com.pjava.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import com.pjava.src.UI.SceneManager;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;


public class WorkBenchController extends AnchorPane {
    private SceneManager manager;

    @FXML
    private VBox sidebarVBox;

    @FXML
    private Pane workbenchPane;

    public WorkBenchController(SceneManager manager) {
        this.manager = manager;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/WorkBench.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();

            // Initialize draggable nodes
            initializeDraggableNodes();
        } catch (IOException ex) {
            System.err.println(WorkBenchController.class.getName() + " " + ex.toString());
        }
    }

    private void initializeDraggableNodes() {
        for (Node node : sidebarVBox.getChildren()) {
            // Make ImageViews draggable
            if (node instanceof ImageView) {
                makeDraggable((ImageView) node);
            }
            // Make AnchorPanes draggable
            else if (node instanceof AnchorPane) {
                makeDraggable((AnchorPane) node);
            }
        }
    }

    private void makeDraggable(ImageView imageView) {
        imageView.setOnDragDetected(event -> {
            ImageView draggedImage = new ImageView(imageView.getImage());
            draggedImage.setFitWidth(imageView.getFitWidth());
            draggedImage.setFitHeight(imageView.getFitHeight());
            setupDraggableNode(draggedImage);
            workbenchPane.getChildren().add(draggedImage);
        });
    }

    private void makeDraggable(AnchorPane anchorPane) {
        anchorPane.setOnDragDetected(event -> {
            try {
                // Create a deep copy of the AnchorPane and its children
                AnchorPane draggedPane = new AnchorPane();
                draggedPane.setPrefWidth(anchorPane.getPrefWidth());
                draggedPane.setPrefHeight(anchorPane.getPrefHeight());

                // Copy all children (including lines and images)
                for (Node child : anchorPane.getChildren()) {
                    Node childCopy = copyNode(child);
                    draggedPane.getChildren().add(childCopy);
                }

                setupDraggableNode(draggedPane);
                workbenchPane.getChildren().add(draggedPane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private Node copyNode(Node original) {
        if (original instanceof ImageView) {
            ImageView originalImg = (ImageView) original;
            ImageView copy = new ImageView(originalImg.getImage());
            copy.setFitWidth(originalImg.getFitWidth());
            copy.setFitHeight(originalImg.getFitHeight());
            copy.setLayoutX(originalImg.getLayoutX());
            copy.setLayoutY(originalImg.getLayoutY());
            copy.setPickOnBounds(originalImg.isPickOnBounds());
            copy.setPreserveRatio(originalImg.isPreserveRatio());
            return copy;
        } else if (original instanceof Line) {
            Line originalLine = (Line) original;
            Line copy = new Line();
            copy.setStartX(originalLine.getStartX());
            copy.setStartY(originalLine.getStartY());
            copy.setEndX(originalLine.getEndX());
            copy.setEndY(originalLine.getEndY());
            copy.setLayoutX(originalLine.getLayoutX());
            copy.setLayoutY(originalLine.getLayoutY());
            return copy;
        } else if (original instanceof Circle) {
            Circle originalCircle = (Circle) original;
            Circle copy = new Circle();
            copy.setCenterX(originalCircle.getCenterX());
            copy.setCenterY(originalCircle.getCenterY());
            copy.setRadius(originalCircle.getRadius());
            copy.setFill(originalCircle.getFill());
            copy.setStroke(originalCircle.getStroke());
            copy.setStrokeWidth(originalCircle.getStrokeWidth());
            return copy;
        }
        return null;
    }

    private void setupDraggableNode(Node node) {
        final double[] offset = new double[2];

        node.setOnMousePressed(pressEvent -> {
            offset[0] = pressEvent.getSceneX() - node.getTranslateX();
            offset[1] = pressEvent.getSceneY() - node.getTranslateY();
            pressEvent.consume();
        });

        node.setOnMouseDragged(dragEvent -> {
            node.setTranslateX(dragEvent.getSceneX() - offset[0]);
            node.setTranslateY(dragEvent.getSceneY() - offset[1]);
            dragEvent.consume();
        });
    }

    @FXML
    public void clickInput(ActionEvent event){
        //liée
    }

    @FXML
    public void click(ActionEvent event) {
        this.manager.activate("main");
        System.out.println("Changement à main!");
    }
}
