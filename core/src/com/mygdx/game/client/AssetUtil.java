package com.mygdx.game.client;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AssetUtil {
    public static Array<TextureRegion> getAnimationArray(String atlasName, String regionName, int tileSize) {
        TextureAtlas textureAtlas = GameController.assetManager.get(atlasName, TextureAtlas.class);
        TextureRegion[][] regions = textureAtlas.findRegion(regionName).split(tileSize, tileSize);
        Array<TextureRegion> resultArray = new Array<>();
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[i].length; j++) {
                resultArray.add(regions[i][j]);
            }
        }
        return resultArray;
    }
}
