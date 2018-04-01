package com.mygdx.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameStateDto;
import com.mygdx.game.ServerPlayerDto;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends ScreenAdapter {
    private SpriteBatch spriteBatch;
    private Player player;
    private GameController gameController;
    private List<Object> dtoToSend;

    public MainScreen(GameController gameController) {
        this.gameController = gameController;
        spriteBatch = new SpriteBatch();
        dtoToSend = new ArrayList<>();
    }

    public void render(float delta) {
        // TODO should probably split ticks and server communication
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
        player.move(direction, delta);
    }

    private void doRender(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        player.render(delta, spriteBatch);
        spriteBatch.end();
    }

    public void dispose() {
        spriteBatch.dispose();
        player.dispose();
    }

    public void setPlayer(ServerPlayerDto serverPlayerDto) {
        this.player = new Player(serverPlayerDto, this);
    }

    public void updateGameState(GameStateDto gameStateDto) {
        player.setPosition(gameStateDto.serverPlayer.position);
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
}
