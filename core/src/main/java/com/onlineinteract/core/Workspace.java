package com.onlineinteract.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.onlineinteract.core.component.ServiceList;
import com.onlineinteract.core.processor.DeviceInputProcessor;
import com.onlineinteract.core.render.WorkspaceRenderer;
import com.onlineinteract.core.type.TemplateType;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchItem;
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
    private WorkspaceRenderer workspaceRenderer;
    private List<WorkbenchItem> workbenchItems = new ArrayList<WorkbenchItem>();
    private DeviceInputProcessor deviceInputProcessor;
    ServiceList serviceListComponent;

    private boolean toggleFSFlag = false;
    private boolean dialogToggleFlag = false;
    private Skin skin;
    private Stage stage;

    public Workspace(MsOrchestrator msOrchestrator, int worldWidth, int worldHeight) {
        this.msOrchestrator = msOrchestrator;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        instantiateTemplates(worldWidth, worldHeight);
        workspaceRenderer = new WorkspaceRenderer(this);
        stage = new Stage();
        serviceListComponent = new ServiceList(this);
        setupInputProcessors();
    }

    private void setupInputProcessors() {
        deviceInputProcessor = new DeviceInputProcessor(this);
        Gdx.input.setInputProcessor(stage);
    }

    private void instantiateTemplates(int worldWidth, int worldHeight) {
        workbenchItems.add(workbenchOutline = new WorkbenchOutline(worldWidth, worldHeight, shapeRenderer, camera));
        workbenchItems.add(new Template(this, worldHeight - MICROSERVICE_TEMPLATE_HEIGHT_OFFSET, Color.FOREST, Color.FOREST, "µicroservice", TemplateType.MICROSERVICE, UUID.randomUUID()));
        workbenchItems.add(new Template(this, worldHeight - INFRASTRUCTURE_TEMPLATE_HEIGHT_OFFSET, Color.CORAL, Color.CORAL, "Infrastructure", TemplateType.INFRASTRUCTURE, UUID.randomUUID()));
        workbenchItems.add(new Template(this, worldHeight - SCRIPTS_TEMPLATE_HEIGHT_OFFSET, Color.BLUE, Color.GRAY, "Scripts", TemplateType.SCRIPT, UUID.randomUUID()));
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
        stage.act();
        stage.draw();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float delta) {
        Input input = Gdx.input;
        if (input.getInputProcessor().getClass().getSimpleName().equals("DeviceInputProcessor")) {
            // fullScreenToggle();
            cameraZoom();
            updateCameraPan();
        }
    }

    private void draw() {
        workspaceRenderer.draw();
    }

    @SuppressWarnings("unused")
    private void fullScreenToggle() {
        Input input = Gdx.input;
        if (input.isKeyPressed(Input.Keys.F)) {
            if (toggleFSFlag)
                Gdx.graphics.setWindowedMode(worldWidth, worldHeight);
            else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
            toggleFSFlag = !toggleFSFlag;
        }
    }

    private void cameraZoom() {
        Input input = Gdx.input;

        if (input.isKeyPressed(Input.Keys.X))
            camera.zoom += 0.02;

        if (input.isKeyPressed(Input.Keys.Z))
            camera.zoom -= 0.02;

        if (input.isKeyPressed(Input.Keys.R))
            camera.zoom = 1;

    }

    private void updateCameraPan() {
        Input input = Gdx.input;
        if (input.isKeyPressed(Input.Keys.LEFT))
            camera.position.x = camera.position.x - 10;
        if (input.isKeyPressed(Input.Keys.RIGHT))
            camera.position.x = camera.position.x + 10;
        if (input.isKeyPressed(Input.Keys.UP))
            camera.position.y = camera.position.y + 10;
        if (input.isKeyPressed(Input.Keys.DOWN))
            camera.position.y = camera.position.y - 10;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Template getMicroserviceTemplate() {
        return microserviceTemplate;
    }

    public Template getInfrastructureTemplate() {
        return infrastructureTemplate;
    }

    public Template getScriptTemplate() {
        return scriptTemplate;
    }

    public WorkbenchOutline getWorkbenchOutline() {
        return workbenchOutline;
    }

    public List<WorkbenchItem> getWorkbenchItems() {
        return workbenchItems;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Stage getStage() {
        return stage;
    }

    public DeviceInputProcessor getDeviceInputProcessor() {
        return deviceInputProcessor;
    }

    public Skin getSkin() {
        return skin;
    }

    public boolean isToggleFSFlag() {
        return toggleFSFlag;
    }

    public ServiceList getServiceListComponent() {
        return serviceListComponent;
    }

    public boolean isDialogToggleFlag() {
        return dialogToggleFlag;
    }

    public void setDialogToggleFlag(boolean dialogToggleFlag) {
        this.dialogToggleFlag = dialogToggleFlag;
    }
}
