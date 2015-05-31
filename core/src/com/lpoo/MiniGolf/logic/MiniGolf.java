package com.lpoo.MiniGolf.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.screens.GameScreen;

public class MiniGolf extends Game {
	
	public static SpriteBatch batch;	
	private static OrthographicCamera cam;
	private static World W;
	
	static int ballN = 0;
	private ArrayList<Player> players;
	
	//FOR TEST PURPOSES
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
	public static int WIDTH = 1000;
	public static int HEIGHT = 1000;
	

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
		
		Assets.queueLoading();

		// INITIALIZING SINGLETONS
		batch = new SpriteBatch();
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		W = new World(new Vector2(0, 0), false);
		
		W.setContactListener(new ContactListener() {

			public void beginContact(Contact arg0) {
				Body bodyA = arg0.getFixtureA().getBody();
                Body bodyB = arg0.getFixtureB().getBody();
                
                if((bodyA.getUserData() == Element.elementType.ball && bodyB.getUserData() == Element.elementType.regularFloor)){
                	bodyA.applyForceToCenter(-4 * (bodyA.getLinearVelocity().x/bodyA.getLinearVelocity().len2()), -4 * (bodyA.getLinearVelocity().y/bodyA.getLinearVelocity().len2()), false);
                	System.out.println(bodyA.getLinearVelocity().x +"" + bodyA.getLinearVelocity().y);
                }
                if((bodyA.getUserData() == Element.elementType.regularFloor && bodyB.getUserData() == Element.elementType.ball)){
                	bodyB.applyForceToCenter(-4 * (bodyB.getLinearVelocity().x/bodyB.getLinearVelocity().len2()), -4 * (bodyB.getLinearVelocity().y/bodyB.getLinearVelocity().len2()), false);
                	System.out.println(bodyB.getLinearVelocity().x +"" + bodyB.getLinearVelocity().y);
                }
                
			}

			public void endContact(Contact arg0) {
				Fixture fixtureA = arg0.getFixtureA();
                Fixture fixtureB = arg0.getFixtureB();
                //Gdx.app.log("endContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
			}

			public void postSolve(Contact arg0, ContactImpulse arg1) {
			}

			public void preSolve(Contact arg0, Manifold arg1) {
			}

		});
		ele = new ArrayList<Element>();
		int count = 0;
		//Grass to Test
		for (int j = 0; j < 10; j++){
			for(int i = 0; i < 10; i++){
			ele.add(new GrassFloor(new Vector2(i+0.5f , j+0.5f ), 1, 1, W));
			count++;
			}
		}
		
		
		cam.update();
		cam.translate(new Vector2(WIDTH / 2, HEIGHT / 2));
		System.out.println(count++);
		System.out.println(cam.position);
		
		players = new ArrayList<Player>();
				
		initGame(1);
		System.out.println("Before setScreen");
		this.setScreen(new GameScreen());
		System.out.println("After setScreen");

	}

	public void fillObstacle() {

	}

	public void initGame(int nPlayers) {
		for (int i = 0; i < nPlayers; i++) {
			Ball ball = new Ball(new Vector2(0.5f, 0.5f), W, 1);
			ele.add(ball);
			players.add(new Player(ball));
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
