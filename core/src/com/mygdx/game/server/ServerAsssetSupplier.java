package com.mygdx.game.server;

import org.mapeditor.core.Map;
import org.mapeditor.io.TMXMapReader;

public class ServerAsssetSupplier {
    public static void loadAssets() throws Exception {
        TMXMapReader reader = new TMXMapReader();
        Map tileMap = reader.readMap(System.getProperty("user.dir") + "/maps/map1.tmx");
        System.out.println(tileMap.getHeight());
    }
}
