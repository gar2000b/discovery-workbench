package com.onlineinteract.core.render;

import java.util.ListIterator;

import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchItem;
import com.onlineinteract.core.workbench.WorkbenchRenderer;

public class WorkspaceRenderer {

	public WorkspaceRenderer() {
	}

	/**
	 * Render all workbench items and template instances
	 */
	public void draw() {
		Workspace.getInstance().getCamera().update();
		Workspace.getInstance().getArrow().draw();
		Workspace.getInstance().getTopic().draw();
		Workspace.getInstance().getDataStore().draw();
		for (WorkbenchRenderer workbenchRenderItem : Workspace.getInstance().getWorkbenchItems()) {
			workbenchRenderItem.draw();
		}

		for (ListIterator<Template> iterator = Workspace.getInstance().getServiceListComponent().getTemplateInstances()
				.listIterator(Workspace.getInstance().getServiceListComponent().getTemplateInstances().size()); iterator
						.hasPrevious();) {
			iterator.previous().draw();
		}

		for (ListIterator<WorkbenchItem> iterator = Workspace.getInstance().getArrowList()
				.listIterator(Workspace.getInstance().getArrowList().size()); iterator.hasPrevious();) {
			iterator.previous().draw();
		}

		for (ListIterator<WorkbenchItem> iterator = Workspace.getInstance().getTopicList()
				.listIterator(Workspace.getInstance().getTopicList().size()); iterator.hasPrevious();) {
			iterator.previous().draw();
		}

		for (ListIterator<WorkbenchItem> iterator = Workspace.getInstance().getDataStoreList()
				.listIterator(Workspace.getInstance().getDataStoreList().size()); iterator.hasPrevious();) {
			iterator.previous().draw();
		}
	}

}
