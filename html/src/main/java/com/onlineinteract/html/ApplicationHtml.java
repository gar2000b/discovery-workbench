package com.onlineinteract.html;

import com.onlineinteract.core.Application;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class ApplicationHtml extends GwtApplication {
	@Override
	public ApplicationListener getApplicationListener () {
		return new Application();
	}
	
	@Override
	public GwtApplicationConfiguration getConfig () {
		return new GwtApplicationConfiguration(480, 320);
	}

	@Override
	public ApplicationListener createApplicationListener() {
		// TODO Auto-generated method stub
		return null;
	}
}
