package com.onlineinteract.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class DiscoveryWorkbench extends Game {

	private final AssetManager assetManager = new AssetManager();
	private int worldWidth;
	private int worldHeight;

	public DiscoveryWorkbench(int worldWidth, int worldHeight) {
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
	}

	@Override
	public void create() {
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		setScreen(new LoadingScreen(this, worldWidth, worldHeight));
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

}
