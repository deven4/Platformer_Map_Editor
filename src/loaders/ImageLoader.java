package loaders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageLoader {

    private static BufferedImage[] tileMap;

    private static BufferedImage[] loadImages(String directory) {
        URL resource = ImageLoader.class.getResource("/" + directory);
        try {
            assert resource != null;
            File folder = new File(resource.getPath());
            File[] files = folder.listFiles();
            assert files != null;
            tileMap = new BufferedImage[files.length];
            for (int i = 0; i < files.length; i++) {
                tileMap[i] = ImageIO.read(files[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tileMap;
    }

    public static BufferedImage[] getTileMap() {
        if (tileMap != null) return tileMap;
        return loadImages("tiles");
    }
}
