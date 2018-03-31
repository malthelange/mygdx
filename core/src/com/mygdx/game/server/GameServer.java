package com.mygdx.game.server;

import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.KryoSetup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameServer {
    private Server server;
    private List<ServerPlayer> serverPlayers;

    private GameServer() {
        serverPlayers = new ArrayList<>();
        serverPlayers.add(new ServerPlayer(UUID.fromString("1112a53a-8d88-4e04-b580-e54bce7f6a17")));
        server = new Server();
        KryoSetup.setUpKryo(server.getKryo());
        server.start();
        try {
            server.bind(8000);
            server.addListener(new ServerListener(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ServerPlayer getServerPlayerFromId(UUID uuid) {
        for (ServerPlayer serverPlayer : serverPlayers) {
            if (serverPlayer.getId().equals(uuid)) {
                return serverPlayer;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        new GameServer();
    }
}
