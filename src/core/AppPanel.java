package core;

import entities.Tile;
import loaders.FileLoader;
import loaders.ImageLoader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class AppPanel extends JPanel {

    private final App app;
    private final ImageLoader imageLoader;
    private final FileLoader fileLoader;
    private final DesignPanel designPanel;

    public AppPanel(App app) {
        this.app = app;
        fileLoader = new FileLoader();
        designPanel = new DesignPanel(app);
        imageLoader = ImageLoader.getInstance();
        TilePanel tilePanel = new TilePanel(designPanel);

        setLayout(new BorderLayout());
        // Wrap designPanel in a panel with margin
        JPanel designWrapper = new JPanel(new BorderLayout());
        designWrapper.setBorder(new EmptyBorder(10, 10, 10, 10)); // top,left,bottom,right margin
        designWrapper.add(designPanel, BorderLayout.CENTER);

        // Wrap tilePanel in a panel with margin
        JPanel tileWrapper = new JPanel(new BorderLayout());
        tileWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
        tileWrapper.add(tilePanel, BorderLayout.CENTER);

        add(designWrapper, BorderLayout.CENTER);
        add(tileWrapper, BorderLayout.EAST);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void update() {
        designPanel.repaint();
        System.out.println(app.getContentPane().getHeight());
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

    public JMenuBar getMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu bgMenu = new JMenu("Add background");
        JMenuItem clearItem = new JMenuItem("Clear");
        JMenuItem saveMap = new JMenuItem("Save map");
        JMenuItem importMap = new JMenuItem("Import map");

        for (String location : imageLoader.getBackgroundList().keySet()) {
            String name = location.substring(location.lastIndexOf('/') + 1, location.length() - 1);
            JCheckBoxMenuItem item = new JCheckBoxMenuItem(name);
            item.addActionListener(e -> addBackground(e, location));
            bgMenu.add(item);
        }
        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.addActionListener(_ -> System.exit(0));
        saveMap.addActionListener(_ -> saveMap());
        importMap.addActionListener(_ -> importMap());
        clearItem.addActionListener(_ -> DesignPanel.tileMapData.clear());
        fileMenu.add(importMap);
        fileMenu.add(saveMap);
        fileMenu.add(exitItem);
        editMenu.add(clearItem);
        editMenu.add(bgMenu);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        return menuBar;
    }

    private void addBackground(ActionEvent e, String location) {
        JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
        BufferedImage img = imageLoader.getBackgroundList().get(location);
        if (checkBoxMenuItem.isSelected()) DesignPanel.bgImageData.add(img);
        else DesignPanel.bgImageData.remove(img);
    }
}
