package com.onlineinteract.core.render;

import java.util.ListIterator;

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

//		for (Template templateInstance : workspace.getTemplateInstances()) {
//			templateInstance.draw();
//		}
		
		/**
		 * Render template instances in reverse order
		 */
		for (ListIterator<Template> iterator = workspace.getTemplateInstances()
				.listIterator(workspace.getTemplateInstances().size()); iterator.hasPrevious();) {
			final Template templateInstance = iterator.previous();
			templateInstance.draw();
		}
	}

}
