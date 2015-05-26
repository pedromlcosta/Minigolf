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
	SpriteBatch batch;
	Sprite img;
	Sprite grass;
	static int ballN = 0;
	ArrayList<Player> players;

	Course c;
	Point endPoint;
	Point startPoint;
	int tacadasMax;
	int tempoMax;
	World W;
	int courseHeight;
	int courseWidth;
	Matrix4 debugMatrix;
	Box2DDebugRenderer debugRenderer;
	static final int PIXEL_METER = 100;
	public static final String TITLE = "Game Project";
	public static int WIDTH = 400;
	public static int HEIGHT = 400;
	OrthographicCamera cam;

	public enum obstacleType {
		glueWall, waterFloor, iceFloor, regularWall, sandFloor, normalSquareWall, normalTriangularWall, bonceWall, speedWall, teleporter, squareOne, regularFloor, accFloor, accWall, startSquareWall, startTriangularWall,
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
		grass = new Sprite(new Texture("grass.png"));
		img = new Sprite(new Texture("bola0.png"));
		img.setPosition(0, 0);
		img.setSize(5, 5);

		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.update();
		// cam.translate(new Vector2(0, 0));
		System.out.println(cam.position);
		debugMatrix = new Matrix4(cam.combined);
		debugMatrix.scale(100f, 100f, 0f);
		debugRenderer = new Box2DDebugRenderer();
		players = new ArrayList<Player>();

		initGame(1);

	}

	public void fillObstacle() {

	}

	public void initGame(int nPlayers) {
		for (int i = 0; i < nPlayers; i++) {
			Vector2 n = new Vector2(10, 10);
			Ball ball = new Ball(n, 20, 20, this.W, 10);
			ball.image = img.getTexture();
			players.add(new Player(ball));

		}
	}

	// testes
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		int xi = 200;
		int yi = 200;
		batch.draw(players.get(0).getBall().getImage(), xi, yi, 50, 50);
		batch.end();

		debugRenderer.render(W, cam.combined);
		W.step(1f / 60, 6, 2);
		debugMatrix = batch.getProjectionMatrix().cpy().scale(100, 100, 0);

	}

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
