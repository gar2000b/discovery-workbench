package com.onlineinteract.core.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.workbench.Template;

public class ServiceDialog extends Dialog {

	private Workspace workspace;
	private Template template;
	TextField labelTextField;
	TextField startupCommandTextField;
	TextField runningTextField;
	Label serviceNameLabel;
	Label startupCommandLabel;
	Label runningLabel;

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
		labelTextField = new TextField("", getSkin());
		startupCommandTextField = new TextField("", getSkin());
		runningTextField = new TextField("", getSkin());
		serviceNameLabel = new Label("Service Name: ", getSkin());
		startupCommandLabel = new Label("Startup Command: ", getSkin());
		runningLabel = new Label("Running Clause: ", getSkin());
		button("Update", true).padBottom(10);
		button("Cancel", false).padBottom(10);
		key(Input.Keys.ENTER, true);
		key(Input.Keys.ESCAPE, false);
		getContentTable().row();
		getContentTable().add(serviceNameLabel).padTop(20);
		getContentTable().add(labelTextField).padTop(20).width(200);
		getContentTable().row();
		getContentTable().add(startupCommandLabel).padBottom(10);
		getContentTable().add(startupCommandTextField).padBottom(10).width(200);
		getContentTable().row();
		getContentTable().add(runningLabel).padBottom(10);
		getContentTable().add(runningTextField).padBottom(10).width(200);
	}

	@Override
	protected void result(Object object) {
		if (object.getClass().getSimpleName().equals("Boolean") && object == Boolean.TRUE) {
			template.setLabel(labelTextField.getText());
			template.setStartupCommand(startupCommandTextField.getText());
		}

		Gdx.input.setInputProcessor(workspace.getDeviceInputProcessor());
	}

	public TextField getLabelTextField() {
		return labelTextField;
	}

	public TextField getStartupCommandTextField() {
		return startupCommandTextField;
	}
}
