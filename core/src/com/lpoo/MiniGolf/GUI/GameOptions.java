package com.lpoo.MiniGolf.GUI;

import java.io.Serializable;

import com.sun.glass.events.KeyEvent;

public class GameOptions implements Serializable{

	int dragonsSize;
	int dartsSize;
	int mazeSize;
	int mazeChoice;
	int dragonChoice;
	int KEY_UP;
	int KEY_DOWN;
	int KEY_LEFT;
	int KEY_RIGHT;

	public GameOptions() {
		// Default Values
		dragonChoice = 1;
		mazeChoice = 1;
		dartsSize = 0;
		dragonsSize = 1;
		mazeSize = 11;

		KEY_UP = KeyEvent.VK_UP;
		KEY_DOWN = KeyEvent.VK_DOWN;
		KEY_LEFT = KeyEvent.VK_LEFT;
		KEY_RIGHT = KeyEvent.VK_RIGHT;
	}

}
