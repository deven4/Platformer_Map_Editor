package core;

import entities.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AppPanel extends JPanel {

    public static ArrayList<Tile> tileMapData = new ArrayList<>();

    private final DesignPanel designPanel;

    public AppPanel() {
        designPanel = new DesignPanel();
        TilePanel tilePanel = new TilePanel(designPanel);

        JScrollPane scrollPane = new JScrollPane(tilePanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setPreferredSize(new Dimension(tilePanel.getPreferredSize().width + 10,
                750)); // Fixed height
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scroll

        add(designPanel);
        add(scrollPane);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    public void update() {
        designPanel.repaint();
    }
}
