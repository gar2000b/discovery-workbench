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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ServiceList {

    private Workspace workspace;
    private com.badlogic.gdx.scenes.scene2d.ui.List<Template> serviceList;
    private List<Template> orderedServiceList;
    ScrollPane scrollPane;
    private List<Template> templateInstances = new ArrayList<Template>();

    public ServiceList(Workspace workspace) {
        this.workspace = workspace;
        serviceList = new com.badlogic.gdx.scenes.scene2d.ui.List<Template>(workspace.getSkin());
        orderedServiceList = new ArrayList<>();

        serviceList.setItems(orderedServiceList.toArray(new Template[orderedServiceList.size()]));
        scrollPane = new ScrollPane(serviceList);
        scrollPane.setBounds(10, 10, 200, 660);
        scrollPane.setSmoothScrolling(true);
        scrollPane.setPosition(workspace.getWorldWidth() - 280, 20);
        scrollPane.setTransform(true);
        scrollPane.setScale(1);
        serviceList.setColor(Color.CYAN);
        serviceList.getSelection().setMultiple(true);
        serviceList.getSelection().setRequired(false);
        serviceList.setSelected(null);
        /*
         * serviceList.addListener(new ChangeListener() {
         * 
         * @Override public void changed(ChangeEvent event, Actor actor) { if (serviceList.getSelectedIndex() == 3) { serviceList.clearItems(); orderedServiceList.remove(3);
         * serviceList.setItems(orderedServiceList.toArray(new Template[orderedServiceList.size()])); serviceList.setSelectedIndex(4); } } });
         */

        workspace.getStage().addActor(scrollPane);
        setupServiceListButtons();
    }

    private void setupServiceListButtons() {
        setupToggleProcessingTypeButton();
        setupUpButton();
        setupDownButton();
        setupStartStopAllButton();
        // setupStopAllButton();
    }

    private void setupToggleProcessingTypeButton() {
        Button toggleProcessingTypeButton = new TextButton("Toggle Processing Type", workspace.getSkin());
        toggleProcessingTypeButton.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                return toggleProcessingTypes(event);
            }
        });
        toggleProcessingTypeButton.setPosition(1280, 750);
        toggleProcessingTypeButton.setWidth(200);
        workspace.getStage().addActor(toggleProcessingTypeButton);
    }

    private boolean toggleProcessingTypes(Event event) {
        if (event.toString().equals("touchDown")) {
            System.out.println("Looks like I am being clicked/toggled. " + event.toString());
            for (Template selection : serviceList.getSelection()) {
                if (selection.getProcessingType() == ProcessingType.SEQ)
                    selection.setProcessingType(ProcessingType.PAR);
                else
                    selection.setProcessingType(ProcessingType.SEQ);
            }

            for (Template template : orderedServiceList) {
                System.out.println(template.getProcessingType());
            }
            return true;
        }
        return false;
    }

    private void setupUpButton() {
        Button upButton = new TextButton("^ - up", workspace.getSkin());
        upButton.addListener(new EventListener() {

            @Override
            public boolean handle(Event event) {
                return moveItemsUp(event);
            }
        });
        upButton.setPosition(1280, 720);
        upButton.setWidth(50);
        workspace.getStage().addActor(upButton);
    }

    private boolean moveItemsUp(Event event) {
        if (event.toString().equals("touchDown")) {
            for (Template selection : serviceList.getSelection()) {
                for (int i = 0; i < orderedServiceList.size(); i++) {
                    if (selection.getUuid() == orderedServiceList.get(i).getUuid()) {
                        System.out.println("*** Found");
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
        Button downButton = new TextButton("v - dn", workspace.getSkin());
        downButton.addListener(new EventListener() {

            @Override
            public boolean handle(Event event) {
                return moveItemsDown(event);
            }
        });
        downButton.setPosition(1280, 690);
        downButton.setWidth(50);
        workspace.getStage().addActor(downButton);
    }

    private boolean moveItemsDown(Event event) {
        if (event.toString().equals("touchDown")) {
            for (Template selection : serviceList.getSelection()) {
                for (int i = 0; i < orderedServiceList.size(); i++) {
                    if (selection.getUuid() == orderedServiceList.get(i).getUuid()) {
                        System.out.println("*** Found");
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

    private void setupStartStopAllButton() {
        Button startStopAllButton = new TextButton("Start/Stop All", workspace.getSkin());
        startStopAllButton.addListener(new EventListener() {

            @Override
            public boolean handle(Event event) {
                return startStopAll(event);
            }
        });
        startStopAllButton.setPosition(1340, 690);
        startStopAllButton.setWidth(140);
        startStopAllButton.setHeight(57);
        workspace.getStage().addActor(startStopAllButton);
    }

    /*
     * private void setupStopAllButton() { Button stopAllButton = new TextButton("Stop All", workspace.getSkin()); stopAllButton.addListener(new EventListener() {
     * 
     * @Override public boolean handle(Event event) { return startStopAll(event); } }); stopAllButton.setPosition(1340, 690); stopAllButton.setWidth(140); workspace.getStage().addActor(stopAllButton);
     * }
     */

    private boolean startStopAll(Event event) {
        if (event.toString().equals("touchDown")) {
            System.out.println("* Start all services");
            new Thread(() -> {
                for (Template template : orderedServiceList) {
                    if (template.getServiceStatus() == ServiceStatus.RUNNING) {
                        template.startStopService();
                        continue;
                    }

                    if (template.getServiceStatus() == ServiceStatus.SHUTDOWN) {
                        template.startStopService();
                        while (template.getServiceStatus() != ServiceStatus.RUNNING && template.getProcessingType() == ProcessingType.SEQ) {
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

    public void addTemplateInstance(Template instance) {
        templateInstances.add(instance);
        if (instance.getType() == TemplateType.MICROSERVICE && instance.getLabel().equals("µicroservice"))
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

    public List<Template> getTemplateInstances() {
        return templateInstances;
    }
}
