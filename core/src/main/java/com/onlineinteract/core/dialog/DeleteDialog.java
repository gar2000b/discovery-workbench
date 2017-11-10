package com.onlineinteract.core.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.workbench.Template;
import com.onlineinteract.core.workbench.WorkbenchItem;

public class DeleteDialog extends Dialog {

	private Workspace workspace;
	private WorkbenchItem item;

	public DeleteDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
	}

	public DeleteDialog(String title, Skin skin, Workspace workspace, WorkbenchItem item) {
		super(title, skin);
		this.workspace = workspace;
		this.item = item;
	}

	{
		button("Yes", true).padBottom(10);
		button("No", false).padBottom(10);
	}

	@Override
	protected void result(Object object) {
		if (item instanceof Template)
			if (object.getClass().getSimpleName().equals("Boolean") && object == Boolean.TRUE)
				workspace.getServiceListComponent().removeTemplateInstance((Template) item);
	}

}
