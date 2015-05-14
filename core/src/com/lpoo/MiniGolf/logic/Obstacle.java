package com.lpoo.MiniGolf.logic;

import com.lpoo.MiniGolf.logic.MiniGolf.obstacleType;

public class Obstacle extends Element {

	boolean mudaPos;
	obstacleType type;

	public Obstacle() {
		super();

	}

	public Obstacle(Point pos, int height, int width, double aceleracaoX, double aceleracaoY, obstacleType type) {
		super(pos, height, width, aceleracaoX, aceleracaoY);
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
