package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.lpoo.MiniGolf.logic.MiniGolf.obstacleType;

public class Obstacle extends Element {

	boolean mudaPos;
	obstacleType type;

	public Obstacle() {
		super();

	}

	public Obstacle(Vector2 pos, int height, int width, obstacleType type) {
		super(pos, height, width);
		this.type = type;
	}

	public boolean isMudaPos() {
		return mudaPos;
	}

	public void setMudaPos(boolean mudaPos) {
		this.mudaPos = mudaPos;
	}

	public obstacleType getType() {
		return type;
	}

	public void setType(obstacleType type) {
		this.type = type;
	}

}
