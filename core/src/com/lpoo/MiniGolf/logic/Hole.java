package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Hole extends Element {

	private float radius;

	public Hole() {
		super();
	}

	public Hole(Vector2 pos, World w, float radius) {
		super(pos, radius * 2, radius * 2);

		this.radius = radius;
		image = new Sprite(new Texture("hole.png"));

	}

	public void createBody(World w) {
		CircleShape circle = new CircleShape();
		circle.setRadius(radius*0.50/f);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = circle;
		fixDef.isSensor = true;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(oldPos);
		body = w.createBody(bodyDef);
		body.setUserData(new ElementType(elementType.hole, 0));
		Fixture fixt = this.body.createFixture(fixDef);
	}

	public void destroyBody() {

		for (int i = 0; i < body.getFixtureList().size; i++) {
			body.destroyFixture(body.getFixtureList().get(i));
		}
		body.getWorld().destroyBody(body);
	}

	public void draw() {
		// System.out.println((body.getPosition().x-width/2f)*MiniGolf.BOX_TO_WORLD
		// + " " + (body.getPosition().y-width/2f)*MiniGolf.BOX_TO_WORLD + " " +
		// width*MiniGolf.BOX_TO_WORLD + " " + height*MiniGolf.BOX_TO_WORLD+
		// " "+ body.getFixtureList().get(0).getShape().getRadius());
		MiniGolf.batch.draw(image, (body.getPosition().x - width / 2f) * MiniGolf.BOX_TO_WORLD, (body.getPosition().y - width / 2f) * MiniGolf.BOX_TO_WORLD, width * MiniGolf.BOX_TO_WORLD, height
				* MiniGolf.BOX_TO_WORLD);
		// image.setPosition(body.getPosition().x*MiniGolf.BOX_TO_WORLD,
		// body.getPosition().y*MiniGolf.BOX_TO_WORLD);
		// image.setSize(width*MiniGolf.BOX_TO_WORLD,
		// height*MiniGolf.BOX_TO_WORLD);
		// image.draw(MiniGolf.batch);
	}

}
