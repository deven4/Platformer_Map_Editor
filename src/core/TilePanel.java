package core;

import entities.Tile;
import loaders.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TilePanel extends JPanel implements MouseMotionListener, MouseListener {

    public static final int PANEL_WIDTH = 120;

    private JLabel titleLabel;
    private Tile selectedTile;
    private final JPanel topBarPanel;
    private final JPanel buttonPanel;
    private final JScrollPane scrollPane;
    private final DesignPanel designPanel;

    private String currAssetKey;
    private int currAssetKeyIdx = 0;
    private final List<String> assetKeys;
    private final HashMap<String, BufferedImage[]> assetSetGrp;

    public TilePanel(DesignPanel designPanel) {
        this.designPanel = designPanel;

        topBarPanel = new JPanel();
        buttonPanel = new JPanel();
        ImageLoader imageLoader = new ImageLoader();
        scrollPane = new JScrollPane(buttonPanel);
        assetSetGrp = imageLoader.getAssetGrp();
        assetKeys = new ArrayList<>(assetSetGrp.keySet());
        currAssetKey = assetKeys.get(currAssetKeyIdx);

        setBackground(new Color(0xadb5bd));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentY(TOP_ALIGNMENT);
        //setOpaque(false); // transparent panel

        addTopBar();
        addTitleLabel();
        addButtonPanel(assetSetGrp.get(currAssetKey));

        setPreferredSize(new Dimension(PANEL_WIDTH, getPreferredSize().height));
    }

    private void addTitleLabel() {
        titleLabel = new JLabel(currAssetKey);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        add(titleLabel);
    }

    private void addTopBar() {
        JButton leftBtn = new JButton("<");
        JButton rightBtn = new JButton(">");
        topBarPanel.add(leftBtn);
        topBarPanel.add(rightBtn);
        topBarPanel.setBackground(new Color(30, 30, 30));
        topBarPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        leftBtn.setFocusable(false);
        rightBtn.setFocusable(false);
        leftBtn.setBackground(Color.DARK_GRAY);
        leftBtn.setForeground(Color.WHITE);
        rightBtn.setBackground(Color.DARK_GRAY);
        rightBtn.setForeground(Color.WHITE);
        leftBtn.addActionListener(_ -> updateButtonPanel(-1));
        rightBtn.addActionListener(_ -> updateButtonPanel(1));

        topBarPanel.setPreferredSize(new Dimension(PANEL_WIDTH, topBarPanel.getPreferredSize().height));
        add(topBarPanel);
    }

    private void updateButtonPanel(int idx) {
        currAssetKeyIdx += idx;
        buttonPanel.removeAll();

        if (currAssetKeyIdx >= assetKeys.size()) currAssetKeyIdx = 0;
        else if (currAssetKeyIdx < 0) currAssetKeyIdx = assetKeys.size() - 1;
        currAssetKey = assetKeys.get(currAssetKeyIdx);
        addButtonPanel(assetSetGrp.get(currAssetKey));
        titleLabel.setText(currAssetKey);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void addButtonPanel(BufferedImage[] images) {
        buttonPanel.setLayout(new GridLayout(0, 2, 2, 2)); // auto rows, 2 columns, 5px spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (BufferedImage image : images) {
            buttonPanel.add(createImageButton(image));
        }
        buttonPanel.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(PANEL_WIDTH, 700)); // adjust height

        add(scrollPane);
        // setPreferredSize(new Dimension(getPreferredSize().width, 500));
    }

    public JButton createImageButton(BufferedImage image) {
        ImageIcon icon = new ImageIcon(image);
        Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(scaled));
        //button.setBorderPainted(false);
        //button.setContentAreaFilled(false);
        //button.setFocusPainted(false);
        //button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("Tile", image);
        button.addMouseListener(this);
        button.addMouseMotionListener(this);
        button.setPreferredSize(new Dimension(32, 32));
        button.setBackground(new Color(0xadb5bd));
        //button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return button;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        Point current = SwingUtilities.convertPoint(button, e.getPoint(), designPanel);
        selectedTile.x = current.x;
        selectedTile.y = current.y;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        Point current = SwingUtilities.convertPoint(button, e.getPoint(), designPanel);
        BufferedImage bufferedImage = (BufferedImage) button.getClientProperty("Tile");
        selectedTile = new Tile((int) current.getX(), (int) current.getY(), bufferedImage);
        designPanel.setCurrSelectedAsset(selectedTile);
        DesignPanel.tileMapData.add(selectedTile);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
