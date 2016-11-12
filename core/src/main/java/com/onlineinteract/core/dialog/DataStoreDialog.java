package com.onlineinteract.core.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.workbench.DataStore;

public class DataStoreDialog extends Dialog {

	private Workspace workspace;
	TextField nameTextField;
	Label nameLabel;

	public DataStoreDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
	}

	public DataStoreDialog(String title, Skin skin, Workspace workspace, DataStore dataStore) {
		super(title, skin);
		this.workspace = workspace;
	}

	{
		button("Update", true).padBottom(10);
		button("Cancel", false).padBottom(10);
		nameTextField = new TextField("", getSkin());
		nameLabel = new Label("Name: ", getSkin());
		getContentTable().add(nameLabel).padTop(20);
		getContentTable().add(nameTextField).padTop(20).width(200);
		getContentTable().row();
	}

	@Override
	protected void result(Object object) {
		if (object.getClass().getSimpleName().equals("Boolean") && object == Boolean.TRUE) {
			System.out.println("*** Update clicked");
		}
		workspace.setDialogToggleFlag(false);
	}

	public TextField getNameTextField() {
		return nameTextField;
	}

	public void setNameTextField(TextField nameTextField) {
		this.nameTextField = nameTextField;
	}

	public Label getNameLabel() {
		return nameLabel;
	}

	public void setNameLabel(Label nameLabel) {
		this.nameLabel = nameLabel;
	}
}
