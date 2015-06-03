package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Obstacle extends Element {
	boolean mudaPos;

	public Obstacle() {
		super();
	}

	public Obstacle(Vector2 pos, float width, float height, World w) {
		super(pos, width, height);
		/*
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		this.body = w.createBody(bodyDef);
		*/
		
	}

	public boolean isMudaPos() {
		return mudaPos;
	}

	public void setMudaPos(boolean mudaPos) {
		this.mudaPos = mudaPos;
	}


}
