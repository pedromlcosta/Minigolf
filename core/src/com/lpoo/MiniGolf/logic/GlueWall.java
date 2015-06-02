package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GlueWall extends Obstacle {
	public GlueWall(Vector2 pos, int height, int width, World w) {
		super(pos, height, width, w);

	}
}
