package com.onlineinteract.core.workbench;

import com.onlineinteract.core.Workspace;

public interface WorkbenchItem extends WorkbenchRenderer {
	public boolean isClickWithinBoundary(float x, float y);

	public void renderDeleteDialog();

	public float getInstanceOffsetX();

	public float getInstanceOffsetY();

	public void setX(float x);

	public void setY(float y);

	public void setWorkspace(Workspace workspace);

	public void renderDialog();

	public void setPreviousTimeMillis(long previousTimeMillis);

	public long getPreviousTimeMillis();
}
