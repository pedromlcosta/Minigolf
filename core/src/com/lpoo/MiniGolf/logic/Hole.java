package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Hole extends Element {

	private float radius;

	public Hole() {
		super();

		image = new Sprite(new Texture("hole.png"));
	}

	public Hole(Vector2 pos, World w, float radius) {
		super(pos, radius * 2, radius * 2);

		this.radius = radius;
		image = new Sprite(new Texture("hole.png"));

	}

	public float getRadius() {
		return radius;
	}

	@Override
	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void createBody(World w) {
		CircleShape circle = new CircleShape();
		circle.setRadius(radius * 0.50f);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = circle;
		fixDef.isSensor = true;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(startPos);
		body = w.createBody(bodyDef);
		body.setUserData(new ElementType(elementType.hole, 0));
		this.body.createFixture(fixDef);
	}

	public void destroyBody() {

		for (int i = 0; i < body.getFixtureList().size; i++) {
			body.destroyFixture(body.getFixtureList().get(i));
		}
		body.getWorld().destroyBody(body);
	}

	// public boolean overlap(Element eleToBeAdded) {
	// // TODO circle
	// if (eleToBeAdded.getType() == this.getType() || eleToBeAdded.getType() !=
	// elementType.grassFloor)
	// return false;
	// Body bodyAdded, bodyInGame;
	//
	// bodyAdded = eleToBeAdded.getBody();
	// bodyInGame = this.getBody();
	//
	// // for (Fixture fixtureAdded : bodyAdded.getFixtureList()) {
	//
	// for (Fixture fixtureInGame : bodyInGame.getFixtureList()) {
	//
	// CircleShape circle = (CircleShape) fixtureInGame.getShape();
	// float radius = circle.getRadius();
	//
	// float dist = EditorScreen.distance(bodyInGame.getPosition().x,
	// bodyInGame.getPosition().y, eleToBeAdded.getOldPosX(),
	// eleToBeAdded.getOldPosY());
	// if (dist <= radius)
	// return true;
	//
	//
	// }
	// // }
	//
	// return true;
	// }

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

	@Override
	public void draw(Batch batch, float parentAlfa) {
		// System.out.println((body.getPosition().x-width/2f)*MiniGolf.BOX_TO_WORLD
		// + " " + (body.getPosition().y-width/2f)*MiniGolf.BOX_TO_WORLD + " " +
		// width*MiniGolf.BOX_TO_WORLD + " " + height*MiniGolf.BOX_TO_WORLD+
		// " "+ body.getFixtureList().get(0).getShape().getRadius());
		batch.draw(image, (body.getPosition().x - width / 2f) * MiniGolf.BOX_TO_WORLD, (body.getPosition().y - width / 2f) * MiniGolf.BOX_TO_WORLD, width * MiniGolf.BOX_TO_WORLD, height
				* MiniGolf.BOX_TO_WORLD);
		
		
//		MiniGolf.batch.draw(image.getTexture(), (startPos.x - width / 2f)  MiniGolf.BOX_TO_WORLD, (startPos.y - height / 2f)  MiniGolf.BOX_TO_WORLD,image.getWidth(), image.getHeight(), 0, 0, 2, 1);
		// image.setPosition(body.getPosition().x*MiniGolf.BOX_TO_WORLD,
		// body.getPosition().y*MiniGolf.BOX_TO_WORLD);
		// image.setSize(width*MiniGolf.BOX_TO_WORLD,
		// height*MiniGolf.BOX_TO_WORLD);
		// image.draw(MiniGolf.batch);
	}
}
