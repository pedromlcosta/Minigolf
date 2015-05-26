package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.logic.MiniGolf.obstacleType;

public class Obstacle extends Element {
	boolean mudaPos;
	obstacleType type;

	public Obstacle() {
		super();
	}
	public Obstacle(Vector2 pos, int height, int width, obstacleType type, World w) {
		super(pos, height, width);
		this.type = type;
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
	public obstacleType getType() {
		return type;
	}
	public void setType(obstacleType type) {
		this.type = type;
	}

}
