package com.onlineinteract.core.render;

import java.util.ListIterator;

import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchItem;
import com.onlineinteract.core.workbench.WorkbenchRenderer;

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
        for (WorkbenchRenderer workbenchRenderItem : workspace.getWorkbenchItems()) {
        	workbenchRenderItem.draw();
        }

        for (ListIterator<Template> iterator = workspace.getServiceListComponent().getTemplateInstances().listIterator(
                        workspace.getServiceListComponent().getTemplateInstances().size()); iterator.hasPrevious();) {
            iterator.previous().draw();
        }

        for (ListIterator<WorkbenchItem> iterator = workspace.getArrowList()
                        .listIterator(workspace.getArrowList().size()); iterator.hasPrevious();) {
            iterator.previous().draw();
        }

        for (ListIterator<WorkbenchItem> iterator = workspace.getTopicList()
                        .listIterator(workspace.getTopicList().size()); iterator.hasPrevious();) {
            iterator.previous().draw();
        }
        
        for (ListIterator<WorkbenchItem> iterator = workspace.getDataStoreList()
        		.listIterator(workspace.getDataStoreList().size()); iterator.hasPrevious();) {
        	iterator.previous().draw();
        }
    }

}
