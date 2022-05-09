package com.lineage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.paint.Color;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile implements Serializable {

    @JsonProperty("name")
    private String name;
    @JsonProperty("xcord")
    private Integer xCord;
    @JsonProperty("ycord")
    private Integer yCord;
    @JsonProperty("rcolor")
    private Integer rColor;
    @JsonProperty("jcolor")
    private Integer jColor;
    @JsonProperty("bcolor")
    private Integer bColor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getXCord() {
        return xCord;
    }

    public void setXCord(Integer xCord) {
        this.xCord = xCord;
    }

    public Integer getYCord() {
        return yCord;
    }

    public void setYCord(Integer yCord) {
        this.yCord = yCord;
    }

    public Integer getRColor() {
        return rColor;
    }

    public void setRColor(Integer rColor) {
        this.rColor = rColor;
    }

    public Integer getJColor() {
        return jColor;
    }

    public void setJColor(Integer jColor) {
        this.jColor = jColor;
    }

    public Integer getBColor() {
        return bColor;
    }

    public void setBColor(Integer bColor) {
        this.bColor = bColor;
    }

    @JsonIgnore
    public Color getColor() {
        return Color.rgb(rColor, jColor, bColor);
    }

    @Override
    public String toString() {
        return name;
    }
}
