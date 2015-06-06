package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.Color;
import com.lpoo.MiniGolf.data.Assets;

public class Teleporter extends Element {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float radius;
	private Vector2 destination = new Vector2();
	private Sprite destinationImage;

	public Teleporter() {
		super();
		image = new Sprite(Assets.manager.get("teleporter.png", Texture.class));
		destinationImage = new Sprite(Assets.manager.get("teleporterDestination.png", Texture.class));
	}

	public Teleporter(elementType type) {
		super(type);
		image = new Sprite(Assets.manager.get("teleporter.png", Texture.class));
		destinationImage = new Sprite(Assets.manager.get("teleporterDestination.png", Texture.class));
	}

	public Teleporter(Vector2 start, Vector2 destination, float radius) {
		super(start, radius * 2, radius * 2);
		this.destination = destination;

		this.radius = radius;
		image = new Sprite(Assets.manager.get("teleporter.png", Texture.class));
		destinationImage = new Sprite(Assets.manager.get("teleporterDestination.png", Texture.class));

		image.setPosition((startPos.x - radius) * MiniGolf.BOX_TO_WORLD, (startPos.y - radius) * MiniGolf.BOX_TO_WORLD);
		image.setSize(radius * 2 * MiniGolf.BOX_TO_WORLD, radius * 2 * MiniGolf.BOX_TO_WORLD);

		destinationImage.setPosition((destination.x - radius) * MiniGolf.BOX_TO_WORLD, (destination.y - radius) * MiniGolf.BOX_TO_WORLD);
		destinationImage.setSize(radius * 2 * MiniGolf.BOX_TO_WORLD, radius * 2 * MiniGolf.BOX_TO_WORLD);
	}

	public Teleporter(Vector2 start, Vector2 destination, float radius, int colorID) {
		super(start, radius * 2, radius * 2);
		this.destination = destination;

		this.radius = radius;
		image = new Sprite(Assets.manager.get("teleporter.png", Texture.class));
		destinationImage = new Sprite(Assets.manager.get("teleporterDestination.png", Texture.class));

		image.setPosition((startPos.x - radius) * MiniGolf.BOX_TO_WORLD, (startPos.y - radius) * MiniGolf.BOX_TO_WORLD);
		image.setSize(radius * 2 * MiniGolf.BOX_TO_WORLD, radius * 2 * MiniGolf.BOX_TO_WORLD);

		destinationImage.setPosition((destination.x - radius) * MiniGolf.BOX_TO_WORLD, (destination.y - radius) * MiniGolf.BOX_TO_WORLD);
		destinationImage.setSize(radius * 2 * MiniGolf.BOX_TO_WORLD, radius * 2 * MiniGolf.BOX_TO_WORLD);

		changeColor(colorID);

	}

	public void changeColor(int colorID) {
		switch (colorID) {
		case 1:
			destinationImage.setColor(Color.ORANGE);
			image.setColor(Color.ORANGE);
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			break;
		case 9:
			break;
		case 10:
			break;
		default:
			break;
		}
	}

	public void createBody(World w) {
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = circle;
		fixDef.isSensor = true;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(startPos);
		body = w.createBody(bodyDef);
		body.setUserData(new ElementType(elementType.teleporter, 0, this));
		this.body.createFixture(fixDef);
	}

	public void destroyBody() {

		for (int i = 0; i < body.getFixtureList().size; i++) {
			body.destroyFixture(body.getFixtureList().get(i));
		}
		body.getWorld().destroyBody(body);
	}

	public void draw() {
		// TODO: Shaperenderer, circulos a volta deles, de cores iguais entre
		// elementos e diferentes
		// entre teleporters, para distinguir os pares
		image.draw(MiniGolf.batch);
		destinationImage.draw(MiniGolf.batch);
	}

	public Vector2 getDestination() {
		return destination;
	}

	public void setDestination(Vector2 destination) {
		this.destination = destination;
	}

	@Override
	public void draw(Batch batch, float parentAlfa) {

		batch.draw(image, (body.getPosition().x - width / 2) * MiniGolf.BOX_TO_WORLD, (body.getPosition().y - height / 2) * MiniGolf.BOX_TO_WORLD, width * MiniGolf.BOX_TO_WORLD, height
				* MiniGolf.BOX_TO_WORLD);
		batch.draw(destinationImage, (destination.x - width / 2) * MiniGolf.BOX_TO_WORLD, (destination.y - height / 2) * MiniGolf.BOX_TO_WORLD, width * MiniGolf.BOX_TO_WORLD, height
				* MiniGolf.BOX_TO_WORLD);
	}
}
