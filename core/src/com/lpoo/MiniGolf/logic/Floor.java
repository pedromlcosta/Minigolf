package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.geometry.Geometry;

public class Floor extends Element {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final float GRASS_DRAG = 1.5f;
	public static final float SAND_DRAG = 6.0f;
	public static final float ICE_DRAG = 0.3f;
	public static final float ACCELERATOR_DRAG = -4.0f;
	private int ballsInsideIllusion = 0;

	public Floor(Vector2 pos, float width, float height, elementType type) {

		super(pos, width, height, type);
		image = new Sprite(Assets.manager.get(type.toString() + ".png", Texture.class));
		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		image.setPosition((pos.x - width / 2f) * MiniGolf.BOX_TO_WORLD, (pos.y - height / 2f) * MiniGolf.BOX_TO_WORLD);
		image.setSize(width * MiniGolf.BOX_TO_WORLD, height * MiniGolf.BOX_TO_WORLD);
	}

	public Floor(elementType type) {

		super(type);

		image = new Sprite(Assets.manager.get(type.toString() + ".png", Texture.class));
		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
	}

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
		//TODO tentar converter angulo
	//	System.out.println("INITIALIZING BODY, ANGLE IN RADS IS: " + angle + " AND IN DEG: " + angle/Geometry.DEG_TO_RAD);
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

		//System.out.println("Width:" + this.width + " Height: " + this.height + " PosX " + this.getPosX() + "  PodY: " + this.getPosY());

	}

	public void destroyBody() {
		for (int i = 0; i < body.getFixtureList().size; i++) {
			body.destroyFixture(body.getFixtureList().get(i));
		}
		body.getWorld().destroyBody(body);
	}

	public void draw() {
		// MiniGolf.batch.draw(image.getTexture(), image.getOriginX(),
		// image.getOriginY(),image.getWidth(), image.getHeight(), 0, 0, 2, 1);
		image.draw(MiniGolf.batch);
	}

	public int getBallsInsideIllusion() {
		return ballsInsideIllusion;
	}

	public void setBallsInsideIllusion(int ballsInsideIllusion) {
		this.ballsInsideIllusion = ballsInsideIllusion;
	}

	public void incrementBallsIllusion() {
		ballsInsideIllusion++;
	}

	public void decrementBallsIllusion() {
		ballsInsideIllusion--;
	}

	public void initializeImage() {
		image = new Sprite(Assets.manager.get(type.toString() + ".png", Texture.class));

		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		//image.setPosition((startPos.x - width / 2f) * MiniGolf.BOX_TO_WORLD, (startPos.y - height / 2f) * MiniGolf.BOX_TO_WORLD);
		image.setSize(width * MiniGolf.BOX_TO_WORLD, height * MiniGolf.BOX_TO_WORLD);
		image.setOriginCenter();
		//TODO tentar converter angulo
		image.setRotation(angle * (1 / Geometry.DEG_TO_RAD));

	}

}
