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

public class OpenDialog extends Dialog {

    private Workspace workspace;
    TextField pathTextField;
    Label pathLabel;

    public OpenDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public OpenDialog(String title, Skin skin, Workspace workspace) {
        super(title, skin);
        this.workspace = workspace;
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
        ServiceList serviceListComponent = workspace.getServiceListComponent();
        ObjectMapper mapper = new ObjectMapper();
        String readLine = "";
        File file = new File(pathTextField.getText());
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            workspace.setInstructions(mapper.readValue(br.readLine(), String.class));
            readLine = br.readLine();
            List<String> orderedList = mapper.readValue(readLine, List.class);
            List<Template> templateInstances = readInTemplateInstances(mapper, br);
            List<Arrow> arrowList = readInArrowList(mapper, br);
            List<Topic> topicList = readInTopicList(mapper, br);
            List<DataStore> dataStoreList = readInDataStoreList(mapper, br);
            br.close();
            serviceListComponent.setTemplateInstances(templateInstances);
            workspace.getDeviceInputProcessor().setTemplateInstances(templateInstances);
            List<Template> orderedServiceList = reconstructOrderedList(orderedList, templateInstances);
            serviceListComponent.setOrderedServiceList(orderedServiceList);
            serviceListComponent.refreshOrderedServiceList();
            workspace.setArrowList(arrowList);
            workspace.setTopicList(topicList);
            workspace.setDataStoreList(dataStoreList);
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

    private List<DataStore> readInDataStoreList(ObjectMapper mapper, BufferedReader br) throws IOException {
    	String readLine;
    	List<DataStore> dataStoreList = new ArrayList<>();
    	while ((readLine = br.readLine()) != null && !readLine.equals("#endDataStores")) {
    		DataStore dataStore = mapper.readValue(readLine, DataStore.class);
    		dataStore.instantiateRenderersAndCamera(workspace.getCamera());
    		dataStoreList.add(dataStore);
    	}
    	return dataStoreList;
    }
    
    private List<Topic> readInTopicList(ObjectMapper mapper, BufferedReader br) throws IOException {
        String readLine;
        List<Topic> topicList = new ArrayList<>();
        while ((readLine = br.readLine()) != null && !readLine.equals("#endTopics")) {
            Topic topic = mapper.readValue(readLine, Topic.class);
            topic.instantiateRenderersAndCamera(workspace.getCamera());
            topicList.add(topic);
        }
        return topicList;
    }

    private List<Arrow> readInArrowList(ObjectMapper mapper, BufferedReader br) throws IOException {
        String readLine;
        List<Arrow> arrowList = new ArrayList<>();
        while ((readLine = br.readLine()) != null && !readLine.equals("#endArrows")) {
            Arrow arrow = mapper.readValue(readLine, Arrow.class);
            arrow.instantiateRenderersAndCamera(workspace.getCamera());
            arrowList.add(arrow);
        }
        return arrowList;
    }

    private List<Template> readInTemplateInstances(ObjectMapper mapper, BufferedReader br) throws IOException, JsonParseException, JsonMappingException {
        String readLine;
        List<Template> templateInstances = new ArrayList<>();
        while ((readLine = br.readLine()) != null && !readLine.equals("#endTemplateInstances")) {
            Template instance = mapper.readValue(readLine, Template.class);
            instance.setWorkspace(workspace);
            instance.setShapeRenderer(workspace.getShapeRenderer());
            instance.setBatch(workspace.getBatch());
            instance.setFont(workspace.getFont());
            instance.setCamera(workspace.getCamera());
            instance.setSkin(workspace.getSkin());
            instance.setStage(workspace.getStage());
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
