package com.lpoo.MiniGolf.logic;

public class Element {
	Point pos;
	Point oldPos;
	int height;
	int width;
	double aceleracaoX;
	double aceleracaoY;

	public Element() {
		pos = new Point();
		oldPos = new Point();
		height = 0;
		width = 0;
		aceleracaoX = 0.0;
		aceleracaoY = 0.0;
	}

	public Element(Point pos, int height, int width, double aceleracaoX, double aceleracaoY) {
		this.pos = pos;
		this.oldPos = pos;
		this.height = height;
		this.width = width;
		this.aceleracaoX = aceleracaoX;
		this.aceleracaoY = aceleracaoY;
	}

	public int getPosX() {
		return pos.getX();
	}

	public int getPosY() {
		return pos.getY();
	}

	public void setPosX(int x) {
		this.pos.setX(x);
	}

	public void setPosY(int y) {
		this.pos.setY(y);
	}

	public int getOldPosX() {
		return oldPos.getX();
	}

	public int getOldPosY() {
		return oldPos.getY();
	}

	public void setOldPosX(int x) {
		this.oldPos.setX(x);
	}

	public void setOldPosY(int y) {
		this.pos.setY(y);
	}

	public Point getPos() {
		return pos;
	}

	public void setPos(Point pos) {
		this.pos = pos;
	}

	public Point getOldPos() {
		return oldPos;
	}

	public void setOldPos(Point oldPos) {
		this.oldPos = oldPos;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public double getAceleracaoX() {
		return aceleracaoX;
	}

	public void setAceleracaoX(double aceleracaoX) {
		this.aceleracaoX = aceleracaoX;
	}

	public double getAceleracaoY() {
		return aceleracaoY;
	}

	public void setAceleracaoY(double aceleracaoY) {
		this.aceleracaoY = aceleracaoY;
	}

}
