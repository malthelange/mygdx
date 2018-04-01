package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.util.ArrayList;
import java.util.UUID;

public class KryoSetup {
    public static void setUpKryo(Kryo kryo) {
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        kryo.register(UUID.class);
        kryo.register(GameDataDto.class);
        kryo.register(GetGameDataDto.class);
        kryo.register(ServerPlayerDto.class);
        kryo.register(Vector2.class);
        kryo.register(GameStateDto.class);
        kryo.register(PlayerUpdateDto.class);
        kryo.register(ArrayList.class);
        kryo.register(PlayerMovementState.class);
    }
}
