package com.mygdx.game.server;

import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.GameStateDto;
import com.mygdx.game.KryoSetup;
import com.mygdx.game.PlayerUpdateDto;
import com.mygdx.game.ServerPlayerDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameServer {
    private Server server;
    private Map<UUID, ServerPlayer> serverPlayers;
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
        try {
            ServerAsssetSupplier.loadAssets();
        } catch (Exception e) {
            e.printStackTrace();
        }
        serverPlayers = new HashMap<>();
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
        return serverPlayers.get(uuid);
    }

    public static void main(String[] args) {
        new GameServer();
    }

    public void updatePlayer(PlayerUpdateDto playerUpdateDto) {
        getServerPlayerFromId(playerUpdateDto.uuid).update(playerUpdateDto);
    }

    public GameStateDto getGameStateDto(UUID playerId) {
        return new GameStateDto(getServerPlayerFromId(playerId).getDto(), getOtherPlayerDtos(playerId));
    }

    public ServerPlayer addServerPlayer(UUID uuid) {
        ServerPlayer serverPlayer = new ServerPlayer(uuid);
        serverPlayers.put(uuid, serverPlayer);
        return serverPlayer;
    }

    public List<ServerPlayerDto> getOtherPlayerDtos(UUID uuid) {
        ServerPlayer serverPlayer = serverPlayers.remove(uuid);
        List<ServerPlayerDto> dtos = serverPlayers.values()
                .stream()
                .map(ServerPlayer::getDto)
                .collect(Collectors.toList());
        serverPlayers.put(uuid, serverPlayer);
        return dtos;
    }
}
