package com.onlineinteract.java;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.onlineinteract.core.DiscoveryWorkbench;

public class DesktopLauncher {
	public static void main(String[] args) {
		// Graphics.DisplayMode primaryMode =
		// LwjglApplicationConfiguration.getDesktopDisplayMode();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// config.setFromDisplayMode(primaryMode);

		// float width =
		// LwjglApplicationConfiguration.getDesktopDisplayMode().width
		// * 0.8f;
		// float height =
		// LwjglApplicationConfiguration.getDesktopDisplayMode().height
		// * 0.8f;

		float width = 1560;
		float height = 800;

		config.width = (int) width;
		config.height = (int) height;
		config.resizable = false;
		config.title = "Discovery Workbench";
		config.addIcon("icon-32.png", FileType.Internal);

		// System.out.println("config.width: " + config.width);
		// System.out.println("config.height: " + config.height);

		new LwjglApplication(new DiscoveryWorkbench(config.width, config.height), config);
	}
}
