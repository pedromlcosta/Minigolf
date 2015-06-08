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
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.screens.GameScreen;

// TODO: Auto-generated Javadoc
/**
 * The Class Ball.
 */
public class Ball extends Element {

	/** The stepping on. */
	public elementType steppingOn = elementType.nothing;
	
	/** The radius. */
	public float radius;
	
	/** The last pos. */
	private Vector2 lastPos;
	
	/** The last pos type. */
	public elementType lastPosType;

	/** The teleport destination. */
	private Vector2 teleportDestination;
	
	/** The velocity before teleport. */
	private Vector2 velocityBeforeTeleport;
	
	/** The on speed pad. */
	private boolean onSpeedPad = false;
	
	/** The accel angle. */
	public float accelAngle;

	/** The touched void. */
	private boolean touchedVoid = false;
	
	/** The touched water. */
	private boolean touchedWater = false;
	
	/** The touched teleporter. */
	private boolean touchedTeleporter = false;

	/**
	 * Instantiates a new ball.
	 */
	public Ball() {
		super();
		
	}

	// Constructor that also creates the body
	/**
	 * Instantiates a new ball.
	 *
	 * @param pos the pos
	 * @param w the w
	 * @param radius the radius
	 * @param player the player
	 */
	public Ball(Vector2 pos, World w, float radius, Player player) {
		super(pos, radius * 2, radius * 2);
		this.radius = radius;

		// firstPos = pos.cpy();
		lastPos = pos.cpy();

		createBody(w, player, pos);
		image = new Sprite(Assets.manager.get("bola" + player.getPlayerID() + ".png", Texture.class));

	}

	/**
	 * Creates the body.
	 *
	 * @param w the w
	 * @param player the player
	 * @param newPos the new pos
	 */
	public void createBody(World w, Player player, Vector2 newPos) {

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
		bodyDef.position.set(newPos);
		this.body = w.createBody(bodyDef);
		Fixture fixtOuter = this.body.createFixture(fixDefOuter);
		body.createFixture(fixDefInner);
		body.setUserData(new ElementType(elementType.ball, 0, player));

		// fixtOuter.setRestitution(0.85f);
		fixtOuter.setFriction(0.0f);

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
		MiniGolf.batch.draw(image, (body.getPosition().x - width / 2f) * MiniGolf.BOX_TO_WORLD, (body.getPosition().y - width / 2f) * MiniGolf.BOX_TO_WORLD, width * MiniGolf.BOX_TO_WORLD, height
				* MiniGolf.BOX_TO_WORLD);
	}

