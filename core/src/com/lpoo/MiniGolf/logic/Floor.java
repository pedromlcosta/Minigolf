package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.screens.GameScreen;

public class Floor extends Obstacle {
	public Floor(Vector2 pos, float width, float height, World w, elementType type) {

		super(pos, width, height, w);
		PolygonShape square = new PolygonShape();
		square.setAsBox(width/2f, height/2f);
		FixtureDef fixDef = new FixtureDef();
		fixDef.shape = square;
		fixDef.isSensor = true;
		
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.StaticBody;
		bodyDef.position.set(pos);
		
		body = w.createBody(bodyDef);
		body.createFixture(fixDef);
		
		switch(type){
		case grassFloor:
			body.setUserData(new ElementType(type, GameScreen.GRASS_DRAG, 0));
			break;
		case sandFloor:
			body.setUserData(new ElementType(type,GameScreen.SAND_DRAG, 0));
			break;
		case iceFloor:
			System.out.println("Ice");
			//body.setUserData(new ElementType(type, 0, 0));
			break;
		default:
			break;
		}

		image = new Sprite(new Texture(type.toString() + ".png"));
		//image.setPosition(pos.x, pos.y);
		//image.setSize(width, height);
	}
	
	public void draw(){
		//System.out.println((body.getPosition().x - width/2f) * MiniGolf.BOX_TO_WORLD + " " + (body.getPosition().y- height/2f)* MiniGolf.BOX_TO_WORLD + " " + width*MiniGolf.BOX_TO_WORLD + " " + height*MiniGolf.BOX_TO_WORLD);
		//image.setCenter(x, y);
		
		MiniGolf.batch.draw(image, (body.getPosition().x - width/2f) * MiniGolf.BOX_TO_WORLD , (body.getPosition().y- height/2f)* MiniGolf.BOX_TO_WORLD, width* MiniGolf.BOX_TO_WORLD , height*MiniGolf.BOX_TO_WORLD);
	}
}
