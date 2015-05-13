package com.lpoo.MiniGolf.logic;

public class Ball extends Element {
	int number;
	double velocidadeX;
	double velocidadeY;

	public Ball() {
		super();
		number = MiniGolf.ballN;
		velocidadeX = 0;
		velocidadeY = 0;
		MiniGolf.ballN++;
	}

	public Ball(Point pos, int height, int width, double aceleracaoX, double aceleracaoY, double velocidadeX, double velocidadeY) {
		super(pos, height, width, aceleracaoX, aceleracaoY);
		number = MiniGolf.ballN;
		this.velocidadeX = velocidadeX;
		this.velocidadeY = velocidadeY;
		MiniGolf.ballN++;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getVelocidadeX() {
		return velocidadeX;
	}

	public void setVelocidadeX(double velocidadeX) {
		this.velocidadeX = velocidadeX;
	}

	public double getVelocidadeY() {
		return velocidadeY;
	}

	public void setVelocidadeY(double velocidadeY) {
		this.velocidadeY = velocidadeY;
	}

}
