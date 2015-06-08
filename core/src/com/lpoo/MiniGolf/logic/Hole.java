package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.data.Assets;

/**
 * The Class Hole, representing the hole element of the course.
 */
public class Hole extends Element {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The radius of the hole. The actual body will only be half of this radius though. */
	private float radius;

	/**
	 * Instantiates a new hole.
	 */
	public Hole() {
		super();
		image = new Sprite(Assets.manager.get("hole.png", Texture.class));
		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

	}

	/**
	 * Gets the radius.
	 *
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}

	/* (non-Javadoc)
	 * @see com.lpoo.MiniGolf.logic.Element#setRadius(float)
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}

	/**
	 * Instantiates a new hole.
	 *
	 * @param pos the position of the hole
	 * @param radius the radius
	 */
	public Hole(Vector2 pos, float radius) {
		super(pos, radius * 2, radius * 2);

		this.radius = radius;
		image = new Sprite(Assets.manager.get("hole.png", Texture.class));

	}

	/* (non-Javadoc)
	 * @see com.lpoo.MiniGolf.logic.Element#createBody(com.badlogic.gdx.physics.box2d.World)
	 */
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
		body.setUserData(new ElementType(elementType.hole, 0, this));
		this.body.createFixture(fixDef);

		System.out.println("Width:" + this.width + " Height: " + this.height + " PosX " + this.getPosX() + "  PodY: " + this.getPosY());

	}

	/* (non-Javadoc)
	 * @see com.lpoo.MiniGolf.logic.Element#destroyBody()
	 */
	public void destroyBody() {

		for (int i = 0; i < body.getFixtureList().size; i++) {
			body.destroyFixture(body.getFixtureList().get(i));
		}
		body.getWorld().destroyBody(body);
	}

	/* (non-Javadoc)
	 * @see com.lpoo.MiniGolf.logic.Element#draw()
	 */
	public void draw() {
		 
		image.draw(MiniGolf.batch);
	}

	/* (non-Javadoc)
	 * @see com.lpoo.MiniGolf.logic.Element#createElement(float, float, float, float)
	 */
	public void createElement(float posInicialX, float posInicialY, float width, float height) {

		this.setStartPos(new Vector2(posInicialX, posInicialY));
		this.setHeight(height);
		this.setWidth(width);
		this.createBody(MiniGolf.getW());
	}

	/* (non-Javadoc)
	 * @see com.lpoo.MiniGolf.logic.Element#initializeImage()
	 */
	public void initializeImage() {
		image = new Sprite(Assets.manager.get("hole.png", Texture.class));

		image.getTexture().setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		image.setPosition((startPos.x - width / 2) * MiniGolf.BOX_TO_WORLD, (startPos.y - height / 2) * MiniGolf.BOX_TO_WORLD);
		image.setSize(width * MiniGolf.BOX_TO_WORLD, height * MiniGolf.BOX_TO_WORLD);
		image.setOriginCenter();
		image.setRotation(angle);

	}

}
