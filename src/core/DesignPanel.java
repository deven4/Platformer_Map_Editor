package core;

import entities.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class DesignPanel extends JPanel implements MouseListener, MouseMotionListener {

    private static final int BRING_TO_FRONT = 0;
    private static final int SEND_TO_BACK = 1;

    private Point start, end;
    private final Rectangle selectionBox;

    private JPopupMenu popupMenu;
    private final List<Tile> currSelectedAsset;
    public static ArrayList<Tile> tileMapData = new ArrayList<>();

    public DesignPanel() {
        end = new Point();
        start = new Point(0, 0);
        selectionBox = new Rectangle();
        currSelectedAsset = new ArrayList<>();
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        addPopupMenu();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void addPopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem bringToFront = new JMenuItem("Bring to front");
        JMenuItem sendToBack = new JMenuItem("Send to back");
        JMenuItem deleteTile = new JMenuItem("Delete");
        popupMenu.add(bringToFront);
        popupMenu.add(sendToBack);
        popupMenu.add(deleteTile);
        bringToFront.addActionListener(e -> changeOrder(BRING_TO_FRONT));
        deleteTile.addActionListener(this::deleteTile);
        sendToBack.addActionListener(e -> changeOrder(SEND_TO_BACK));
    }

    private void deleteTile(ActionEvent actionEvent) {
        for (Tile tile : currSelectedAsset) {
            tileMapData.remove(tile);
        }
        currSelectedAsset.clear();
    }

    private void changeOrder(int order) {
        if (tileMapData.isEmpty()) return;
        for (Tile tile : currSelectedAsset) {
            tileMapData.remove(tile);
            if (order == BRING_TO_FRONT) tileMapData.addLast(tile);
            else tileMapData.addFirst(tile);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawTileMap(g); /* Draw Map */
        drawSelectionBox(g);
    }

    private void drawTileMap(Graphics g) {
        for (Tile tile : tileMapData) {
            g.drawImage(tile.getImage(), tile.x, tile.y, null);
            if (!currSelectedAsset.isEmpty() && currSelectedAsset.contains(tile)) {
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

    private void drawSelectionBox(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.f, new float[]{5.f}, 0.0f));
        selectionBox.setBounds(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.abs(start.x - end.x), Math.abs(start.y - end.y));
        g.drawRect(selectionBox.x, selectionBox.y, selectionBox.width, selectionBox.height);
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
        // Case 1: Click is over the current selection
        if (isClickOverSelection(e)) {
            for (Tile tile : currSelectedAsset) {
                tile.deltaX = e.getX() - tile.x;
                tile.deltaY = e.getY() - tile.y;
            }
            return;
        }

        // Case 2: Check if clicked on any tile (from top to bottom)
        currSelectedAsset.clear();
        for (int i = tileMapData.size() - 1; i >= 0; i--) {
            Tile tile = tileMapData.get(i);
            if (isClickInsideBBox(e, tile)) {
                tile.deltaX = e.getX() - tile.x;
                tile.deltaY = e.getY() - tile.y;
                currSelectedAsset.add(tile);
                return;
            }
        }

        // Case 3: Start selection box
        start = e.getPoint();
        end = start;
        currSelectedAsset.clear();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showPopup(e);
        for (Tile tile : tileMapData) {
            if (selectionBox.intersects(new Rectangle(tile.x, tile.y, tile.getImage().getWidth(), tile.getImage().getHeight()))) {
                currSelectedAsset.add(tile);
            }
        }
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
        if (!currSelectedAsset.isEmpty()) {
            for (Tile tile : currSelectedAsset) {
                tile.x = e.getX() - tile.deltaX;
                tile.y = e.getY() - tile.deltaY;
            }
        } else end = e.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void setCurrSelectedAsset(Tile tile) {
        this.currSelectedAsset.clear();
        this.currSelectedAsset.add(tile);
    }

    private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger() && !currSelectedAsset.isEmpty()) { // catches right-click
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private boolean isClickOverSelection(MouseEvent e) {
        for (Tile tile : currSelectedAsset) {
            if (isClickInsideBBox(e, tile)) return true;
        }
        return false;
    }

    private boolean isClickInsideBBox(MouseEvent e, Tile tile) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        return mouseX >= tile.x && mouseX <= tile.x + tile.getImage().getWidth() && mouseY >= tile.y && mouseY <= tile.y + tile.getImage().getHeight();
    }
}
