package core;

import entities.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class DesignPanel extends JPanel implements MouseListener, MouseMotionListener {

    private Point start, end;
    private int deltaX, deltaY;

    private JPopupMenu popupMenu;
    private Tile currSelectedAsset;
    public static ArrayList<Tile> tileMapData = new ArrayList<>();

    public DesignPanel() {
        start = new Point(0, 0);
        end = new Point();
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
            if (tileMapData.isEmpty() || tileMapData.getLast() == currSelectedAsset) return;
            tileMapData.remove(currSelectedAsset);
            tileMapData.addLast(currSelectedAsset);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = Math.min(start.x, end.x);
        int y = Math.min(start.y, end.y);
        int width = Math.abs(start.x - end.x);
        int height = Math.abs(start.y - end.y);
        g.drawRect(x, y, width, height);

        /* Draw Map */
        for (Tile tile : tileMapData) {
            g.drawImage(tile.getImage(), tile.x, tile.y, null);
            if (currSelectedAsset == tile) {
                g.setColor(Color.BLACK);
                g.drawRect(tile.x, tile.y, tile.getImage().getWidth(), tile.getImage().getHeight());
                g.setColor(Color.RED);
//                Point botRight = new Point(tile.x + tile.getImage().getWidth(), tile.y + tile.getImage().getHeight());
//                g.drawOval(botRight.x, botRight.y, 5, 5);
//                g.drawLine(0, botRight.y, botRight.x, botRight.y);
//                g.drawLine(0, mouseY, mouseX, mouseY);
//                g.drawString(String.valueOf(botRight.x), botRight.x / 2 - 10, botRight.y - 5);
//                g.drawString(String.valueOf(mouseX), mouseX / 2 - 10, mouseY - 5);
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
        for (int i = tileMapData.size() - 1; i >= 0; i--) {
            Tile tile = tileMapData.get(i);
            if (isClickInsideBBox(e, tile)) {
                deltaX = e.getX() - tile.x;
                deltaY = e.getY() - tile.y;
                currSelectedAsset = tile;
                return;
            }
        }
        start = e.getPoint();
        end = start;
        currSelectedAsset = null;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showPopup(e);
        end = start;
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
            currSelectedAsset.x = e.getX() - deltaX;
            currSelectedAsset.y = e.getY() - deltaY;
        } else end = e.getPoint();
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
        return mouseX >= tile.x && mouseX <= tile.x + tile.getImage().getWidth() && mouseY >= tile.y && mouseY <= tile.y + tile.getImage().getHeight();
    }

    private boolean isClickInsideBBox(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        return mouseX >= currSelectedAsset.x && mouseX <= currSelectedAsset.x + currSelectedAsset.getImage().getWidth() && mouseY >= currSelectedAsset.y && mouseY <= currSelectedAsset.y + currSelectedAsset.getImage().getHeight();
    }
}
