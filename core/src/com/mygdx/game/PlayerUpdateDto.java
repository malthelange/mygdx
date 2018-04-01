package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

public class PlayerUpdateDto {
    public UUID uuid;
    public Vector2 direction;

    public PlayerUpdateDto(UUID uuid, Vector2 direction) {
        this.uuid = uuid;
        this.direction = direction;
    }
}
