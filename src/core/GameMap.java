package core;

import entities.Tile;
import loaders.FileLoader;
import loaders.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class GameMap {

    private final List<Tile> map = new ArrayList<>();

    public GameMap() {
        FileLoader fileLoader = new FileLoader();
        ImageLoader imageLoader = new ImageLoader();

        for (Tile tile : fileLoader.getTileMap()) {
            tile.setImage(imageLoader.getTileMap()[tile.getIdx()]);
            map.add(tile);
        }
    }

    public List<Tile> getMap() {
        return map;
    }
}
