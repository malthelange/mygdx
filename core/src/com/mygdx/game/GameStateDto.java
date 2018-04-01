package com.mygdx.game;

import java.util.UUID;

public class GameStateDto {
    public UUID playerId;
    public ServerPlayerDto serverPlayer;

    public GameStateDto(UUID playerId) {
        this.playerId = playerId;
    }

    public GameStateDto(ServerPlayerDto serverPlayer) {
        this.serverPlayer = serverPlayer;
    }
}
