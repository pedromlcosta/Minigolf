package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class GrassFloor extends Floor {
	public GrassFloor(Vector2 pos, int height, int width, World w) {

		super(pos, height, width, MiniGolf.obstacleType.regularFloor, w);

		PolygonShape square = new PolygonShape();
		square.setAsBox(width, height);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = square;
		fixDef.isSensor = true;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(pos);
		body = w.createBody(bodyDef);
		this.body.createFixture(fixDef);

	}
}
