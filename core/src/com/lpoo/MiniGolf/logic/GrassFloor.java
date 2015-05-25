package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GrassFloor extends Floor {
	public GrassFloor(Vector2 pos, int height, int width, World w) {
		//see type
		super(pos, height, width,MiniGolf.obstacleType.regularFloor,w);

	 
	}
}
