package com.mygdx.game.server;

import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.GameStateDto;
import com.mygdx.game.KryoSetup;
import com.mygdx.game.PlayerUpdateDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameServer {
    private Server server;
    private List<ServerPlayer> serverPlayers;
    private static final float UPS = 1000 / 60;
    private float lastUpdate = 0;
    private float delta = 0;
    private boolean running = true;

    private GameServer() {
        setUp();
        loop();
    }

    private void loop() {
        lastUpdate = System.currentTimeMillis();
        while (running) {
            float currentTime = System.currentTimeMillis();
            delta += currentTime;
            lastUpdate = currentTime;
            while (delta >= GameServer.UPS) {
                tick();
                delta -= GameServer.UPS;
            }
        }
    }

    private void tick() {
    }

    private void setUp() {
        serverPlayers = new ArrayList<>();
        server = new Server();
        KryoSetup.setUpKryo(server.getKryo());
        server.start();
        try {
            server.bind(8000);
            server.addListener(new ServerListener(this));
        } catch (IOException e) {
            return;
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

    public void updatePlayer(PlayerUpdateDto playerUpdateDto) {
        getServerPlayerFromId(playerUpdateDto.uuid).move(playerUpdateDto.direction);
    }

    public GameStateDto getGameStateDto(UUID playerId) {
        return new GameStateDto(getServerPlayerFromId(playerId).getDto());
    }

    public ServerPlayer addServerPlayer(UUID uuid) {
        ServerPlayer serverPlayer = new ServerPlayer(uuid);
        serverPlayers.add(serverPlayer);
        return serverPlayer;
    }
}
