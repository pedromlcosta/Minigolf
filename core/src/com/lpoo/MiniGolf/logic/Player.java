package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Player {

	Ball ball;
	int pontuacao;
	int tacadasJogada;
	int tacadaTotal;
	boolean over;
	boolean won;

	Player() {
		ball = new Ball();
		pontuacao = 0;
		tacadasJogada = 0;
		tacadaTotal = 0;
		over = false;
		won = false;
	}

	Player(Ball b) {
		ball = b;
		pontuacao = 0;
		tacadasJogada = 0;
		tacadaTotal = 0;
		over = false;
		won = false;
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

	public float getBallPosX() {
		return ball.body.getPosition().x;
	}

	public Body getBallBody() {
		return ball.body;
	}

	public void setBallBody(Body body) {
		this.ball.body = body;
	}

	public float getBallPosY() {
		return ball.body.getPosition().y;
	}

	public float getBallOldPosX() {
		return ball.oldPos.x;
	}

	public float getBallOldPosY() {
		return ball.oldPos.y;
	}

	public void setBallOldPosX(float x) {
		this.ball.oldPos.x = x;
	}

	public Vector2 getBallPos() {
		return ball.body.getPosition();
	}

	public Vector2 getBallOldPos() {
		return ball.oldPos;
	}

	public void setBallOldPos(Vector2 oldPos) {
		this.ball.oldPos = oldPos;
	}

	public int getBallHeight() {
		return ball.height;
	}

	public void setBallHeight(int height) {
		this.ball.height = height;
	}

	public int getBallWidth() {
		return ball.width;
	}

	public void setBallWidth(int width) {
		this.ball.width = width;
	}

}
