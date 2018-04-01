package com.mygdx.game;

import java.util.List;

public class GameDataDto {
    public final List<ServerPlayerDto> otherPlayers;
    public final ServerPlayerDto player;

    public GameDataDto(ServerPlayerDto player, List<ServerPlayerDto> otherPlayers) {
        this.player = player;
        this.otherPlayers = otherPlayers;
    }
}
