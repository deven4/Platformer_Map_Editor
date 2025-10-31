package core;

import entities.Tile;
import entities.TileHelper;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DesignPanel extends JPanel implements MouseListener, MouseMotionListener {

    private static final int BRING_TO_FRONT = 0;
    private static final int SEND_TO_BACK = 1;

    public enum MouseAction {
        NONE, DRAG, RESIZE, CLK_TILE, CLK_OVER_SELECTION
    }

    private Point start, end;
    private final Rectangle selectionBox;

    private JPopupMenu popupMenu;
    boolean resizing;
    Tile firstAsset;
    private Point lastMouse;

    public static ArrayList<Tile> tileMapData = new ArrayList<>();
    public static final List<Tile> currSelectedAsset = new ArrayList<>();
    public static ArrayList<BufferedImage> bgImageData = new ArrayList<>();

    public DesignPanel(App app) {
        end = new Point();
        start = new Point(0, 0);
        selectionBox = new Rectangle();
        Border line = BorderFactory.createLineBorder(Color.BLACK, 2, true); // Colored outer border
        Border margin = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        setBorder(BorderFactory.createCompoundBorder(line, margin));
        setPreferredSize(new Dimension(1024, 200));

        addPopupMenu();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        DrawBgImg(g);
        drawTileMap(g); /* Draw Map */
        drawSelectionBox(g);

        if (!currSelectedAsset.isEmpty()) {
            g.setColor(Color.BLUE);
            g.fillOval(currSelectedAsset.getFirst().getX() - 10, currSelectedAsset.getFirst().getY() - 10, 20, 20);
        }
    }

    private void addPopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem bringToFront = new JMenuItem("Bring to front");
        JMenuItem sendToBack = new JMenuItem("Send to back");
        JMenuItem setType = new JMenuItem("Set type to platform");
        JMenuItem deleteTile = new JMenuItem("Delete");
        popupMenu.add(bringToFront);
        popupMenu.add(sendToBack);
        popupMenu.add(setType);
        popupMenu.add(deleteTile);
        bringToFront.addActionListener(_ -> changeOrder(BRING_TO_FRONT));
        deleteTile.addActionListener(this::deleteTile);
        sendToBack.addActionListener(_ -> changeOrder(SEND_TO_BACK));
        setType.addActionListener(_ -> {
            for (Tile tile : currSelectedAsset) tile.setType("platform");
        });
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

    private void DrawBgImg(Graphics g) {
        for (BufferedImage image : bgImageData) {
            g.drawImage(image, 0, 0, null);
        }
    }

    private void drawTileMap(Graphics g) {
        for (Tile tile : tileMapData) {
            g.drawImage(tile.getImage(), tile.x, tile.y, tile.getWidth(), tile.getHeight(), null);
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
        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.f,
                new float[]{5.f}, 0.0f));
        selectionBox.setBounds(Math.min(start.x, end.x), Math.min(start.y, end.y), Math.abs(start.x - end.x),
                Math.abs(start.y - end.y));
        g.drawRect(selectionBox.x, selectionBox.y, selectionBox.width, selectionBox.height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        MouseAction mouseAction = TileHelper.getAction(e);

        switch (mouseAction) {
            case CLK_TILE -> {
                Tile tile = TileHelper.getSelectedtile();
                if (tile != null) {
                    tile.deltaX = e.getX() - tile.x;
                    tile.deltaY = e.getY() - tile.y;
                    currSelectedAsset.add(tile);
                } else System.err.println("NO TILE SELECTED");
            }
            case CLK_OVER_SELECTION -> {
                for (Tile tile : currSelectedAsset) {
                    tile.deltaX = e.getX() - tile.x;
                    tile.deltaY = e.getY() - tile.y;
                }
            }
            case DRAG ->  {
                start = e.getPoint();
                end = start;
                currSelectedAsset.clear();
            }
        }
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

        if (resizing) {

            //  firstAsset.setWidth(width_new_box);
            //  firstAsset.setHeight(height_new_box);
            firstAsset.setX(e.getX());
            firstAsset.setY(e.getY());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!currSelectedAsset.isEmpty()) {
            firstAsset = currSelectedAsset.getFirst();
            //System.out.println(firstAsset.x + ", " + firstAsset.y);
            Ellipse2D ellipse2D = new Ellipse2D.Float(currSelectedAsset.getFirst().getX() - 10,
                    currSelectedAsset.getFirst().getY() - 10, 20, 20);

            if (ellipse2D.contains(e.getX(), e.getY())) {
                setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
            } else setCursorToDefault();

        } else setCursorToDefault();
    }

    private void setCursorToDefault() {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        resizing = false;
    }

    public void setCurrSelectedAsset(Tile tile) {
        currSelectedAsset.clear();
        currSelectedAsset.add(tile);
    }

    private void showPopup(MouseEvent e) {
        if (e.isPopupTrigger() && !currSelectedAsset.isEmpty()) { // catches right-click
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
