package com.pjava.src.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * An abstract class used to get access to useful static functions globally.
 * Targeted towards the UI.
 */
public abstract class UIUtlis {
    /**
     * The constructor of the Utils class.
     */
    public UIUtlis() {
    }

    /**
     * Create an error popup with the given content.
     *
     * @param content An array list of string.
     * @return The stage of the created popup.
     */
    public static Stage errorPopup(ArrayList<String> content) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);

        VBox dialogVbox = new VBox(5);
        dialogVbox.setFillWidth(true);
        dialogVbox.alignmentProperty().set(Pos.CENTER);
        errorPopupSetup(dialogVbox, content);

        Button close = new Button("Close");
        close.setOnAction(event -> {
            dialog.close();
        });
        dialogVbox.getChildren().add(close);

        ScrollPane container = new ScrollPane(dialogVbox);
        container.setFitToWidth(true);
        container.setFitToHeight(true);

        Scene dialogScene = new Scene(container, 300, 200);
        dialog.setScene(dialogScene);
        dialog.setTitle("Error");
        dialog.show();
        return dialog;
    }

    /**
     * Create an error popup with the given message.
     *
     * @param message The message to display.
     * @return The stage of the created popup.
     */
    public static Stage errorPopup(String message) {
        return errorPopup(new String[] { message });
    }

    /**
     * Create an error popup with the given messages.
     *
     * @param message The array of messages to display.
     * @return The stage of the created popup.
     */
    public static Stage errorPopup(String[] message) {
        ArrayList<String> array = new ArrayList<String>();
        for (String msg : message) {
            array.add(msg);
        }
        return errorPopup(array);
    }

    /**
     * Create an error popup with the throwable. It will show the message and the
     * stack trace.
     *
     * @param error The error to display.
     * @return The stage of the created popup.
     */
    public static Stage errorPopup(Throwable error) {
        ArrayList<String> array = new ArrayList<String>();
        if (error != null) {
            array.add(error.getMessage());
            for (StackTraceElement stackTrace : error.getStackTrace()) {
                array.add(stackTrace.toString());
            }
            return errorPopup(array);
        }
        return errorPopup("Unknown error");
    }

    /**
     * Private functions used to add all the content inside the popup VBox.
     *
     * @param container The vbox of the popup.
     * @param content   The array list of content. Blank, empty or null will be
     *                  removed.
     */
    private static void errorPopupSetup(VBox container, ArrayList<String> content) {
        if (container == null || content == null || content.size() == 0) {
            return;
        }
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i) != null && !content.get(i).isBlank()) {
                container.getChildren().add(new Text(content.get(i)));
            }
        }
    }

    /**
     * Return type for the callback of the validation popup.
     */
    public enum ValidationAnwser {
        /**
         * Cancel is either the cancel button, or closing the popup window.
         */
        CANCELED,
        /**
         * When the popup question si approved.
         */
        APPROVED,
        /**
         * When the popup question si denied.
         */
        DENIED
    }

    public static Stage validationPopup(String question, Consumer<ValidationAnwser> callback, String approve,
            String deny, String cancel) {
        if (callback == null) {
            throw new NullPointerException("Expected callback to be an instance of Function, received null");
        }

        if (question == null || question.isBlank()) {
            question = "Do you wish to continue?";
        }
        if (approve == null || approve.isBlank()) {
            approve = "Approve";
        }
        if (deny == null || deny.isBlank()) {
            deny = "Deny";
        }
        if (cancel == null || cancel.isBlank()) {
            cancel = "Cancel";
        }

        class InternalPopup extends VBox {
            public Parent container = null;

            @FXML
            public Button approveButton;

            @FXML
            public Button cancelButton;

            @FXML
            public Button denyButton;

            @FXML
            public Text text;

            public InternalPopup() {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ValidationPopup.fxml"));
                    loader.setRoot(this);
                    loader.setController(this);
                    container = loader.load();
                } catch (IOException e) {
                    throw new Error(e);
                }
            }
        }

        final Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);

        InternalPopup popup = new InternalPopup();

        popup.text.setText(question);

        popup.cancelButton.setText(cancel);
        popup.denyButton.setText(deny);
        popup.approveButton.setText(approve);

        popup.cancelButton.setOnAction(event -> {
            dialog.close();
            callback.accept(ValidationAnwser.CANCELED);
        });
        popup.denyButton.setOnAction(event -> {
            dialog.close();
            callback.accept(ValidationAnwser.DENIED);
        });
        popup.approveButton.setOnAction(event -> {
            dialog.close();
            callback.accept(ValidationAnwser.APPROVED);
        });

        Scene dialogScene = new Scene(popup.container, 500, 200);
        dialog.setScene(dialogScene);
        dialog.setTitle("Validation");
        dialog.show();
        return dialog;
    }

    public static Stage validationPopup(String question, Consumer<ValidationAnwser> callback) {
        return validationPopup(question, callback, null, null, null);
    }
}
