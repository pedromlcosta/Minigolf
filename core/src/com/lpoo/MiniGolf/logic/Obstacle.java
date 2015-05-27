package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Obstacle extends Element {
	boolean mudaPos;
	elementType type;

	public Obstacle() {
		super();
	}

	public Obstacle(Vector2 pos, int height, int width, elementType type, World w) {
		super(pos, height, width, type);

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		this.body = w.createBody(bodyDef);
		
	}

	public boolean isMudaPos() {
		return mudaPos;
	}

	public void setMudaPos(boolean mudaPos) {
		this.mudaPos = mudaPos;
	}

	public elementType getType() {
		return type;
	}

	public void setType(elementType type) {
		this.type = type;
	}

}
