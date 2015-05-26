package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.logic.MiniGolf.obstacleType;

public class Floor extends Obstacle {

	public Floor() {
		super();
	}

	public Floor(Vector2 pos, int height, int width, obstacleType type, World w) {
		super(pos, height, width, type,w);
	}
}
