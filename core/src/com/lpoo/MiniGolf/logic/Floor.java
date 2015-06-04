package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.screens.GameScreen;

public class Floor extends Element {
	public Floor(Vector2 pos, float width, float height, elementType type) {

		super(pos, width, height, type);

		image = new Sprite(new Texture(type.toString() + ".png"));
		// image.setPosition(pos.x, pos.y);
		// image.setSize(width, height);
	}

	public Floor(elementType type) {

		super(type);

		image = new Sprite(new Texture(type.toString() + ".png"));

	}

	public void createBody(World w) {

		PolygonShape square = new PolygonShape();
		square.setAsBox(width / 2f, height / 2f);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = square;
		fixDef.isSensor = true;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(oldPos);

		body = w.createBody(bodyDef);
		body.createFixture(fixDef);

		switch (type) {
		case grassFloor:
			body.setUserData(new ElementType(type, GameScreen.GRASS_DRAG));
			break;
		case sandFloor:
			body.setUserData(new ElementType(type, GameScreen.SAND_DRAG));
			break;
		case iceFloor:
			System.out.println("Ice"); // body.setUserData(new ElementType(type,
										// 0, 0));
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

	@Override
	public void draw(Batch batch, float parentAlfa) {
		// System.out.println((body.getPosition().x - width/2f) *
		// MiniGolf.BOX_TO_WORLD + " " + (body.getPosition().y- height/2f)*
		// MiniGolf.BOX_TO_WORLD + " " + width*MiniGolf.BOX_TO_WORLD + " " +
		// height*MiniGolf.BOX_TO_WORLD);
		// image.setCenter(x, y);
		// System.out.println("IN PosX: " + (body.getPosition().x - width / 2f)
		// * MiniGolf.BOX_TO_WORLD + " PosY: " + (body.getPosition().y - height
		// / 2f) * MiniGolf.BOX_TO_WORLD + " Width: " + width
		// * MiniGolf.BOX_TO_WORLD + " Height: " + height *
		// MiniGolf.BOX_TO_WORLD);
		batch.draw(image, (body.getPosition().x - width / 2f) * MiniGolf.BOX_TO_WORLD, (body.getPosition().y - height / 2f) * MiniGolf.BOX_TO_WORLD, width * MiniGolf.BOX_TO_WORLD, height
				* MiniGolf.BOX_TO_WORLD);
	}

}
