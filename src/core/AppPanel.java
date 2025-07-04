package core;

import entities.Tile;

import javax.swing.*;
import java.awt.*;

public class AppPanel extends JPanel {

    private final GameMap gameMap;

    public AppPanel() {
        gameMap = new GameMap();

        add(new TilePanel());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void update() {

    }
}
