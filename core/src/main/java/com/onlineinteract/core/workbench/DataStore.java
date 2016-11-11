package com.onlineinteract.core.workbench;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.dialog.DataStoreDialog;
import com.onlineinteract.core.dialog.DeleteDialog;

public class DataStore implements WorkbenchItem {
	public static final int X_OFFSET = 80;
	public static final int Y_OFFSET = 280;
	public static final int DOUBLE_CLICK_RANGE = 400;

	private ShapeRenderer shapeRenderer;
	private BitmapFont font;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private float x;
	private float y;
	private float instanceOffsetX;
	private float instanceOffsetY;
	private Workspace workspace;
	private String label;
	private long previousTimeMillis = -DOUBLE_CLICK_RANGE - 1;

	public DataStore() {
	}

	public DataStore(Workspace workspace, OrthographicCamera camera) {
		this(workspace, X_OFFSET, Y_OFFSET, camera);
	}

	public DataStore(Workspace workspace, float x, float y, OrthographicCamera camera) {
		this.camera = camera;
		this.x = x;
		this.y = y;
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		batch = new SpriteBatch();
		this.workspace = workspace;
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
		shapeRenderer.line(120, -100, 120, 0);
		shapeRenderer.curve(0, 0, 10, -20, 110, -20, 120, 0, 100);
		shapeRenderer.curve(0, 0, 10, 20, 110, 20, 120, 0, 100);
		shapeRenderer.curve(0, -100, 10, -120, 110, -120, 120, -100, 100);
		shapeRenderer.end();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.setColor(Color.ORANGE);
		font.getData().setScale(1);
		font.draw(batch, "Data Store", x + 25, y - 40);
		batch.end();
	}

	public boolean isClickWithinBoundary(float x, float y) {
		float clickX = x;
		float clickY = y;

		if (clickX >= this.x && clickX <= (this.x + 120) && clickY <= this.y && clickY >= (this.y - 100)) {
			instanceOffsetX = clickX - this.x;
			instanceOffsetY = clickY - this.y;
			return true;
		}

		return false;
	}

	public void renderDialog() {
		Gdx.input.setInputProcessor(workspace.getStage());
		DataStoreDialog dataStoreDialog = new DataStoreDialog("Data Store", workspace.getSkin(), workspace, this);
		dataStoreDialog.getNameTextField().setText(label);
        workspace.getStage().act();
        dataStoreDialog.show(workspace.getStage());
        workspace.setDialogToggleFlag(true);
	}

	public float getInstanceOffsetX() {
		return instanceOffsetX;
	}

	public float getInstanceOffsetY() {
		return instanceOffsetY;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	@JsonIgnore
	@Override
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	@Override
	public void renderDeleteDialog() {
		Gdx.input.setInputProcessor(workspace.getStage());
		DeleteDialog deleteServiceDialog = new DeleteDialog("Really Delete Data Store?", workspace.getSkin(), workspace,
				this);
		workspace.getStage().act();
		deleteServiceDialog.show(workspace.getStage());
	}

	@Override
	public void startStopService(float x, float y) {
		// TODO Auto-generated method stub
	}
	
	@Override
    public long getPreviousTimeMillis() {
        return previousTimeMillis;
    }

	@Override
    public void setPreviousTimeMillis(long previousTimeMillis) {
        this.previousTimeMillis = previousTimeMillis;
    }
}
