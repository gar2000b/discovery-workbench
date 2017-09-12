package com.onlineinteract.core.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ExitDialog extends Dialog {

	public ExitDialog(String title, Skin skin, String windowStyleName) {
		super(title, skin, windowStyleName);
	}

	public ExitDialog(String title, Skin skin) {
		super(title, skin);
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
	}
}
