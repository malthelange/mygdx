package com.mygdx.game.client;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.PlayerMovementState;
import com.mygdx.game.PlayerUpdateDto;
import com.mygdx.game.ServerPlayerDto;

import java.util.UUID;

public class Player {
    private static final float SPEED = 10f;
    // TODO Would probably be better static
    private static final float WIDTH = 1f;
    private static final float HEIGHT = 1f;
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
        moveForwardAnimation = AssetSupplier.PLAYER_MOVES_FORWARD_ANIMATION;
        moveBackWardAnimation = AssetSupplier.PLAYER_MOVES_BACKWARD_ANIMATION;
        moveSideAnimation = AssetSupplier.PLAYER_MOVES_SIDEWAYS_ANIMATION;
        movementState = PlayerMovementState.IDLE;
        this.mainScreen = mainScreen;
    }

    void move(Vector2 direction, float delta) {
        direction.scl(delta * Player.SPEED);
        if (direction.y > 0) {
            setMovementState(PlayerMovementState.UP);
        } else if (direction.y < 0) {
            setMovementState(PlayerMovementState.DOWN);
        } else {
            if (direction.x > 0) {
                setMovementState(PlayerMovementState.RIGHT);
            } else if (direction.x < 0) {
                setMovementState(PlayerMovementState.LEFT);
            } else if (direction.x == 0) {
                setMovementState(PlayerMovementState.IDLE);
            }
        }
        position.add(direction);
        mainScreen.addDtoToSend(new PlayerUpdateDto(uuid, direction, movementState));
    }

    public void setMovementState(PlayerMovementState movementState) {
        if (this.movementState == movementState) {
            return;
        }
        this.movementState = movementState;
        movementStateTime = 0;
    }

    void render(float delta, SpriteBatch spriteBatch) {
        movementStateTime += delta;
        switch (movementState) {
            case UP:
                spriteBatch.draw(
                        moveBackWardAnimation.getKeyFrame(movementStateTime, true),
                        position.x, position.y,
                        Player.WIDTH, Player.HEIGHT);
                break;
            case DOWN:
                spriteBatch.draw(
                        moveForwardAnimation.getKeyFrame(movementStateTime, true),
                        position.x, position.y,
                        Player.WIDTH, Player.HEIGHT);
                break;
            case LEFT:
                TextureRegion transformedFrame = new TextureRegion(moveSideAnimation.getKeyFrame(movementStateTime, true));
                transformedFrame.flip(true, false);
                spriteBatch.draw(
                        transformedFrame,
                        position.x, position.y,
                        Player.WIDTH, Player.HEIGHT);
                break;
            case RIGHT:
                spriteBatch.draw(
                        moveSideAnimation.getKeyFrame(movementStateTime, true),
                        position.x, position.y,
                        Player.WIDTH, Player.HEIGHT);
                break;
            case IDLE:
                Sprite sprite = new Sprite(moveForwardAnimation.getKeyFrame(0, true));
                sprite.setSize(40, 40);
                spriteBatch.draw(
                        sprite,
                        position.x, position.y,
                        Player.WIDTH, Player.HEIGHT);
                break;
        }
    }

    void setPosition(Vector2 position) {
        this.position = position;
    }

    void dispose() {
    }

    void update(ServerPlayerDto serverPlayerDto) {
        if (position.dst(serverPlayerDto.position) > 20f) {
            setPosition(serverPlayerDto.position);
        }
        setMovementState(serverPlayerDto.movementState);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void updateMainPlayer(ServerPlayerDto playerDto) {
        if (position.dst(playerDto.position) > 2) {
            position.set(playerDto.position);
        }
    }
}
