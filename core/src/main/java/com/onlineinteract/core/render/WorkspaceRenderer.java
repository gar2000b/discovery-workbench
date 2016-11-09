package com.onlineinteract.core.render;

import java.util.ListIterator;

import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.workbench.Arrow;
import com.onlineinteract.core.workbench.DataStore;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.Topic;
import com.onlineinteract.core.workbench.WorkbenchItem;

public class WorkspaceRenderer {

    private Workspace workspace;

    public WorkspaceRenderer(Workspace workspace) {
        this.workspace = workspace;
    }

    /**
     * Render all workbench items and template instances
     */
    public void draw() {
        workspace.getCamera().update();
        workspace.getArrow().draw();
        workspace.getTopic().draw();
        workspace.getDataStore().draw();
        for (WorkbenchItem workbenchItem : workspace.getWorkbenchItems()) {
            workbenchItem.draw();
        }

        for (ListIterator<Template> iterator = workspace.getServiceListComponent().getTemplateInstances().listIterator(
                        workspace.getServiceListComponent().getTemplateInstances().size()); iterator.hasPrevious();) {
            iterator.previous().draw();
        }

        for (ListIterator<Arrow> iterator = workspace.getArrowList()
                        .listIterator(workspace.getArrowList().size()); iterator.hasPrevious();) {
            iterator.previous().draw();
        }

        for (ListIterator<Topic> iterator = workspace.getTopicList()
                        .listIterator(workspace.getTopicList().size()); iterator.hasPrevious();) {
            iterator.previous().draw();
        }
        
        for (ListIterator<DataStore> iterator = workspace.getDataStoreList()
        		.listIterator(workspace.getDataStoreList().size()); iterator.hasPrevious();) {
        	iterator.previous().draw();
        }
    }

}
