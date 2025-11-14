package Utils;

import java.awt.*;

public class TileUtil {

    private static final int HANDLE_SIZE = 10;

    public static Point[] getHandlePoints(Rectangle rect) {
        return new Point[]{
                new Point(rect.x, rect.y), // TL
                new Point(rect.x + rect.width, rect.y), // TR
                new Point(rect.x, rect.y + rect.height), // BL
                new Point(rect.x + rect.width, rect.y + rect.height) // BR
        };
    }

    public static int getCorner(Rectangle rect, Point p)  {
        // returns 0=TL, 1=TR, 2=BL, 3=BR
        Point[] handles = getHandlePoints(rect);
        for (int i = 0; i < handles.length; i++) {
            Rectangle handleRect = new Rectangle(
                    handles[i].x - HANDLE_SIZE / 2,
                    handles[i].y - HANDLE_SIZE / 2,
                    HANDLE_SIZE, HANDLE_SIZE
            );
            if (handleRect.contains(p)) return i;
        }
        return -1;
    }
}
