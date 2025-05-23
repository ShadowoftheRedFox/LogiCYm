package com.pjava.src.UI.components.gates;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pjava.src.components.gates.Schema;
import com.pjava.src.utils.UtilsSave;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import com.pjava.src.UI.components.UIElement;
import com.pjava.src.UI.components.UIGate;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class UISchema extends UIGate {

    @FXML
    private VBox schemaContainer;

    @FXML
    private ListView<String> schemaListView;

    @FXML
    private Button loadSchemaButton;

    private GridPane targetGridPane;
    private List<Schema> availableSchemas;
    private SchemaSelectionCallback callback;

    /**
     * Interface callback pour gérer la sélection de schémas
     */
    public interface SchemaSelectionCallback {
        void onSchemaSelected(Schema schema);
        void addSchemaToGrid(Schema schema, int gridX, int gridY);
    }

    /**
     * Méthode statique pour créer une instance UISchema
     * @param fxml Le nom du fichier FXML (sans extension)
     * @return Une instance UISchema configurée
     */
    public static UISchema create(String fxml) {
//code
    }

    /**
     * Constructeur par défaut
     */
    public UISchema() {
        this.availableSchemas = new ArrayList<>();
        initializeSchemaList();
    }

    /**
     * Constructeur avec callback
     * @param schemaContainer Le container pour l'affichage des schémas
     * @param gridPane Le GridPane cible pour placer les schémas
     * @param callback Le callback pour gérer les sélections
     */
    public UISchema(VBox schemaContainer, GridPane gridPane, SchemaSelectionCallback callback) {
        this.schemaContainer = schemaContainer;
        this.targetGridPane = gridPane;
        this.callback = callback;
        this.availableSchemas = new ArrayList<>();
        initializeSchemaList();
    }

    @FXML
    private void initialize() {
        if (schemaListView != null) {
            // Configuration de la ListView
            schemaListView.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) { // Double-clic
                    handleSchemaSelection();
                }
            });
        }

        if (loadSchemaButton != null) {
            loadSchemaButton.setOnAction(event -> handleSchemaSelection());
        }
    }

    /**
     * Initialise la liste des schémas disponibles
     */
    private void initializeSchemaList() {
        loadAvailableSchemas();
        refreshSchemaListView();
    }

    /**
     * Charge tous les schémas disponibles depuis le dossier de sauvegarde
     */
    private void loadAvailableSchemas() {
        availableSchemas.clear();

        try {
            File schemaFolder = new File(UtilsSave.saveFolder + "schema/");
            if (!schemaFolder.exists()) {
                schemaFolder.mkdirs();
                return;
            }

            File[] schemaFiles = schemaFolder.listFiles((dir, name) -> name.endsWith(".json"));
            if (schemaFiles != null) {
                for (File file : schemaFiles) {
                    try {
                        Schema schema = new Schema(file.getAbsolutePath());
                        availableSchemas.add(schema);
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement du schéma " + file.getName() + ": " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des schémas: " + e.getMessage());
        }
    }

    /**
     * Met à jour l'affichage de la ListView avec les schémas disponibles
     */
    private void refreshSchemaListView() {
        if (schemaListView != null) {
            schemaListView.getItems().clear();
            for (Schema schema : availableSchemas) {
                schemaListView.getItems().add(schema.getName());
            }
        }
    }

    /**
     * Gère la sélection d'un schéma dans la liste
     */
    private void handleSchemaSelection() {
        if (schemaListView == null) return;

        String selectedSchemaName = schemaListView.getSelectionModel().getSelectedItem();
        if (selectedSchemaName == null) return;

        Schema selectedSchema = availableSchemas.stream()
            .filter(schema -> schema.getName().equals(selectedSchemaName))
            .findFirst()
            .orElse(null);

        if (selectedSchema != null && callback != null) {
            callback.onSchemaSelected(selectedSchema);
        }
    }


    /**
     * Rafraîchit la liste des schémas (à appeler après ajout/suppression)
     */
    public void refreshSchemas() {
        loadAvailableSchemas();
        refreshSchemaListView();
    }

    /**
     * Ajoute un nouveau schéma à la liste
     * @param schemaPath Chemin vers le nouveau schéma
     */
    public void addNewSchema(String schemaPath) {
        try {
            Schema newSchema = new Schema(schemaPath);
            availableSchemas.add(newSchema);
            refreshSchemaListView();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout du nouveau schéma: " + e.getMessage());
        }
    }

    /**
     * Retourne la liste des schémas disponibles
     * @return Liste des schémas
     */
    public List<Schema> getAvailableSchemas() {
        return new ArrayList<>(availableSchemas);
    }

    /**
     * Retourne un schéma par son nom
     * @param name Nom du schéma
     * @return Le schéma correspondant ou null
     */
    public Schema getSchemaByName(String name) {
        return availableSchemas.stream()
            .filter(schema -> schema.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    // Getters et Setters
    public VBox getSchemaContainer() {
        return schemaContainer;
    }

    public void setSchemaContainer(VBox schemaContainer) {
        this.schemaContainer = schemaContainer;
    }

    public void setCallback(SchemaSelectionCallback callback) {
        this.callback = callback;
    }
}
