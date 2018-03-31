package com.mygdx.game.server;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ServerPlayerDto;

import java.util.UUID;

public class ServerPlayer {
    private Vector2 position;
    private  UUID id;

    public ServerPlayer(UUID id) {
        position = new Vector2(0, 0);
        this.id = id;
    }
    public void move(Vector2 direction) {
        position.add(direction);
    }

    public UUID getId() {
        return id;
    }

    public ServerPlayerDto getDto() {
        return new ServerPlayerDto(id, position);
    }
}
