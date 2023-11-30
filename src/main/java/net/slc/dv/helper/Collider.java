package net.slc.dv.helper;

import lombok.Getter;

@Getter
public class Collider {
    private double x;
    private double y;
    private double width;
    private double height;

    public Collider(double pivotX, double pivotY) {
        this.x = pivotX;
        this.y = pivotY;
        this.width = 32;
        this.height = 32;
    }

    public void setCollider(double pivotX, double pivotY) {
        this.x = pivotX;
        this.y = pivotY;
        this.width = 32;
        this.height = 32;
    }

    public boolean collidesWith(Collider other) {
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }
}
