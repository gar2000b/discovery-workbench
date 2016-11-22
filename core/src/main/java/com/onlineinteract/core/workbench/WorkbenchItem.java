package com.onlineinteract.core.workbench;

import com.badlogic.gdx.Gdx;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.dialog.DeleteDialog;

public abstract class WorkbenchItem implements WorkbenchRenderer {

	String label;
	float instanceOffsetX;
	float instanceOffsetY;

	public abstract boolean isClickWithinBoundary(float x, float y);

	public abstract void setX(float x);

	public abstract void setY(float y);

	public abstract void setWorkspace(Workspace workspace);

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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
