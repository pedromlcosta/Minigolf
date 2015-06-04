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
import com.lpoo.MiniGolf.logic.Element.elementType;
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
		
		PolygonShape square2 = new PolygonShape();
		square2.setAsBox(width / 2f, height / 2f);
		FixtureDef fixDefSensor = new FixtureDef();
		fixDefSensor.shape = square2;
		fixDefSensor.isSensor = true;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(oldPos);

		body = w.createBody(bodyDef);
		Fixture fixt = body.createFixture(fixDef);            //Wall physic fixture for contact
		Fixture fixtSense = body.createFixture(fixDefSensor); //Wall sensor
		

		

		fixt.setFriction(0.0f);

		switch (type) {
		case regularWall:
			fixt.setRestitution(1.0f);
			body.setUserData(new ElementType(elementType.regularWall, 0));
			break;
		case glueWall:
			fixt.setRestitution(0.1f);
			body.setUserData(new ElementType(elementType.glueWall, 0));
			break;
		case bonceWall:
			fixt.setRestitution(2.0f);
			body.setUserData(new ElementType(elementType.bonceWall, 0));
			break;
		default:
			break;
		

	}
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