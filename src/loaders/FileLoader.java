package loaders;

import com.google.gson.Gson;
import core.DesignPanel;
import entities.Tile;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class FileLoader {

    private final Gson gson;

    public FileLoader() {
        gson = new Gson();
    }

    public Tile[] readMap(String fileName) throws IOException, URISyntaxException {
        File file = new File(fileName);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }

        Tile[] tiles = gson.fromJson(builder.toString(), Tile[].class);
        for (Tile tile : tiles) {
            URL resource = ImageLoader.class.getResource(tile.getImagePath());
            assert resource != null;
            File imageFile = new File(resource.toURI());
            System.out.println(imageFile.getPath());
            tile.setImage(ImageIO.read(imageFile));
        }
        return tiles;
    }

    public void saveMap() throws IOException {
        String json = gson.toJson(DesignPanel.tileMapData);
        FileWriter fileWriter = new FileWriter("map001.json");
        BufferedWriter writer = new BufferedWriter(fileWriter);
        writer.write(json);
        writer.close();
    }
}
