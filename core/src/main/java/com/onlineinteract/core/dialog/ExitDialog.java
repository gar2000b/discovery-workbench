package com.onlineinteract.core.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.onlineinteract.core.Workspace;

public class ExitDialog extends Dialog {

	private Workspace workspace;

	public ExitDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
	}

	public ExitDialog(String title, Skin skin, Workspace workspace) {
		super(title, skin);
		this.workspace = workspace;
	}

	public ExitDialog(String title, WindowStyle windowStyle) {
		super(title, windowStyle);
	}
	
	{
		text("Do you really want to leave?");
		button("Yes");
		button("No");
	}
	
	@Override
	protected void result(Object object) {
		System.out.println("Some result here");
		Gdx.input.setInputProcessor(workspace.getDeviceInputProcessor());
	}
}
