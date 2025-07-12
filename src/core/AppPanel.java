package core;

import entities.Tile;
import loaders.FileLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class AppPanel extends JPanel {

    private final FileLoader fileLoader;
    private final DesignPanel designPanel;

    public AppPanel() {
        fileLoader = new FileLoader();
        designPanel = new DesignPanel();

        add(designPanel);
        add(new TilePanel(designPanel));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void update() {
        designPanel.repaint();
    }

    public void saveMap() {
        if (DesignPanel.tileMapData.isEmpty()) return;
        try {
            fileLoader.saveMap();
            JOptionPane.showMessageDialog(this, "Saved map!",
                    "SAVE SUCCESS", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "SAVE FAIL", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void importMap() {
        JFileChooser chooser = new JFileChooser();
        // Only allow .json files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON files", "json");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            String imagePath = selectedFile.getAbsolutePath();
            System.out.println("Selected image path: " + imagePath);
            try {
                Tile[] tiles = fileLoader.readMap(imagePath);
                DesignPanel.tileMapData.clear();
                DesignPanel.tileMapData.addAll(List.of(tiles));
            } catch (IOException | URISyntaxException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                        "Import Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
