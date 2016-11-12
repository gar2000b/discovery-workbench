package com.onlineinteract.core.processor;

import java.util.List;
import java.util.UUID;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.type.TemplateType;
import com.onlineinteract.core.util.ListTypeRetainer;
import com.onlineinteract.core.workbench.Arrow;
import com.onlineinteract.core.workbench.DataStore;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.Topic;
import com.onlineinteract.core.workbench.WorkbenchItem;
import com.onlineinteract.core.workbench.WorkbenchOutline;
import com.onlineinteract.core.workbench.WorkbenchRenderer;

public class DeviceInputProcessor {

	private List<Template> retainedTemplateList;
	private List<Template> templateInstances;
	private boolean instanceDragFlag = false;
	private WorkbenchItem currentInstanceItem;

	public DeviceInputProcessor() {
		templateInstances = Workspace.getInstance().getServiceListComponent().getTemplateInstances();
		List<WorkbenchRenderer> workbenchItems = Workspace.getInstance().getWorkbenchItems();
		retainedTemplateList = ListTypeRetainer.<WorkbenchRenderer, Template>retainedList(workbenchItems,
				Template.class);
		processEvents();
	}

	private void processEvents() {
		Workspace.getInstance().getStage().addListener(new ClickListener(this, Buttons.LEFT));
		Workspace.getInstance().getStage().addListener(new ClickListener(this, Buttons.RIGHT));
	}

	protected void processTouchDown(InputEvent evt) {
		if (evt.getButton() == Input.Buttons.LEFT) {
			detectClickTemplateList(evt.getStageX(), evt.getStageY());
			detectClickTemplateInstances(evt.getStageX(), evt.getStageY());
			detectClickArrow(evt.getStageX(), evt.getStageY());
			detectClickTopic(evt.getStageX(), evt.getStageY());
			detectClickDataStore(evt.getStageX(), evt.getStageY());
			detectClickArrowInstances(evt.getStageX(), evt.getStageY());
			detectClickTopicInstances(evt.getStageX(), evt.getStageY());
			detectClickDataStoreInstances(evt.getStageX(), evt.getStageY());
		}

		if (evt.getButton() == Input.Buttons.RIGHT) {
			removeWorkbenchItemInstance(evt.getStageX(), evt.getStageY(), templateInstances);
			removeWorkbenchItemInstance(evt.getStageX(), evt.getStageY(), Workspace.getInstance().getDataStoreList());
			removeWorkbenchItemInstance(evt.getStageX(), evt.getStageY(), Workspace.getInstance().getTopicList());
			removeWorkbenchItemInstance(evt.getStageX(), evt.getStageY(), Workspace.getInstance().getArrowList());
		}
	}

	private void detectClickDataStore(float x, float y) {
		DataStore dataStore = Workspace.getInstance().getDataStore();
		if (dataStore.isClickWithinBoundary(x, y))
			createDataStoreInstance();
	}

	private void detectClickTopic(float x, float y) {
		Topic topic = Workspace.getInstance().getTopic();
		if (topic.isClickWithinBoundary(x, y))
			createTopicInstance();
	}

	private void detectClickArrow(float x, float y) {
		Arrow arrow = Workspace.getInstance().getArrow();
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
		for (WorkbenchItem arrow : Workspace.getInstance().getArrowList()) {
			if (arrow.isClickWithinBoundary(x, y)) {
				putInstanceToBeginningOfList(arrow, Workspace.getInstance().getArrowList());
				instanceDragFlag = true;
				currentInstanceItem = arrow;
				break;
			}
		}
	}

	private void detectClickDataStoreInstances(float x, float y) {
		for (WorkbenchItem dataStore : Workspace.getInstance().getDataStoreList()) {
			if (dataStore.isClickWithinBoundary(x, y)) {
				putInstanceToBeginningOfList(dataStore, Workspace.getInstance().getDataStoreList());
				instanceDragFlag = true;
				currentInstanceItem = dataStore;
				detectAndProcessDoubleClick(dataStore);
				break;
			}
		}
	}

	private void detectClickTopicInstances(float x, float y) {
		for (WorkbenchItem topic : Workspace.getInstance().getTopicList()) {
			if (topic.isClickWithinBoundary(x, y)) {
				putInstanceToBeginningOfList(topic, Workspace.getInstance().getTopicList());
				instanceDragFlag = true;
				currentInstanceItem = topic;
				break;
			}
		}
	}

	private void removeWorkbenchItemInstance(float x, float y, List<? extends WorkbenchItem> workbenchItems) {
		for (WorkbenchItem instanceItem : workbenchItems) {
			if (instanceItem.isClickWithinBoundary(x, y))
				instanceItem.renderDeleteDialog();
		}
	}

