package com.onlineinteract.core.workbench;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.type.Compass;

public class Arrow extends WorkbenchItem {

	public static final int X_OFFSET = 20;
	public static final int Y_OFFSET = 300;
	public static final int ARROW_HEAD_HEIGHT = 20;
	public static final int ARROW_HEAD_WIDTH = 10;

	private ShapeRenderer lineShapeRenderer;
	private ShapeRenderer arrowHeadShapeRenderer;
	private float length = 50;
	private Compass rotatePosition = Compass.EAST;

	public Arrow() {
		this(X_OFFSET, Y_OFFSET);
	}

	public Arrow(float x, float y) {
		this.x = x;
		this.y = y;
		lineShapeRenderer = new ShapeRenderer();
		arrowHeadShapeRenderer = new ShapeRenderer();
		this.label = "Arrow";
	}

	public void instantiateRenderers() {
		lineShapeRenderer = new ShapeRenderer();
		arrowHeadShapeRenderer = new ShapeRenderer();
	}

	@Override
	public void draw() {
		lineShapeRenderer.begin(ShapeType.Line);
		lineShapeRenderer.setProjectionMatrix(Workspace.getInstance().getCamera().combined);
		lineShapeRenderer.identity();
		lineShapeRenderer.translate(x, y, 0);
		lineShapeRenderer.setColor(Color.ORANGE);
		lineShapeRenderer.line(0, 0, length, 0);
		lineShapeRenderer.end();

		arrowHeadShapeRenderer.begin(ShapeType.Line);
		arrowHeadShapeRenderer.setProjectionMatrix(Workspace.getInstance().getCamera().combined);
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

	@Override
	public void renderDialog() {
		// TODO require dialog TBD.
	}
}
