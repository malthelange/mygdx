package com.mygdx.game.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.GameDataDto;

public class ClientListener extends Listener {
    GameController gameController;

    public ClientListener(GameController gameController) {
        this.gameController = gameController;
    }

    public void received (Connection connection, Object object) {
        if (object instanceof GameDataDto) {
           GameDataDto gameDataDto = (GameDataDto) object;
           gameController.startGame(new Player(gameDataDto.player));
        }
    }
}
