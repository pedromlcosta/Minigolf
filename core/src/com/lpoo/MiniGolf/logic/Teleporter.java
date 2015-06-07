package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
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
	private Vector2 destination = new Vector2(1, 1);
	transient private Sprite destinationImage;

	public Teleporter() {
		super();
		image = new Sprite(Assets.manager.get("teleporter.png", Texture.class));
		destinationImage = new Sprite(Assets.manager.get("teleporterDestination.png", Texture.class));
		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		destinationImage.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
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

	public void initializeImage() {
		image = new Sprite(Assets.manager.get("teleporter.png", Texture.class));
		destinationImage = new Sprite(Assets.manager.get("teleporterDestination.png", Texture.class));
		
		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		image.setPosition((startPos.x - width / 2f) * MiniGolf.BOX_TO_WORLD, (startPos.y - height / 2f) * MiniGolf.BOX_TO_WORLD);
		image.setSize(width * MiniGolf.BOX_TO_WORLD, height * MiniGolf.BOX_TO_WORLD);
		image.setOriginCenter();
		image.setRotation(angle);

		destinationImage.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		destinationImage.setPosition((destination.x - width / 2f) * MiniGolf.BOX_TO_WORLD, (destination.y - height / 2f) * MiniGolf.BOX_TO_WORLD);
		destinationImage.setSize(width * MiniGolf.BOX_TO_WORLD, height * MiniGolf.BOX_TO_WORLD);
		destinationImage.setOriginCenter();
		destinationImage.setRotation(angle);
	}

	public void changeColor(int colorID) {
		switch (colorID) {
		case 1:
			destinationImage.setColor(Color.ORANGE);
			image.setColor(Color.ORANGE);
			break;
		case 2:
			destinationImage.setColor(Color.BLUE);
			image.setColor(Color.BLUE);
			break;
		case 3:
			destinationImage.setColor(Color.RED);
			image.setColor(Color.RED);
			break;
		case 4:
			destinationImage.setColor(Color.PINK);
			image.setColor(Color.PINK);
			break;
		case 5:
			destinationImage.setColor(Color.YELLOW);
			image.setColor(Color.YELLOW);
			break;
		case 6:
			destinationImage.setColor(Color.CYAN);
			image.setColor(Color.CYAN);
			break;
		case 7:
			destinationImage.setColor(Color.WHITE);
			image.setColor(Color.WHITE);
			break;
		case 8:
			destinationImage.setColor(Color.GREEN);
			image.setColor(Color.GREEN);
			break;
		case 9:
			destinationImage.setColor(Color.TEAL);
			image.setColor(Color.TEAL);
			break;
		case 10:
			destinationImage.setColor(Color.MAROON);
			image.setColor(Color.MAROON);
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
