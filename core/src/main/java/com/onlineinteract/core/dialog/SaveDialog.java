package com.onlineinteract.core.dialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.component.ServiceList;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchItem;

public class SaveDialog extends Dialog {

	private Workspace workspace;
	TextField pathTextField;
	Label pathLabel;

	public SaveDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
	}

	public SaveDialog(String title, Skin skin, Workspace workspace) {
		super(title, skin);
		this.workspace = workspace;
	}

	{
		button("Save", true).padBottom(10);
		button("Cancel", false).padBottom(10);
		pathTextField = new TextField("", getSkin());
		pathLabel = new Label("File Path: ", getSkin());
		getContentTable().add(pathLabel).padTop(20);
		getContentTable().add(pathTextField).padTop(20).width(200);
		getContentTable().row();
	}

	@Override
	protected void result(Object object) {
		if (object.getClass().getSimpleName().equals("Boolean") && object == Boolean.TRUE) {
			saveToFile();
		}
	}

	private void saveToFile() {
		ServiceList serviceListComponent = workspace.getServiceListComponent();
		ObjectMapper mapper = new ObjectMapper();
		File file = new File(pathTextField.getText());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		writeOutInstructions(mapper, fos);
		writeOutOrderList(serviceListComponent, mapper, fos);
		writeOutTemplateInstances(serviceListComponent, mapper, fos);
		writeOutArrows(mapper, fos);
		writeOutTopics(mapper, fos);
		writeOutDataStores(mapper, fos);
		try {
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gdx.graphics.setTitle("Discovery Workbench - " + pathTextField.getText());
	}

	private void writeOutDataStores(ObjectMapper mapper, FileOutputStream fos) {
		List<WorkbenchItem> dataStoreList = workspace.getDataStoreList();
		try {
			for (WorkbenchItem dataStore : dataStoreList) {
				String dataStoreString = mapper.writeValueAsString(dataStore);
				dataStoreString += "\n";
				fos.write(dataStoreString.getBytes());
			}
			fos.write("#endDataStores\n".getBytes());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeOutTopics(ObjectMapper mapper, FileOutputStream fos) {
		List<WorkbenchItem> topicList = workspace.getTopicList();
		try {
			for (WorkbenchItem topic : topicList) {
				String topicString = mapper.writeValueAsString(topic);
				topicString += "\n";
				fos.write(topicString.getBytes());
			}
			fos.write("#endTopics\n".getBytes());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeOutArrows(ObjectMapper mapper, FileOutputStream fos) {
		List<WorkbenchItem> arrowList = workspace.getArrowList();
		try {
			for (WorkbenchItem arrow : arrowList) {
				String arrowString = mapper.writeValueAsString(arrow);
				arrowString += "\n";
				fos.write(arrowString.getBytes());
			}
			fos.write("#endArrows\n".getBytes());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeOutInstructions(ObjectMapper mapper, FileOutputStream fos) {
		try {
			String instructions = mapper.writeValueAsString(workspace.getInstructions()) + "\n";
			fos.write(instructions.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeOutTemplateInstances(ServiceList serviceListComponent, ObjectMapper mapper,
			FileOutputStream fos) {
		try {
			for (Template instance : serviceListComponent.getTemplateInstances()) {
				String instanceString = mapper.writeValueAsString(instance);
				instanceString += "\n";
				fos.write(instanceString.getBytes());
			}
			fos.write("#endTemplateInstances\n".getBytes());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeOutOrderList(ServiceList serviceListComponent, ObjectMapper mapper, FileOutputStream fos) {
		List<UUID> orderList = new ArrayList<>();
		for (Template instance : serviceListComponent.getOrderedServiceList()) {
			orderList.add(instance.getUuid());
		}
		try {
			String orderListString = mapper.writeValueAsString(orderList);
			orderListString += "\n";
			fos.write(orderListString.getBytes());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
