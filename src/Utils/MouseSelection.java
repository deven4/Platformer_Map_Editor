package Utils;

import core.DesignPanel;
import entities.Tile;

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
    private boolean isClickOverSelection(MouseEvent e) {
        for (Tile tile : DesignPanel.currSelectedAsset) {
            if (isClickInsideBBox(e, tile)) return true;
        }
        return false;
    }

    /**
     * Check if clicked on any tile (from top to bottom)
     **/
    private boolean isClkOnTile(MouseEvent e) {
        for (int i = tileMapData.size() - 1; i >= 0; i--) {
            Tile tile = tileMapData.get(i);
            if (isClickInsideBBox(e, tile)) {
                selectedTile = tile;
                return true;
            }
        }
        return false;
    }

    private boolean isClickInsideBBox(MouseEvent e, Tile tile) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        return mouseX >= tile.x && mouseX <= tile.x + tile.getWidth() && mouseY >= tile.y
                && mouseY <= tile.y + tile.getHeight();
    }

    public Tile getSelectedtile() {
        return selectedTile;
    }

    public MouseAction getAction() {
        return action;
    }

    public void setAction(Rectangle rect, MouseEvent e) {
        if (TileUtil.getCorner(rect, e.getPoint()) != -1) action = MouseAction.SCALE;
        else if (isClickOverSelection(e)) action = MouseAction.CLK_OVER_SELECTION;
        else if (isClkOnTile(e)) action = MouseAction.CLK_TILE;
        else action = MouseAction.DRAG;
    }
}