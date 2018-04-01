package com.mygdx.game.client;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.PlayerUpdateDto;
import com.mygdx.game.ServerPlayerDto;

import java.util.UUID;

public class Player {
    private final Animation<TextureRegion> moveForwardAnimation;
    private final Animation<TextureRegion> moveBackWardAnimation;
    private final Animation<TextureRegion> moveSideAnimation;
    private Vector2 position;
    private UUID uuid;
    private MainScreen mainScreen;
    private float movementStateTime = 0;
    private PlayerMovementState movementState;

    public Player(ServerPlayerDto serverPlayerDto, MainScreen mainScreen) {
        this.position = serverPlayerDto.position;
        this.uuid = serverPlayerDto.id;
        moveForwardAnimation =
                new Animation<>(
                        0.25f,
                        AssetUtil.getAnimationArray("packed/pack-file.atlas", "player/Player", 16),
                        Animation.PlayMode.LOOP);
        moveBackWardAnimation =
                new Animation<>(
                        0.25f,
                        AssetUtil.getAnimationArray("packed/pack-file.atlas", "player/Player_back", 16),
                        Animation.PlayMode.LOOP);
        moveSideAnimation =
                new Animation<>(
                        0.25f,
                        AssetUtil.getAnimationArray("packed/pack-file.atlas", "player/Player_sideways", 16),
                        Animation.PlayMode.LOOP);
        movementState = PlayerMovementState.IDLE;
        this.mainScreen = mainScreen;
    }

    void move(Vector2 direction, float delta) {
        direction.scl(delta * 100);
        if (direction.x > 0 && movementState != PlayerMovementState.RIGHT) {
            setMovementState(PlayerMovementState.RIGHT);
        } else if (direction.x < 0 && movementState != PlayerMovementState.LEFT) {
            setMovementState(PlayerMovementState.LEFT);
        } else if (direction.y > 0 && movementState != PlayerMovementState.UP) {
            setMovementState(PlayerMovementState.UP);
        } else if (direction.y < 0 && movementState != PlayerMovementState.DOWN) {
            setMovementState(PlayerMovementState.DOWN);
        } else if (direction.x == 0 && direction.y == 0 && movementState != PlayerMovementState.IDLE) {
            setMovementState(PlayerMovementState.IDLE);
        }
        position.add(direction);
        mainScreen.addDtoToSend(new PlayerUpdateDto(uuid, direction));
    }

    public void setMovementState(PlayerMovementState movementState) {
        this.movementState = movementState;
        movementStateTime = 0;
    }

    void render(float delta, SpriteBatch spriteBatch) {
        movementStateTime += delta;
        switch (movementState) {
            case UP:
                spriteBatch.draw(
                        moveBackWardAnimation.getKeyFrame(movementStateTime, true),
                        position.x, position.y);
                break;
            case DOWN:
                spriteBatch.draw(
                        moveForwardAnimation.getKeyFrame(movementStateTime, true),
                        position.x, position.y);
                break;
            case LEFT:
                TextureRegion transformedFrame = new TextureRegion(moveSideAnimation.getKeyFrame(movementStateTime, true));
                transformedFrame.flip(true, false);
                spriteBatch.draw(
                        transformedFrame,
                        position.x, position.y);
                break;
            case RIGHT:
                spriteBatch.draw(
                        moveSideAnimation.getKeyFrame(movementStateTime, true),
                        position.x, position.y);
                break;
            case IDLE:
                Sprite sprite = new Sprite(moveForwardAnimation.getKeyFrame(0, true));
                sprite.setSize(40, 40);
                spriteBatch.draw(
                        sprite,
                        position.x, position.y);
                break;
        }
    }

    void setPosition(Vector2 position) {
        this.position = position;
    }

    void dispose() {
    }

    void update(ServerPlayerDto serverPlayerDto) {
        setPosition(serverPlayerDto.position);
    }

    public static enum PlayerMovementState {
        IDLE, UP, DOWN, LEFT, RIGHT
    }
}
