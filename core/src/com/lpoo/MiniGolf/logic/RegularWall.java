package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class RegularWall extends Wall {

	RegularWall() {
	}
	public RegularWall(Vector2 pos, int height, int width, World w) {
		super(pos, height,width,MiniGolf.obstacleType.regularWall ,w);
	}
}
