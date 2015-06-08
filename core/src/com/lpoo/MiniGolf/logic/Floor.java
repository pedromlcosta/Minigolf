package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.geometry.Geometry;

/**
 * The Class Floor, subclass of Element, representing the elements which usually
 * have sensors in them and have some kind of drag or acceleration.
 */
public class Floor extends Element {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant GRASS_DRAG. Drag value for the type "grassFloor". */
	public static final float GRASS_DRAG = 1.5f;

	/** The Constant SAND_DRAG. Drag value for the type "sandFloor". */
	public static final float SAND_DRAG = 6.0f;

	/** The Constant ICE_DRAG. Drag value for the type "iceFloor". */
	public static final float ICE_DRAG = 0.3f;

	/**
	 * The Constant ACCELERATOR_DRAG. Accel value for the type
	 * "acceleratorFloor".
	 */
	public static final float ACCELERATOR_DRAG = -4.0f;

	/** The balls inside illusion. Applicable to illusionWalls, otherwise is 0. */
	private int ballsInsideIllusion = 0;

	/**
	 * Instantiates a new floor.
	 *
	 * @param pos
	 *            the central position of the floor
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 * @param type
	 *            the type of floor
	 */
	public Floor(Vector2 pos, float width, float height, elementType type) {

		super(pos, width, height, type);
		image = new Sprite(Assets.manager.get(type.toString() + ".png", Texture.class));
		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		image.setPosition((pos.x - width / 2f) * MiniGolf.BOX_TO_WORLD, (pos.y - height / 2f) * MiniGolf.BOX_TO_WORLD);
		image.setSize(width * MiniGolf.BOX_TO_WORLD, height * MiniGolf.BOX_TO_WORLD);
	}

	/**
	 * Instantiates a new floor.
	 *
	 * @param type
	 *            the type of floor
	 */
	public Floor(elementType type) {

		super(type);

		image = new Sprite(Assets.manager.get(type.toString() + ".png", Texture.class));
		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.lpoo.MiniGolf.logic.Element#createBody(com.badlogic.gdx.physics.box2d
	 * .World)
	 */
	public void createBody(World w) {

		PolygonShape square = new PolygonShape();
		square.setAsBox(width / 2f, height / 2f);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = square;
		fixDef.isSensor = true;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(startPos);

		body = w.createBody(bodyDef);
		body.createFixture(fixDef);
		body.setTransform(startPos, angle);

		switch (type) {
		case grassFloor:
		case illusionWall:
			body.setUserData(new ElementType(type, GRASS_DRAG, this));
			break;
		case sandFloor:
			body.setUserData(new ElementType(type, SAND_DRAG, this));
			break;
		case iceFloor:
			body.setUserData(new ElementType(type, ICE_DRAG, this));
			break;
		case acceleratorFloor:
			body.setUserData(new ElementType(type, ACCELERATOR_DRAG, this));
			break;
		case voidFloor:
			body.setUserData(new ElementType(type, 0, this));
			break;
		case waterFloor:
			body.setUserData(new ElementType(type, 0, this));
		default:
			body.setUserData(new ElementType(type, 0, this));
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lpoo.MiniGolf.logic.Element#destroyBody()
	 */
	public void destroyBody() {
		for (int i = 0; i < body.getFixtureList().size; i++) {
			body.destroyFixture(body.getFixtureList().get(i));
		}
		body.getWorld().destroyBody(body);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lpoo.MiniGolf.logic.Element#draw()
	 */
	public void draw() {
		image.draw(MiniGolf.batch);
	}

	/**
	 * Gets the number of balls inside the floor, if it is a illusionWall (which
	 * is in reality a floor, masked as a wall)
	 *
	 * @return the balls inside the floor
	 */
	public int getBallsInsideIllusion() {
		return ballsInsideIllusion;
	}

	/**
	 * Sets the number of balls inside the illusionWall (floor).
	 *
	 * @param ballsInsideIllusion
	 *            the new number of balls inside illusion
	 */
	public void setBallsInsideIllusion(int ballsInsideIllusion) {
		this.ballsInsideIllusion = ballsInsideIllusion;
	}

	/**
	 * Increment the number of balls in the illusion by 1.
	 */
	public void incrementBallsIllusion() {
		ballsInsideIllusion++;
	}

	/**
	 * Decrement the number of balls in the illusion by 1.
	 */
	public void decrementBallsIllusion() {
		ballsInsideIllusion--;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lpoo.MiniGolf.logic.Element#initializeImage()
	 */
	public void initializeImage() {
		image = new Sprite(Assets.manager.get(type.toString() + ".png", Texture.class));

		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		image.setPosition((startPos.x - width / 2f) * MiniGolf.BOX_TO_WORLD, (startPos.y - height / 2f) * MiniGolf.BOX_TO_WORLD);
		image.setSize(width * MiniGolf.BOX_TO_WORLD, height * MiniGolf.BOX_TO_WORLD);
		image.setOriginCenter();
		image.setRotation(angle * (1 / Geometry.DEG_TO_RAD));

	}

}
