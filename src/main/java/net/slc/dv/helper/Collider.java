package net.slc.dv.helper;

public class Collider {
    private double x;
    private double y;
    private double width;
    private double height;

    public Collider(double pivotX, double pivotY, double width) {
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean collidesWith(Collider other) {
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }
}
