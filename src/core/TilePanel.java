package core;

import loaders.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TilePanel extends JPanel {

    private int x, y;

    public TilePanel() {
        setBackground(Color.RED);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < ImageLoader.getTileMap().length; i++) {
            BufferedImage image = ImageLoader.getTileMap()[i];
            g.drawImage(image, x, y, null);
            if (i % 2 == 0 && i != 0) {
                x = 0;
                y = y + image.getHeight() + 10;
            } else x = x + image.getWidth() + 10;
        }
    }
}
