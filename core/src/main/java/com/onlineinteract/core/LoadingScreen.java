package com.onlineinteract.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by Digilogue on 19/11/2016.
 */
public class LoadingScreen extends ScreenAdapter {

	private static final float PROGRESS_BAR_WIDTH = 100;
	private static final float PROGRESS_BAR_HEIGHT = 25;

	private int worldWidth;
	private int worldHeight;

	private Viewport viewport;
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private Workspace workspace;

	private float progress = 0;
	private final DiscoveryWorkbench discoveryWorkbench;

	public LoadingScreen(DiscoveryWorkbench discoveryWorkbench, int worldWidth, int worldHeight) {
		this.discoveryWorkbench = discoveryWorkbench;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	@Override
	public void show() {
		super.show();
		camera = new OrthographicCamera();
		camera.position.set(worldWidth / 2, worldHeight / 2, 0);
		camera.update();
		viewport = new FitViewport(worldWidth, worldHeight, camera);
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		update();
		clearScreen();
		draw();
	}

	@Override
	public void dispose() {
		super.dispose();
		shapeRenderer.dispose();
	}

	private void update() {
		if (discoveryWorkbench.getAssetManager().update()) {
			workspace = new Workspace(discoveryWorkbench, worldWidth, worldHeight);
			discoveryWorkbench.setScreen(workspace);
		} else {
			progress = discoveryWorkbench.getAssetManager().getProgress();
		}
	}

	private void clearScreen() {
		Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private void draw() {
		shapeRenderer.setProjectionMatrix(camera.projection);
		shapeRenderer.setTransformMatrix(camera.view);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		shapeRenderer.rect((worldWidth - PROGRESS_BAR_WIDTH) / 2, worldHeight / 2 - PROGRESS_BAR_HEIGHT / 2,
				progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);
		shapeRenderer.end();
	}
}
