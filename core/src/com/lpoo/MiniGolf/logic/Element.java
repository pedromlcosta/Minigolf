package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Element {

	public enum elementType {
		nothing, hole, ball, glueWall, waterFloor, iceFloor, regularWall, sandFloor, normalSquareWall, normalTriangularWall, bonceWall, speedWall, teleporter, squareOne, grassFloor, accFloor, accWall, startSquareWall, startTriangularWall,
	};

	protected Body body;
	protected Vector2 oldPos;
	protected float width;
	protected float height;
	protected elementType type;
	

	protected Sprite image;

	public Element() {
		oldPos = new Vector2();
		height = 0;
		width = 0;

	}

	public Element(Vector2 pos, float width, float height) {
		this.oldPos = pos;
		this.width = width;
		this.height = height;
	}

	public Element(Vector2 pos, float width, float height, elementType type) {
		this.oldPos = pos;
		this.width = width;
		this.height = height;
		this.type = type;
	}

	public Element(float width, float height, elementType type) {

		this.width = width;
		this.height = height;
		this.type = type;
	}

	public Element(elementType type) {
		this.type = type;
	}

	public void draw() {
		// System.out.println("Walrus");
		// System.out.println(body.getPosition().x + " " + body.getPosition().y
		// + " " + width*MiniGolf.BOX_TO_WORLD + " " +
		// height*MiniGolf.BOX_TO_WORLD);
		// MiniGolf.batch.draw(image, body.getPosition().x,
		// body.getPosition().y, width*MiniGolf.BOX_TO_WORLD ,
		// height*MiniGolf.BOX_TO_WORLD );
	}

	public Sprite getImage() {
		return image;
	}

	public void setImage(Sprite image) {
		this.image = image;
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

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
	
	public elementType getType() {
		return type;
	}

	public void setType(elementType type) {
		this.type = type;
	}
	
	public void createBody(World w){
		
	}
	
	public void destroyBody(){
		
	}

}
