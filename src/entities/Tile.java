package entities;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

    public int x, y;
    public String type;
    public String imagePath;
    public transient int deltaX, deltaY;
    private transient BufferedImage image; // transient means, this variable will not be included in the json file
    public int width;
    public int height;

    /* Gson library */
    public Tile(int x, int y, String imagePath, String type, int width, int height) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;
    }

    public Tile(int x, int y, String imagePath, String type, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.type = type;
        this.imagePath = imagePath;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
