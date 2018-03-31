package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

public class ServerPlayerDto {
    public UUID id;
    public Vector2 position;

    public ServerPlayerDto(UUID id, Vector2 position) {
        this.id = id;
        this.position = position;
    }
}
