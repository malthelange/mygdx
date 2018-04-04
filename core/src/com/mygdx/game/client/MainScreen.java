package com.mygdx.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameStateDto;
import com.mygdx.game.ServerPlayerDto;

import java.util.*;

public class MainScreen extends ScreenAdapter {
    private SpriteBatch spriteBatch;
    private Player myPlayer;
    private GameController gameController;
    private List<Object> dtoToSend;
    private Map<UUID, Player> otherPlayers;
    private OrthographicCamera camera;
    private TiledMap currentMap;
    private MapRenderer currentMapRenderer;
    private FPSLogger fpsLogger;

    public MainScreen(GameController gameController) {
        fpsLogger = new FPSLogger();
        this.gameController = gameController;
        spriteBatch = new SpriteBatch();
        dtoToSend = new ArrayList<>();
        otherPlayers = new HashMap<>();
        camera = new OrthographicCamera(30, 30f * (4 / 3));
        currentMap = AssetSupplier.assetManager.get("maps/map1.tmx");
        currentMapRenderer = new OrthogonalTiledMapRenderer(currentMap, 1 / 16f);
    }

    public void render(float delta) {
        // TODO should probably split ticks and server communication
        fpsLogger.log();
        getFromServer();
        doUpdate(delta);
        doRender(delta);
        sendDtos();
    }

    private void getFromServer() {
        gameController.getClient().sendTCP(new GameStateDto(gameController.getUuid()));
    }

    private void doUpdate(float delta) {
        Vector2 direction = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y -= 1;
        }
        myPlayer.move(direction, delta);
    }

    private void doRender(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(myPlayer.getPosition(), 0);
        camera.update();
        currentMapRenderer.setView(camera);
        currentMapRenderer.render();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        myPlayer.render(delta, spriteBatch);
        for (Player otherPlayer : otherPlayers.values()) {
            otherPlayer.render(delta, spriteBatch);
        }
        spriteBatch.end();
    }

    public void dispose() {
        spriteBatch.dispose();
        myPlayer.dispose();
    }

    public void setMyPlayer(ServerPlayerDto serverPlayerDto) {
        this.myPlayer = new Player(serverPlayerDto, this);
    }

    public void updateGameState(GameStateDto gameStateDto) {
        myPlayer.updateMainPlayer(gameStateDto.serverPlayer);
        updateOtherPlayers(gameStateDto.otherPlayerDtos);
    }

    private void updateOtherPlayers(List<ServerPlayerDto> otherPlayerDtos) {
        for (ServerPlayerDto serverPlayerDto : otherPlayerDtos) {
            if (otherPlayers.get(serverPlayerDto.id) == null) {
                Player player = new Player(serverPlayerDto, this);
                otherPlayers.put(serverPlayerDto.id, player);
            } else {
                otherPlayers.get(serverPlayerDto.id).update(serverPlayerDto);
            }
        }
    }

    public void addDtoToSend(Object object) {
        dtoToSend.add(object);
    }

    public void sendDtos() {
        for (Object dto : dtoToSend) {
            gameController.getClient().sendTCP(dto);
        }
        dtoToSend.clear();
    }

    public void setOtherPlayers(List<ServerPlayerDto> otherPlayerDtos) {
        for (ServerPlayerDto serverPlayerDto : otherPlayerDtos) {
            Player player = new Player(serverPlayerDto, this);
            if (otherPlayers.get(serverPlayerDto.id) == null) {
                otherPlayers.put(serverPlayerDto.id, player);
            } else {
                otherPlayers.replace(serverPlayerDto.id, player);
            }
        }
    }
}
