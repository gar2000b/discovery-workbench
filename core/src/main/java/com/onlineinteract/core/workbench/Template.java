package com.onlineinteract.core.workbench;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.dialog.ServiceDialog;

public class Template implements WorkbenchItem {

	public static final int DOUBLE_CLICK_RANGE = 400;
	private static float BOX_OFFEST_X = 20;
	private static float LABEL_OFFSET_X = 10;
	private static float LABEL_OFFSET_Y = 90;
	private static int BOX_WIDTH = 180;
	public static int BOX_HEIGHT = 100;

	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;
	private BitmapFont font;
	private OrthographicCamera camera;
	private float x;
	private float y;
	private Workspace workspace;
	private Color color1;
	private Color color2;
	private String label;
	private String startupCommand;
	private ServiceDialog serviceDialog;
	private Skin skin;
	private Stage stage;

	private float instanceOffsetX;
	private float instanceOffsetY;
	
	private long previousTimeMillis = -DOUBLE_CLICK_RANGE - 1;

	public Template() {
	}

	public Template(Workspace workspace, float y, Color color1, Color color2, String label) {
		this(workspace, BOX_OFFEST_X, y, color1, color2, label);
	}

	public Template(Workspace workspace, float x, float y, Color color1, Color color2, String label) {
		this.workspace = workspace;
		this.shapeRenderer = workspace.getShapeRenderer();
		this.batch = workspace.getBatch();
		this.font = workspace.getFont();
		this.camera = workspace.getCamera();
		this.skin = workspace.getSkin();
		this.stage = workspace.getStage();
		this.x = x;
		this.y = y;
		this.color1 = color1;
		this.color2 = color2;
		this.label = label;
	}

	public void draw() {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(color1);
		shapeRenderer.rect(x, y, BOX_WIDTH, BOX_HEIGHT);
		shapeRenderer.end();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.setColor(color2);
		font.draw(batch, label, x + LABEL_OFFSET_X, y + LABEL_OFFSET_Y);
		batch.end();
	}

	public boolean isClickWithinBoundary(Vector3 coordinates) {

		float clickX = coordinates.x;
		float clickY = coordinates.y;

		if (clickX >= x && clickX <= (x + BOX_WIDTH) && clickY >= y && clickY <= (y + BOX_HEIGHT)) {
			instanceOffsetX = clickX - x;
			instanceOffsetY = clickY - y;
			return true;
		}

		return false;
	}

	public void renderServiceDialog() {
		Gdx.input.setInputProcessor(stage);
		serviceDialog = new ServiceDialog("Service Configuration", skin, workspace, this);
		if (label.equals("µicroservice"))
			serviceDialog.getLabelTextField().setText("New Service");
		else
			serviceDialog.getLabelTextField().setText(label);
		serviceDialog.getStartupCommandTextField().setText(startupCommand);
		stage.act();
		serviceDialog.show(stage);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getInstanceOffsetX() {
		return instanceOffsetX;
	}

	public float getInstanceOffsetY() {
		return instanceOffsetY;
	}

	public long getPreviousTimeMillis() {
		return previousTimeMillis;
	}

	public void setPreviousTimeMillis(long previousTimeMillis) {
		this.previousTimeMillis = previousTimeMillis;
	}

	public String getStartupCommand() {
		return startupCommand;
	}

	public void setStartupCommand(String startupCommand) {
		this.startupCommand = startupCommand;
	}
}
