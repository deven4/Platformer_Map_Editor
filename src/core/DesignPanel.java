package core;

import entities.Tile;

import javax.swing.*;
import java.awt.*;

import static core.AppPanel.tileMapData;

public class DesignPanel extends JPanel {

    public DesignPanel() {

        //setBackground(Color.BLUE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        System.out.println(tileMapData.size());
        for (Tile tile : tileMapData) {
            System.out.println(tile.x  + "," + tile.y);
            g.drawImage(tile.getImage(), tile.x, tile.y, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1024, 768);
    }
}
