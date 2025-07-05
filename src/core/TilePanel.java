package core;

import entities.Tile;
import loaders.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class TilePanel extends JPanel implements MouseMotionListener, MouseListener {

    private Tile selectedTile;
    private final DesignPanel designPanel;
    private GridBagConstraints gbc;

    public TilePanel(DesignPanel appPanel) {
        this.designPanel = appPanel;
        this.gbc = new GridBagConstraints();

        setBackground(Color.GRAY);
        setLayout(new GridBagLayout());
        setAlignmentY(TOP_ALIGNMENT);
        //setOpaque(false); // transparent panel
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTH;

        add(getTopBar(), gbc);
        addButtons();

        setPreferredSize(new Dimension(180, getPreferredSize().height));
    }

    private JPanel getTopBar() {
        JPanel topBar = new JPanel();
        JButton leftBtn = new JButton("<");
        JButton rightBtn = new JButton(">");
        topBar.add(leftBtn);
        topBar.add(rightBtn);
        // Sticky top trick
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 1;
        return topBar;
    }

    private void addButtons() {
        int buttonIdx = 0;
        for (int i = 0; i < ImageLoader.getTileMap().length; i++) {
            if (i % 2 == 1) {
                gbc.gridx = 1;
                gbc.gridy = buttonIdx;
            } else {
                gbc.gridx = 0;
                gbc.gridy = buttonIdx + 1;
            }
            gbc.gridwidth = 1;
            add(createImageButton(ImageLoader.getTileMap()[i]), gbc);
            buttonIdx++;
        }
        // setPreferredSize(new Dimension(getPreferredSize().width, 500));
    }

    public JButton createImageButton(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(scaled));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("Tile", image);
        button.addMouseListener(this);
        button.addMouseMotionListener(this);
        button.setPreferredSize(new Dimension(32, 32));
        button.setBackground(Color.RED);
        return button;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        Point current = SwingUtilities.convertPoint(button, e.getPoint(), designPanel);
        selectedTile.x = current.x;
        selectedTile.y = current.y;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        Point current = SwingUtilities.convertPoint(button, e.getPoint(), designPanel);
        BufferedImage bufferedImage = (BufferedImage) button.getClientProperty("Tile");
        selectedTile = new Tile((int) current.getX(), (int) current.getY(), bufferedImage);
        AppPanel.tileMapData.add(selectedTile);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
