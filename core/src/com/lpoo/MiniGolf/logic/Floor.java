package com.lpoo.MiniGolf.logic;

import com.lpoo.MiniGolf.logic.MiniGolf.obstacleType;

public class Floor extends Obstacle {

	public Floor() {
		super();
	}

	public Floor(Point pos, int height, int width, double aceleracaoX, double aceleracaoY, obstacleType type) {
		super(pos, height, width, aceleracaoX, aceleracaoY, type);
	}

}