	/**
	 * Begin contact listener.
	 *
	 * @param ballUserData the ball user data
	 * @param obstacleUserData the obstacle user data
	 * @param innerSensor the inner sensor
	 */
	public void beginContactListener(ElementType ballUserData, ElementType obstacleUserData, boolean innerSensor) {

		if (innerSensor) {
			ballUserData.endContacts = 0;

			// Entered a begin - reset the check to 1 and type
			ballUserData.readyToCheckEnd = true;
			ballUserData.typeToCheck = obstacleUserData.type;

			steppingOn = obstacleUserData.type;
			getBodyUserData().accel = obstacleUserData.accel;
			onSpeedPad = false;

			if (obstacleUserData.type == Element.elementType.hole && ballUserData.player.getBallSpeedLen() < 10.0f) {
				// Ball + hole = removes ball and player ends the course
				ballUserData.player.setOver(true);
				GameScreen.playerRemovalList.add(ballUserData.player);

				body.setLinearVelocity(0f, 0f);

			} else if (obstacleUserData.type == Element.elementType.voidFloor) {
				// Can't teleport immediately, because object might be locked,
				// because of world.step(...);
				this.touchedVoid = true;
			} else if (obstacleUserData.type == Element.elementType.waterFloor) {
				// Can't teleport immediately, because object might be locked,
				// because of world.step(...);
				this.touchedWater = true;
			} else if (obstacleUserData.type == Element.elementType.acceleratorFloor) {
				Floor tempAccelFloor = (Floor) obstacleUserData.element;
				accelAngle = tempAccelFloor.getBody().getAngle();
				onSpeedPad = true;
			}

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
			} else if (obstacleUserData.type == Element.elementType.teleporter) {
				// Can't teleport immediately, because object might be locked,
				// because of world.step(...);
				Teleporter tempTeleRef = (Teleporter) obstacleUserData.element;
				teleportDestination = tempTeleRef.getDestination();
				this.touchedTeleporter = true;
			}
		}

	}

	/**
	 * End contact listener.
	 *
	 * @param ballUserData the ball user data
	 * @param obstacleUserData the obstacle user data
	 * @param innerSensor the inner sensor
	 */
	public void endContactListener(ElementType ballUserData, ElementType obstacleUserData, boolean innerSensor) {

		// steppingOn = Element.elementType.grassFloor;
		// ballUserData.accel = Floor.GRASS_DRAG;

		if (innerSensor) {
			ballUserData.endContacts++;

			if (ballUserData.readyToCheckEnd) { // was ready to be checked
				// if the type from the last begin is the same as the one in
				// this end
				if (ballUserData.typeToCheck == obstacleUserData.type) {
					// Checks criteria to have entered something and went back
					// to grass after
					steppingOn = Element.elementType.grassFloor;
					ballUserData.accel = Floor.GRASS_DRAG;
					ballUserData.readyToCheckEnd = false;
				}
			}

			if (ballUserData.endContacts == 2) {
				steppingOn = Element.elementType.grassFloor;
				ballUserData.accel = Floor.GRASS_DRAG;
			}
			if (obstacleUserData.type == Element.elementType.acceleratorFloor) {
				onSpeedPad = false;
			}
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

	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public Player getPlayer() {

		ElementType bodyUserData = (ElementType) body.getUserData();
		return bodyUserData.player;
	}

	/**
	 * Gets the body user data.
	 *
	 * @return the body user data
	 */
	public ElementType getBodyUserData() {
		return (ElementType) body.getUserData();
	}

	/**
	 * Gets the last pos.
	 *
	 * @return the last pos
	 */
	public Vector2 getLastPos() {
		return lastPos;
	}

	/**
	 * Sets the last pos.
	 *
	 * @param lastPos the new last pos
	 */
	public void setLastPos(Vector2 lastPos) {
		this.lastPos = lastPos.cpy();
	}

	/**
	 * Check elements touched.
	 */
	public void checkElementsTouched() {

		if (touchedVoid) {
			body.setLinearVelocity(0f, 0f);
			teleport(startPos, false);
			lastPos = startPos.cpy();
			touchedVoid = false;
		}

		if (touchedWater) {
			body.setLinearVelocity(0f, 0f);
			teleport(lastPos, false);
			touchedWater = false;
		}

		if (touchedTeleporter) {
			teleport(teleportDestination, true);
			touchedTeleporter = false;
		}
	}

	/**
	 * Checks if is touched void.
	 *
	 * @return true, if is touched void
	 */
	public boolean isTouchedVoid() {
		return touchedVoid;
	}

	/**
	 * Sets the touched void.
	 *
	 * @param touchedVoid the new touched void
	 */
	public void setTouchedVoid(boolean touchedVoid) {
		this.touchedVoid = touchedVoid;
	}

	/**
	 * Checks if is touched water.
	 *
	 * @return true, if is touched water
	 */
	public boolean isTouchedWater() {
		return touchedWater;
	}

	/**
	 * Sets the touched water.
	 *
	 * @param touchedWater the new touched water
	 */
	public void setTouchedWater(boolean touchedWater) {
		this.touchedWater = touchedWater;
	}

	/**
	 * Teleport.
	 *
	 * @param destination the destination
	 * @param keepVelocity the keep velocity
	 */
	public void teleport(Vector2 destination, boolean keepVelocity) {

		if (keepVelocity) {
			velocityBeforeTeleport = body.getLinearVelocity().cpy();
		}

		ElementType element = (ElementType) this.getBody().getUserData();
		Player player = element.player;
		World w = body.getWorld();

		destroyBody();
		createBody(w, player, destination); // Recreates ball on the
											// lastPosition where it had stopped

		if (keepVelocity) {
			body.setLinearVelocity(velocityBeforeTeleport);
		}

	}

	/**
	 * Gets the velocity before teleport.
	 *
	 * @return the velocity before teleport
	 */
	public Vector2 getVelocityBeforeTeleport() {
		return velocityBeforeTeleport;
	}

	/**
	 * Sets the velocity before teleport.
	 *
	 * @param velocityBeforeTeleport the new velocity before teleport
	 */
	public void setVelocityBeforeTeleport(Vector2 velocityBeforeTeleport) {
		this.velocityBeforeTeleport = velocityBeforeTeleport;
	}

	/**
	 * Checks if is on speed pad.
	 *
	 * @return true, if is on speed pad
	 */
	public boolean isOnSpeedPad() {
		return onSpeedPad;
	}

	/**
	 * Sets the on speed pad.
	 *
	 * @param onSpeedPad the new on speed pad
	 */
	public void setOnSpeedPad(boolean onSpeedPad) {
		this.onSpeedPad = onSpeedPad;
	}

}
