package com.pjava.src.UI.components;

import com.pjava.src.components.gates.Not;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class UINot extends UIGate {
    private double mouseAnchorX;
    private double mouseAnchorY;
    private double translateAnchorX;
    private double translateAnchorY;

    // Nœuds pour l'accès aux éléments FXML
    @FXML
    private Polygon notTriangle;
    @FXML
    private Circle notCircle;
    @FXML
    private Arc inputArc;
    @FXML
    private Arc outputArc;

    // Propriétés pour le redimensionnement
    private double initialWidth;
    private double initialHeight;
    private double initialScale = 1.0;

    public UINot() {
        setLogic(new Not());
        initializeHandlers();
    }

    @FXML
    public void initialize() {
        // initialWidth = getPrefWidth();
        // initialHeight = getPrefHeight();

        // Ajoutez l'initialisation des entrées/sorties ici si nécessaire
    }

    private void initializeHandlers() {
        // Gestionnaire pour le déplacement (drag)
        // setOnMousePressed(this::handleMousePressed);
        // setOnMouseDragged(this::handleMouseDragged);

        // Gestionnaire pour le redimensionnement (à ajouter à une zone spécifique)
        createResizeHandle();
    }

    private void handleMousePressed(MouseEvent event) {
        // Enregistrer la position initiale de la souris
        mouseAnchorX = event.getSceneX();
        mouseAnchorY = event.getSceneY();

        // Enregistrer la position initiale du composant
        // translateAnchorX = getTranslateX();
        // translateAnchorY = getTranslateY();

        // Amener le composant au premier plan
        // toFront();

        // Marquer l'événement comme traité
        event.consume();
    }

    private void handleMouseDragged(MouseEvent event) {
        // Calculer le déplacement
        double deltaX = event.getSceneX() - mouseAnchorX;
        double deltaY = event.getSceneY() - mouseAnchorY;

        // Appliquer le déplacement
        // setTranslateX(translateAnchorX + deltaX);
        // setTranslateY(translateAnchorY + deltaY);

        // Marquer l'événement comme traité
        event.consume();
    }

    private void createResizeHandle() {
        // Créer une poignée de redimensionnement dans le coin inférieur droit
        Circle resizeHandle = new Circle(5);
        resizeHandle.setStyle("-fx-fill: gray; -fx-stroke: black;");

        // Positionner la poignée dans le coin inférieur droit
        AnchorPane.setBottomAnchor(resizeHandle, 0.0);
        AnchorPane.setRightAnchor(resizeHandle, 0.0);

        // Ajouter la poignée à ce composant
        // getChildren().add(resizeHandle);

        // Variables pour le redimensionnement
        final double[] initialSize = new double[2];
        final double[] initialMousePos = new double[2];

        resizeHandle.setOnMousePressed(event -> {
            initialSize[0] = getWidth();
            initialSize[1] = getHeight();
            initialMousePos[0] = event.getSceneX();
            initialMousePos[1] = event.getSceneY();
            event.consume();
        });

        resizeHandle.setOnMouseDragged(event -> {
            // Calculer la variation de taille
            double deltaX = event.getSceneX() - initialMousePos[0];
            double deltaY = event.getSceneY() - initialMousePos[1];

            // Calculer le facteur d'échelle pour maintenir le ratio
            double scale = Math.max(
                    (initialSize[0] + deltaX) / initialWidth,
                    (initialSize[1] + deltaY) / initialHeight);

            // Appliquer l'échelle à tous les éléments
            applyScale(scale);

            event.consume();
        });
    }

    private void applyScale(double scale) {
        // Appliquer l'échelle à tous les éléments graphiques
        // setScaleX(scale);
        // setScaleY(scale);
        initialScale = scale;
    }

    public double getInitialScale() {
        return initialScale;
    }

    // Méthodes accesseurs pour les composants visuels
    public Polygon getNotTriangle() {
        return notTriangle;
    }

    public Circle getNotCircle() {
        return notCircle;
    }

    public Arc getInputArc() {
        return inputArc;
    }

    public Arc getOutputArc() {
        return outputArc;
    }

    @Override
    public Not getLogic() {
        return (Not) super.getLogic();
    }

    public void setLogic(Not not) {
        super.setLogic(not);
    }
}
