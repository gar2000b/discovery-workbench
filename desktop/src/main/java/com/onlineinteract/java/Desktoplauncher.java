package com.onlineinteract.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.onlineinteract.core.MsOrchestrator;

public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        // float width = LwjglApplicationConfiguration.getDesktopDisplayMode().width * 0.8f;
        // float height = LwjglApplicationConfiguration.getDesktopDisplayMode().height * 0.8f;

        float width = 1560;
        float height = 800;

        config.width = (int) width;
        config.height = (int) height;

        System.out.println("config.width: " + config.width);
        System.out.println("config.height: " + config.height);

        new LwjglApplication(new MsOrchestrator(config.width, config.height), config);
    }
}
