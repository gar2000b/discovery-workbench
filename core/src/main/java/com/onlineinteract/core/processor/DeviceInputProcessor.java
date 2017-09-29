package com.onlineinteract.core.processor;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.util.ListTypeRetainer;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchItem;
import com.onlineinteract.core.workbench.WorkbenchOutline;

public class DeviceInputProcessor implements InputProcessor {

	private Workspace workspace;
	private List<Template> retainedTemplateList;
	private List<Template> templateInstances;
	private boolean instanceDragFlag = false;
	private Template currentInstanceItem;

	public DeviceInputProcessor(Workspace workspace) {
		this.workspace = workspace;
		templateInstances = this.workspace.getTemplateInstances();
		List<WorkbenchItem> workbenchItems = this.workspace.getWorkbenchItems();
		retainedTemplateList = ListTypeRetainer.<WorkbenchItem, Template>retainedList(workbenchItems, Template.class);
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		return processTouchDown(button);
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		return processTouchUp(button);
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector3 coordinates = workspace.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		if (instanceDragFlag) {
			currentInstanceItem.setX(coordinates.x - currentInstanceItem.getInstanceOffsetX());
			currentInstanceItem.setY(coordinates.y - currentInstanceItem.getInstanceOffsetY());
			return true;
		}

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	private boolean processTouchDown(int button) {
		if (button == Input.Buttons.LEFT) {
			Vector3 coordinates = workspace.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
			detectClickTemplateList(coordinates);
			detectClickTemplateInstances(coordinates);
			return true;
		}
		return false;
	}

	private void detectClickTemplateList(Vector3 coordinates) {
		for (Template templateItem : retainedTemplateList) {
			if (templateItem.isClickWithinBoundary(coordinates))
				createTemplateInstance(templateItem);
		}
	}

	private void detectClickTemplateInstances(Vector3 coordinates) {
		for (Template instanceItem : templateInstances) {
			if (instanceItem.isClickWithinBoundary(coordinates)) {
				instanceDragFlag = true;
				instanceItem.startStopService(coordinates);
				if (detectAndProcessDoubleClick(instanceItem)) {
					break;
				}
			}
		}
	}

	private boolean detectAndProcessDoubleClick(Template instanceItem) {
		currentInstanceItem = instanceItem;
		long currentTimeMillis = System.currentTimeMillis();
		if (currentTimeMillis - currentInstanceItem.getPreviousTimeMillis() < Template.DOUBLE_CLICK_RANGE
				&& !workspace.isToggleFSFlag()) {
			System.out.println("*** Double click detected");
			currentInstanceItem.renderServiceDialog();
			instanceDragFlag = false;
			currentInstanceItem.setPreviousTimeMillis(currentTimeMillis);
			return true;
		}
		currentInstanceItem.setPreviousTimeMillis(currentTimeMillis);
		return false;
	}
	
//	private void detectClickStartStopInstances(Vector3 coordinates, Template instanceItem) {
//		if (instanceItem.isClickWithinStartStopBoundary(coordinates)) {
//			
//		}
//		
//	}

	private boolean processTouchUp(int button) {
		if (button == Input.Buttons.LEFT) {
			instanceDragFlag = false;
			currentInstanceItem = null;
			return true;
		}
		return false;
	}

	private void createTemplateInstance(Template templateItem) {

		WorkbenchOutline workbenchOutline = workspace.getWorkbenchOutline();

		float x = (WorkbenchOutline.BOX_X * 2) + WorkbenchOutline.COLUMN_WIDTH;
		float y = workbenchOutline.getBoxHeight() - Template.BOX_HEIGHT;

		if (templateItem.getLabel().equals("µicroservice"))
			templateInstances.add(new Template(workspace, x, y, Color.FOREST, Color.FOREST, "µicroservice"));
		if (templateItem.getLabel().equals("Infrastructure"))
			templateInstances.add(new Template(workspace, x, y, Color.CORAL, Color.CORAL, "Infrastructure"));
		if (templateItem.getLabel().equals("Scripts"))
			templateInstances.add(new Template(workspace, x, y, Color.BLUE, Color.GRAY, "Scripts"));
	}
}
