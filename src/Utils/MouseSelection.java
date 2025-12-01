package Utils;

import core.DesignPanel;
import entities.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import static core.DesignPanel.currSelectedAsset;
import static core.DesignPanel.tileMapData;

public class MouseSelection {

    public enum MouseAction {
        SCALE,
        CLK_OVER_SELECTION,
        CLK_TILE,
        DRAG,
        PAN,
        NONE
    }

    private MouseAction action = MouseAction.NONE;
    private Tile selectedTile = null;

    private boolean isClkOverEdge(MouseEvent e) {
        if(currSelectedAsset.size() != 1) return false;
        Ellipse2D ellipse2D = new Ellipse2D.Float(DesignPanel.currSelectedAsset.getFirst().getX() - 10,
                DesignPanel.currSelectedAsset.getFirst().getY() - 10, 20, 20);
        return ellipse2D.contains(e.getX(), e.getY());
    }

    /**
     * Check if click is over the current selection (inside the bounding box)
     **/
    private boolean isClickOverSelection(Point e) {
        for (Tile tile : DesignPanel.currSelectedAsset) {
            if (isClickInsideBBox(e, tile)) return true;
        }
        return false;
    }

    /**
     * Check if clicked on any tile (from top to bottom)
     **/
    private boolean isClkOnTile(Point e) {
        for (int i = tileMapData.size() - 1; i >= 0; i--) {
            Tile tile = tileMapData.get(i);
            if (isClickInsideBBox(e, tile)) {
                selectedTile = tile;
                return true;
            }
        }
        return false;
    }

    private boolean isClickInsideBBox(Point mouse, Tile tile) {
        return mouse.x >= (tile.x - DesignPanel.cameraX) && mouse.y <= (tile.x - DesignPanel.cameraX) + tile.getWidth()
                && mouse.y >= (tile.y - DesignPanel.cameraY) && mouse.y <= (tile.y - DesignPanel.cameraY)
                + tile.getHeight();
    }

    public Tile getSelectedtile() {
        return selectedTile;
    }

    public MouseAction getAction() {
        return action;
    }

    public void setAction(Rectangle rect,Point e1, MouseEvent e) {
        if(rect != null)System.out.print(rect.getX() + "," + rect.getY());
        System.out.println("; " + e1);
        if (rect != null && TileUtil.getCorner(rect, e1) != -1) action = MouseAction.SCALE;
        else if (isClickOverSelection(e1)) action = MouseAction.CLK_OVER_SELECTION;
        else if (isClkOnTile(e1)) action = MouseAction.CLK_TILE;
        else if (SwingUtilities.isMiddleMouseButton(e)) action = MouseAction.PAN;
        else action = MouseAction.DRAG;
    }
}