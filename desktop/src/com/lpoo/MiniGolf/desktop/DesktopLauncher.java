package com.lpoo.MiniGolf.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lpoo.MiniGolf.logic.MiniGolf;

public class DesktopLauncher {
	public static void main(String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		/*
		config.addIcon("golfWindowsLinux.png", FileType.Local);
		config.addIcon("golfMac.png", FileType.Local);
		config.addIcon("golfWindowsLinux.png", FileType.Local);
		config.addIcon("golfWindowsLinux.png", FileType.Local);
	*/
		config.width = MiniGolf.WIDTH; // sets window width
		config.height = MiniGolf.HEIGHT; // sets window height

		new LwjglApplication(new MiniGolf(), config);
	}
}
