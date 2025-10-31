package entities;

import core.DesignPanel.MouseAction;

import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import static core.DesignPanel.currSelectedAsset;
import static core.DesignPanel.tileMapData;

public class TileHelper {

    private static Tile selectedTile = null;

    public static MouseAction getAction(MouseEvent e) {
        if (isClickOverSelection(e)) return MouseAction.CLK_OVER_SELECTION;
        else if(isClkOnTile(e)) return MouseAction.CLK_TILE;
        else return MouseAction.DRAG;
    }

    private static boolean isClkOverCorner(MouseEvent e) {
        Ellipse2D ellipse2D = new Ellipse2D.Float(currSelectedAsset.getFirst().getX() - 10,
                currSelectedAsset.getFirst().getY() - 10, 20, 20);
        return false;
    }

    /**
     * Check if click is over the current selection (inside the bounding box)
     **/
    private static boolean isClickOverSelection(MouseEvent e) {
        for (Tile tile : currSelectedAsset) {
            if (isClickInsideBBox(e, tile)) return true;
        }
        return false;
    }

    /**
     * Check if clicked on any tile (from top to bottom)
     **/
    private static boolean isClkOnTile(MouseEvent e) {
        for (int i = tileMapData.size() - 1; i >= 0; i--) {
            Tile tile = tileMapData.get(i);
            if (isClickInsideBBox(e, tile)) {
                selectedTile = tile;
                return true;
            }
        }
        return false;
    }


    private static boolean isClickInsideBBox(MouseEvent e, Tile tile) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        return mouseX >= tile.x && mouseX <= tile.x + tile.getImage().getWidth() && mouseY >= tile.y
                && mouseY <= tile.y + tile.getImage().getHeight();
    }

    public static Tile getSelectedtile() {
        return selectedTile;
    }
}