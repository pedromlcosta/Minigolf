package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.lpoo.MiniGolf.logic.Element.elementType;

public class Ball extends Element {
	int number; //will no longer be necessary, the body has an id already for this
	public elementType steppingOn = elementType.nothing; // By default, the ball is on nothing
	public float previousVelX = 0f;
	
	public Ball() {
		super();
	}

	public Ball(Vector2 pos, World w, float radius) {
		super(pos, radius*2, radius*2);

		CircleShape circle = new CircleShape();
		circle.setPosition(pos);
		circle.setRadius(radius);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = circle;
		fixDef.isSensor = false;
		

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(pos);
		body = w.createBody(bodyDef);
		this.body.createFixture(fixDef);
		this.body.setUserData(new ElementType(elementType.ball, 0));
		this.body.setLinearVelocity(new Vector2(2, 2));
		
		image = new Sprite(new Texture("bola0.png"));

	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public void draw(){
		//System.out.println(body.getPosition().x + " " + body.getPosition().y + " " + width*MiniGolf.BOX_TO_WORLD + " " + height*MiniGolf.BOX_TO_WORLD+ " "+ body.getFixtureList().get(0).getShape().getRadius());
		MiniGolf.batch.draw(image, (body.getPosition().x-width/2f)*MiniGolf.BOX_TO_WORLD, (body.getPosition().y-width/2f)*MiniGolf.BOX_TO_WORLD, width*MiniGolf.BOX_TO_WORLD , height*MiniGolf.BOX_TO_WORLD );
		//image.setPosition(body.getPosition().x*MiniGolf.BOX_TO_WORLD, body.getPosition().y*MiniGolf.BOX_TO_WORLD);
		//image.setSize(width*MiniGolf.BOX_TO_WORLD, height*MiniGolf.BOX_TO_WORLD);
		//image.draw(MiniGolf.batch);
	}

}
