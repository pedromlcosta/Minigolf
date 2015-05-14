package com.lpoo.MiniGolf.logic;

import com.lpoo.MiniGolf.logic.MiniGolf.obstacleType;

public class Obstacle extends Element {

	boolean mudaPos;
	obstacleType Type;

	public Obstacle() {
		super();

	}

	public Obstacle(Point pos, int height, int width, double aceleracaoX, double aceleracaoY) {
		super(pos, height, width, aceleracaoX, aceleracaoY);
		Type = MiniGolf.obstacleType.accFloor;
	}

}
