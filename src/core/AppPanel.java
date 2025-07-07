package core;

import javax.swing.*;
import java.awt.*;

public class AppPanel extends JPanel {

    private final DesignPanel designPanel;

    public AppPanel() {
        designPanel = new DesignPanel();
        TilePanel tilePanel = new TilePanel(designPanel);

        add(designPanel);
        add(tilePanel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    public void update() {
        designPanel.repaint();
    }
}
