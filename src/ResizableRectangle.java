import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ResizableRectangle extends JPanel {
    private Rectangle rect = new Rectangle(100, 100, 150, 100);
    private static final int HANDLE_SIZE = 10;
    private Point dragStartPoint;
    private boolean resizing = false;
    private int resizeCorner = -1; // 0=TL, 1=TR, 2=BL, 3=BR

    public ResizableRectangle() {
        MouseAdapter mouseHandler = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                int corner = getCorner(e.getPoint());
                if (corner != -1) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                resizeCorner = getCorner(e.getPoint());
                if (resizeCorner != -1) {
                    resizing = true;
                    dragStartPoint = e.getPoint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (resizing && dragStartPoint != null) {
                    int dx = e.getX() - dragStartPoint.x;
                    int dy = e.getY() - dragStartPoint.y;

                    switch (resizeCorner) {
                        case 0 -> { // Top-left
                            rect.x += dx;
                            rect.y += dy;
                            rect.width -= dx;
                            rect.height -= dy;
                        }
                        case 1 -> { // Top-right
                            rect.y += dy;
                            rect.width += dx;
                            rect.height -= dy;
                        }
                        case 2 -> { // Bottom-left
                            rect.x += dx;
                            rect.width -= dx;
                            rect.height += dy;
                        }
                        case 3 -> { // Bottom-right
                            rect.width += dx;
                            rect.height += dy;
                        }
                    }
                    dragStartPoint = e.getPoint();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                resizing = false;
                resizeCorner = -1;
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        ((Graphics2D) g).draw(rect);
        drawHandles(g);
    }

    private void drawHandles(Graphics g) {
        g.setColor(Color.RED);
        for (Point p : getHandlePoints()) {
            g.fillRect(p.x - HANDLE_SIZE / 2, p.y - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
        }
    }

    private Point[] getHandlePoints() {
        return new Point[]{
                new Point(rect.x, rect.y), // TL
                new Point(rect.x + rect.width, rect.y), // TR
                new Point(rect.x, rect.y + rect.height), // BL
                new Point(rect.x + rect.width, rect.y + rect.height) // BR
        };
    }

    private int getCorner(Point p) {
        Point[] handles = getHandlePoints();
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Resizable Rectangle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ResizableRectangle());
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
}
