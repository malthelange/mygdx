package com.mygdx.game;

import java.util.List;
import java.util.UUID;

public class GameStateDto {
    public UUID playerId;
    public ServerPlayerDto serverPlayer;
    public List<ServerPlayerDto> otherPlayerDtos;

    public GameStateDto(UUID playerId) {
        this.playerId = playerId;
    }

    public GameStateDto(ServerPlayerDto serverPlayer, List<ServerPlayerDto> otherPlayerDtos) {
        this.serverPlayer = serverPlayer;
        this.otherPlayerDtos = otherPlayerDtos;
    }
}
