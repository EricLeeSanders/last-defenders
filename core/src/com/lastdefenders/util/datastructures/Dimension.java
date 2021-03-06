package com.lastdefenders.util.datastructures;

public class Dimension {

    private float width;
    private float height;

    public Dimension(float width, float height) {

        this.setWidth(width);
        this.setHeight(height);
    }

    public float getWidth() {

        return width;
    }

    private void setWidth(float width) {

        this.width = width;
    }

    public float getHeight() {

        return height;
    }

    private void setHeight(float height) {

        this.height = height;
    }

    public void set(float width, float height) {

        this.width = width;
        this.height = height;
    }
}
