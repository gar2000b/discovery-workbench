package com.onlineinteract.core.processor;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.type.TemplateType;
import com.onlineinteract.core.util.ListTypeRetainer;
import com.onlineinteract.core.workbench.Arrow;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchItem;
import com.onlineinteract.core.workbench.WorkbenchOutline;

import java.util.List;
import java.util.UUID;

public class DeviceInputProcessor {

	private Workspace workspace;
	private List<Template> retainedTemplateList;
	private List<Template> templateInstances;
	private boolean instanceDragFlag = false;
	private boolean arrowDragFlag = false;
	private Template currentInstanceItem;
	private Arrow currentInstanceArrow;

	public DeviceInputProcessor(Workspace workspace) {
		this.workspace = workspace;
		templateInstances = this.workspace.getServiceListComponent().getTemplateInstances();
		List<WorkbenchItem> workbenchItems = this.workspace.getWorkbenchItems();
		retainedTemplateList = ListTypeRetainer.<WorkbenchItem, Template>retainedList(workbenchItems, Template.class);
		processEvents();
	}

	private void processEvents() {
		workspace.getStage().addListener(new ClickListener(this, Buttons.LEFT));
		workspace.getStage().addListener(new ClickListener(this, Buttons.RIGHT));
	}

	protected void processTouchDown(InputEvent evt) {
		if (evt.getButton() == Input.Buttons.LEFT) {
			detectClickTemplateList(evt.getStageX(), evt.getStageY());
			detectClickTemplateInstances(evt.getStageX(), evt.getStageY());
			detectClickArrow(evt.getStageX(), evt.getStageY());
			detectClickArrowInstances(evt.getStageX(), evt.getStageY());
		}

		if (evt.getButton() == Input.Buttons.RIGHT) {
			removeTemplateInstance(evt.getStageX(), evt.getStageY());
		}
	}

	private void detectClickArrow(float x, float y) {
		Arrow arrow = workspace.getArrow();
		if (arrow.isClickWithinBoundary(x, y))
			createArrowInstance();
	}

	private void detectClickTemplateList(float x, float y) {
		for (Template templateItem : retainedTemplateList) {
			if (templateItem.isClickWithinBoundary(x, y))
				createTemplateInstance(templateItem);
		}
	}

	private void detectClickTemplateInstances(float x, float y) {
		for (Template instanceItem : templateInstances) {
			if (instanceItem.isClickWithinBoundary(x, y)) {
				putInstanceToBeginningOfList(instanceItem, templateInstances);
				instanceDragFlag = true;
				instanceItem.startStopService(x, y);
				currentInstanceItem = instanceItem;
				detectAndProcessDoubleClick(instanceItem);
				break;
			}
		}
	}

	private void detectClickArrowInstances(float x, float y) {
		for (Arrow arrow : workspace.getArrowList()) {
			if (arrow.isClickWithinBoundary(x, y)) {
				putInstanceToBeginningOfList(arrow, workspace.getArrowList());
				arrowDragFlag = true;
				currentInstanceArrow = arrow;
				break;
			}
		}
	}

	private void removeTemplateInstance(float x, float y) {
		for (Template instanceItem : templateInstances) {
			if (instanceItem.isClickWithinBoundary(x, y))
				instanceItem.renderDeleteServiceDialog();
		}
	}

	@SuppressWarnings("unchecked")
	private <T> void putInstanceToBeginningOfList(WorkbenchItem instanceItem, List<T> workbenchItems) {
		int index = workbenchItems.indexOf(instanceItem);
		workbenchItems.remove(index);
		workbenchItems.add(0, (T) instanceItem);
	}

	private void detectAndProcessDoubleClick(Template instanceItem) {
		long currentTimeMillis = System.currentTimeMillis();
		if (currentTimeMillis - currentInstanceItem.getPreviousTimeMillis() < Template.DOUBLE_CLICK_RANGE
				&& !workspace.isToggleFSFlag() && !workspace.isDialogToggleFlag()) {
			currentInstanceItem.renderServiceDialog();
			instanceDragFlag = false;
			arrowDragFlag = false;
			currentInstanceItem.setPreviousTimeMillis(currentTimeMillis);
		}
		currentInstanceItem.setPreviousTimeMillis(currentTimeMillis);
	}

	private void createTemplateInstance(Template templateItem) {
		WorkbenchOutline workbenchOutline = workspace.getWorkbenchOutline();
		float x = (WorkbenchOutline.BOX_X * 2) + WorkbenchOutline.COLUMN_WIDTH;
		float y = workbenchOutline.getBoxHeight() - Template.BOX_HEIGHT;

		if (templateItem.getLabel().equals("Application/Service"))
			workspace.getServiceListComponent().addTemplateInstance(new Template(workspace, x, y, Color.FOREST,
					Color.FOREST, "Application/Service", TemplateType.MICROSERVICE, UUID.randomUUID()));
		if (templateItem.getLabel().equals("Infrastructure"))
			workspace.getServiceListComponent().addTemplateInstance(new Template(workspace, x, y, Color.CORAL,
					Color.CORAL, "Infrastructure", TemplateType.INFRASTRUCTURE, UUID.randomUUID()));
		if (templateItem.getLabel().equals("Scripts"))
			workspace.getServiceListComponent().addTemplateInstance(new Template(workspace, x, y, Color.BLUE,
					Color.GRAY, "Scripts", TemplateType.SCRIPT, UUID.randomUUID()));
		if (templateItem.getLabel().equals("Provisioning"))
			workspace.getServiceListComponent().addTemplateInstance(new Template(workspace, x, y, Color.WHITE,
					Color.WHITE, "Provisioning", TemplateType.PROVISIONING, UUID.randomUUID()));
	}

	private void createArrowInstance() {
		WorkbenchOutline workbenchOutline = workspace.getWorkbenchOutline();
		float x = (WorkbenchOutline.BOX_X * 2) + WorkbenchOutline.COLUMN_WIDTH;
		float y = workbenchOutline.getBoxHeight() - 20;
		workspace.getArrowList().add(new Arrow(x, y, workspace.getCamera()));
	}

	protected void processTouchUp(InputEvent evt) {
		if (evt.getButton() == Input.Buttons.LEFT) {
			instanceDragFlag = false;
			arrowDragFlag = false;
			currentInstanceItem = null;
		}
	}

	protected void processTouchDragged(InputEvent evt) {
		if (instanceDragFlag && !workspace.isDialogToggleFlag()) {
			currentInstanceItem.setX(evt.getStageX() - currentInstanceItem.getInstanceOffsetX());
			currentInstanceItem.setY(evt.getStageY() - currentInstanceItem.getInstanceOffsetY());
		}

		if (arrowDragFlag && !workspace.isDialogToggleFlag()) {
			currentInstanceArrow.setX(evt.getStageX() - currentInstanceArrow.getInstanceOffsetX());
			currentInstanceArrow.setY(evt.getStageY() - currentInstanceArrow.getInstanceOffsetY());
		}
	}

	public List<Template> getTemplateInstances() {
		return templateInstances;
	}

	public void setTemplateInstances(List<Template> templateInstances) {
		this.templateInstances = templateInstances;
	}
}
