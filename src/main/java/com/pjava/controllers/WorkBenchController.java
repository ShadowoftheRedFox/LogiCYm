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

            // Initialize draggable images
            initializeDraggableImages();
        } catch (IOException ex) {
            System.err.println(WorkBenchController.class.getName() + " " + ex.toString());
        }
    }

    private void initializeDraggableImages() {
        for (javafx.scene.Node node : sidebarVBox.getChildren()) {
            if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                makeDraggable(imageView);
            }
        }
    }

    private void makeDraggable(ImageView imageView) {
        // Create a copy of the image when dragged
        imageView.setOnDragDetected(event -> {
            // Clone the image for dragging
            ImageView draggedImage = new ImageView(imageView.getImage());
            draggedImage.setFitWidth(imageView.getFitWidth());
            draggedImage.setFitHeight(imageView.getFitHeight());

            // Make the dragged image movable within the workbench
            draggedImage.setOnMousePressed(pressEvent -> {
                draggedImage.setTranslateX(pressEvent.getSceneX() - workbenchPane.getLayoutX());
                draggedImage.setTranslateY(pressEvent.getSceneY() - workbenchPane.getLayoutY());
            });

            draggedImage.setOnMouseDragged(dragEvent -> {
                draggedImage.setTranslateX(dragEvent.getSceneX() - workbenchPane.getLayoutX());
                draggedImage.setTranslateY(dragEvent.getSceneY() - workbenchPane.getLayoutY());
            });

            // Add the dragged image to the workbench
            workbenchPane.getChildren().add(draggedImage);
        });
    }

    @FXML
    public void click(ActionEvent event) {
        this.manager.activate("main");
        System.out.println("Changement à main!");
    }
}
