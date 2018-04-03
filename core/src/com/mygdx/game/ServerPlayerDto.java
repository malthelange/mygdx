package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

import java.util.UUID;

public class ServerPlayerDto {
    public final PlayerMovementState movementState;
    public UUID id;
    public Vector2 position;

    public ServerPlayerDto(UUID id, Vector2 position, PlayerMovementState movementState) {
        this.id = id;
        this.position = position;
        this.movementState = movementState;
    }
}
