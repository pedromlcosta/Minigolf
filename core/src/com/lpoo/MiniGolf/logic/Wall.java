package com.lpoo.MiniGolf.logic;

import com.lpoo.MiniGolf.logic.MiniGolf.obstacleType;

public class Wall extends Obstacle {
	public Wall() {
		super();
	}

	public Wall(Point pos, int height, int width, double aceleracaoX, double aceleracaoY, obstacleType type) {
		super(pos, height, width, aceleracaoX, aceleracaoY, type);
	}

}
