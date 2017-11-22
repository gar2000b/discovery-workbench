package com.onlineinteract.core.workbench;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlineinteract.core.Workspace;

public class Topic extends WorkbenchItem {
	public static final int X_OFFSET = 20;
	public static final int Y_OFFSET = 280;

	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	public Topic() {
	}

	public Topic(OrthographicCamera camera) {
		this(X_OFFSET, Y_OFFSET, camera);
	}

	public Topic(float x, float y, OrthographicCamera camera) {
		this.camera = camera;
		this.x = x;
		this.y = y;
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		batch = new SpriteBatch();
		this.label = "Topic";
	}

	public void instantiateRenderersAndCamera(OrthographicCamera camera) {
		this.camera = camera;
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		batch = new SpriteBatch();
	}

	@Override
	public void draw() {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.identity();
		shapeRenderer.translate(x, y, 0);
		shapeRenderer.setColor(Color.ORANGE);
		shapeRenderer.line(0, 0, 0, -100);
		shapeRenderer.line(0, 0, 40, 0);
		shapeRenderer.line(40, 0, 40, -100);
		shapeRenderer.line(0, -100, 40, -100);
		shapeRenderer.curve(5, -10, 15, -20, 25, -20, 35, -10, 100);
		shapeRenderer.curve(5, -10, 15, 0, 25, 0, 35, -10, 100);
		shapeRenderer.curve(5, -90, 15, -100, 25, -100, 35, -90, 100);
		shapeRenderer.line(5, -10, 5, -90);
		shapeRenderer.line(35, -10, 35, -90);
		shapeRenderer.end();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.setColor(Color.ORANGE);
		font.getData().setScale(1);
		font.draw(batch, "T", x + 15, y - 22);
		font.draw(batch, "o", x + 15, y - 35);
		font.draw(batch, "p", x + 15, y - 50);
		font.draw(batch, "i", x + 16, y - 67);
		font.draw(batch, "c", x + 15, y - 80);
		batch.end();
		shapeRenderer.end();
	}

	public boolean isClickWithinBoundary(float x, float y) {
		float clickX = x;
		float clickY = y;

		if (clickX >= this.x && clickX <= (this.x + 40) && clickY <= this.y && clickY >= (this.y - 100)) {
			instanceOffsetX = clickX - this.x;
			instanceOffsetY = clickY - this.y;
			return true;
		}

		return false;
	}

	@JsonIgnore
	@Override
	public void setWorkspace(Workspace workspace) {
		// TODO: revisit
	}

	@Override
	public void renderDialog() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setPreviousTimeMillis(long previousTimeMillis) {
		// TODO Auto-generated method stub
	}

	@Override
	public long getPreviousTimeMillis() {
		// TODO Auto-generated method stub
		return 0;
	}
}
