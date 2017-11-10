package com.onlineinteract.core.workbench;

public interface WorkbenchItem {
    public void draw();
    public boolean isClickWithinBoundary(float x, float y);
    public void renderDeleteDialog();
    public float getInstanceOffsetX();
    public float getInstanceOffsetY();
    public void setX(float x);
    public void setY(float y);
    // public UUID getUuid();
}
