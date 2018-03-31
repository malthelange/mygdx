package com.mygdx.game.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.ServerPlayerDto;

public class MainScreen extends ScreenAdapter {
    private SpriteBatch spriteBatch;
    private Player player;
    GameController gameController;

    public MainScreen(GameController gameController) {
        this.gameController = gameController;
        spriteBatch = new SpriteBatch();
    }

    public void render(float delta) {
        doUpdate();
        doRender(delta);
    }

    private void doUpdate() {
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
        player.move(direction, gameController.getClient());
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
        this.player = new Player(serverPlayerDto);
    }
}
