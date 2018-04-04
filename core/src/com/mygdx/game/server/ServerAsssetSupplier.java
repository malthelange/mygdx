package com.mygdx.game.server;

import org.mapeditor.core.Map;
import org.mapeditor.io.TMXMapReader;

public class ServerAsssetSupplier {
    public static Map tileMap;

    public static void loadAssets() throws Exception {
        TMXMapReader reader = new TMXMapReader();
        tileMap = reader.readMap(System.getProperty("user.dir") + "/maps/map1.tmx");
    }
}
