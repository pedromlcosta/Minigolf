package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Ball extends Element {
	
	public elementType steppingOn = elementType.nothing;
	public float radius;
	
	public Ball() {
		super();
	}

	public Ball(Vector2 pos, World w, float radius, Player player) {
		super(pos, radius*2, radius*2);
		this.radius = radius;

		CircleShape circleOuter = new CircleShape();
		circleOuter.setRadius(radius);
		FixtureDef fixDefOuter = new FixtureDef();
		fixDefOuter.shape = circleOuter;
		fixDefOuter.isSensor = false;
		
		
		CircleShape circleInner = new CircleShape();

		circleInner.setRadius(radius/8);
		FixtureDef fixDefInner = new FixtureDef();
		fixDefInner.shape = circleInner;
		fixDefInner.isSensor = true;
		

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(pos);
		this.body = w.createBody(bodyDef);
		Fixture fixtOuter = this.body.createFixture(fixDefOuter);
		body.createFixture(fixDefInner);
		body.setUserData(new ElementType(elementType.ball, 0, player));
		
		fixtOuter.setRestitution(0.85f);
		fixtOuter.setFriction(0.0f);
		
		image = new Sprite(new Texture("bola0.png"));

	}
	
	public void createBody(World w, Player player){
		
		CircleShape circleOuter = new CircleShape();
		circleOuter.setRadius(radius);
		FixtureDef fixDefOuter = new FixtureDef();
		fixDefOuter.shape = circleOuter;
		fixDefOuter.isSensor = false;
		
		
		CircleShape circleInner = new CircleShape();

		circleInner.setRadius(radius/8);
		FixtureDef fixDefInner = new FixtureDef();
		fixDefInner.shape = circleInner;
		fixDefInner.isSensor = true;
		

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(oldPos);
		this.body = w.createBody(bodyDef);
		Fixture fixtOuter = this.body.createFixture(fixDefOuter);
		body.createFixture(fixDefInner);
		body.setUserData(new ElementType(elementType.ball, 0, player));
		
		fixtOuter.setRestitution(0.85f);
		fixtOuter.setFriction(0.0f);
		
	}

	public void draw(){
		//System.out.println((body.getPosition().x-width/2f)*MiniGolf.BOX_TO_WORLD + " " + (body.getPosition().y-width/2f)*MiniGolf.BOX_TO_WORLD + " " + width*MiniGolf.BOX_TO_WORLD + " " + height*MiniGolf.BOX_TO_WORLD+ " "+ body.getFixtureList().get(0).getShape().getRadius());
		MiniGolf.batch.draw(image, (body.getPosition().x-width/2f)*MiniGolf.BOX_TO_WORLD, (body.getPosition().y-width/2f)*MiniGolf.BOX_TO_WORLD, width*MiniGolf.BOX_TO_WORLD , height*MiniGolf.BOX_TO_WORLD );
		//image.setPosition(body.getPosition().x*MiniGolf.BOX_TO_WORLD, body.getPosition().y*MiniGolf.BOX_TO_WORLD);
		//image.setSize(width*MiniGolf.BOX_TO_WORLD, height*MiniGolf.BOX_TO_WORLD);
		//image.draw(MiniGolf.batch);
	}
	
	/*
	 * Destroys the Fixtures and Body of a ball
	 */
	public void destroy(){
		for(int i = 0; i < body.getFixtureList().size; i++){
			body.destroyFixture(body.getFixtureList().get(i));
		}
		body.getWorld().destroyBody(body);
		
	}

}
