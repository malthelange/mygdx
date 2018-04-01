package com.mygdx.game.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.game.GameStateDto;
import com.mygdx.game.GetGameDataDto;
import com.mygdx.game.KryoSetup;
import com.mygdx.game.ServerPlayerDto;

import java.io.IOException;
import java.util.UUID;

public class GameController extends Game {
	private MainScreen mainScreen;
	private UUID uuid = UUID.fromString("1112a53a-8d88-4e04-b580-e54bce7f6a17");
	private Client client;
	// TODO should probably change to concurrency logs
	private boolean initFromServer = false;
	public static AssetManager assetManager;

	@Override
	public void create () {
		loadAssets();
		mainScreen = new MainScreen(this);
		if (setUpConnection()) return;
		while(!initFromServer) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean setUpConnection() {
		client = new Client();
		KryoSetup.setUpKryo(client.getKryo());
		client.addListener(new ClientListener(this));
		client.start();
		try {
			client.connect(5000, "localhost", 8000);
			client.sendTCP(new GetGameDataDto(uuid));
		} catch (IOException ignored) {
			System.out.println("Couldn't connect to server");
			return true;
		}
		return false;
	}

	public void startGame(ServerPlayerDto serverPlayerDto) {
		mainScreen.setPlayer(serverPlayerDto);
		setScreen(mainScreen);
		initFromServer = true;
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		mainScreen.dispose();
		disposeAssets();
	}

	private static void disposeAssets() {
		assetManager.dispose();
	}

	public MainScreen getMainScreen() {
		return mainScreen;
	}

	public Client getClient() {
		return  client;
	}

	public static void loadAssets() {
		// TODO should be run in sepperate thread;
		assetManager = new AssetManager();
		assetManager.load("badlogic.jpg", Texture.class);
		assetManager.finishLoading();
	}

	public UUID getUuid() {
		return uuid;
	}

	public void updateGameState(GameStateDto gameStateDto) {
		mainScreen.updateGameState(gameStateDto);
	}
}
