package com.lpoo.MiniGolf.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.screens.Splash;

public class MiniGolf extends Game {
	SpriteBatch batch;
	Sprite img;
	static int ballN = 0;
	ArrayList<Player> players;
	ArrayList<Obstacle> obstacles;
	Point endPoint;
	Point startPoint;
	int tacadasMax;
	int tempoMax;
	World W;
	int courseHeight;
	int courseWidth;

	public static final String TITLE = "Game Project";
	public static int WIDTH = 480; // used later to set
									// window size
	public static int HEIGHT = 800;

	public enum obstacleType {
		normalSquareWall, normalTriangularWall,bonceWall, speedWall,teleporter, squareOne, regularFloor, accFloor, accWall, startSquareWall, startTriangularWall,
	};

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public World getW() {
		return W;
	}

	public void setW(World w) {
		W = w;
	}

	public int getCourseHeight() {
		return courseHeight;
	}

	public void setCourseHeight(int courseHeight) {
		this.courseHeight = courseHeight;
	}

	public int getCourseWidth() {
		return courseWidth;
	}

	public void setCourseWidth(int courseWidth) {
		this.courseWidth = courseWidth;
	}

	public MiniGolf() {

	}

	public void create() {
		// batch = new SpriteBatch();
		// img = new Sprite(new Texture("hero.png"));
		System.out.println("Teste\n");
		setScreen(new Splash());
	}

	// testes
	// public void render() {
	// // W = new World(new Vector2(0, -10), true);
	// //
	// // Gdx.gl.glClearColor(1, 0, 0, 1);
	// // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	// // batch.begin();
	// // int xi = 0;
	// // int yi = 0;
	// // for (int i = 0; i < 15; i++) {
	// // batch.draw(img.getTexture(), xi, yi, 20, 20);
	// // xi += 20;
	// // yi += 20;
	// // }
	// // batch.end();
	//
	//
	// }

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public Sprite getImg() {
		return img;
	}

	public void setImg(Sprite img) {
		this.img = img;
	}

	public ArrayList<Player> getBalls() {
		return players;
	}

	public void setBalls(ArrayList<Player> players) {
		this.players = players;
	}

	public Player getBall(int i) {

		if (i < players.size())
			return players.get(i);
		else
			return null;
	}

	public int getNBalls() {
		return players.size();
	}

	public int getNObstacles() {
		return obstacles.size();
	}

	public Obstacle getObstacle(int i) {

		if (i < obstacles.size())
			return obstacles.get(i);
		else
			return null;
	}

	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}

	public void setObstacles(ArrayList<Obstacle> obstacles) {
		this.obstacles = obstacles;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public int getTacadasMax() {
		return tacadasMax;
	}

	public void setTacadasMax(int tacadasMax) {
		this.tacadasMax = tacadasMax;
	}

	public int getTempoMax() {
		return tempoMax;
	}

	public void setTempoMax(int tempoMax) {
		this.tempoMax = tempoMax;
	}

	public static int getBallN() {
		return ballN;
	}

	public static void setBallN(int ballN) {
		MiniGolf.ballN = ballN;
	}

	public int getHeight() {
		return HEIGHT;
	}

	public void setHeight(int height) {
		this.HEIGHT = height;
	}

	public int getWidth() {
		return WIDTH;
	}

	public void setWidth(int width) {
		this.WIDTH = width;
	}
}
