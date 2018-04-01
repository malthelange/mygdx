package com.mygdx.game.server;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.PlayerMovementState;
import com.mygdx.game.PlayerUpdateDto;
import com.mygdx.game.ServerPlayerDto;

import java.util.UUID;

public class ServerPlayer {
    private Vector2 position;
    private UUID id;
    private PlayerMovementState movementState;

    public ServerPlayer(UUID id) {
        position = new Vector2(0, 0);
        this.id = id;
    }

    public void move(Vector2 direction) {
        position.add(direction);
    }

    public void setMovementState(PlayerMovementState movementState) {
        this.movementState = movementState;
    }

    public UUID getId() {
        return id;
    }

    public ServerPlayerDto getDto() {
        return new ServerPlayerDto(id, position, movementState);
    }

    public void update(PlayerUpdateDto playerUpdateDto) {
        move(playerUpdateDto.direction);
        movementState = playerUpdateDto.movementState;
    }
}
