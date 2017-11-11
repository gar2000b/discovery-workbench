package com.onlineinteract.core.workbench;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.dialog.DeleteDialog;
import com.onlineinteract.core.type.Compass;

public class Arrow implements WorkbenchItem {

	public static final int X_OFFSET = 20;
	public static final int Y_OFFSET = 300;
	public static final int ARROW_HEAD_HEIGHT = 20;
	public static final int ARROW_HEAD_WIDTH = 10;

	private ShapeRenderer lineShapeRenderer;
	private ShapeRenderer arrowHeadShapeRenderer;
	private OrthographicCamera camera;
	private float x;
	private float y;
	private float length = 50;
	private float instanceOffsetX;
	private float instanceOffsetY;
	private Compass rotatePosition = Compass.EAST;
	private Workspace workspace;

	public Arrow(){}
	
	public Arrow(OrthographicCamera camera) {
		this(X_OFFSET, Y_OFFSET, camera);
	}

	public Arrow(float x, float y, OrthographicCamera camera) {
		this.camera = camera;
		this.x = x;
		this.y = y;
		lineShapeRenderer = new ShapeRenderer();
		arrowHeadShapeRenderer = new ShapeRenderer();
	}
	
	public void instantiateRenderersAndCamera(OrthographicCamera camera) {
		this.camera = camera;
		lineShapeRenderer = new ShapeRenderer();
		arrowHeadShapeRenderer = new ShapeRenderer();
	}

	@Override
	public void draw() {

		lineShapeRenderer.begin(ShapeType.Line);
		lineShapeRenderer.setProjectionMatrix(camera.combined);
		lineShapeRenderer.identity();
		lineShapeRenderer.translate(x, y, 0);
		lineShapeRenderer.setColor(Color.ORANGE);
		lineShapeRenderer.line(0, 0, length, 0);
		lineShapeRenderer.end();

		arrowHeadShapeRenderer.begin(ShapeType.Line);
		arrowHeadShapeRenderer.setProjectionMatrix(camera.combined);
		arrowHeadShapeRenderer.identity();
		arrowHeadShapeRenderer.translate(x + length, y - 10, 0);
		arrowHeadShapeRenderer.setColor(Color.ORANGE);
		// arrowHeadShapeRenderer.rotate(0.f, 0.f, 1.f, 90f);
		arrowHeadShapeRenderer.triangle(0, 0, 10, 10, 0, 20);
		arrowHeadShapeRenderer.end();
	}

	public boolean isClickWithinBoundary(float x, float y) {
		float clickX = x;
		float clickY = y;

		if (rotatePosition == Compass.EAST || rotatePosition == Compass.WEST) {
			if (clickX >= this.x && clickX <= (this.x + length + ARROW_HEAD_WIDTH)
					&& clickY >= this.y - (ARROW_HEAD_HEIGHT / 2) && clickY <= (this.y + (ARROW_HEAD_HEIGHT / 2))) {
				instanceOffsetX = clickX - this.x;
				instanceOffsetY = clickY - this.y;
				return true;
			}
		}

		return false;
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
        DeleteDialog deleteServiceDialog = new DeleteDialog("Really Delete Arrow?", workspace.getSkin(), workspace, this);
        workspace.getStage().act();
        deleteServiceDialog.show(workspace.getStage());
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
	public void startStopService(float x, float y) {
		// TODO Auto-generated method stub
	}

	@Override
	public long getPreviousTimeMillis() {
		// TODO Auto-generated method stub
		return 0;
	}
}
