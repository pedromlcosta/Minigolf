package com.lpoo.MiniGolf.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class MiniGolf extends Game {
	
	private static SpriteBatch batch;	
	private static OrthographicCamera cam;
	private static World W;
	

	static int ballN = 0;
	ArrayList<Player> players;

	Course c;
	Point endPoint;
	Point startPoint;
	int tacadasMax;
	int tempoMax;
	int courseHeight;
	int courseWidth;
	
	static final int PIXEL_METER = 100;
	public static final String TITLE = "Game Project";
	public static int WIDTH = 400;
	public static int HEIGHT = 400;
	

	public static World getW() {
		return W;
	}

	public static void setW(World w) {
		W = w;
	}
	
	public static SpriteBatch getBatch() {
		return batch;
	}

	public static void setBatch(SpriteBatch batch) {
		MiniGolf.batch = batch;
	}
	
	public static OrthographicCamera getCam() {
		return cam;
	}

	public static void setCam(OrthographicCamera cam) {
		MiniGolf.cam = cam;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
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

		W = new World(new Vector2(0, 0), false);
		W.setContactListener(new ContactListener() {

			public void beginContact(Contact arg0) {
			}

			public void endContact(Contact arg0) {
			}

			public void postSolve(Contact arg0, ContactImpulse arg1) {
			}

			public void preSolve(Contact arg0, Manifold arg1) {
			}

		});
		ArrayList<Element> ele = new ArrayList<Element>();

		for (int i = 0; i < 10; i++)
			ele.add(new GrassFloor(new Vector2(10 + i * 10, 10 - i * 10), 10, 10, W));

		batch = new SpriteBatch();

		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.update();
		// cam.translate(new Vector2(0, 0));
		System.out.println(cam.position);
		
		players = new ArrayList<Player>();

		initGame(1);

	}

	public void fillObstacle() {

	}

	public void initGame(int nPlayers) {
		for (int i = 0; i < nPlayers; i++) {
			Vector2 n = new Vector2(10, 10);
			Ball ball = new Ball(n, 20, 20, this.W, 10);
			players.add(new Player(ball));

		}
	}

	// testes
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

	public static int getHeight() {
		return HEIGHT;
	}

	public static void setHeight(int height) {
		HEIGHT = height;
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static void setWidth(int width) {
		WIDTH = width;
	}
}
