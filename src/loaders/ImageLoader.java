package loaders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

public class ImageLoader {

    private final HashMap<String, BufferedImage[]> assetGrp;

    public ImageLoader() {
        assetGrp = new HashMap<>();

        assetGrp.put("Tiles", loadImages("tiles"));
        assetGrp.put("Environment", loadImages("environment"));
        assetGrp.put("Building", loadImages("building"));
    }

    private BufferedImage[] loadImages(String directory) {
        BufferedImage[] asset;
        URL resource = ImageLoader.class.getResource("/" + directory);
        try {
            assert resource != null;
            File folder = new File(resource.toURI());
            File[] files = folder.listFiles();
            assert files != null;
            asset = new BufferedImage[files.length];
            for (int i = 0; i < files.length; i++) {
                asset[i] = ImageIO.read(files[i]);
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return asset;
    }

    public HashMap<String, BufferedImage[]> getAssetGrp() {
        return assetGrp;
    }
}
