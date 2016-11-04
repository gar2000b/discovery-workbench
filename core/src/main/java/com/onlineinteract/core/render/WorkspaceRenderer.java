package com.onlineinteract.core.render;

import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchItem;

public class WorkspaceRenderer {

    private Workspace workspace;

    public WorkspaceRenderer(Workspace workspace) {
        this.workspace = workspace;
    }

    public void draw() {
        workspace.getCamera().update();

        for (WorkbenchItem workbenchItem : workspace.getWorkbenchItems()) {
            workbenchItem.draw();
        }

        for (Template templateInstance : workspace.getTemplateInstances()) {
            templateInstance.draw();
        }
    }

}
