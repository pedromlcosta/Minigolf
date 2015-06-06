package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Player {

	private int playerID;  //Starts at 0
	private Ball ball;
	private int pontuacao;
	private int tacadasJogada;
	private int tacadaTotal;
	private int playTime = 0;
	private boolean justPlayed;
	private boolean over;
	private boolean won;
	private Vector2 clubMovement;

	public Player(int id) {
		playerID = id;
		justPlayed = false;
		pontuacao = 0;
		tacadasJogada = 0;
		tacadaTotal = 0;
		over = false;
		won = false;
	}

	public Player(Ball b) {
		justPlayed = false;
		ball = b;
		pontuacao = 0;
		tacadasJogada = 0;
		tacadaTotal = 0;
		over = false;
		won = false;
	}
	
	public void createBall(Vector2 pos, World w, float radius){
		ball = new Ball(pos, w, radius, this);
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public int getPontuacao() {
		return pontuacao;
	}

	public void setPontuacao(int pontuacao) {
		this.pontuacao = pontuacao;
	}

	public int getTacadasJogada() {
		return tacadasJogada;
	}

	public void setTacadasJogada(int tacadasJogada) {
		this.tacadasJogada = tacadasJogada;
	}

	public int getTacadaTotal() {
		return tacadaTotal;
	}

	public void setTacadaTotal(int tacadaTotal) {
		this.tacadaTotal = tacadaTotal;
	}

	public Vector2 getClubMovement() {
		return clubMovement;
	}

	public void setClubMovement(Vector2 clubMovement) {
		this.clubMovement = clubMovement;
	}
	
	public float getBallPosX() {
		return ball.body.getPosition().x;
	}

	public Body getBallBody() {
		return ball.body;
	}

	public void setBallBody(Body body) {
		this.ball.body = body;
	}
	
	public Vector2 getBallSpeed() {
		return ball.body.getLinearVelocity();
	}

	public void setBallSpeed(float vX, float vY) {
		this.ball.body.setLinearVelocity(vX, vY);
	}
	
	public float getBallSpeedLen() {
		return ball.body.getLinearVelocity().len();
	}

	public float getBallPosY() {
		return ball.body.getPosition().y;
	}

	public float getBallOldPosX() {
		return ball.startPos.x;
	}

	public float getBallOldPosY() {
		return ball.startPos.y;
	}

	public void setBallOldPosX(float x) {
		this.ball.startPos.x = x;
	}

	public Vector2 getBallPos() {
		return ball.body.getPosition();
	}

	public Vector2 getBallOldPos() {
		return ball.startPos;
	}

	public void setBallOldPos(Vector2 oldPos) {
		this.ball.startPos = oldPos;
	}

	public float getBallHeight() {
		return ball.height;
	}

	public void setBallHeight(int height) {
		this.ball.height = height;
	}

	public float getBallWidth() {
		return ball.width;
	}

	public void setBallWidth(int width) {
		this.ball.width = width;
	}

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}

	public boolean isWon() {
		return won;
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public boolean isJustPlayed() {
		return justPlayed;
	}

	public void setJustPlayed(boolean justPlayed) {
		this.justPlayed = justPlayed;
	}

	public int getPlayTime() {
		return playTime;
	}

	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	public void addTacadaTotal(){
		tacadaTotal++;
	}
	
	public void addTacadaJogada(){
		tacadasJogada++;
	}

}
