package loaders;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Singleton class
 * **/
public class ImageLoader {

    private static ImageLoader instance;

    private final HashMap<String, BufferedImage> backgroundList;
    private final HashMap<String, HashMap<String, BufferedImage>> assetGrp;

    private ImageLoader() {
        assetGrp = new HashMap<>();
        backgroundList = loadImages("background");

        assetGrp.put("Tiles", loadImages("tiles"));
        assetGrp.put("Environment", loadImages("environment"));
        assetGrp.put("Building", loadImages("building"));
    }

    // Singleton accessor
    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    private HashMap<String, BufferedImage> loadImages(String directory) {
        HashMap<String, BufferedImage> assetMap = new HashMap<>();
        URL resource = ImageLoader.class.getResource("/" + directory);
        try {
            assert resource != null;
            File folder = new File(resource.toURI());
            File[] files = folder.listFiles();
            assert files != null;
            for (File file : files) {
                assetMap.put(getRelativePath(file), ImageIO.read(file));
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return assetMap;
    }

    private static String getRelativePath(File file) throws URISyntaxException, UnsupportedEncodingException {
        String filePath = file.toURI().toString();
        String basePath = Objects.requireNonNull(ImageLoader.class.getResource("../")).toURI().toString();

        if (filePath.startsWith(basePath)) {
            String relative = filePath.substring(basePath.length());
            relative = URLDecoder.decode(relative, StandardCharsets.UTF_8);
            return "/" + relative; // add slash if needed
        }

        return file.getName(); // fallback
    }

    public HashMap<String, HashMap<String, BufferedImage>> getAssetGrp() {
        return assetGrp;
    }

    public HashMap<String, BufferedImage> getBackgroundList() {
        return backgroundList;
    }
}
