package com.onlineinteract.core.workbench;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.dialog.ServiceDialog;
import com.onlineinteract.core.type.ProcessingType;
import com.onlineinteract.core.type.ServiceStatus;
import com.onlineinteract.core.type.TemplateType;

public class Template extends WorkbenchItem {

	UUID uuid;

	public static final int DOUBLE_CLICK_RANGE = 400;
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
	private String startupCommand;
	private String shutdownCommand;
	private String shutdown2Command;
	private String runningClause;
	private String servicePortNo;
	private ServiceDialog serviceDialog;
	private Skin skin;
	private Stage stage;
	private Process exec;
	private Process shutDownExec;
	private Runtime runtime;
	private TemplateType templateType;
	private ProcessingType processingType;

	private ServiceStatus serviceStatus = ServiceStatus.SHUTDOWN;

	private long previousTimeMillis = -DOUBLE_CLICK_RANGE - 1;

	public Template() {
	}

	public Template(float y, Color color1, Color color2, String label, TemplateType type, UUID uuid) {
		this(BOX_OFFEST_X, y, color1, color2, label, type, uuid);
	}

	public Template(float x, float y, Color color1, Color color2, String label, TemplateType type, UUID uuid) {
		this.shapeRenderer = Workspace.getInstance().getShapeRenderer();
		this.batch = Workspace.getInstance().getBatch();
		this.font = Workspace.getInstance().getFont();
		this.camera = Workspace.getInstance().getCamera();
		this.skin = Workspace.getInstance().getSkin();
		this.stage = Workspace.getInstance().getStage();
		this.x = x;
		this.y = y;
		this.color1 = color1;
		this.color2 = color2;
		this.label = label;
		this.templateType = type;
		this.processingType = ProcessingType.SEQ;
		this.uuid = (uuid != null) ? uuid : UUID.randomUUID();
		runtime = Runtime.getRuntime();
	}

