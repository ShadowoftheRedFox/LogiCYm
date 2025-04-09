package com.pjava.src;

import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class SceneManager {
    private HashMap<String, Pane> screenMap = new HashMap<>();
    private Scene main;

    public SceneManager(Scene main) {
        this.main = main;
    }

    public void addScreen(String name, Pane pane) {
        screenMap.put(name, pane);
    }

    public void removeScreen(String name) {
        screenMap.remove(name);
    }

    public void activate(String name) {
        main.setRoot(screenMap.get(name));
    }
}
