package com.pjava.src.utils;

import java.util.ArrayList;

import javafx.geometry.Pos;
import javafx.scene.Scene;
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
        dialog.initModality(Modality.NONE);
        VBox dialogVbox = new VBox(5);
        dialogVbox.setFillWidth(true);
        dialogVbox.alignmentProperty().set(Pos.CENTER);
        errorPopupSetup(dialogVbox, content);
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
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
}
