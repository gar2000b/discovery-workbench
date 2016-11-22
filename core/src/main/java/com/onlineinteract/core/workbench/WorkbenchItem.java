package com.onlineinteract.core.workbench;

import com.badlogic.gdx.Gdx;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.dialog.DeleteDialog;

public abstract class WorkbenchItem implements WorkbenchRenderer {

	String label;
	float instanceOffsetX;
	float instanceOffsetY;
	float x;
	float y;

	public abstract boolean isClickWithinBoundary(float x, float y);

	public abstract void renderDialog();

	public abstract void setPreviousTimeMillis(long previousTimeMillis);

	public abstract long getPreviousTimeMillis();

	public void renderDeleteDialog() {
		Gdx.input.setInputProcessor(Workspace.getInstance().getStage());
		Workspace.getInstance().getStage().act();
		new DeleteDialog("Really Delete Instance: " + label + "?", Workspace.getInstance().getSkin(), this)
				.show(Workspace.getInstance().getStage());
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

	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
