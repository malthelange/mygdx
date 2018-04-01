package com.mygdx.game.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.GameDataDto;
import com.mygdx.game.GameStateDto;
import com.mygdx.game.GetGameDataDto;
import com.mygdx.game.PlayerUpdateDto;

public class ServerListener extends Listener {
    GameServer gameServer;

    public ServerListener(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    public void received (Connection connection, Object object) {
        if (object instanceof GetGameDataDto) {
            ServerPlayer serverPlayer = gameServer.getServerPlayerFromId(((GetGameDataDto) object).id);
            connection.sendTCP(new GameDataDto(serverPlayer.getDto()));
        }
        else if (object instanceof PlayerUpdateDto) {
            gameServer.updatePlayer((PlayerUpdateDto) object);
        }
        else if (object instanceof GameStateDto) {
            connection.sendTCP(gameServer.getGameStateDto(((GameStateDto) object).playerId));
        }
    }
}
