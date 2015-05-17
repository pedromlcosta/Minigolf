package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Ball extends Element {
	int number;

	public Ball() {
		super();
		number = MiniGolf.ballN;
		MiniGolf.ballN++;
	}

	public Ball(Vector2 pos, int height, int width, World w) {
		super(pos, height, width);
		number = MiniGolf.ballN;
		MiniGolf.ballN++;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		this.body = w.createBody(bodyDef);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
