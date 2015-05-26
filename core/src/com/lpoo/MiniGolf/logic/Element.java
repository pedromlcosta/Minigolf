package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Element    {
	Body body;
	Vector2 oldPos;
	int height;
	int width;
	Texture image;

	public Element() {
		oldPos = new Vector2();
		height = 0;
		width = 0;

	}

	public Texture getImage() {
		return image;
	}

	public void setImage(Texture image) {
		this.image = image;
	}

	public Element(Vector2 pos, int height, int width) {
		this.oldPos = pos;
		this.height = height;
		this.width = width;
	}

	public float getPosX() {
		return body.getPosition().x;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public float getPosY() {
		return body.getPosition().y;
	}

	public float getOldPosX() {
		return oldPos.x;
	}

	public float getOldPosY() {
		return oldPos.y;
	}

	public void setOldPosX(float x) {
		this.oldPos.x = x;
	}

	public Vector2 getPos() {
		return body.getPosition();
	}

	public Vector2 getOldPos() {
		return oldPos;
	}

	public void setOldPos(Vector2 oldPos) {
		this.oldPos = oldPos;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}



}
