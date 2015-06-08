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
import com.lpoo.MiniGolf.geometry.Geometry;
import com.lpoo.MiniGolf.screens.EditorScreen;

/**
 * The Class Element.
 */
public class Element extends Actor implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * The Enum elementType.
	 */
	public enum elementType {

		/** The nothing. */
		nothing,
		/** The hole. */
		hole,
		/** The ball. */
		ball,
		/** The glue wall. */
		glueWall,
		/** The water floor. */
		waterFloor,
		/** The ice floor. */
		iceFloor,
		/** The illusion wall. */
		illusionWall,
		/** The regular wall. */
		regularWall,
		/** The sand floor. */
		sandFloor,
		/** The bouncy wall. */
		bouncyWall,
		/** The void floor. */
		voidFloor,
		/** The teleporter. */
		teleporter,
		/** The accelerator floor. */
		acceleratorFloor,
		/** The square one. */
		squareOne,
		/** The grass floor. */
		grassFloor
	};

	/** The body. */
	transient protected Body body;

	/** The start pos. */
	protected Vector2 startPos;

	/** The width. */
	protected float width;

	/** The height. */
	protected float height;

	/** The type. */
	protected elementType type;

	/** The image. */
	transient protected Sprite image;

	/** The angle. */
	protected float angle = 0; // in radians

	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * Sets the angle.
	 *
	 * @param angle
	 *            the new angle
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

	/**
	 * Instantiates a new element.
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.scenes.scene2d.Actor#hit(float, float, boolean)
	 */
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if ((touchable && this.getTouchable() != Touchable.enabled) || type == elementType.grassFloor) {
			EditorScreen.middleMousebutton = false;
			EditorScreen.rKeyPressed = false;
			return null;
		}

		float newX = x / MiniGolf.BOX_TO_WORLD;
		float newY = y / MiniGolf.BOX_TO_WORLD;
		if (newX >= (startPos.x - width / 2) && newX < startPos.x + width / 2 && newY >= (startPos.y - height / 2) && newY < (startPos.y + height / 2)) {
			if (EditorScreen.middleMousebutton) {

				System.out.println("TOUCHED: " + type);
				EditorScreen.removeFromArrayElement(this);
				this.destroyBody();
				this.remove();
				EditorScreen.middleMousebutton = false;
				return this;
			} else if (type == elementType.acceleratorFloor) {
				if (EditorScreen.rKeyPressed) {

					Vector2 bodyPos = body.getPosition();

					angle = body.getAngle() + (90 * Geometry.DEG_TO_RAD);
					angle = (float) (angle % (2 * Math.PI)); // Between 0 and
																// 2*pi

					// TODO tentar converter angulo
					image.setOriginCenter();
					image.rotate90(false);
					body.setTransform(bodyPos.x, bodyPos.y, angle);
					EditorScreen.rKeyPressed = false;
				}
			}
		}
		return null;
	}

	/**
	 * Instantiates a new element.
	 *
	 * @param pos
	 *            the pos
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
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

	/**
	 * Instantiates a new element.
	 *
	 * @param pos
	 *            the pos
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param type
	 *            the type
	 */
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

	/**
	 * Instantiates a new element.
	 *
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param type
	 *            the type
	 */
	public Element(float width, float height, elementType type) {

		this.width = width;
		this.height = height;
		this.type = type;
		this.setVisible(true);
		this.setTouchable(Touchable.enabled);
	}

	/**
	 * Instantiates a new element.
	 *
	 * @param type
	 *            the type
	 */
	public Element(elementType type) {
		this.type = type;
		this.setVisible(true);
		this.setTouchable(Touchable.enabled);

	}

	/**
	 * Overlap.
	 *
	 * @param eleToBeAdded
	 *            the ele to be added
	 * @return true, if successful
	 */
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

	/**
	 * Draw.
	 */
	public void draw() {

		image.draw(MiniGolf.batch);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.badlogic.gdx.scenes.scene2d.Actor#draw(com.badlogic.gdx.graphics.
	 * g2d.Batch, float)
	 */
	@Override
	public void draw(Batch batch, float parentAlfa) {
		image.draw(batch);
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public Sprite getImage() {
		return image;
	}

	/**
	 * Sets the image.
	 *
	 * @param image
	 *            the new image
	 */
	public void setImage(Sprite image) {
		this.image = image;
	}

	/**
	 * Gets the pos x.
	 *
	 * @return the pos x
	 */
	public float getPosX() {
		return body.getPosition().x;
	}

	/**
	 * Gets the body.
	 *
	 * @return the body
	 */
	public Body getBody() {
		return body;
	}

	/**
	 * Sets the body.
	 *
	 * @param body
	 *            the new body
	 */
	public void setBody(Body body) {
		this.body = body;
	}

	/**
	 * Gets the pos y.
	 *
	 * @return the pos y
	 */
	public float getPosY() {
		return body.getPosition().y;
	}

	/**
	 * Gets the start pos x.
	 *
	 * @return the start pos x
	 */
	public float getStartPosX() {
		return startPos.x;
	}

	/**
	 * Gets the start pos y.
	 *
	 * @return the start pos y
	 */
	public float getStartPosY() {
		return startPos.y;
	}

	/**
	 * Sets the start pos x.
	 *
	 * @param x
	 *            the new start pos x
	 */
	public void setStartPosX(float x) {
		this.startPos.x = x;
	}

	/**
	 * Gets the pos.
	 *
	 * @return the pos
	 */
	public Vector2 getPos() {
		return body.getPosition();
	}

	/**
	 * Gets the start pos.
	 *
	 * @return the start pos
	 */
	public Vector2 getStartPos() {
		return startPos;
	}

	/**
	 * Sets the start pos.
	 *
	 * @param Start
	 *            the new start pos
	 */
	public void setStartPos(Vector2 Start) {
		this.startPos = Start;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.scenes.scene2d.Actor#getHeight()
	 */
	public float getHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.scenes.scene2d.Actor#setHeight(float)
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.scenes.scene2d.Actor#getWidth()
	 */
	public float getWidth() {
		return width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.scenes.scene2d.Actor#setWidth(float)
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public elementType getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the new type
	 */
	public void setType(elementType type) {
		this.type = type;
	}

	/**
	 * Creates the body.
	 *
	 * @param w
	 *            the w
	 */
	public void createBody(World w) {
	}

	/**
	 * Destroy body.
	 */
	public void destroyBody() {

	}

	/**
	 * Initialize image.
	 */
	public void initializeImage() {
		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		image.setPosition((startPos.x - width / 2f) * MiniGolf.BOX_TO_WORLD, (startPos.y - height / 2f) * MiniGolf.BOX_TO_WORLD);
		image.setSize(width * MiniGolf.BOX_TO_WORLD, height * MiniGolf.BOX_TO_WORLD);
		image.setOriginCenter();
		image.setRotation(angle / Geometry.DEG_TO_RAD); // -> radians to degree
		image.setAlpha(1);
	}

	/**
	 * Sets the radius.
	 *
	 * @param holeRadius
	 *            the new radius
	 */
	public void setRadius(float holeRadius) {
	}

	/**
	 * Sets the destination.
	 *
	 * @param destination
	 *            the new destination
	 */
	public void setDestination(Vector2 destination) {
	}

	/**
	 * Change color.
	 *
	 * @param colorID
	 *            the color id
	 */
	public void changeColor(int colorID) {
	}

	/**
	 * Creates the element.
	 *
	 * @param posInicialX
	 *            the pos inicial x
	 * @param posInicialY
	 *            the pos inicial y
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void createElement(float posInicialX, float posInicialY, float width, float height) {

		this.setStartPos(new Vector2(posInicialX + width / 2, posInicialY + height / 2));
		this.setHeight(height);
		this.setWidth(width);
		this.createBody(MiniGolf.getW());
	}

	/**
	 * Initialize dest image.
	 */
	public void initializeDestImage() {

	}
}
