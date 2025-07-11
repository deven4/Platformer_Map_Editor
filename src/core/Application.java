package core;

import javax.swing.*;
import java.util.Arrays;

public class Application extends JFrame implements Runnable {

    private boolean running = false;
    private final AppPanel appPanel;

    Application() {
        appPanel = new AppPanel();
        addMenuBar();
        start();

        getContentPane().add(appPanel);
        pack(); // Adjusts frame to panel’s preferred size
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setSize(1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void addMenuBar() {
        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenuItem clearItem = new JMenuItem("Clear");
        JMenuItem saveMap = new JMenuItem("Save map");
        JMenuItem importMap = new JMenuItem("Import map");
        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.addActionListener(_ -> System.exit(0));
        saveMap.addActionListener(_ -> appPanel.saveMap());
        importMap.addActionListener(_ -> appPanel.importMap());
        clearItem.addActionListener(_ -> DesignPanel.tileMapData.clear());
        fileMenu.add(importMap);
        fileMenu.add(saveMap);
        fileMenu.add(exitItem);
        editMenu.add(clearItem);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        setJMenuBar(menuBar);
    }

    private void start() {
        running = true;
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long currentTime = System.nanoTime();
            int TARGET_FPS = 100;
            double TIME_PER_FRAME = 1000000000.0 / TARGET_FPS;
            delta += (currentTime - lastTime) / TIME_PER_FRAME;
            lastTime = currentTime;

            while (delta >= 1) {
                appPanel.update();
                delta--;
            }

            // Optional: Sleep a bit to reduce CPU usage
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public static void main(String[] args) {
        new Application();
    }
}
