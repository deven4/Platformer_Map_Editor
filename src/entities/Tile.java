package entities;

import java.awt.image.BufferedImage;

public class Tile {

    public int x, y;
    public String imagePath;
    public transient int deltaX, deltaY;
    private transient BufferedImage image;

    public Tile(int x, int y, String imagePath) {
        this.x = x;
        this.y = y;
        this.imagePath = imagePath;
    }

    public Tile(int x, int y, String imagePath, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.imagePath = imagePath;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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
}
