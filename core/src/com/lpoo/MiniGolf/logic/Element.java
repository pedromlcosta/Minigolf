package com.lpoo.MiniGolf.logic;

import geometry.Geometrey;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Element extends Actor {

	public enum elementType {
		nothing, hole, ball, glueWall, waterFloor, iceFloor, illusionWall, regularWall, sandFloor, bouncyWall, voidFloor, teleporter, squareOne, grassFloor
	};

	protected Body body;
	protected Vector2 startPos;
	protected float width;
	protected float height;
	protected elementType type;

	protected Sprite image;

	public boolean overlap(Element eleToBeAdded) {

		if (type == Element.elementType.grassFloor)
			return false;

		Body bodyToAdd = eleToBeAdded.getBody();

		Shape shapeToAdd = eleToBeAdded.getBody().getFixtureList().get(0).getShape();
		Shape shapeEle = body.getFixtureList().get(0).getShape();

		if (shapeToAdd.getType() == Type.Circle && shapeEle.getType() == Type.Circle) {
			return Geometrey.overlapCircles((CircleShape) shapeEle, startPos, (CircleShape) shapeToAdd, eleToBeAdded.getStartPos());
		} else if (shapeToAdd.getType() == Type.Polygon && shapeEle.getType() == Type.Polygon) {
			if (type == eleToBeAdded.getType())
				return false;
			else
				return Geometrey.overlapPloygons(this, startPos, eleToBeAdded, eleToBeAdded.getStartPos());
		} else {
			float radius;
			if (shapeToAdd.getType() == Type.Polygon) {
				return Geometrey.overlap(eleToBeAdded, eleToBeAdded.getStartPos(), shapeToAdd.getRadius(), this, startPos);
			} else {
				return Geometrey.overlap(this, startPos, shapeEle.getRadius(), eleToBeAdded, eleToBeAdded.getStartPos());
			}

		}
	}

	public Element() {
		startPos = new Vector2();
		height = 0;
		width = 0;

	}

	public Element(Vector2 pos, float width, float height) {
		this.startPos = pos;
		this.width = width;
		this.height = height;
	}

	public Element(Vector2 pos, float width, float height, elementType type) {
		this.startPos = pos;
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

	@Override
	public void draw(Batch batch, float parentAlfa) {
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
		return startPos.x;
	}

	public float getOldPosY() {
		return startPos.y;
	}

	public void setOldPosX(float x) {
		this.startPos.x = x;
	}

	public Vector2 getPos() {
		return body.getPosition();
	}

	public Vector2 getStartPos() {
		return startPos;
	}

	public void setStartPos(Vector2 oldPos) {
		this.startPos = oldPos;
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

	public void createBody(World w) {

	}

	public void destroyBody() {

	}

	public void setRadius(float holeRadius) {
		// System.out.println("here Rad");
		// TODO Auto-generated method stub

	}

}