	public void draw() {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(color1);
		Gdx.gl.glLineWidth(2);
		shapeRenderer.rect(x, y, BOX_WIDTH, BOX_HEIGHT);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rect(x + 1, y + 1, BOX_WIDTH - 2, BOX_HEIGHT - 2);
		drawServiceStatus();

		if (serviceStatus == ServiceStatus.RUNNING) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(color1);
			shapeRenderer.rect(x + 60, y + 110, 120, 25);
			shapeRenderer.line(x + 120, y + 100, x + 120, y + 110);
			shapeRenderer.end();
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			font.setColor(Color.FOREST);
			font.getData().setScale(1);
			font.draw(batch, "Running", x + 90, y + 130);
			batch.end();
		}

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		font.setColor(color2);
		font.getData().setScale(1);
		font.draw(batch, label, x + LABEL_OFFSET_X, y + LABEL_OFFSET_Y);
		batch.end();
	}

	private void drawServiceStatus() {
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);

		switch (serviceStatus) {
		case SHUTDOWN:
			shapeRenderer.setColor(Color.FOREST);
			shapeRenderer.triangle(x + 10, y + 10, x + 10, y + 30, x + 30, y + 20);
			break;
		case LOADING:
			shapeRenderer.setColor(Color.GOLD);
			shapeRenderer.circle(x + 20, y + 20, 10, 100);
			break;
		case RUNNING:
			shapeRenderer.setColor(Color.FIREBRICK);
			shapeRenderer.rect(x + 10, y + 10, 20, 20);
			break;
		}

		shapeRenderer.end();
	}

	public boolean isClickWithinBoundary(float x, float y) {
		float clickX = x;
		float clickY = y;

		if (clickX >= this.x && clickX <= (this.x + BOX_WIDTH) && clickY >= this.y && clickY <= (this.y + BOX_HEIGHT)) {
			instanceOffsetX = clickX - this.x;
			instanceOffsetY = clickY - this.y;
			return true;
		}

		return false;
	}

	public void startStopService(float x, float y) {
		float clickX = x;
		float clickY = y;

		if (clickX >= this.x + 10 && clickX <= (this.x + 30) && clickY >= this.y + 10 && clickY <= (this.y + 30)
				&& !Workspace.getInstance().isDialogToggleFlag())
			determineStartStop();
	}

	public void startStopService() {
		determineStartStop();
	}

	private void determineStartStop() {
		switch (serviceStatus) {
		case SHUTDOWN:
			serviceStatus = ServiceStatus.LOADING;
			spawnServiceInstance();
			break;
		case RUNNING:
			serviceStatus = ServiceStatus.LOADING;
			destroyServiceInstance();
			break;
		case LOADING:
			destroyServiceInstance();
			break;
		default:
			break;
		}
	}

	public void spawnServiceInstance() {
		if ((startupCommand == null || runningClause == null || startupCommand.isEmpty() || runningClause.isEmpty())
				&& templateType != TemplateType.SCRIPT) {
			System.out.println("Startup command or running clause not set");
			serviceStatus = ServiceStatus.SHUTDOWN;
			return;
		}
		String launchCommand = replaceEnvVars(startupCommand);
		try {
			exec = runtime.exec(launchCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}

		processInputStream();
		processErrorStream();
	}

	private void processInputStream() {
		new Thread(() -> {
			try {
				InputStream inputStream = exec.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

				String line = "";
				while ((line = bufferedReader.readLine()) != null) {
					System.out.println(line);
					if (templateType != TemplateType.SCRIPT) {
						if (line.contains(runningClause)) {
							System.out.println("Application launched successfully!");
							serviceStatus = ServiceStatus.RUNNING;
						}
					}
				}
				if (templateType == TemplateType.SCRIPT) {
					destroyServiceInstance();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	private void processErrorStream() {
		new Thread(() -> {
			try {
				InputStream errorStream = exec.getErrorStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(errorStream, "UTF-8"));

				String line = "";
				while ((line = bufferedReader.readLine()) != null) {
					System.out.println(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void destroyServiceInstance() {
		exec.destroy();
		System.out.println(label + " Destroyed");
		executeShutdownCommand(shutdownCommand);
		executeShutdownCommand(shutdown2Command);
		serviceStatus = ServiceStatus.SHUTDOWN;
	}

	private void executeShutdownCommand(String shutdownCommand) {
		if (shutdownCommand == null || shutdownCommand.isEmpty()) {
			return;
		}
		String launchShutdownCommand = replaceEnvVars(shutdownCommand);
		System.out.println("*** Shutdown command is: " + launchShutdownCommand);
		try {
			shutDownExec = runtime.exec(launchShutdownCommand);
		} catch (IOException e) {
			e.printStackTrace();
		}

		processShutdownInputStream();
	}

	private void processShutdownInputStream() {
		try {
			InputStream inputStream = shutDownExec.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(label + " shutdown successfully STDOUT");
	}

	protected String replaceEnvVars(String command) {
		String launchCommand = command;
		while (launchCommand.indexOf("%") != -1) {
			int startIndex = launchCommand.indexOf("%");
			int endIndex = launchCommand.indexOf("%", startIndex + 1);
			if (endIndex == -1)
				break;
			String envVar = launchCommand.substring(startIndex + 1, endIndex);
			if (envVar.isEmpty()) {
				return command;
			}
			launchCommand = launchCommand.replace("%" + envVar + "%", System.getenv(envVar));
		}
		return launchCommand;
	}

	public void renderDialog() {
		Gdx.input.setInputProcessor(stage);
		serviceDialog = new ServiceDialog("Service Configuration", skin, Workspace.getInstance(), this);
		if (label.equals("Application/Service"))
			serviceDialog.getLabelTextField().setText("New Service");
		else
			serviceDialog.getLabelTextField().setText(label);
		serviceDialog.getStartupCommandTextField().setText(startupCommand);
		if (templateType != TemplateType.SCRIPT) {
			serviceDialog.getShutdown1CommandTextField().setText(shutdownCommand);
			serviceDialog.getShutdown2CommandTextField().setText(shutdown2Command);
			serviceDialog.getRunningClauseTextField().setText(runningClause);
			serviceDialog.getServicePortNoTextField().setText(servicePortNo);
		}
		stage.act();
		serviceDialog.show(stage);
		Workspace.getInstance().setDialogToggleFlag(true);
	}

	@Override
	public String toString() {
		String tempLabel = "";

		if (label.equals("Application/Service"))
			tempLabel = "New Service";
		else
			tempLabel = label;

		return "(" + processingType + ") " + tempLabel;
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

	public long getPreviousTimeMillis() {
		return previousTimeMillis;
	}

	public void setPreviousTimeMillis(long previousTimeMillis) {
		this.previousTimeMillis = previousTimeMillis;
	}

	public String getStartupCommand() {
		return startupCommand;
	}

	public void setStartupCommand(String startupCommand) {
		this.startupCommand = startupCommand;
	}

	public String getShutdownCommand() {
		return shutdownCommand;
	}

	public void setShutdownCommand(String shutdownCommand) {
		this.shutdownCommand = shutdownCommand;
	}

	public void setShutdown2Command(String shutdownCommand) {
		this.shutdown2Command = shutdownCommand;
	}

	public String getShutdown2Command() {
		return shutdown2Command;
	}

	public String getRunningClause() {
		return runningClause;
	}

	public void setRunningClause(String runningClause) {
		this.runningClause = runningClause;
	}

	public String getServicePortNo() {
		return servicePortNo;
	}

	public void setServicePortNo(String servicePortNo) {
		this.servicePortNo = servicePortNo;
	}

	public TemplateType getType() {
		return templateType;
	}

	public void setType(TemplateType type) {
		this.templateType = type;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public ProcessingType getProcessingType() {
		return processingType;
	}

	public void setProcessingType(ProcessingType processingType) {
		this.processingType = processingType;
	}

	public ServiceStatus getServiceStatus() {
		return serviceStatus;
	}

	public void setServiceStatus(ServiceStatus serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public Color getColor1() {
		return color1;
	}

	public void setColor1(Color color1) {
		this.color1 = color1;
	}

	public Color getColor2() {
		return color2;
	}

	public void setColor2(Color color2) {
		this.color2 = color2;
	}

	public Workspace getWorkspace() {
		return Workspace.getInstance();
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	@JsonIgnore
	public void setShapeRenderer(ShapeRenderer shapeRenderer) {
		this.shapeRenderer = shapeRenderer;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	@JsonIgnore
	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	@JsonIgnore
	public void setFont(BitmapFont font) {
		this.font = font;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	@JsonIgnore
	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public Skin getSkin() {
		return skin;
	}

	@JsonIgnore
	public void setSkin(Skin skin) {
		this.skin = skin;
	}

	public Stage getStage() {
		return stage;
	}

	@JsonIgnore
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Runtime getRuntime() {
		return runtime;
	}

	@JsonIgnore
	public void setRuntime(Runtime runtime) {
		this.runtime = runtime;
	}

	@JsonIgnore
	@Override
	public void setWorkspace(Workspace workspace) {
		// TODO Revisit and strip out.
	}

}
