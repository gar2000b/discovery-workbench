package com.onlineinteract.core.workbench;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class DataStore implements WorkbenchItem {
    public static final int X_OFFSET = 70;
    public static final int Y_OFFSET = 280;

    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private float x;
    private float y;
    private float instanceOffsetX;
    private float instanceOffsetY;

    public DataStore() {}

    public DataStore(OrthographicCamera camera) {
        this(X_OFFSET, Y_OFFSET, camera);
    }

    public DataStore(float x, float y, OrthographicCamera camera) {
        this.camera = camera;
        this.x = x;
        this.y = y;
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        batch = new SpriteBatch();
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

        if (clickX >= this.x && clickX <= (this.x + 40) && clickY <= this.y && clickY >= (this.y - 100)) {
            instanceOffsetX = clickX - this.x;
            instanceOffsetY = clickY - this.y;
            return true;
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
}
