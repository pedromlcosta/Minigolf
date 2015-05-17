package com.lpoo.MiniGolf.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lpoo.MiniGolf.logic.MiniGolf;

public class DesktopLauncher {
	public static void main(String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// switch (Gdx.app.getType()) {
		// // case Android: {
		// // break;
		// // }
		// // case Desktop: {
		config.addIcon("golfWindowsLinux.png", FileType.Local);
		// // break;
		// // }
		// // case iOS: {
		config.addIcon("golfMac.png", FileType.Local);
		// // break;
		// // }
		// default: {
		config.addIcon("golfWindowsLinux.png", FileType.Local);
		// break;
		// }
		// }
		config.addIcon("golfWindowsLinux.png", FileType.Local);
		new LwjglApplication(new MiniGolf(), config);
	}
}
