package com.onlineinteract.core.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.workbench.Arrow;
import com.onlineinteract.core.workbench.DataStore;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.Topic;
import com.onlineinteract.core.workbench.WorkbenchItem;

public class DeleteDialog extends Dialog {

	private WorkbenchItem item;

	public DeleteDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
	}

	public DeleteDialog(String title, Skin skin, WorkbenchItem item) {
		super(title, skin);
		this.item = item;
	}

	{
		button("Yes", true).padBottom(10);
		button("No", false).padBottom(10);
	}

	@Override
	protected void result(Object object) {
		if (object.getClass().getSimpleName().equals("Boolean") && object == Boolean.TRUE) {
			if (item instanceof Template)
				Workspace.getInstance().getServiceListComponent().removeTemplateInstance((Template) item);
			if (item instanceof DataStore)
				Workspace.getInstance().getDataStoreList().remove(item);
			if (item instanceof Topic)
				Workspace.getInstance().getTopicList().remove(item);
			if (item instanceof Arrow)
				Workspace.getInstance().getArrowList().remove(item);
		}
	}

}
