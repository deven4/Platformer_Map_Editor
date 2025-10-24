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
    private final List<String> assetGrpNameSet;
    private HashMap<String, BufferedImage> selectedAssetGrp;
    private final HashMap<String, HashMap<String, BufferedImage>> assetGroup;

    public TilePanel(DesignPanel designPanel) {
        this.designPanel = designPanel;

        topBarPanel = new JPanel();
        buttonPanel = new JPanel();
        scrollPane = new JScrollPane(buttonPanel);
        assetGroup = ImageLoader.getInstance().getAssetGrp();
        assetGrpNameSet = new ArrayList<>(assetGroup.keySet());
        currAssetKey = assetGrpNameSet.get(currAssetKeyIdx);
        selectedAssetGrp = assetGroup.get(currAssetKey);

        setBackground(new Color(0xadb5bd));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentY(TOP_ALIGNMENT);

        addTopBar();
        addTitleLabel();
        addButtonPanel();

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

        add(topBarPanel);
    }

    private void updateButtonPanel(int idx) {
        currAssetKeyIdx += idx;
        buttonPanel.removeAll();

        if (currAssetKeyIdx >= assetGrpNameSet.size()) currAssetKeyIdx = 0;
        else if (currAssetKeyIdx < 0) currAssetKeyIdx = assetGrpNameSet.size() - 1;
        currAssetKey = assetGrpNameSet.get(currAssetKeyIdx);
        selectedAssetGrp = assetGroup.get(currAssetKey);
        addButtonPanel();
        titleLabel.setText(currAssetKey);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void addButtonPanel() {
        buttonPanel.setLayout(new GridLayout(0, 2, 2, 2)); // auto rows, 2 columns, 5px spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (String imagePath : selectedAssetGrp.keySet()) {
            buttonPanel.add(createImageButton(imagePath));
        }
        buttonPanel.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(PANEL_WIDTH, App.HEIGHT - 150)); // adjust height

        add(scrollPane);
    }

    public JButton createImageButton(String imagePath) {
        ImageIcon icon = new ImageIcon(selectedAssetGrp.get(imagePath));
        Image scaled = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(scaled));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.putClientProperty("Tile", imagePath);
        button.addMouseListener(this);
        button.addMouseMotionListener(this);
        button.setPreferredSize(new Dimension(32, 32));
        button.setBackground(new Color(0xadb5bd));
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
        String imagePath = (String) button.getClientProperty("Tile");
        selectedTile = new Tile((int) current.getX(), (int) current.getY(), imagePath, "", selectedAssetGrp.get(imagePath));
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
