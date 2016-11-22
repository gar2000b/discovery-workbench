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
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.onlineinteract.core.component.ServiceList;
import com.onlineinteract.core.dialog.InstructionsDialog;
import com.onlineinteract.core.dialog.OpenDialog;
import com.onlineinteract.core.dialog.SaveDialog;
import com.onlineinteract.core.processor.DeviceInputProcessor;
import com.onlineinteract.core.render.WorkspaceRenderer;
import com.onlineinteract.core.type.TemplateType;
import com.onlineinteract.core.workbench.Arrow;
import com.onlineinteract.core.workbench.DataStore;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.Topic;
import com.onlineinteract.core.workbench.WorkbenchItem;
import com.onlineinteract.core.workbench.WorkbenchOutline;
import com.onlineinteract.core.workbench.WorkbenchRenderer;

/**
 * 
 * Workspace is the main engine that drives Discovery Workbench. It
 * implements a singleton pattern as it is only intended to be
 * instantiated once and provides a nice convenient getInstance()
 * method for all dependencies that require a reference to it.
 * 
 * @author Digilogue
 *
 */
public class Workspace extends ScreenAdapter {

	private static final int MICROSERVICE_TEMPLATE_HEIGHT_OFFSET = 120;
	private static final int INFRASTRUCTURE_TEMPLATE_HEIGHT_OFFSET = 240;
	private static final int SCRIPTS_TEMPLATE_HEIGHT_OFFSET = 360;
	private static final int PROVISIONING_TEMPLATE_HEIGHT_OFFSET = 480;

	private static int worldWidth;
	private static int worldHeight;

	private static OrthographicCamera camera;
	private static Viewport viewport;
	private static SpriteBatch batch;
	private static ShapeRenderer shapeRenderer;
	private static BitmapFont font = new BitmapFont();
	private static WorkbenchOutline workbenchOutline;
	private static Template microserviceTemplate;
	private static Template infrastructureTemplate;
	private static Template scriptTemplate;
	private static WorkspaceRenderer workspaceRenderer;
	private static List<WorkbenchRenderer> workbenchRenderItems = new ArrayList<WorkbenchRenderer>();
	private static Arrow arrow;
	private static Topic topic;
	private static DataStore dataStore;
	private static DeviceInputProcessor deviceInputProcessor;
	private static String instructions;
	private static ServiceList serviceListComponent;
	private static List<WorkbenchItem> arrowList;
	private static List<WorkbenchItem> topicList;
	private static List<WorkbenchItem> dataStoreList;

	private static boolean toggleFSFlag = false;
	private static boolean dialogToggleFlag = false;
	private static Skin skin;
	private static Stage stage;

	private static Workspace instance = null;

	public static Workspace getInstance() {
		if (instance != null)
			return instance;

		return null;
	}

	public static Workspace getInstance(int worldWidth, int worldHeight) {
		if (instance != null)
			return instance;
		else
			return new Workspace(worldWidth, worldHeight);
	}

	private Workspace() {
	}

	private Workspace(int worldWidth, int worldHeight) {
		instance = this;

		Workspace.worldWidth = worldWidth;
		Workspace.worldHeight = worldHeight;

		camera = new OrthographicCamera();
		viewport = new ScreenViewport(camera);
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();

		skin = new Skin(Gdx.files.internal("uiskin.json"));
		instantiateTemplates(worldWidth, worldHeight);
		instantiateArrow();
		instantiateTopic();
		instantiateDataStore();
		workspaceRenderer = new WorkspaceRenderer();
		stage = new Stage();
		serviceListComponent = new ServiceList();
		arrowList = new ArrayList<>();
		topicList = new ArrayList<>();
		dataStoreList = new ArrayList<>();
		setupInputProcessors();
		setupWorkspaceButtons();
	}

	private void setupWorkspaceButtons() {
		setupLoadButton();
		setupSaveButton();
		setupInstructionsButton();
		setupOverrideEnvVarsButton();
	}

