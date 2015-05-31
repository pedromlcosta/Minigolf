package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Ball extends Element {
	int number;

	public Ball() {
		super();
	}

	public Ball(Vector2 pos, World w, int radius) {
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
		this.body.setUserData(elementType.ball);
		this.body.setLinearVelocity(new Vector2(1, 1));
		
		image = new Sprite(new Texture("bola0.png"));
		image.setPosition(pos.x, pos.y);
		image.setSize(radius,radius);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public void draw(){
		//System.out.println(body.getPosition().x + " " + body.getPosition().y + " " + width*MiniGolf.BOX_TO_WORLD + " " + height*MiniGolf.BOX_TO_WORLD+ " "+ body.getFixtureList().get(0).getShape().getRadius());
		MiniGolf.batch.draw(image, (body.getPosition().x-width/4f)*MiniGolf.BOX_TO_WORLD, (body.getPosition().y-width/4f)*MiniGolf.BOX_TO_WORLD, width*MiniGolf.BOX_TO_WORLD , height*MiniGolf.BOX_TO_WORLD );
	}

}
