package com.lpoo.MiniGolf.GUI;

//import java.io.Serializable;

import com.sun.glass.events.KeyEvent;

public class GameOptions/* implements Serializable */{

	int nPLayers;
	int courseWidth;
	int courseHeight;
	int KEY_X_UP;
	int KEY_X_DOWN;
	int KEY_Y_DOWN;
	int KEY_Y_UP;
	int timeMax;
	int tacadasMax;
	float gForce;

	public GameOptions() {
		// Default Values
		nPLayers = 1;
		courseWidth = 1500;
		courseHeight = 1500;
		KEY_X_UP = KeyEvent.VK_RIGHT;
		KEY_X_DOWN = KeyEvent.VK_LEFT;
		KEY_Y_DOWN = KeyEvent.VK_DOWN;
		KEY_Y_UP = KeyEvent.VK_UP;
		timeMax = 60;
		tacadasMax = 20;
		gForce = -9.8f;
	}

	public GameOptions(int nPLayers, int courseWidth, int courseHeight, int KEY_X_UP, int KEY_X_DOWN, int KEY_Y_DOWN, int KEY_Y_UP, float gForce, int tacadasMax, int timeMax) {
		this.nPLayers = nPLayers;
		this.courseWidth = courseWidth;
		this.courseHeight = courseHeight;
		this.KEY_X_UP = KEY_X_UP;
		this.KEY_X_DOWN = KEY_X_DOWN;
		this.KEY_Y_DOWN = KEY_Y_DOWN;
		this.KEY_Y_UP = KEY_Y_UP;
		this.timeMax = timeMax;
		this.tacadasMax = tacadasMax;
		this.gForce = gForce;
	}

	public GameOptions(int nPLayers, int courseWidth, int courseHeight, int KEY_X_UP, int KEY_X_DOWN, int KEY_Y_DOWN, int KEY_Y_UP) {
		// Default Values
		this.nPLayers = nPLayers;
		this.courseWidth = courseWidth;
		this.courseHeight = courseHeight;
		this.KEY_X_UP = KEY_X_UP;
		this.KEY_X_DOWN = KEY_X_DOWN;
		this.KEY_Y_DOWN = KEY_Y_DOWN;
		this.KEY_Y_UP = KEY_Y_UP;
		timeMax = 60;
		tacadasMax = 20;
		gForce = -9.8f;

	}

	public int getTimeMax() {
		return timeMax;
	}

	public void setTimeMax(int timeMax) {
		this.timeMax = timeMax;
	}

	public int getTacadasMax() {
		return tacadasMax;
	}

	public void setTacadasMax(int tacadasMax) {
		this.tacadasMax = tacadasMax;
	}

	public float getgForce() {
		return gForce;
	}

	public void setgForce(float gForce) {
		this.gForce = gForce;
	}

	public int getnPLayers() {
		return nPLayers;
	}

	public void setnPLayers(int nPLayers) {
		this.nPLayers = nPLayers;
	}

	public int getCourseWidth() {
		return courseWidth;
	}

	public void setCourseWidth(int courseWidth) {
		this.courseWidth = courseWidth;
	}

	public int getCourseHeight() {
		return courseHeight;
	}

	public void setCourseHeight(int courseHeight) {
		this.courseHeight = courseHeight;
	}

	public int getKEY_X_UP() {
		return KEY_X_UP;
	}

	public void setKEY_X_UP(int kEY_X_UP) {
		KEY_X_UP = kEY_X_UP;
	}

	public int getKEY_X_DOWN() {
		return KEY_X_DOWN;
	}

	public void setKEY_X_DOWN(int kEY_X_DOWN) {
		KEY_X_DOWN = kEY_X_DOWN;
	}

	public int getKEY_Y_DOWN() {
		return KEY_Y_DOWN;
	}

	public void setKEY_Y_DOWN(int kEY_Y_DOWN) {
		KEY_Y_DOWN = kEY_Y_DOWN;
	}

	public int getKEY_Y_UP() {
		return KEY_Y_UP;
	}

	public void setKEY_Y_UP(int kEY_Y_UP) {
		KEY_Y_UP = kEY_Y_UP;
	}

}
