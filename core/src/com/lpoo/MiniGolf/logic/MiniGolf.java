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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.logic.Element.elementType;
import com.lpoo.MiniGolf.screens.GameScreen;
import com.lpoo.MiniGolf.screens.MenuScreen;

public class MiniGolf extends Game {

	public static SpriteBatch batch;
	public static OrthographicCamera cam;
	public static Viewport viewport;
	private static World W;

	private int nrPlayers = 2;
	public static boolean allBallsStopped = false;
	private static ArrayList<Player> players = new ArrayList<Player>();
	private static ArrayList<Course> selectedCourses = new ArrayList<Course>();
	private static ArrayList<Course> allCourses = new ArrayList<Course>();


	private static int nrCourses = 2;
	public static final int MAX_PLAYERS = 4;

	private static boolean randomCourse;
	private Point endPoint;
	private Point startPoint;
	private int tacadasMax;
	private int tempoMax;
	private int courseHeight;
	private int courseWidth;

	public static final float BOX_TO_WORLD = 100f;
	public static final String TITLE = "Game Project";
	public static float WIDTH = 1920;
	public static float HEIGHT = 1080;

	public MiniGolf() {
	}

	public void create() {

		Assets.queueLoading();

		// INITIALIZING SINGLETONS

		batch = new SpriteBatch();
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		viewport = new FitViewport(WIDTH, HEIGHT, cam);
		W = new World(new Vector2(0, 0), false);

		// ///////////////////////////////////////////////////////////////////
		// /// TEST COURSE /////
		// ///////////////////////////////////////////////////////////////////

		//setCurrentCourse(new Course());
		
		//Course 1
		Floor grass1 = new Floor(new Vector2((WIDTH / 2f / BOX_TO_WORLD), (HEIGHT / 2f / BOX_TO_WORLD)), WIDTH / BOX_TO_WORLD, HEIGHT / BOX_TO_WORLD, elementType.grassFloor);
		Floor sand1 = new Floor(new Vector2(3 * (WIDTH / 4f / BOX_TO_WORLD), 3 * (HEIGHT / 4f / BOX_TO_WORLD)), WIDTH / 2f / BOX_TO_WORLD, HEIGHT / 2f / BOX_TO_WORLD, elementType.sandFloor);
		Hole hole1 = new Hole(new Vector2(5f, 5f), W, 0.3f);
		//addCourseElement(grass1);
		//addCourseElement(sand1);
		//addCourseElement(hole1);
		
		//Course 2
		Floor grass2 = new Floor(new Vector2((WIDTH / 2f / BOX_TO_WORLD), (HEIGHT / 2f / BOX_TO_WORLD)), WIDTH / BOX_TO_WORLD, HEIGHT / BOX_TO_WORLD, elementType.grassFloor);
		Floor sand2 = new Floor(new Vector2(1 * (WIDTH / 4f / BOX_TO_WORLD), 1 * (HEIGHT / 4f / BOX_TO_WORLD)), WIDTH / 2f / BOX_TO_WORLD, HEIGHT / 2f / BOX_TO_WORLD, elementType.sandFloor);
		Hole hole2 = new Hole(new Vector2(7f, 7f), W, 0.3f);
		

		createEdge(0.0f, 0.0f, WIDTH / BOX_TO_WORLD, 0.0f);
		createEdge(WIDTH / BOX_TO_WORLD, 0.0f, WIDTH / BOX_TO_WORLD, HEIGHT / BOX_TO_WORLD);
		createEdge(WIDTH / BOX_TO_WORLD, HEIGHT / BOX_TO_WORLD, 0.0f, HEIGHT / BOX_TO_WORLD);
		createEdge(0.0f, HEIGHT / BOX_TO_WORLD, 0.0f, 0.0f);

		// ///////////////////////////////////////////////////////////////////
		// /// END OF TEST COURSE /////
		// ///////////////////////////////////////////////////////////////////

		cam.update();
		cam.translate(new Vector2(WIDTH / 2, HEIGHT / 2));
		
		this.setScreen(new MenuScreen(this));

	}

	// TODO PASSAR ISTO PARA O SHOW DO GAMESCREEN

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

	public static ArrayList<Course> getSelectedCourses() {
		return selectedCourses;
	}

	public static void setSelectedCourses(ArrayList<Course> courses) {
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
		shape.set(new Vector2(x1, y1), new Vector2(x2, y2));
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

	public static int getNrCourses() {
		return nrCourses;
	}

	public static void setNrCourses(int nrCourses) {
		MiniGolf.nrCourses = nrCourses;
	}

	
public int getNrPlayers() {
		return nrPlayers;
	}

	
public void setNrPlayers(int nrPlayers) {
		this.nrPlayers = nrPlayers;
	}

}
