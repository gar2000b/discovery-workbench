package com.onlineinteract.core.dialog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.component.ServiceList;
import com.onlineinteract.core.workbench.Arrow;
import com.onlineinteract.core.workbench.DataStore;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.Topic;
import com.onlineinteract.core.workbench.WorkbenchItem;

public class OpenDialog extends Dialog {

	TextField pathTextField;
	Label pathLabel;

	public OpenDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
	}

	public OpenDialog(String title, Skin skin) {
		super(title, skin);
	}

	{
		button("Open", true).padBottom(10);
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
			openFile();
		}
	}

	@SuppressWarnings("unchecked")
	private void openFile() {
		ServiceList serviceListComponent = Workspace.getInstance().getServiceListComponent();
		ObjectMapper mapper = new ObjectMapper();
		String readLine = "";
		File file = new File(pathTextField.getText());
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			Workspace.getInstance().setInstructions(mapper.readValue(br.readLine(), String.class));
			readLine = br.readLine();
			List<String> orderedList = mapper.readValue(readLine, List.class);
			List<Template> templateInstances = readInTemplateInstances(mapper, br);
			List<WorkbenchItem> arrowList = readInArrowList(mapper, br);
			List<WorkbenchItem> topicList = readInTopicList(mapper, br);
			List<WorkbenchItem> dataStoreList = readInDataStoreList(mapper, br);
			br.close();
			serviceListComponent.setTemplateInstances(templateInstances);
			Workspace.getInstance().getDeviceInputProcessor().setTemplateInstances(templateInstances);
			List<Template> orderedServiceList = reconstructOrderedList(orderedList, templateInstances);
			serviceListComponent.setOrderedServiceList(orderedServiceList);
			serviceListComponent.refreshOrderedServiceList();
			Workspace.getInstance().setArrowList(arrowList);
			Workspace.getInstance().setTopicList(topicList);
			Workspace.getInstance().setDataStoreList(dataStoreList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Gdx.graphics.setTitle("Discovery Workbench - " + pathTextField.getText());
	}

	private List<Template> reconstructOrderedList(List<String> orderedList, List<Template> templateInstances) {
		List<Template> orderedServiceList = new ArrayList<>();
		for (int i = 0; i < orderedList.size(); i++)
			orderedServiceList.add(fetchTemplate(orderedList.get(i).toString(), templateInstances));
		return orderedServiceList;
	}

	private List<WorkbenchItem> readInDataStoreList(ObjectMapper mapper, BufferedReader br) throws IOException {
		String readLine;
		List<WorkbenchItem> dataStoreList = new ArrayList<>();
		while ((readLine = br.readLine()) != null && !readLine.equals("#endDataStores")) {
			DataStore dataStore = mapper.readValue(readLine, DataStore.class);
			dataStore.instantiateRenderers();
			dataStoreList.add(dataStore);
		}
		return dataStoreList;
	}

	private List<WorkbenchItem> readInTopicList(ObjectMapper mapper, BufferedReader br) throws IOException {
		String readLine;
		List<WorkbenchItem> topicList = new ArrayList<>();
		while ((readLine = br.readLine()) != null && !readLine.equals("#endTopics")) {
			Topic topic = mapper.readValue(readLine, Topic.class);
			topic.instantiateRenderers();
			topicList.add(topic);
		}
		return topicList;
	}

	private List<WorkbenchItem> readInArrowList(ObjectMapper mapper, BufferedReader br) throws IOException {
		String readLine;
		List<WorkbenchItem> arrowList = new ArrayList<>();
		while ((readLine = br.readLine()) != null && !readLine.equals("#endArrows")) {
			Arrow arrow = mapper.readValue(readLine, Arrow.class);
			arrow.instantiateRenderers();
			arrowList.add(arrow);
		}
		return arrowList;
	}

	private List<Template> readInTemplateInstances(ObjectMapper mapper, BufferedReader br)
			throws IOException, JsonParseException, JsonMappingException {
		String readLine;
		List<Template> templateInstances = new ArrayList<>();
		while ((readLine = br.readLine()) != null && !readLine.equals("#endTemplateInstances")) {
			Template instance = mapper.readValue(readLine, Template.class);
			instance.setShapeRenderer(Workspace.getInstance().getShapeRenderer());
			instance.setBatch(Workspace.getInstance().getBatch());
			instance.setFont(Workspace.getInstance().getFont());
			instance.setSkin(Workspace.getInstance().getSkin());
			instance.setStage(Workspace.getInstance().getStage());
			instance.setRuntime(Runtime.getRuntime());
			templateInstances.add(instance);
		}
		return templateInstances;
	}

	private Template fetchTemplate(String uuid, List<Template> templateInstances) {
		for (Template template : templateInstances) {
			if (template.getUuid().toString().equals(uuid)) {
				return template;
			}
		}
		return null;
	}
}
