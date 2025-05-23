package com.pjava.src.utils;

import org.json.JSONObject;

import com.pjava.src.UI.Rotation;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class SaveData {
    public Color color = null;
    public String label = null;
    public Point2D position = null;
    public Rotation rotation = null;
    public int uuid = -1;

    public SaveData(int uuid, String label, Color color, Point2D position, Rotation rotation) {
            this.uuid = uuid;
            this.label = label;
            this.color = color;
            this.position = position;
            this.rotation = rotation;
        }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("uuid", uuid);
        json.put("label", label);
        json.put("color", color);
        json.put("x", position.getX());
        json.put("y", position.getY());
        json.put("rotation", rotation.toString());
        return json;
    }
}
