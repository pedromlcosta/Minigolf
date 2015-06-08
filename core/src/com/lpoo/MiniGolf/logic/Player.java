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
	private int playerID;  //Starts at 0
	
	/** The ball. */
	private Ball ball;
	
	/** The pontuacao. */
	private int pontuacao;
	
	/** The tacadas jogada. */
	private int tacadasJogada;
	
	/** The tacada total. */
	private int tacadaTotal;
	
	/** The play time. */
	private int playTime = 0;
	
	/** The just played. */
	private boolean justPlayed;
	
	/** The over. */
	private boolean over;
	
	/** The won. */
	private boolean won;
	
	/** The club movement. */
	private Vector2 clubMovement;

	/**
	 * Instantiates a new player.
	 *
	 * @param id the id
	 */
	public Player(int id) {
		playerID = id;
		justPlayed = false;
		pontuacao = 0;
		tacadasJogada = 0;
		tacadaTotal = 0;
		over = false;
		won = false;
	}

	/**
	 * Instantiates a new player.
	 *
	 * @param b the b
	 */
	public Player(Ball b) {
		justPlayed = false;
		ball = b;
		pontuacao = 0;
		tacadasJogada = 0;
		tacadaTotal = 0;
		over = false;
		won = false;
	}
	
	/**
	 * Creates the ball.
	 *
	 * @param pos the pos
	 * @param w the w
	 * @param radius the radius
	 */
	public void createBall(Vector2 pos, World w, float radius){
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
	 * @param ball the new ball
	 */
	public void setBall(Ball ball) {
		this.ball = ball;
	}

	/**
	 * Gets the pontuacao.
	 *
	 * @return the pontuacao
	 */
	public int getPontuacao() {
		return pontuacao;
	}

	/**
	 * Sets the pontuacao.
	 *
	 * @param pontuacao the new pontuacao
	 */
	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

	/**
	 * Gets the tacadas jogada.
	 *
	 * @return the tacadas jogada
	 */
	public int getTacadasJogada() {
		return tacadasJogada;
	}

	/**
	 * Sets the tacadas jogada.
	 *
	 * @param tacadasJogada the new tacadas jogada
	 */
	public void setTacadasJogada(int tacadasJogada) {
		this.tacadasJogada = tacadasJogada;
	}

	/**
	 * Gets the tacada total.
	 *
	 * @return the tacada total
	 */
	public int getTacadaTotal() {
		return tacadaTotal;
	}

	/**
	 * Sets the tacada total.
	 *
	 * @param tacadaTotal the new tacada total
	 */
	public void setTacadaTotal(int tacadaTotal) {
		this.tacadaTotal = tacadaTotal;
	}

	/**
	 * Gets the club movement.
	 *
	 * @return the club movement
	 */
	public Vector2 getClubMovement() {
		return clubMovement;
	}

	/**
	 * Sets the club movement.
	 *
	 * @param clubMovement the new club movement
	 */
	public void setClubMovement(Vector2 clubMovement) {
		this.clubMovement = clubMovement;
	}
	
	/**
	 * Gets the ball pos x.
	 *
	 * @return the ball pos x
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
	 * @param body the new ball body
	 */
	public void setBallBody(Body body) {
		this.ball.body = body;
	}
	
	/**
	 * Gets the ball speed.
	 *
	 * @return the ball speed
	 */
	public Vector2 getBallSpeed() {
		return ball.body.getLinearVelocity();
	}

	/**
	 * Sets the ball speed.
	 *
	 * @param vX the v x
	 * @param vY the v y
	 */
	public void setBallSpeed(float vX, float vY) {
		this.ball.body.setLinearVelocity(vX, vY);
	}
	
	/**
	 * Gets the ball speed len.
	 *
	 * @return the ball speed len
	 */
	public float getBallSpeedLen() {
		return ball.body.getLinearVelocity().len();
	}

	/**
	 * Gets the ball pos y.
	 *
	 * @return the ball pos y
	 */
	public float getBallPosY() {
		return ball.body.getPosition().y;
	}

	/**
	 * Gets the ball old pos x.
	 *
	 * @return the ball old pos x
	 */
	public float getBallOldPosX() {
		return ball.startPos.x;
	}

	/**
	 * Gets the ball old pos y.
	 *
	 * @return the ball old pos y
	 */
	public float getBallOldPosY() {
		return ball.startPos.y;
	}

	/**
	 * Sets the ball old pos x.
	 *
	 * @param x the new ball old pos x
	 */
	public void setBallOldPosX(float x) {
		this.ball.startPos.x = x;
	}

	/**
	 * Gets the ball pos.
	 *
	 * @return the ball pos
	 */
	public Vector2 getBallPos() {
		return ball.body.getPosition();
	}

	/**
	 * Gets the ball old pos.
	 *
	 * @return the ball old pos
	 */
	public Vector2 getBallOldPos() {
		return ball.startPos;
	}

	/**
	 * Sets the ball old pos.
	 *
	 * @param oldPos the new ball old pos
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
	 * @param height the new ball height
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
	 * @param width the new ball width
	 */
	public void setBallWidth(int width) {
		this.ball.width = width;
	}

	/**
	 * Checks if is over.
	 *
	 * @return true, if is over
	 */
	public boolean isOver() {
		return over;
	}

	/**
	 * Sets the over.
	 *
	 * @param over the new over
	 */
	public void setOver(boolean over) {
		this.over = over;
	}

	/**
	 * Checks if is won.
	 *
	 * @return true, if is won
	 */
	public boolean isWon() {
		return won;
	}

	/**
	 * Sets the won.
	 *
	 * @param won the new won
	 */
	public void setWon(boolean won) {
		this.won = won;
	}

	/**
	 * Checks if is just played.
	 *
	 * @return true, if is just played
	 */
	public boolean isJustPlayed() {
		return justPlayed;
	}

	/**
	 * Sets the just played.
	 *
	 * @param justPlayed the new just played
	 */
	public void setJustPlayed(boolean justPlayed) {
		this.justPlayed = justPlayed;
	}

	/**
	 * Gets the play time.
	 *
	 * @return the play time
	 */
	public int getPlayTime() {
		return playTime;
	}

	/**
	 * Sets the play time.
	 *
	 * @param playTime the new play time
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
	 * @param playerID the new player id
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	/**
	 * Adds the tacada total.
	 */
	public void addTacadaTotal(){
		tacadaTotal++;
	}
	
	/**
	 * Adds the tacada jogada.
	 */
	public void addTacadaJogada(){
		tacadasJogada++;
	}

}
