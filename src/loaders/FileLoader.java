package loaders;

import com.google.gson.Gson;
import entities.Tile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class FileLoader {

    private Tile[] tileMap;

    public FileLoader() {
        readMaps("map1.json");
    }

    private void readMaps(String fileName) {
        URL resource = getClass().getResource("/levels/" + fileName);
        try {
            assert resource != null;
            System.out.println(resource);
            File file = new File(resource.getPath());
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            Gson gson = new Gson();
            tileMap = gson.fromJson(builder.toString(), Tile[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Tile[] getTileMap() {
        return tileMap;
    }

    public static void main(String[] args) {
        new FileLoader();
    }
}
