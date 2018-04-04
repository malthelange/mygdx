package com.mygdx.game.client;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

public class AssetSupplier {
    public static AssetManager assetManager;
    public static Animation<TextureRegion> PLAYER_MOVES_FORWARD_ANIMATION;
    public static Animation<TextureRegion> PLAYER_MOVES_BACKWARD_ANIMATION;
    public static Animation<TextureRegion> PLAYER_MOVES_SIDEWAYS_ANIMATION;

    public static void loadAssets() {
        assetManager = new AssetManager();
        assetManager.load("badlogic.jpg", Texture.class);
        assetManager.load("packed/pack-file.atlas", TextureAtlas.class);
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        // TODO use the preprocessed tileset data for the map, to enable padding. This should avoid lines.
        assetManager.load("maps/map1.tmx", TiledMap.class);
        assetManager.finishLoading();

        PLAYER_MOVES_FORWARD_ANIMATION =
                new Animation<>(
                        0.25f,
                        getAnimationArray("packed/pack-file.atlas", "player/Player", 16),
                        Animation.PlayMode.LOOP);
        PLAYER_MOVES_BACKWARD_ANIMATION =
                new Animation<>(
                        0.25f,
                        getAnimationArray("packed/pack-file.atlas", "player/Player_back", 16),
                        Animation.PlayMode.LOOP);
        PLAYER_MOVES_SIDEWAYS_ANIMATION =
                new Animation<>(
                        0.25f,
                        getAnimationArray("packed/pack-file.atlas", "player/Player_sideways", 16),
                        Animation.PlayMode.LOOP);
    }

    public static Array<TextureRegion> getAnimationArray(String atlasName, String regionName, int tileSize) {
        TextureAtlas textureAtlas = assetManager.get(atlasName, TextureAtlas.class);
        TextureRegion[][] regions = textureAtlas.findRegion(regionName).split(tileSize, tileSize);
        Array<TextureRegion> resultArray = new Array<>();
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[i].length; j++) {
                resultArray.add(regions[i][j]);
            }
        }
        return resultArray;
    }

    public static void dispose() {
        assetManager.dispose();
    }
}
