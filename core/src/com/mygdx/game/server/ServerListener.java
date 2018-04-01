package com.mygdx.game.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.*;

import java.util.List;

public class ServerListener extends Listener {
    GameServer gameServer;

    public ServerListener(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void received(Connection connection, Object object) {
        if (object instanceof GetGameDataDto) {
            GetGameDataDto getGameDataDto = (GetGameDataDto) object;
            ServerPlayer serverPlayer = gameServer.addServerPlayer(getGameDataDto.uuid);
            List<ServerPlayerDto> otherPlayers = gameServer.getOtherPlayerDtos(getGameDataDto.uuid);
            connection.sendTCP(new GameDataDto(serverPlayer.getDto(), otherPlayers));
        } else if (object instanceof PlayerUpdateDto) {
            gameServer.updatePlayer((PlayerUpdateDto) object);
        } else if (object instanceof GameStateDto) {
            connection.sendTCP(gameServer.getGameStateDto(((GameStateDto) object).playerId));
        }
    }
}
