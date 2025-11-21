package core;

import Utils.TileUtil;
import entities.Tile;
import Utils.MouseSelection;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DesignPanel extends JPanel implements MouseListener, MouseMotionListener {
    private static final int BRING_TO_FRONT = 0;
    private static final int SEND_TO_BACK = 1;

    int resizeStartX, resizeStartY;
    int initialWidth, initialHeight;
    int cameraX, cameraY;
    int lastMouseX, lastMouseY;

    private Point start, end;
    private int deltaX, deltaY;
    private final Rectangle boundingBox; /* Red dotted line */
    private final Rectangle selectionBox;

    private JPopupMenu popupMenu;
    private final MouseSelection mouseHelper;
    public static ArrayList<Tile> tileMapData = new ArrayList<>();
    public static final List<Tile> currSelectedAsset = new ArrayList<>();
    public static ArrayList<BufferedImage> bgImageData = new ArrayList<>();

    public DesignPanel() {
        end = new Point();
        start = new Point(0, 0);
        boundingBox = new Rectangle();
        selectionBox = new Rectangle();
        mouseHelper = new MouseSelection();
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
    }

    private void addPopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem bringToFront = new JMenuItem("Bring to front");
        JMenuItem sendToBack = new JMenuItem("Send to back");
        JMenuItem setTypeP = new JMenuItem("Set type to platform");
        JMenuItem setTypeC = new JMenuItem("Set Collision");
        JMenuItem deleteTile = new JMenuItem("Delete");
        popupMenu.add(bringToFront);
        popupMenu.add(sendToBack);
        popupMenu.add(setTypeP);
        popupMenu.add(setTypeC);
        popupMenu.add(deleteTile);
        bringToFront.addActionListener(_ -> changeOrder(BRING_TO_FRONT));
        deleteTile.addActionListener(this::deleteTile);
        sendToBack.addActionListener(_ -> changeOrder(SEND_TO_BACK));
        setTypeP.addActionListener(_ -> {
            for (Tile tile : currSelectedAsset) tile.setType("platform");
        });
        setTypeC.addActionListener(_ -> {
            for (Tile tile : currSelectedAsset) tile.setType("Collision");
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
            g.drawImage(tile.getImage(), tile.x - cameraX, tile.y - cameraY, tile.getWidth(), tile.getHeight(),
                    null);
            if (!currSelectedAsset.isEmpty() && currSelectedAsset.contains(tile)) {
                g.setColor(Color.BLACK);
                g.drawRect(tile.x - cameraX, tile.y - cameraY, tile.getWidth(), tile.getHeight());
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

        g.setColor(Color.RED);
        g.drawRect(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);

        if (currSelectedAsset.size() <= 1) boundingBox.setBounds(0, 0, 0, 0);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (currSelectedAsset.size() > 1) mouseHelper.setAction(boundingBox, e);
        else {
            if (!currSelectedAsset.isEmpty())
                mouseHelper.setAction(currSelectedAsset.getFirst().getRect(), e);
        }

        switch (mouseHelper.getAction()) {
            case DRAG -> {
                // Start selection box
                start = e.getPoint();
                end = start;
                currSelectedAsset.clear();
            }
            case CLK_TILE -> {
                currSelectedAsset.clear();
                Tile tileClicked = mouseHelper.getSelectedtile();
                tileClicked.deltaX = e.getX() - tileClicked.x;
                tileClicked.deltaY = e.getY() - tileClicked.y;
                currSelectedAsset.add(tileClicked);
            }
            case CLK_OVER_SELECTION -> {
                deltaX = e.getX() - boundingBox.x;
                deltaY = e.getY() - boundingBox.y;
                for (Tile tile : currSelectedAsset) {
                    tile.deltaX = e.getX() - tile.x;
                    tile.deltaY = e.getY() - tile.y;
                }
            }
            case SCALE -> {
                resizeStartX = e.getX();
                resizeStartY = e.getY();
                initialWidth = currSelectedAsset.getFirst().getWidth();
                initialHeight = currSelectedAsset.getFirst().getHeight();
            }
            case PAN -> {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
            }
            default -> {

            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showPopup(e);
        for (Tile tile : tileMapData) {
            if (selectionBox.intersects(new Rectangle(tile.x, tile.y, tile.getWidth(), tile.getHeight()))) {
                currSelectedAsset.add(tile);
            }
        }

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Tile tile : currSelectedAsset) {
            // Update bounds
            if (tile.x < minX) minX = tile.x;
            if (tile.y < minY) minY = tile.y;
            if (tile.x + tile.getWidth() > maxX) maxX = tile.x + tile.getWidth();
            if (tile.y + tile.getHeight() > maxY) maxY = tile.y + tile.getHeight();
        }
        boundingBox.setBounds(minX, minY, maxX - minX, maxY - minY);

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
        switch (mouseHelper.getAction()) {
            case DRAG -> end = e.getPoint();
            case SCALE -> {
                deltaX = e.getX() - resizeStartX;
                deltaY = e.getY() - resizeStartY;
                System.out.println(initialWidth + deltaX);
                currSelectedAsset.getFirst().setWidth(initialWidth + deltaX);
                currSelectedAsset.getFirst().setHeight(initialHeight + deltaY);
            }
            case PAN -> {
                /* Calculate how much the user dragged */
                deltaX = e.getX() - lastMouseX;
                deltaY = e.getY() - lastMouseY;
                cameraX -= deltaX;
                cameraY -= deltaY;

                lastMouseX = e.getX();
                lastMouseY = e.getY();
            }
            default -> {
                for (Tile tile : currSelectedAsset) {
                    tile.x = e.getX() - tile.deltaX;
                    tile.y = e.getY() - tile.deltaY;
                }
                boundingBox.setLocation(e.getX() - deltaX, e.getY() - deltaY);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (currSelectedAsset.isEmpty()) return;

        Rectangle rect;
        if (currSelectedAsset.size() == 1) rect = currSelectedAsset.getFirst().getRect();
        else rect = selectionBox;

        int corner = TileUtil.getCorner(rect, e.getPoint()); // check hovered over a tile corner
        switch (corner) {
            case 0 -> setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
            case 1 -> setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
            case 2 -> setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
            case 3 -> setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
            default -> setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
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
