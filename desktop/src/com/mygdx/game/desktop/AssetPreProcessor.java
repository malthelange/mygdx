package com.mygdx.game.desktop;


import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetPreProcessor {
    public static void main(String[] args) {
        AssetPreProcessor.packTextures();
    }

    public static void packTextures() {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
        settings.duplicatePadding = true;
        TexturePacker.process(settings, "raw-images", "packed", "pack-file");
    }
}
