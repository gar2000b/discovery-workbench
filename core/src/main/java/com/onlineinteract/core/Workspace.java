package com.onlineinteract.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchOutline;

public class Workspace extends ScreenAdapter {

    private static final int MICROSERVICE_TEMPLATE_HEIGHT_OFFSET = 120;
    private static final int INFRASTRUCTURE_TEMPLATE_HEIGHT_OFFSET = 240;
    private static final int SCRIPTS_TEMPLATE_HEIGHT_OFFSET = 360;

    private int worldWidth;
    private int worldHeight;

    @SuppressWarnings("unused")
    private final MsOrchestrator msOrchestrator;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font = new BitmapFont();
    private WorkbenchOutline workbenchOutline;
    private Template microserviceTemplate;
    private Template infrastructureTemplate;
    private Template scriptTemplate;


    private boolean toggleFSFlag = false;

    public Workspace(MsOrchestrator msOrchestrator, int worldWidth, int worldHeight) {
        this.msOrchestrator = msOrchestrator;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        this.workbenchOutline = new WorkbenchOutline(worldWidth, worldHeight, shapeRenderer, camera);
        this.microserviceTemplate = new Template(shapeRenderer, batch, font, camera, worldHeight - MICROSERVICE_TEMPLATE_HEIGHT_OFFSET, Color.FOREST, Color.FOREST, "µicroservice");
        this.infrastructureTemplate = new Template(shapeRenderer, batch, font, camera, worldHeight - INFRASTRUCTURE_TEMPLATE_HEIGHT_OFFSET, Color.CORAL, Color.CORAL, "Infrastructure");
        this.scriptTemplate = new Template(shapeRenderer, batch, font, camera, worldHeight - SCRIPTS_TEMPLATE_HEIGHT_OFFSET, Color.BLUE, Color.GRAY, "Scripts");
    }

    @Override
    public void show() {
        super.show();

        camera.position.set(worldWidth / 2, worldHeight / 2, camera.position.z);
        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        clearScreen();
        draw();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float delta) {
        fullScreenToggle();
    }

    private void draw() {
        camera.update();
        workbenchOutline.draw();
        microserviceTemplate.draw();
        infrastructureTemplate.draw();
        scriptTemplate.draw();
    }

    private void fullScreenToggle() {
        Input input = Gdx.input;
        if (input.isKeyPressed(Input.Keys.F)) {
            if (toggleFSFlag)
                Gdx.graphics.setWindowedMode(1920, 1080);
            else
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            toggleFSFlag = !toggleFSFlag;
        }
    }

}
