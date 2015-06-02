package com.lpoo.MiniGolf.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.logic.Element.elementType;
import com.lpoo.MiniGolf.screens.GameScreen;

public class MiniGolf extends Game {

	public static SpriteBatch batch;
	private static OrthographicCamera cam;
	private static World W;
	

	static int ballN = 0;
	private static ArrayList<Player> players;

	// FOR TEST PURPOSES
	private static ArrayList<Element> ele;

	Course c;
	Point endPoint;
	Point startPoint;
	int tacadasMax;
	int tempoMax;
	int courseHeight;
	int courseWidth;

	public static final int BOX_TO_WORLD = 100;
	public static final String TITLE = "Game Project";
	public static float WIDTH = 1000;
	public static float HEIGHT = 1000;

	public static ArrayList<Element> getEle() {
		return ele;
	}

	public static void setEle(ArrayList<Element> ele) {
		MiniGolf.ele = ele;
	}

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

	public static ArrayList<Player> getPlayers() {
		return players;
	}

	public static void setPlayers(ArrayList<Player> player) {
		players = player;
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
	
	void createEdge(float x1, float y1, float x2, float y2) {
		  BodyDef bodyDef = new BodyDef();
		  bodyDef.type = BodyDef.BodyType.StaticBody;
		  EdgeShape shape = new EdgeShape();
		  shape.set(new Vector2(x1,y1) , new Vector2(x2,y2)  );
		  FixtureDef fixtureDef = new FixtureDef();
		  fixtureDef.shape = shape;
		  Body body = W.createBody(bodyDef);
		  body.createFixture(fixtureDef);
	}

	public void create() {

		Assets.queueLoading();
		
		
		// INITIALIZING SINGLETONS
		
		batch = new SpriteBatch();
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		W = new World(new Vector2(0, 0), false);

		ele = new ArrayList<Element>();
		// Grass to Test
		
		ele.add(new GrassFloor(new Vector2( 3*(WIDTH / 4f / BOX_TO_WORLD) , 3*(WIDTH / 4f / BOX_TO_WORLD)), WIDTH / 2f / BOX_TO_WORLD, HEIGHT / 2f / BOX_TO_WORLD, W));
		//System.out.println("Grass X: " + ele.get(0).body.getPosition().x);
		//System.out.println("Grass Y: " + ele.get(0).body.getPosition().y);
		//ele.add(new GrassFloor(new Vector2(5 * (WIDTH / 8f / BOX_TO_WORLD) , 5 * (WIDTH / 8f / BOX_TO_WORLD)), WIDTH / 4f / BOX_TO_WORLD, HEIGHT / 4f / BOX_TO_WORLD, W));
		//ele.add(new GrassFloor(new Vector2(5 * (WIDTH / 8f / BOX_TO_WORLD) , 7 * (HEIGHT / 8f / BOX_TO_WORLD)), WIDTH / 4f / BOX_TO_WORLD, HEIGHT / 4f / BOX_TO_WORLD, W));
		//ele.add(new GrassFloor(new Vector2(7 * (WIDTH / 8f / BOX_TO_WORLD) , 5 * (HEIGHT / 8f / BOX_TO_WORLD)), WIDTH / 4f / BOX_TO_WORLD, HEIGHT / 4f / BOX_TO_WORLD, W));
		//ele.add(new GrassFloor(new Vector2(7 * (WIDTH / 8f / BOX_TO_WORLD) , 7 * (HEIGHT / 8f / BOX_TO_WORLD)), WIDTH / 4f / BOX_TO_WORLD, HEIGHT / 4f / BOX_TO_WORLD, W));
		
		createEdge(0.0f, 0.0f, WIDTH/BOX_TO_WORLD,0.0f);
		createEdge( WIDTH/BOX_TO_WORLD,0.0f, WIDTH/BOX_TO_WORLD, HEIGHT/BOX_TO_WORLD);
		createEdge(WIDTH/BOX_TO_WORLD, HEIGHT/BOX_TO_WORLD, 0.0f, HEIGHT/BOX_TO_WORLD);
		createEdge(0.0f, HEIGHT/BOX_TO_WORLD, 0.0f, 0.0f);
				  		
		
		cam.update();
		cam.translate(new Vector2(WIDTH / 2, HEIGHT / 2));
		System.out.println("Cam X: " + cam.position.x/BOX_TO_WORLD);
		System.out.println("Cam Y: " + cam.position.y/BOX_TO_WORLD);

		players = new ArrayList<Player>();

		initGame(1);
		GameScreen game = new GameScreen();
		this.setScreen(new GameScreen());
		Gdx.input.setInputProcessor(game);

	}

	public void initGame(int nPlayers) {
		for (int i = 0; i < nPlayers; i++) {
			
			Vector2 pos = new Vector2(5f, 5f);
			float radius = 2.5f;
			
			
			Ball ball = new Ball(new Vector2(5f, 5f), W, 0.25f);
			ball.getBody().setUserData(new ElementType(elementType.ball, 0));
			ball.getBody().setLinearVelocity(new Vector2(3, 3));
			players.add(new Player(ball));
			
			
			Ball ball2 = new Ball(new Vector2(1f, 1f), W, 0.25f);
			ball2.getBody().setUserData(new ElementType(elementType.ball, 1));
			ball2.getBody().setLinearVelocity(new Vector2(3, 3));
			players.add(new Player(ball2));
			System.out.println("initGame");
			
		}
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

	public static float getHeight() {
		return HEIGHT;
	}

	public static void setHeight(int height) {
		HEIGHT = height;
	}

	public static float getWidth() {
		return WIDTH;
	}

	public static void setWidth(int width) {
		WIDTH = width;
	}
}
