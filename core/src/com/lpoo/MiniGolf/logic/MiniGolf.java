package com.lpoo.MiniGolf.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.logic.Element.elementType;
import com.lpoo.MiniGolf.logic.ElementType;
import com.lpoo.MiniGolf.screens.GameScreen;

public class MiniGolf extends Game {

	public static SpriteBatch batch;
	private static OrthographicCamera cam;
	private static World W;
	

	private static int nrPlayers = 2;
	private static ArrayList<Player> players = new ArrayList<Player>();
	private static Course currentCourse = new Course();
	private static ArrayList<Course> selectedCourses = new ArrayList<Course>();
	// FOR TEST PURPOSES
	private static ArrayList<Element> ele;

	private static boolean randomCourse;
	private Point endPoint;
	private Point startPoint;
	private int tacadasMax;
	private int tempoMax;
	private int courseHeight;
	private int courseWidth;

	public static final float BOX_TO_WORLD = 100f;
	public static final String TITLE = "Game Project";
	public static float WIDTH = 1000;
	public static float HEIGHT = 1000;
	
	public MiniGolf() {
	}

	public void create() {

		Assets.queueLoading();
		
		
		// INITIALIZING SINGLETONS
		
		batch = new SpriteBatch();
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		W = new World(new Vector2(0, 0), false);

		// Grass to Test
		
		setCurrentCourse(new Course());
		Floor grass1 = new Floor(new Vector2( (WIDTH / 2f / BOX_TO_WORLD) , (HEIGHT /2f/ BOX_TO_WORLD)), WIDTH / BOX_TO_WORLD, HEIGHT / BOX_TO_WORLD, W, elementType.grassFloor);
		ElementType element1 = (ElementType) grass1.getBody().getUserData();
		element1.id = 1;
		addCourseElement(grass1);
//		GrassFloor grass2 = new GrassFloor(new Vector2( (WIDTH / 2f / BOX_TO_WORLD) , (HEIGHT /2f/ BOX_TO_WORLD)), WIDTH / 2f/ BOX_TO_WORLD, HEIGHT / 2f/ BOX_TO_WORLD, W);
//		ElementType element2 = (ElementType) grass2.getBody().getUserData();
//		element2.id = 2;
//		GrassFloor grass3 = new GrassFloor(new Vector2( (WIDTH / 2f / BOX_TO_WORLD) , (HEIGHT /2f/ BOX_TO_WORLD)), WIDTH / 2f/ BOX_TO_WORLD, HEIGHT / 2f/ BOX_TO_WORLD, W);
//		ElementType element3 = (ElementType) grass3.getBody().getUserData();
//		element3.id = 3;
//		
//		addCourseElement(grass2);
//		addCourseElement(grass3);
		
		//ele.add(new GrassFloor(new Vector2( 3*(WIDTH / 4f / BOX_TO_WORLD) , 3*(WIDTH / 4f / BOX_TO_WORLD)), WIDTH / 2f / BOX_TO_WORLD, HEIGHT / 2f / BOX_TO_WORLD, W));
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

		//
		initGame(2);
		
		GameScreen game = new GameScreen();
		this.setScreen(new GameScreen());
		Gdx.input.setInputProcessor(game);

	}

	public void initGame(int nPlayers) {
		for (int i = 0; i < nPlayers; i++) {
			Vector2 pos = new Vector2(5f, 5f);
			float radius = 2.5f;
			
			//Vector2 ballPos = courseBallPos.get(i);
			Ball ball = new Ball(new Vector2(i+1, i+1), W, 0.5f);
			ball.getBody().setUserData(new ElementType(elementType.ball, i));
			Player player = new Player(ball);
			players.add(player);
						
		}
	}
	
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

	public static Course getCurrentCourse() {
		return currentCourse;
	}

	public static void setCurrentCourse(Course course) {
		currentCourse = course;
	}
	
	public static ArrayList<Element> getCourseElements(){
		return currentCourse.getElementos();
	}
	
	/*
	 * Adds element to the Element array in the selected course
	 */
	public static  void addCourseElement(Element e){
		System.out.println("Derp2");
		currentCourse.getElementos().add(e);
	}
	
	public static ArrayList<Course> getSelectedCourses() {
		return selectedCourses;
	}

	public static  void setSelectedCourses(ArrayList<Course> courses) {
		selectedCourses = courses;
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

	public Player getPlayer(int i) {

		if (i < players.size())
			return players.get(i);
		else
			return null;
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

	public static boolean isRandomCourse() {
		return randomCourse;
	}

	public static void setRandomCourse(boolean randomCourse) {
		MiniGolf.randomCourse = randomCourse;
	}

	public static int getNrPlayers() {
		return nrPlayers;
	}

	public static void setNrPlayers(int nrPlayers) {
		MiniGolf.nrPlayers = nrPlayers;
	}

}
