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

        y = 0;
        for (int i = 0; i < ImageLoader.getTileMap().length; i++) {
            BufferedImage image = ImageLoader.getTileMap()[i];
            System.out.println(image);
            g.drawImage(image, x, y, null);
            System.out.println(x + "," + y);
            if (i % 2 == 1) {
                x = 0;
                y = y + image.getHeight() + 10;
            } else x = image.getWidth() + 10;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // Calculate width and height based on your tile sizes and spacing
        return new Dimension(400, 720); // example; customize as needed
    }
}
