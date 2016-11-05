package com.onlineinteract.core.workbench;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class Template implements WorkbenchItem {

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
    private Color color1;
    private Color color2;
    private String label;

    private float instanceOffsetX;
    private float instanceOffsetY;

    public Template(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font, OrthographicCamera camera, float y, Color color1, Color color2, String label) {
        this.shapeRenderer = shapeRenderer;
        this.batch = batch;
        this.font = font;
        this.camera = camera;
        this.x = BOX_OFFEST_X;
        this.y = y;
        this.color1 = color1;
        this.color2 = color2;
        this.label = label;
    }

    public Template(ShapeRenderer shapeRenderer, SpriteBatch batch, BitmapFont font, OrthographicCamera camera, float x, float y, Color color1, Color color2, String label) {
        this.shapeRenderer = shapeRenderer;
        this.batch = batch;
        this.font = font;
        this.camera = camera;
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

    public String getLabel() {
        return label;
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
}
