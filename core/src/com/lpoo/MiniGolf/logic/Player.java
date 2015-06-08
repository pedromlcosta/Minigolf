package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

// TODO: Auto-generated Javadoc
/**
 * The Class Player.
 */
public class Player {

	/** The player id. */
	private int playerID; // Starts at 0

	/** The ball. */
	private Ball ball;

	/**
	 * Number of total shots the player has attempted. By the end of the game,
	 * the player with the less shots wins
	 */
	private int tacadaTotal;

	/** The play time, during the current turn where the player is playing. */
	private int playTime = 0;

	/** Tells us if this player was the one to play in the last turn. */
	private boolean justPlayed;

	/** Indicates whether a player has ended the current course he is playing on */
	private boolean over;

	/**
	 * Instantiates a new player.
	 *
	 * @param id
	 *            the id of the player
	 */
	public Player(int id) {
		playerID = id;
		justPlayed = false;
		tacadaTotal = 0;
		over = false;
	}

	/**
	 * Instantiates a new player.
	 *
	 * @param b
	 *            the ball the player owns
	 */
	public Player(Ball b) {
		justPlayed = false;
		ball = b;
		tacadaTotal = 0;
		over = false;
	}

	/**
	 * Creates the ball.
	 *
	 * @param pos
	 *            the starting position of the players ball
	 * @param w
	 *            the world where the ball body will be created
	 * @param radius
	 *            the radius of the ball
	 */
	public void createBall(Vector2 pos, World w, float radius) {
		ball = new Ball(pos, w, radius, this);
	}

	/**
	 * Gets the ball.
	 *
	 * @return the ball
	 */
	public Ball getBall() {
		return ball;
	}

	/**
	 * Sets the ball.
	 *
	 * @param ball
	 *            the new ball
	 */
	public void setBall(Ball ball) {
		this.ball = ball;
	}

	/**
	 * Gets the number of total shots attempted by the player
	 *
	 * @return the number of total shots
	 */
	public int getTacadaTotal() {
		return tacadaTotal;
	}

	/**
	 * Sets the number of total shots.
	 *
	 * @param tacadaTotal
	 *            the new number of total shots
	 */
	public void setTacadaTotal(int tacadaTotal) {
		this.tacadaTotal = tacadaTotal;
	}

	/**
	 * Gets the ball x position
	 *
	 * @return the ball x position
	 */
	public float getBallPosX() {
		return ball.body.getPosition().x;
	}

	/**
	 * Gets the ball body.
	 *
	 * @return the ball body
	 */
	public Body getBallBody() {
		return ball.body;
	}

	/**
	 * Sets the ball body.
	 *
	 * @param body
	 *            the new ball body
	 */
	public void setBallBody(Body body) {
		this.ball.body = body;
	}

	/**
	 * Gets the ball's speed.
	 *
	 * @return the ball's speed
	 */
	public Vector2 getBallSpeed() {
		return ball.body.getLinearVelocity();
	}

	/**
	 * Sets the ball speed.
	 *
	 * @param vX
	 *            the speed in the x axis
	 * @param vY
	 *            the speed in the y axis
	 */
	public void setBallSpeed(float vX, float vY) {
		this.ball.body.setLinearVelocity(vX, vY);
	}

	/**
	 * Gets the ball absolute speed.
	 *
	 * @return the ball absolute speed.
	 */
	public float getBallSpeedLen() {
		return ball.body.getLinearVelocity().len();
	}

	/**
	 * Gets the ball y position.
	 *
	 * @return the ball y position.
	 */
	public float getBallPosY() {
		return ball.body.getPosition().y;
	}

	/**
	 * Gets the ball starting x position.
	 *
	 * @return the ball starting x position.
	 */
	public float getBallOldPosX() {
		return ball.startPos.x;
	}

	/**
	 * Gets the ball starting y position.
	 *
	 * @return the ball starting y position.
	 */
	public float getBallOldPosY() {
		return ball.startPos.y;
	}

	/**
	 * Sets the ball starting x position.
	 *
	 * @param x
	 *            the new ball starting x position.
	 */
	public void setBallOldPosX(float x) {
		this.ball.startPos.x = x;
	}

	/**
	 * Gets the ball position.
	 *
	 * @return the ball position.
	 */
	public Vector2 getBallPos() {
		return ball.body.getPosition();
	}

	/**
	 * Gets the ball starting position.
	 *
	 * @return the ball starting position.
	 */
	public Vector2 getBallOldPos() {
		return ball.startPos;
	}

	/**
	 * Sets the ball starting position.
	 *
	 * @param oldPos
	 *            the new ball starting position.
	 */
	public void setBallOldPos(Vector2 oldPos) {
		this.ball.startPos = oldPos;
	}

	/**
	 * Gets the ball height.
	 *
	 * @return the ball height
	 */
	public float getBallHeight() {
		return ball.height;
	}

	/**
	 * Sets the ball height.
	 *
	 * @param height
	 *            the new ball height
	 */
	public void setBallHeight(int height) {
		this.ball.height = height;
	}

	/**
	 * Gets the ball width.
	 *
	 * @return the ball width
	 */
	public float getBallWidth() {
		return ball.width;
	}

	/**
	 * Sets the ball width.
	 *
	 * @param width
	 *            the new ball width
	 */
	public void setBallWidth(int width) {
		this.ball.width = width;
	}

	/**
	 * Checks if player ended the current course.
	 *
	 * @return true, if it is over.
	 */
	public boolean isOver() {
		return over;
	}

	/**
	 * Sets the over value.
	 *
	 * @param over
	 *            true or false, whether he has finished the course or not
	 */
	public void setOver(boolean over) {
		this.over = over;
	}

	/**
	 * Checks if player has just played in the last turn.
	 *
	 * @return true, if he has just played
	 */
	public boolean isJustPlayed() {
		return justPlayed;
	}

	/**
	 * Sets the just played.
	 *
	 * @param justPlayed
	 *            the new value, indicating if the player has just played
	 */
	public void setJustPlayed(boolean justPlayed) {
		this.justPlayed = justPlayed;
	}

	/**
	 * Gets the play time of the turn.
	 *
	 * @return the play time of the turn
	 */
	public int getPlayTime() {
		return playTime;
	}

	/**
	 * Sets the play time of the turn.
	 *
	 * @param playTime
	 *            the new play time of the turn.
	 */
	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}

	/**
	 * Gets the player id.
	 *
	 * @return the player id
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Sets the player id.
	 *
	 * @param playerID
	 *            the new player id
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * Adds 1 shot to the total shots.
	 */
	public void addTacadaTotal() {
		tacadaTotal++;
	}

}
