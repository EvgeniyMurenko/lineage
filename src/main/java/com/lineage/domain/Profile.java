package com.lineage.domain;

import javafx.scene.paint.Color;

public class Profile {

    private String name;
    private Integer xCord;
    private Integer yCord;
    private Color color;

    public Profile(String name, Integer xCord, Integer yCord, Color color) {
        this.name = name;
        this.xCord = xCord;
        this.yCord = yCord;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getxCord() {
        return xCord;
    }

    public void setxCord(Integer xCord) {
        this.xCord = xCord;
    }

    public Integer getyCord() {
        return yCord;
    }

    public void setyCord(Integer yCord) {
        this.yCord = yCord;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return getName();
    }
}