	private void setupOverrideEnvVarsButton() {
		Button envVarsButton = new TextButton("Override Env", getSkin());
		envVarsButton.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event.toString().equals("touchDown")) {
					Gdx.input.setInputProcessor(stage);
					InstructionsDialog instructionsDialog = new InstructionsDialog("Instructions", skin);
					instructionsDialog.getInstructionsTextArea().setText(instructions);
					stage.act();
					instructionsDialog.show(stage);
					setDialogToggleFlag(true);
				}
				return false;
			}
		});
		envVarsButton.setPosition(15, 45);
		envVarsButton.setWidth(125);
		stage.addActor(envVarsButton);
	}

	private void setupInstructionsButton() {
		Button instructionsButton = new TextButton("Instructions", getSkin());
		instructionsButton.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event.toString().equals("touchDown")) {
					Gdx.input.setInputProcessor(stage);
					InstructionsDialog instructionsDialog = new InstructionsDialog("Instructions", skin);
					instructionsDialog.getInstructionsTextArea().setText(instructions);
					stage.act();
					instructionsDialog.show(stage);
					setDialogToggleFlag(true);
				}
				return false;
			}
		});
		instructionsButton.setPosition(15, 15);
		instructionsButton.setWidth(125);
		stage.addActor(instructionsButton);
	}

	private void setupLoadButton() {
		Button loadButton = new TextButton("Load", getSkin());
		loadButton.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event.toString().equals("touchDown")) {
					Gdx.input.setInputProcessor(stage);
					OpenDialog openDialog = new OpenDialog("Open workspace", skin);
					stage.act();
					openDialog.show(stage);
				}
				return false;
			}
		});
		loadButton.setPosition(145, 45);
		loadButton.setWidth(60);
		stage.addActor(loadButton);
	}

	private void setupSaveButton() {
		Button saveButton = new TextButton("Save", getSkin());
		saveButton.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				if (event.toString().equals("touchDown")) {
					Gdx.input.setInputProcessor(stage);
					SaveDialog saveDialog = new SaveDialog("Save workspace", skin);
					stage.act();
					saveDialog.show(stage);
				}
				return false;
			}
		});
		saveButton.setPosition(145, 15);
		saveButton.setWidth(60);
		stage.addActor(saveButton);
	}

	private void setupInputProcessors() {
		deviceInputProcessor = new DeviceInputProcessor();
		Gdx.input.setInputProcessor(stage);
	}

	private void instantiateTemplates(int worldWidth, int worldHeight) {
		workbenchRenderItems
				.add(workbenchOutline = new WorkbenchOutline(worldWidth, worldHeight, shapeRenderer, camera));
		workbenchRenderItems.add(new Template(worldHeight - MICROSERVICE_TEMPLATE_HEIGHT_OFFSET, Color.FOREST,
				Color.FOREST, "Application/Service", TemplateType.MICROSERVICE, UUID.randomUUID()));
		workbenchRenderItems.add(new Template(worldHeight - INFRASTRUCTURE_TEMPLATE_HEIGHT_OFFSET, Color.CORAL,
				Color.CORAL, "Infrastructure", TemplateType.INFRASTRUCTURE, UUID.randomUUID()));
		workbenchRenderItems.add(new Template(worldHeight - SCRIPTS_TEMPLATE_HEIGHT_OFFSET, Color.BLUE, Color.GRAY,
				"Scripts", TemplateType.SCRIPT, UUID.randomUUID()));
		workbenchRenderItems.add(new Template(worldHeight - PROVISIONING_TEMPLATE_HEIGHT_OFFSET, Color.WHITE,
				Color.WHITE, "Provisioning", TemplateType.PROVISIONING, UUID.randomUUID()));
	}

	private void instantiateArrow() {
		arrow = new Arrow();
	}

	private void instantiateTopic() {
		topic = new Topic();
	}

	private void instantiateDataStore() {
		dataStore = new DataStore();
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

	public List<WorkbenchRenderer> getWorkbenchItems() {
		return workbenchRenderItems;
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

	public void setDeviceInputProcessor(DeviceInputProcessor deviceInputProcessor) {
		Workspace.deviceInputProcessor = deviceInputProcessor;
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
		Workspace.dialogToggleFlag = dialogToggleFlag;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		Workspace.instructions = instructions;
	}

	public Arrow getArrow() {
		return arrow;
	}

	public List<WorkbenchItem> getArrowList() {
		return arrowList;
	}

	public void setArrowList(List<WorkbenchItem> arrowList) {
		Workspace.arrowList = arrowList;
	}

	public Topic getTopic() {
		return topic;
	}

	public List<WorkbenchItem> getTopicList() {
		return topicList;
	}

	public void setTopicList(List<WorkbenchItem> topicList) {
		Workspace.topicList = topicList;
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	public List<WorkbenchItem> getDataStoreList() {
		return dataStoreList;
	}

	public void setDataStoreList(List<WorkbenchItem> dataStoreList) {
		Workspace.dataStoreList = dataStoreList;
	}

}
