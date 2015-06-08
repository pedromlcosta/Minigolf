package com.lpoo.MiniGolf.screens;

import java.io.Serializable;

public class Comando implements Serializable {

	
	private static final long serialVersionUID = 1L;

	int action;
	
	int x, y;
	
	public Comando() {
		
	}
	
	public Comando(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
