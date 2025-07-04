package entities;

import java.awt.image.BufferedImage;

public class Tile {

    public int x, y, idx;
    private transient BufferedImage image;

    public Tile(int x, int y, int idx) {
        this.x = x;
        this.y = y;
        this.idx = idx;
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

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
