package core;

import entities.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class DesignPanel extends JPanel implements MouseListener, MouseMotionListener {

    private JPopupMenu popupMenu;
    private Tile currSelectedAsset;
    public static ArrayList<Tile> tileMapData = new ArrayList<>();

    public DesignPanel() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addPopupMenu();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void addPopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem bringToFront = new JMenuItem("Bring to front");
        popupMenu.add(bringToFront);
        bringToFront.addActionListener(e -> {
            System.out.println(tileMapData.getFirst() == currSelectedAsset);
            if (tileMapData.isEmpty() || tileMapData.getFirst() == currSelectedAsset) return;
            System.out.println("helo");
            tileMapData.remove(currSelectedAsset);
            tileMapData.addFirst(currSelectedAsset);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Tile tile : tileMapData) {

            g.drawImage(tile.getImage(), tile.x, tile.y, null);
            if (currSelectedAsset == tile) {
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


        for (Tile tile : tileMapData) {
//            System.out.println(tile.x + ", " + tile.y + "; " + tile.getImage().getWidth() + ", " + tile.getImage().getHeight());
            if (isClickInsideBBox(e, tile)) {
                currSelectedAsset = tile;
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showPopup(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (currSelectedAsset != null) {
//            System.out.println(e.getX() + "," + e.getY());
            currSelectedAsset.x = e.getX();
            currSelectedAsset.y = e.getY();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void setCurrSelectedAsset(Tile currSelectedAsset) {
        this.currSelectedAsset = currSelectedAsset;
    }

    private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger() && currSelectedAsset != null && isClickInsideBBox(e)) { // catches right-click
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private boolean isClickInsideBBox(MouseEvent e, Tile tile) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        return mouseX >= tile.x && mouseX <= tile.x + tile.getImage().getWidth()
                && mouseY >= tile.y && mouseY <= tile.y + tile.getImage().getHeight();
    }

    private boolean isClickInsideBBox(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        return mouseX >= currSelectedAsset.x && mouseX <= currSelectedAsset.x + currSelectedAsset.getImage().getWidth()
                && mouseY >= currSelectedAsset.y
                && mouseY <= currSelectedAsset.y + currSelectedAsset.getImage().getHeight();
    }
}
