package com.lpoo.MiniGolf.logic;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
 
import com.lpoo.MiniGolf.geometry.Geometry;
 

public class Element extends Actor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum elementType {
		nothing, hole, ball, glueWall, waterFloor, iceFloor, illusionWall, regularWall, sandFloor, bouncyWall, voidFloor, teleporter, acceleratorFloor, squareOne, grassFloor
	};

	transient protected Body body;
	protected Vector2 startPos;
	protected float width;
	protected float height;
	protected elementType type;

	transient protected Sprite image;

	public boolean overlap(Element eleToBeAdded) {

		if (type == Element.elementType.grassFloor)
			return false;

		Shape shapeToAdd = eleToBeAdded.getBody().getFixtureList().get(0).getShape();
		Shape shapeEle = body.getFixtureList().get(0).getShape();

		if (shapeToAdd.getType() == Type.Circle && shapeEle.getType() == Type.Circle) {
			return Geometry.overlapCircles((CircleShape) shapeEle, startPos, (CircleShape) shapeToAdd, eleToBeAdded.getStartPos());
		} else if (shapeToAdd.getType() == Type.Polygon && shapeEle.getType() == Type.Polygon) {

			return Geometry.overlapPloygons(this, startPos, eleToBeAdded, eleToBeAdded.getStartPos());
		} else {
			if (shapeToAdd.getType() == Type.Polygon) {
				return Geometry.overlap(this, startPos, shapeEle.getRadius(), eleToBeAdded, eleToBeAdded.getStartPos());
			} else {
				return Geometry.overlap(eleToBeAdded, eleToBeAdded.getStartPos(), shapeToAdd.getRadius(), this, startPos);
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

	public float getStartPosX() {
		return startPos.x;
	}

	public float getStartPosY() {
		return startPos.y;
	}

	public void setStartPosX(float x) {
		this.startPos.x = x;
	}

	public Vector2 getPos() {
		return body.getPosition();
	}

	public Vector2 getStartPos() {
		return startPos;
	}

	public void setStartPos(Vector2 Start) {
		this.startPos = Start;
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
	}

	public void setDestination(Vector2 destination) {
	}

	public void changeColor(int colorID) {
	}

	public void createElement(float posInicialX, float posInicialY, float width, float height) {

		this.setStartPos(new Vector2(posInicialX, posInicialY));
		this.setHeight(height);
		this.setWidth(width);
		this.createBody(MiniGolf.getW());
	}

}
