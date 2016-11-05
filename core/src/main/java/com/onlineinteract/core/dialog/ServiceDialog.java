package com.onlineinteract.core.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.workbench.Template;

public class ServiceDialog extends Dialog {

	private Workspace workspace;
	private Template template;
	TextField textField;

	public ServiceDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
	}

	public ServiceDialog(String title, Skin skin, Workspace workspace, Template template) {
		super(title, skin);
		this.workspace = workspace;
		this.template = template;
	}

	public ServiceDialog(String title, WindowStyle windowStyle) {
		super(title, windowStyle);
	}

	{
		setModal(true);
		setResizable(true);
		setPosition(300, 300);
		textField = new TextField("", getSkin());
		text("Service Configuration");
		button("Update", true);
		button("Cancel", false);
		key(Input.Keys.ENTER, true);
		key(Input.Keys.ESCAPE, false);
		getContentTable().row();
		getContentTable().add(textField).width(400);
	}
	
//	@Override
//    public float getPrefWidth() {
//        return 600;
//    }
//
//    @Override
//    public float getPrefHeight() {
//        return 600;
//    }

	@Override
	protected void result(Object object) {
		if (object.getClass().getSimpleName().equals("Boolean") && object == Boolean.TRUE)
			template.setLabel(textField.getText());

		Gdx.input.setInputProcessor(workspace.getDeviceInputProcessor());
	}

	public TextField getTextField() {
		return textField;
	}
}
