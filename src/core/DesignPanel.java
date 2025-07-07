package core;

import entities.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class DesignPanel extends JPanel implements MouseListener, MouseMotionListener {

    private Tile currentSelectedAsset;
    public static ArrayList<Tile> tileMapData = new ArrayList<>();

    public DesignPanel() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Tile tile : tileMapData) {
            g.drawImage(tile.getImage(), tile.x, tile.y, null);
            if (currentSelectedAsset == tile) {
                g.drawRect(tile.x, tile.y, tile.getImage().getWidth(), tile.getImage().getHeight());
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1024, 768);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        System.out.println("mouse click: " + mouseX + ", " + mouseY);
        for (Tile tile : tileMapData) {
            System.out.println(tile.x + ", " + tile.y + "; " + tile.getImage().getWidth() + ", " + tile.getImage().getHeight());
            if (mouseX >= tile.x && mouseX <= tile.x + tile.getImage().getWidth() && mouseY >= tile.y && mouseY <= tile.y + tile.getImage().getHeight()) {
                currentSelectedAsset = tile;
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentSelectedAsset = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (currentSelectedAsset != null) {
            System.out.println(e.getX() + "," + e.getY());
            currentSelectedAsset.x = e.getX();
            currentSelectedAsset.y = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
