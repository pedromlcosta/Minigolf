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
import com.lpoo.MiniGolf.screens.GameScreen;

public class Wall extends Element {
	public Wall(Vector2 pos, float width, float height, elementType type) {

		super(pos, width, height, type);

		image = new Sprite(new Texture(type.toString() + ".png"));
		//image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		// image.setPosition(pos.x, pos.y);
		// image.setSize(width, height);
	}

	public Wall(elementType type) {

		super(type);

		image = new Sprite(new Texture(type.toString() + ".png"));

	}

	public void createBody(World w) {

		PolygonShape square = new PolygonShape();
		square.setAsBox(width / 2f, height / 2f);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = square;
		fixDef.isSensor = false;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(oldPos);

		switch (type) {
		case regularWall:
			fixDef.restitution = 1.0f;
			break;
		case glueWall:
			fixDef.restitution = 0.1f;
			break;
		case bonceWall:
			fixDef.restitution = 2.0f;
			break;
		default:
			break;
		}
		
		body = w.createBody(bodyDef);
		Fixture fixt = body.createFixture(fixDef);

		fixt.setFriction(0.0f);

		

	}

	public void destroyBody() {
		for (int i = 0; i < body.getFixtureList().size; i++) {
			body.destroyFixture(body.getFixtureList().get(i));
		}
		body.getWorld().destroyBody(body);
	}

	public void draw() {
		image.setPosition((body.getPosition().x - width / 2f) * MiniGolf.BOX_TO_WORLD, (body.getPosition().y - height / 2f) * MiniGolf.BOX_TO_WORLD);
		image.setSize(width * MiniGolf.BOX_TO_WORLD, height * MiniGolf.BOX_TO_WORLD);
		image.draw(MiniGolf.batch);

	}

}