	@SuppressWarnings("unchecked")
	private <T> void putInstanceToBeginningOfList(WorkbenchItem instanceItem, List<T> workbenchItems) {
		int index = workbenchItems.indexOf(instanceItem);
		workbenchItems.remove(index);
		workbenchItems.add(0, (T) instanceItem);
	}

	private void detectAndProcessDoubleClick(WorkbenchItem instanceItem) {
		long currentTimeMillis = System.currentTimeMillis();
		if (currentTimeMillis - currentInstanceItem.getPreviousTimeMillis() < Template.DOUBLE_CLICK_RANGE
				&& !Workspace.getInstance().isToggleFSFlag() && !Workspace.getInstance().isDialogToggleFlag()) {
			currentInstanceItem.renderDialog();
			instanceDragFlag = false;
			currentInstanceItem.setPreviousTimeMillis(currentTimeMillis);
		}
		currentInstanceItem.setPreviousTimeMillis(currentTimeMillis);
	}

	private void createTemplateInstance(Template templateItem) {
		WorkbenchOutline workbenchOutline = Workspace.getInstance().getWorkbenchOutline();
		float x = (WorkbenchOutline.BOX_X * 2) + WorkbenchOutline.COLUMN_WIDTH;
		float y = workbenchOutline.getBoxHeight() - Template.BOX_HEIGHT;

		if (templateItem.getLabel().equals("Application/Service"))
			Workspace.getInstance().getServiceListComponent().addTemplateInstance(new Template(x, y, Color.FOREST,
					Color.FOREST, "Application/Service", TemplateType.MICROSERVICE, UUID.randomUUID()));
		if (templateItem.getLabel().equals("Infrastructure"))
			Workspace.getInstance().getServiceListComponent().addTemplateInstance(new Template(x, y, Color.CORAL,
					Color.CORAL, "Infrastructure", TemplateType.INFRASTRUCTURE, UUID.randomUUID()));
		if (templateItem.getLabel().equals("Scripts"))
			Workspace.getInstance().getServiceListComponent().addTemplateInstance(
					new Template(x, y, Color.BLUE, Color.GRAY, "Scripts", TemplateType.SCRIPT, UUID.randomUUID()));
		if (templateItem.getLabel().equals("Provisioning"))
			Workspace.getInstance().getServiceListComponent().addTemplateInstance(new Template(x, y, Color.WHITE,
					Color.WHITE, "Provisioning", TemplateType.PROVISIONING, UUID.randomUUID()));
	}

	private void createArrowInstance() {
		WorkbenchOutline workbenchOutline = Workspace.getInstance().getWorkbenchOutline();
		float x = (WorkbenchOutline.BOX_X * 2) + WorkbenchOutline.COLUMN_WIDTH;
		float y = workbenchOutline.getBoxHeight() - 20;
		Workspace.getInstance().getArrowList().add(new Arrow(x, y, Workspace.getInstance().getCamera()));
	}

	private void createDataStoreInstance() {
		WorkbenchOutline workbenchOutline = Workspace.getInstance().getWorkbenchOutline();
		float x = (WorkbenchOutline.BOX_X * 2) + WorkbenchOutline.COLUMN_WIDTH;
		float y = workbenchOutline.getBoxHeight() - 20;
		Workspace.getInstance().getDataStoreList().add(new DataStore(x, y, Workspace.getInstance().getCamera()));
	}

	private void createTopicInstance() {
		WorkbenchOutline workbenchOutline = Workspace.getInstance().getWorkbenchOutline();
		float x = (WorkbenchOutline.BOX_X * 2) + WorkbenchOutline.COLUMN_WIDTH;
		float y = workbenchOutline.getBoxHeight() - 20;
		Workspace.getInstance().getTopicList().add(new Topic(x, y, Workspace.getInstance().getCamera()));
	}

	protected void processTouchUp(InputEvent evt) {
		if (evt.getButton() == Input.Buttons.LEFT) {
			instanceDragFlag = false;
			currentInstanceItem = null;
		}
	}

	protected void processTouchDragged(InputEvent evt) {
		if (instanceDragFlag && !Workspace.getInstance().isDialogToggleFlag()) {
			currentInstanceItem.setX(evt.getStageX() - currentInstanceItem.getInstanceOffsetX());
			currentInstanceItem.setY(evt.getStageY() - currentInstanceItem.getInstanceOffsetY());
		}
	}

	public List<Template> getTemplateInstances() {
		return templateInstances;
	}

	public void setTemplateInstances(List<Template> templateInstances) {
		this.templateInstances = templateInstances;
	}
}
