package com.lpoo.MiniGolf.logic;

import java.io.Serializable;

import com.badlogic.gdx.graphics.Texture.TextureWrap;
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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lpoo.MiniGolf.geometry.Geometry;
import com.lpoo.MiniGolf.screens.OptionsScreen;

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
	protected float angle;

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public Element() {
		startPos = new Vector2();
		height = 0;
		width = 0;
		this.setVisible(true);
		this.setTouchable(Touchable.enabled);

		this.addCaptureListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent Event, float posX, float posY, int arg2, int button) {
				System.out.println("TOUCHED: " + type);
				return false;
			}

		});

	}

	public Element(Vector2 pos, float width, float height) {
		this.startPos = pos;
		this.width = width;
		this.height = height;
		this.setVisible(true);
		this.setTouchable(Touchable.enabled);

		this.addCaptureListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent Event, float posX, float posY, int arg2, int button) {
				System.out.println("TOUCHED: " + type);
				return false;
			}

		});
	}

	public Element(Vector2 pos, float width, float height, elementType type) {
		this.startPos = pos;
		this.width = width;
		this.height = height;
		this.type = type;
		this.setVisible(true);
		this.setTouchable(Touchable.enabled);

		this.addCaptureListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent Event, float posX, float posY, int arg2, int button) {
				System.out.println("TOUCHED: " + type);
				return false;
			}

		});

	}

	public Element(float width, float height, elementType type) {

		this.width = width;
		this.height = height;
		this.type = type;
		this.setVisible(true);
		this.setTouchable(Touchable.enabled);

		this.addCaptureListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent Event, float posX, float posY, int arg2, int button) {
				System.out.println("TOUCHED: " + type);
				return false;
			}

		});
	}

	public Element(elementType type) {
		this.type = type;
		this.setVisible(true);
		this.setTouchable(Touchable.enabled);

		this.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("CLICKED: " + type);
			}
			// this.addCaptureListener(new InputListener() {
			// @Override
			// public boolean touchDown(InputEvent Event, float posX, float
			// posY, int arg2, int button) {
			// System.out.println("TOUCHED: " + type);
			// return false;
			// }
			//
			//
			// });
		});
	}

	public boolean overlap(Element eleToBeAdded) {

		if (type == Element.elementType.grassFloor)
			return false;

		Shape shapeToAdd = eleToBeAdded.getBody().getFixtureList().get(0).getShape();
		Shape shapeEle = body.getFixtureList().get(0).getShape();

		Vector2 temp = new Vector2(startPos.x - width / 2, startPos.y - height / 2);
		Vector2 temp2 = new Vector2(eleToBeAdded.getPosX() - eleToBeAdded.getWidth() / 2, eleToBeAdded.getPosY() - eleToBeAdded.getHeight() / 2);
		if (shapeToAdd.getType() == Type.Circle && shapeEle.getType() == Type.Circle) {
			return Geometry.overlapCircles((CircleShape) shapeEle, temp, (CircleShape) shapeToAdd, temp2);
		} else if (shapeToAdd.getType() == Type.Polygon && shapeEle.getType() == Type.Polygon) {

			return Geometry.overlapPloygons(this, temp, eleToBeAdded, temp2);
		} else {
			if (shapeToAdd.getType() == Type.Polygon) {
				return Geometry.overlap(this, temp, shapeEle.getRadius(), eleToBeAdded, temp2);
			} else {
				return Geometry.overlap(eleToBeAdded, temp2, shapeToAdd.getRadius(), this, temp);
			}

		}
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
		System.out.println("Width:" + this.width + " Height: " + this.height + " PosX " + this.getPosX() + "  PodY: " + this.getPosY());
	}

	public void destroyBody() {

	}

	public void initializeImage() {
		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		image.setPosition((startPos.x - width / 2f) * MiniGolf.BOX_TO_WORLD, (startPos.y - height / 2f) * MiniGolf.BOX_TO_WORLD);
		image.setSize(width * MiniGolf.BOX_TO_WORLD, height * MiniGolf.BOX_TO_WORLD);
		image.setOriginCenter();
		image.setRotation(angle);
		image.setAlpha(1);
	}

	public void setRadius(float holeRadius) {
	}

	public void setDestination(Vector2 destination) {
	}

	public void changeColor(int colorID) {
	}

	public void createElement(float posInicialX, float posInicialY, float width, float height) {

		this.setStartPos(new Vector2(posInicialX + width / 2, posInicialY + height / 2));
		this.setHeight(height);
		this.setWidth(width);
		this.createBody(MiniGolf.getW());
	}

	public void draw2() {

	}
	

}
