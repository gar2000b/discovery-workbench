package com.onlineinteract.core.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.type.ProcessingType;
import com.onlineinteract.core.type.ServiceStatus;
import com.onlineinteract.core.type.TemplateType;
import com.onlineinteract.core.workbench.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ServiceList {

	private com.badlogic.gdx.scenes.scene2d.ui.List<Template> serviceList;
	private List<Template> orderedServiceList;
	ScrollPane scrollPane;
	private List<Template> templateInstances = new ArrayList<Template>();

	public ServiceList() {
		serviceList = new com.badlogic.gdx.scenes.scene2d.ui.List<Template>(Workspace.getInstance().getSkin());
		orderedServiceList = new ArrayList<>();

		serviceList.setItems(orderedServiceList.toArray(new Template[orderedServiceList.size()]));
		scrollPane = new ScrollPane(serviceList);
		scrollPane.setBounds(10, 10, 200, 660);
		scrollPane.setSmoothScrolling(true);
		scrollPane.setPosition(Workspace.getInstance().getWorldWidth() - 280, 20);
		scrollPane.setTransform(true);
		scrollPane.setScale(1);
		serviceList.setColor(Color.CYAN);
		serviceList.getSelection().setMultiple(true);
		serviceList.getSelection().setRequired(false);
		serviceList.setSelected(null);

		Workspace.getInstance().getStage().addActor(scrollPane);
		setupServiceListButtons();
	}

	private void setupServiceListButtons() {
		setupToggleProcessingTypeButton();
		setupUpButton();
		setupDownButton();
		setupStartAllButton();
		setupStopAllButton();
	}

	private void setupToggleProcessingTypeButton() {
		Button toggleProcessingTypeButton = new TextButton("Toggle Processing Type", Workspace.getInstance().getSkin());
		toggleProcessingTypeButton.addListener(new EventListener() {
			@Override
			public boolean handle(Event event) {
				return toggleProcessingTypes(event);
			}
		});
		toggleProcessingTypeButton.setPosition(1280, 750);
		toggleProcessingTypeButton.setWidth(200);
		Workspace.getInstance().getStage().addActor(toggleProcessingTypeButton);
	}

	private boolean toggleProcessingTypes(Event event) {
		if (event.toString().equals("touchDown")) {
			for (Template selection : serviceList.getSelection()) {
				if (selection.getProcessingType() == ProcessingType.SEQ)
					selection.setProcessingType(ProcessingType.PAR);
				else
					selection.setProcessingType(ProcessingType.SEQ);
			}
			return true;
		}
		return false;
	}

	private void setupUpButton() {
		Button upButton = new TextButton("^ - up", Workspace.getInstance().getSkin());
		upButton.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return moveItemsUp(event);
			}
		});
		upButton.setPosition(1280, 720);
		upButton.setWidth(50);
		Workspace.getInstance().getStage().addActor(upButton);
	}

	private boolean moveItemsUp(Event event) {
		if (event.toString().equals("touchDown")) {
			for (Template selection : serviceList.getSelection()) {
				for (int i = 0; i < orderedServiceList.size(); i++) {
					if (selection.getUuid() == orderedServiceList.get(i).getUuid()) {
						if (i != 0) {
							Template temp = orderedServiceList.get(i);
							orderedServiceList.set(i, orderedServiceList.get(i - 1));
							orderedServiceList.set(i - 1, temp);
							serviceList.clearItems();
							serviceList.setItems(orderedServiceList.toArray(new Template[orderedServiceList.size()]));
							serviceList.setSelectedIndex(i - 1);
							return true;
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	private void setupDownButton() {
		Button downButton = new TextButton("v - dn", Workspace.getInstance().getSkin());
		downButton.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return moveItemsDown(event);
			}
		});
		downButton.setPosition(1280, 690);
		downButton.setWidth(50);
		Workspace.getInstance().getStage().addActor(downButton);
	}

	private boolean moveItemsDown(Event event) {
		if (event.toString().equals("touchDown")) {
			for (Template selection : serviceList.getSelection()) {
				for (int i = 0; i < orderedServiceList.size(); i++) {
					if (selection.getUuid() == orderedServiceList.get(i).getUuid()) {
						if (i != orderedServiceList.size() - 1) {
							Template temp = orderedServiceList.get(i);
							orderedServiceList.set(i, orderedServiceList.get(i + 1));
							orderedServiceList.set(i + 1, temp);
							serviceList.clearItems();
							serviceList.setItems(orderedServiceList.toArray(new Template[orderedServiceList.size()]));
							serviceList.setSelectedIndex(i + 1);
							return true;
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	private void setupStartAllButton() {
		Button startAllButton = new TextButton("Start All", Workspace.getInstance().getSkin());
		startAllButton.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return startAll(event);
			}
		});
		startAllButton.setPosition(1340, 720);
		startAllButton.setWidth(140);
		Workspace.getInstance().getStage().addActor(startAllButton);
	}

	private void setupStopAllButton() {
		Button stopAllButton = new TextButton("Stop All", Workspace.getInstance().getSkin());
		stopAllButton.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				return stopAll(event);
			}
		});
		stopAllButton.setPosition(1340, 690);
		stopAllButton.setWidth(140);
		Workspace.getInstance().getStage().addActor(stopAllButton);
	}

	private boolean startAll(Event event) {
		if (event.toString().equals("touchDown")) {
			new Thread(() -> {
				for (Template template : orderedServiceList) {
					if (template.getServiceStatus() == ServiceStatus.SHUTDOWN) {
						template.setServiceStatus(ServiceStatus.LOADING);
						template.spawnServiceInstance();
						while (template.getServiceStatus() != ServiceStatus.RUNNING
								&& template.getProcessingType() == ProcessingType.SEQ) {
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}).start();
			return true;
		}
		return false;
	}

	private boolean stopAll(Event event) {
		if (event.toString().equals("touchDown")) {
			new Thread(() -> {

				for (ListIterator<Template> iterator = orderedServiceList
						.listIterator(orderedServiceList.size()); iterator.hasPrevious();) {
					final Template template = iterator.previous();
					if (template.getServiceStatus() == ServiceStatus.RUNNING) {
						template.destroyServiceInstance();
						continue;
					}
				}
			}).start();
			return true;
		}
		return false;
	}

	public void addTemplateInstance(Template instance) {
		templateInstances.add(instance);
		if (instance.getType() == TemplateType.MICROSERVICE && instance.getLabel().equals("Application/Service"))
			orderedServiceList.add(instance);
		else if (instance.getType() == TemplateType.INFRASTRUCTURE)
			orderedServiceList.add(instance);
		serviceList.clearItems();
		serviceList.setItems(orderedServiceList.toArray(new Template[orderedServiceList.size()]));
	}

	public void removeTemplateInstance(Template instance) {
		orderedServiceList.remove(instance);
		templateInstances.remove(instance);

		serviceList.clearItems();
		serviceList.setItems(orderedServiceList.toArray(new Template[orderedServiceList.size()]));
	}

	public void updateServiceList(Template instance) {
		for (int i = 0; i < orderedServiceList.size(); i++) {
			if (orderedServiceList.get(i).getLabel().contains(instance.getUuid().toString()))
				orderedServiceList.set(i, instance);
		}
		serviceList.clearItems();
		serviceList.setItems(orderedServiceList.toArray(new Template[orderedServiceList.size()]));
	}

	public void refreshOrderedServiceList() {
		serviceList.clearItems();
		serviceList.setItems(orderedServiceList.toArray(new Template[orderedServiceList.size()]));
	}

	public List<Template> getTemplateInstances() {
		return templateInstances;
	}

	public List<Template> getOrderedServiceList() {
		return orderedServiceList;
	}

	public void setOrderedServiceList(List<Template> orderedServiceList) {
		this.orderedServiceList = orderedServiceList;
	}

	public void setTemplateInstances(List<Template> templateInstances) {
		this.templateInstances = templateInstances;
	}
}
