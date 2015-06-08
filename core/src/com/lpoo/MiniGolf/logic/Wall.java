package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.data.Assets;

/**
 * The Class Wall. Subclass of Element, represents only the game obstacles which
 * have no sensors and can't be crossed normally.
 */
public class Wall extends Element {

	/**
	 * Instantiates a new wall.
	 *
	 * @param pos
	 *            the central position of the wall and body
	 * @param width
	 *            the width of the wall
	 * @param height
	 *            the height of the wall
	 * @param type 
	 *            the type of the wall
	 */
	public Wall(Vector2 pos, float width, float height, elementType type) {

		super(pos, width, height, type);
		image = new Sprite(Assets.manager.get(type.toString() + ".png", Texture.class));
		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		image.setPosition((pos.x - width / 2f) * MiniGolf.BOX_TO_WORLD, (pos.y - height / 2f) * MiniGolf.BOX_TO_WORLD);
		image.setSize(width * MiniGolf.BOX_TO_WORLD, height * MiniGolf.BOX_TO_WORLD);
	}

	/**
	 * Instantiates a new wall.
	 *
	 * @param type
	 *            the type of the wall
	 */
	public Wall(elementType type) {

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
		fixDef.isSensor = false;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(startPos);

		body = w.createBody(bodyDef);
		Fixture fixt = body.createFixture(fixDef); // Wall physic fixture for
													// contact
		fixt.setFriction(0.0f);

		switch (type) {
		case regularWall:
			fixt.setRestitution(1.0f);
			body.setUserData(new ElementType(elementType.regularWall, 0, this));
			break;
		case glueWall:
			fixt.setRestitution(0.01f);
			System.out.println("Hue, restitution!");
			body.setUserData(new ElementType(elementType.glueWall, 0, this));
			break;
		case bouncyWall:
			fixt.setRestitution(1.15f);
			body.setUserData(new ElementType(elementType.bouncyWall, 0, this));
			break;
		default:
			break;

		}

		System.out.println("Width:" + this.width + " Height: " + this.height + " PosX " + this.getPosX() + "  PodY: " + this.getPosY());

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
		MiniGolf.batch.draw(image.getTexture(), (startPos.x - width / 2f) * MiniGolf.BOX_TO_WORLD, (startPos.y - height / 2f) * MiniGolf.BOX_TO_WORLD, image.getWidth(), image.getHeight(), 0, 0, 2, 1);
		// image.draw(MiniGolf.batch);

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
		image.setRotation(angle);

	}
}
