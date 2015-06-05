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
import com.lpoo.MiniGolf.screens.GameScreen;

public class Ball extends Element {

	public elementType steppingOn = elementType.nothing;
	public float radius;
	private Vector2 firstPos;
	private Vector2 lastPos;
	public elementType lastPosType;
	private boolean fellInVoid = false;
	private boolean fellInWater = false;

	public Ball() {
		super();
	}

	public Ball(Vector2 pos, World w, float radius, Player player) {
		super(pos, radius * 2, radius * 2);
		this.radius = radius;

		firstPos = pos.cpy();
		lastPos = pos.cpy();

		CircleShape circleOuter = new CircleShape();
		circleOuter.setRadius(radius);
		FixtureDef fixDefOuter = new FixtureDef();
		fixDefOuter.shape = circleOuter;
		fixDefOuter.isSensor = false;

		CircleShape circleInner = new CircleShape();

		circleInner.setRadius(radius / 8);
		FixtureDef fixDefInner = new FixtureDef();
		fixDefInner.shape = circleInner;
		fixDefInner.isSensor = true;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.bullet = true;
		bodyDef.position.set(pos);
		this.body = w.createBody(bodyDef);
		Fixture fixtOuter = this.body.createFixture(fixDefOuter);
		body.createFixture(fixDefInner);
		body.setUserData(new ElementType(elementType.ball, 0, player));

		fixtOuter.setRestitution(0.85f);
		fixtOuter.setFriction(0.0f);

		String imageName = "bola" + player.getPlayerID() + ".png";

		image = new Sprite(new Texture(imageName));

	}

	public void createBody(World w, Player player) {

		CircleShape circleOuter = new CircleShape();
		circleOuter.setRadius(radius);
		FixtureDef fixDefOuter = new FixtureDef();
		fixDefOuter.shape = circleOuter;
		fixDefOuter.isSensor = false;

		CircleShape circleInner = new CircleShape();

		circleInner.setRadius(radius / 8);
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

		// fixtOuter.setRestitution(0.85f);
		fixtOuter.setFriction(0.0f);

	}

	public void destroyBody() {
		for (int i = 0; i < body.getFixtureList().size; i++) {
			body.destroyFixture(body.getFixtureList().get(i));
		}
		body.getWorld().destroyBody(body);

	}

	public void draw() {
		MiniGolf.batch.draw(image, (body.getPosition().x - width / 2f) * MiniGolf.BOX_TO_WORLD, (body.getPosition().y - width / 2f) * MiniGolf.BOX_TO_WORLD, width * MiniGolf.BOX_TO_WORLD, height
				* MiniGolf.BOX_TO_WORLD);
	}

	public void beginContactListener(ElementType ballUserData, ElementType obstacleUserData, boolean innerSensor) {
		System.out.println("Begin Contact: " + ballUserData.type+" " + obstacleUserData.type);
		if (innerSensor) {
			
			steppingOn = obstacleUserData.type;
			getBodyUserData().accel = obstacleUserData.accel;

			if (obstacleUserData.type == Element.elementType.hole && ballUserData.player.getBallSpeedLen() < 10.0f) {
				// Ball + hole = removes ball and player ends the course
				ballUserData.player.setOver(true);
				GameScreen.playerRemovalList.add(ballUserData.player);

				body.setLinearVelocity(0f, 0f);

			} else if (obstacleUserData.type == Element.elementType.voidFloor) {
				this.fellInVoid = true; // Assumes that the ball who called this
										// function is the same as in
										// ballUserData.element
			} else if (obstacleUserData.type == Element.elementType.waterFloor) {
				System.out.println("Water");
				this.fellInWater = true; // Needs a boolean!!!
											// body.transform(pos,angle) can't
											// be called in the listener, or the
											// program will crash!!!!
			}

			// Inherits the obstacles acceleration
			steppingOn = obstacleUserData.type;
			ballUserData.accel = obstacleUserData.accel;

		} else { // else if outerSensor/outer part of the ball

			if (obstacleUserData.type == Element.elementType.glueWall) {
				// Ball + glueWall = ball loses all speed
				lastPos = body.getPosition().cpy();
				body.setLinearVelocity(0f, 0f);
			} else if (obstacleUserData.type == Element.elementType.illusionWall) {
				// Ball + illusionWall = enters the wall, but changes its
				// transparency
				obstacleUserData.element.getImage().setAlpha(0.75f);
				Floor tempFloorRef = (Floor) obstacleUserData.element;
				tempFloorRef.incrementBallsIllusion();
			}
		}

	}

	public void endContactListener(ElementType ballUserData, ElementType obstacleUserData, boolean innerSensor) {

		System.out.println("End Contact: " + ballUserData.type+" " + obstacleUserData.type);
		// These 2 lines are just for safety. When a object gets out of
		// something, it enters something else immediately and changes these
		// values
		//steppingOn = Element.elementType.grassFloor;
		//ballUserData.accel = GameScreen.GRASS_DRAG;
		System.out.println("Here");

		if (innerSensor) {
			// No inner sensor does anything particular to it when leaving yet
		} else { // else if outerSensor

			if (obstacleUserData.type == Element.elementType.illusionWall) {
				Floor tempFloorRef = (Floor) obstacleUserData.element;
				tempFloorRef.decrementBallsIllusion();
				if (tempFloorRef.getBallsInsideIllusion() == 0)
					obstacleUserData.element.getImage().setAlpha(1.0f);
			}

		}
	}

	public Player getPlayer() {

		ElementType bodyUserData = (ElementType) body.getUserData();
		return bodyUserData.player;
	}

	public ElementType getBodyUserData() {
		return (ElementType) body.getUserData();
	}

	public Vector2 getLastPos() {
		return lastPos;
	}

	public void setLastPos(Vector2 lastPos) {
		this.lastPos = lastPos.cpy();
	}

	public void checkIfFallen() {

		if (fellInVoid) {
			body.setLinearVelocity(0f, 0f);
			ElementType element = (ElementType) this.getBody().getUserData();
			Player player = element.player;
			World w = body.getWorld();
			
			destroyBody();
			createBody(w, player);
			//body.setTransform(firstPos, body.getAngle());
			lastPos = firstPos.cpy();
			fellInVoid = false;
		}

		if (fellInWater) {
			body.setLinearVelocity(0f, 0f);
			ElementType element = (ElementType) this.getBody().getUserData();
			Player player = element.player;
			World w = body.getWorld();
			
			destroyBody();
			createBody(w, player);
			//body.setTransform(lastPos, body.getAngle());
			
			fellInWater = false;
		}
	}

	public boolean isFellInVoid() {
		return fellInVoid;
	}

	public void setFellInVoid(boolean fellInVoid) {
		this.fellInVoid = fellInVoid;
	}

	public boolean isFellInWater() {
		return fellInWater;
	}

	public void setFellInWater(boolean fellInWater) {
		this.fellInWater = fellInWater;
	}

}
