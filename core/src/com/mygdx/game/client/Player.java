package com.mygdx.game.client;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.PlayerUpdateDto;
import com.mygdx.game.ServerPlayerDto;

import java.util.UUID;

public class Player {
    private Texture texture;
    private Vector2 position;
    private UUID uuid;
    private MainScreen mainScreen;

    public Player(ServerPlayerDto serverPlayerDto, MainScreen mainScreen) {
        this.position = serverPlayerDto.position;
        this.uuid = serverPlayerDto.id;
        texture = GameController.assetManager.get("badlogic.jpg");
        this.mainScreen = mainScreen;
    }

    public void move(Vector2 direction, float delta) {
        direction.scl(delta * 100);
        position.add(direction);
        mainScreen.addDtoToSend(new PlayerUpdateDto(uuid, direction));
    }

    public void render(float delta, SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, position.x, position.y);
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void dispose() {
        texture.dispose();
    }

    public void update(ServerPlayerDto serverPlayerDto) {
        setPosition(serverPlayerDto.position);
    }
}
