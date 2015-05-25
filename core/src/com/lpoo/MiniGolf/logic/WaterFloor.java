package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class WaterFloor extends Element {
	public WaterFloor(Vector2 pos, int height, int width, World w) {
		super(pos, height, width);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		this.body = w.createBody(bodyDef);
	}
}
