package com.pjava.src.UI;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * Enable to change between scene roots easily.
 */
public class SceneManager {
    /**
     * Where all the scene roots are saved.
     */
    private HashMap<String, Pane> screenMap = new HashMap<>();

    /**
     * A reference to the scene.
     */
    private Scene main;

    /**
     * Create a new SceneManager.
     *
     * @param main The reference to the scene.
     */
    public SceneManager(Scene main) {
        this.main = main;
    }

    /**
     * Add a new root under the given name.
     *
     * @param name The name of the root.
     * @param pane The root element.
     */
    public void addScreen(String name, Pane pane) {
        screenMap.put(name, pane);
    }

    /**
     * Remove the root from the manager.
     *
     * @param name Name of the root used in {@link #addScreen(String, Pane)}.
     */
    public void removeScreen(String name) {
        screenMap.remove(name);
    }

    /**
     * Change the scene to the provided name.
     *
     * @param name Name of the root to use given in
     *             {@link #addScreen(String, Pane)}.
     */
    public void activate(String name) {
        main.setRoot(screenMap.get(name));
    }

    /**
     * Getter for {@link #main}.
     *
     * @return The scene referenced into the scene manager.
     */
    public Scene getScene() {
        return main;
    }
}
