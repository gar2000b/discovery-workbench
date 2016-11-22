package com.onlineinteract.core.dialog;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.onlineinteract.core.Workspace;
import com.onlineinteract.core.type.TemplateType;
import com.onlineinteract.core.workbench.Template;

public class ServiceDialog extends Dialog {

	private Workspace workspace;
	private Template template;
	TextField labelTextField;
	TextField startupCommandTextField;
	TextField shutdown1CommandTextField;
	TextField shutdown2CommandTextField;
	TextField runningClauseTextField;
	TextField servicePortNoTextField;
	Label serviceNameLabel;
	Label startupCommandLabel;
	Label shutdown1CommandLabel;
	Label shutdown2CommandLabel;
	Label runningLabel;
	Label servicePortNoLabel;

	public ServiceDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
	}

	public ServiceDialog(String title, Skin skin, Workspace workspace, Template template) {
		super(title, skin);
		this.workspace = workspace;
		this.template = template;

		button("Update", true).padBottom(10);
		button("Cancel", false).padBottom(10);
		key(Input.Keys.ENTER, true);
		key(Input.Keys.ESCAPE, false);

		serviceNameLabel = new Label("Service Name: ", getSkin());
		labelTextField = new TextField("", getSkin());
		startupCommandTextField = new TextField("", getSkin());
		getContentTable().row();
		getContentTable().add(serviceNameLabel).padTop(20);
		getContentTable().add(labelTextField).padTop(20).width(200);

		startupCommandLabel = new Label("Startup Command: ", getSkin());
		getContentTable().row();
		getContentTable().add(startupCommandLabel).padBottom(10);
		getContentTable().add(startupCommandTextField).padBottom(10).width(200);

		System.out.println(template.getType());
		if (template.getType() != TemplateType.SCRIPT) {
			runningLabel = new Label("Running Clause: ", getSkin());
			runningClauseTextField = new TextField("", getSkin());
			getContentTable().row();
			getContentTable().add(runningLabel).padBottom(10);
			getContentTable().add(runningClauseTextField).padBottom(10).width(200);
			shutdown1CommandTextField = new TextField("", getSkin());

			shutdown1CommandLabel = new Label("Shutdown Command:", getSkin());
			getContentTable().row();
			getContentTable().add(shutdown1CommandLabel).padBottom(10);
			getContentTable().add(shutdown1CommandTextField).padBottom(10).width(200);
			shutdown2CommandTextField = new TextField("", getSkin());

			shutdown2CommandLabel = new Label("Shutdown Command 2:", getSkin());
			getContentTable().row();
			getContentTable().add(shutdown2CommandLabel).padBottom(10);
			getContentTable().add(shutdown2CommandTextField).padBottom(10).width(200);

			servicePortNoLabel = new Label("Port Number: ", getSkin());
			servicePortNoTextField = new TextField("", getSkin());
			getContentTable().row();
			getContentTable().add(servicePortNoLabel).padBottom(10);
			getContentTable().add(servicePortNoTextField).padBottom(10).width(200);
		}
	}

	public ServiceDialog(String title, WindowStyle windowStyle) {
		super(title, windowStyle);
	}

	{

	}

	@Override
	protected void result(Object object) {
		if (object.getClass().getSimpleName().equals("Boolean") && object == Boolean.TRUE) {
			template.setLabel(labelTextField.getText());
			template.setStartupCommand(startupCommandTextField.getText());
			if (template.getType() != TemplateType.SCRIPT) {
				template.setShutdownCommand(shutdown1CommandTextField.getText());
				template.setShutdown2Command(shutdown2CommandTextField.getText());
				template.setRunningClause(runningClauseTextField.getText());
				template.setServicePortNo(servicePortNoTextField.getText());
			}
			workspace.getServiceListComponent().updateServiceList(template);
		}

		workspace.setDialogToggleFlag(false);
	}

	public TextField getLabelTextField() {
		return labelTextField;
	}

	public TextField getStartupCommandTextField() {
		return startupCommandTextField;
	}

	public TextField getShutdown1CommandTextField() {
		return shutdown1CommandTextField;
	}

	public TextField getShutdown2CommandTextField() {
		return shutdown2CommandTextField;
	}

	public TextField getRunningClauseTextField() {
		return runningClauseTextField;
	}

	public Label getServicePortNoLabel() {
		return servicePortNoLabel;
	}

	public TextField getServicePortNoTextField() {
		return servicePortNoTextField;
	}
}
