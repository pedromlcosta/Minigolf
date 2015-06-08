package com.lpoo.MiniGolf.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lpoo.MiniGolf.data.GameIO;
import com.lpoo.MiniGolf.screens.LoadScreen;

// TODO: Auto-generated Javadoc
/**
 * The Class MiniGolf.
 */
public class MiniGolf extends Game {

	/** The batch. */
	public static SpriteBatch batch;
	
	/** The cam. */
	public static OrthographicCamera cam;
	
	/** The viewport. */
	public static Viewport viewport;
	
	/** The w. */
	private static World W;
	
	/** The load save. */
	private GameIO loadSave;
	
	/** The nr players. */
	private int nrPlayers = 3;
	
	/** The selected courses. */
	private ArrayList<Course> selectedCourses = new ArrayList<Course>();
	
	/** The all courses. */
	private static ArrayList<Course> allCourses = new ArrayList<Course>();

	/** The nr courses. */
	private static int nrCourses = 1;
	
	/** The Constant MAX_PLAYERS. */
	public static final int MAX_PLAYERS = 4;

	/** The random course. */
	private static boolean randomCourse;
	
	/** The end point. */
	private Vector2 endPoint;
	
	/** The start point. */
	private Vector2 startPoint;
	
	/** The tacadas max. */
	private int tacadasMax = 3;
	
	/** The tempo max. */
	private int tempoMax = 10;
	
	/** The course height. */
	private int courseHeight;
	
	/** The course width. */
	private int courseWidth;

	/** The Constant BOX_TO_WORLD. */
	public static final float BOX_TO_WORLD = 100f;
	
	/** The Constant TITLE. */
	public static final String TITLE = "Game Project";
	
	/** The width. */
	public static float WIDTH = 1920;
	
	/** The height. */
	public static float HEIGHT = 1080;
	
	/** The Constant BUTTON_WIDTH. */
	public  static final float BUTTON_WIDTH = 200f;
	
	/** The Constant BUTTON_HEIGHT. */
	public static final float BUTTON_HEIGHT = 50f;
	
	/**
	 * Instantiates a new mini golf.
	 */
	public MiniGolf() {

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	public void create() {

		loadSave = new GameIO();

		// INITIALIZING SINGLETONS

		batch = new SpriteBatch();
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		viewport = new FitViewport(WIDTH, HEIGHT, cam);
		W = new World(new Vector2(0, 0), false);
		W.setContinuousPhysics(true);
		W.setWarmStarting(true);

		// LOADING MAPS
		try {
			allCourses = loadSave.loadAllIndividualCourses();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.setScreen(new LoadScreen(this));
		// ///////////////////////////////////////////////////////////////////
		// /// TEST COURSE /////
		// ///////////////////////////////////////////////////////////////////

		// // Course 1
		// while (!Assets.update()) {
		// }
		// Course Course1 = new Course();
		// Course1.setNome("Course 1");
		//
		// Vector2 pos1 = new Vector2(1.0f, 1.0f);
		// Vector2 pos2 = new Vector2(2.0f, 2.0f);
		// Vector2 pos3 = new Vector2(3.0f, 3.0f);
		// Vector2 pos4 = new Vector2(4.0f, 4.0f);
		//
		// Floor grass1 = new Floor(new Vector2((WIDTH / 2f / BOX_TO_WORLD),
		// (HEIGHT / 2f / BOX_TO_WORLD)), WIDTH / BOX_TO_WORLD, HEIGHT /
		// BOX_TO_WORLD, elementType.grassFloor);
		//
		// Hole hole1 = new Hole(new Vector2(5f, 5f), 0.3f);
		//
		// Floor sand1 = new Floor(new Vector2(3 * (WIDTH / 12f / BOX_TO_WORLD),
		// 9 * (HEIGHT / 12f / BOX_TO_WORLD)), WIDTH / 6f / BOX_TO_WORLD, HEIGHT
		// / 2f / BOX_TO_WORLD, elementType.sandFloor);
		//
		// Floor water1 = new Floor(new Vector2(7 * (WIDTH / 12f /
		// BOX_TO_WORLD), 9 * (HEIGHT / 12f / BOX_TO_WORLD)), WIDTH / 6f /
		// BOX_TO_WORLD, HEIGHT / 2f / BOX_TO_WORLD, elementType.waterFloor);
		//
		// Wall glue1 = new Wall(new Vector2(5 * (WIDTH / 12f / BOX_TO_WORLD), 9
		// * (HEIGHT / 12f / BOX_TO_WORLD)), WIDTH / 6f / BOX_TO_WORLD, HEIGHT /
		// 2f / BOX_TO_WORLD, elementType.glueWall);
		//
		// Floor void1 = new Floor(new Vector2(9 * (WIDTH / 12f / BOX_TO_WORLD),
		// 9 * (HEIGHT / 12f / BOX_TO_WORLD)), WIDTH / 6f / BOX_TO_WORLD, HEIGHT
		// / 2f / BOX_TO_WORLD, elementType.voidFloor);
		//
		// Floor illusion1 = new Floor(new Vector2(11 * (WIDTH / 12f /
		// BOX_TO_WORLD), 9 * (HEIGHT / 12f / BOX_TO_WORLD)), WIDTH / 6f /
		// BOX_TO_WORLD, HEIGHT / 2f / BOX_TO_WORLD, elementType.illusionWall);
		//
		// Floor accel1 = new Floor(new Vector2(1 * (WIDTH / 12f /
		// BOX_TO_WORLD), 9 * (HEIGHT / 12f / BOX_TO_WORLD)), WIDTH / 6f /
		// BOX_TO_WORLD, HEIGHT / 2f / BOX_TO_WORLD,
		// elementType.acceleratorFloor);
		//
		// Teleporter teleporter1 = new Teleporter(new Vector2(7f, 5f), new
		// Vector2(7f, 3f), 0.3f, 1);
		//
		// // Floor illusion1 = new Floor(new Vector2(1 * (WIDTH / 4f /
		// // BOX_TO_WORLD),
		// // 3 * (HEIGHT / 4f / BOX_TO_WORLD)), WIDTH / 2f / BOX_TO_WORLD,
		// // HEIGHT / 2f / BOX_TO_WORLD, elementType.illusionWall);
		//
		// Course1.addEle(grass1);
		// Course1.addEle(accel1);
		// Course1.addEle(glue1);
		// Course1.addEle(hole1);
		// Course1.addEle(void1);
		// Course1.addEle(water1);
		// Course1.addEle(sand1);
		// Course1.addEle(illusion1);
		// Course1.addEle(teleporter1);
		// Course1.addPosition(pos1);
		// Course1.addPosition(pos2);
		// Course1.addPosition(pos3);
		// Course1.addPosition(pos4);
		//
		// // Course 2
		// Course Course2 = new Course();
		// Course2.setNome("Course 2");
		// Vector2 pos5 = new Vector2(1.0f, 2.0f);
		// Vector2 pos6 = new Vector2(2.0f, 3.0f);
		// Vector2 pos7 = new Vector2(3.0f, 4.0f);
		// Vector2 pos8 = new Vector2(4.0f, 5.0f);
		// Floor grass2 = new Floor(new Vector2((WIDTH / 2f / BOX_TO_WORLD),
		// (HEIGHT / 2f / BOX_TO_WORLD)), WIDTH / BOX_TO_WORLD, HEIGHT /
		// BOX_TO_WORLD, elementType.grassFloor);
		// Floor sand2 = new Floor(new Vector2(1 * (WIDTH / 4f / BOX_TO_WORLD),
		// 1 * (HEIGHT / 4f / BOX_TO_WORLD)), WIDTH / 2f / BOX_TO_WORLD, HEIGHT
		// / 2f / BOX_TO_WORLD, elementType.sandFloor);
		// Hole hole2 = new Hole(new Vector2(7f, 7f), 0.3f);
		// Course2.addEle(grass2);
		// Course2.addEle(sand2);
		// Course2.addEle(hole2);
		// Course2.addPosition(pos5);
		// Course2.addPosition(pos6);
		// Course2.addPosition(pos7);
		// Course2.addPosition(pos8);
		//
		// // Adding to all and selected
		// addToAllCourses(Course1);
		// addToAllCourses(Course2);
		// addToSelectedCourses(Course1);
		// addToSelectedCourses(Course2);

		// try {
		// ObjectOutputStream out = new ObjectOutputStream(new
		// FileOutputStream("AllCourses.sav"));
		// out.writeObject(selectedCourses);
		// out.close();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// System.out.println("huehue");
		// }

		// ///////////////////////////////////////////////////////////////////
		// /// END OF TEST COURSE /////
		// ///////////////////////////////////////////////////////////////////

		
		createEdge(0.0f, 0.0f, WIDTH / BOX_TO_WORLD, 0.0f);
		createEdge(WIDTH / BOX_TO_WORLD, 0.0f, WIDTH / BOX_TO_WORLD, HEIGHT / BOX_TO_WORLD);
		createEdge(WIDTH / BOX_TO_WORLD, HEIGHT / BOX_TO_WORLD, 0.0f, HEIGHT / BOX_TO_WORLD);
		createEdge(0.0f, HEIGHT / BOX_TO_WORLD, 0.0f, 0.0f);

		cam.update();
		cam.translate(new Vector2(WIDTH / 2, HEIGHT / 2));

	}

	// TODO PASSAR ISTO PARA O SHOW DO GAMESCREEN

	/**
	 * Gets the w.
	 *
	 * @return the w
	 */
	public static World getW() {
		return W;
	}

	/**
	 * Sets the w.
	 *
	 * @param w the new w
	 */
	public static void setW(World w) {
		W = w;
	}

	/**
	 * Gets the batch.
	 *
	 * @return the batch
	 */
	public static SpriteBatch getBatch() {
		return batch;
	}

	/**
	 * Sets the batch.
	 *
	 * @param batch the new batch
	 */
	public static void setBatch(SpriteBatch batch) {
		MiniGolf.batch = batch;
	}

	/**
	 * Gets the cam.
	 *
	 * @return the cam
	 */
	public static OrthographicCamera getCam() {
		return cam;
	}

	/**
	 * Sets the cam.
	 *
	 * @param cam the new cam
	 */
	public static void setCam(OrthographicCamera cam) {
		MiniGolf.cam = cam;
	}

	/**
	 * Gets the course height.
	 *
	 * @return the course height
	 */
	public int getCourseHeight() {
		return courseHeight;
	}

	/**
	 * Sets the course height.
	 *
	 * @param courseHeight the new course height
	 */
	public void setCourseHeight(int courseHeight) {
		this.courseHeight = courseHeight;
	}

	/**
	 * Gets the course width.
	 *
	 * @return the course width
	 */
	public int getCourseWidth() {
		return courseWidth;
	}

	/**
	 * Sets the course width.
	 *
	 * @param courseWidth the new course width
	 */
	public void setCourseWidth(int courseWidth) {
		this.courseWidth = courseWidth;
	}

	/**
	 * Creates the edge.
	 *
	 * @param x1 the x1
	 * @param y1 the y1
	 * @param x2 the x2
	 * @param y2 the y2
	 */
	void createEdge(float x1, float y1, float x2, float y2) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		EdgeShape shape = new EdgeShape();
		shape.set(new Vector2(x1, y1), new Vector2(x2, y2));
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		Body body = W.createBody(bodyDef);
		Fixture fixt = body.createFixture(fixtureDef);
		fixt.setRestitution(0.8f);
		fixt.setFriction(0.5f);
	}

	/**
	 * Gets the end point.
	 *
	 * @return the end point
	 */
	public Vector2 getEndPoint() {
		return endPoint;
	}

	/**
	 * Sets the end point.
	 *
	 * @param endPoint the new end point
	 */
	public void setEndPoint(Vector2 endPoint) {
		this.endPoint = endPoint;
	}

	/**
	 * Gets the start point.
	 *
	 * @return the start point
	 */
	public Vector2 getStartPoint() {
		return startPoint;
	}

	/**
	 * Sets the start point.
	 *
	 * @param startPoint the new start point
	 */
	public void setStartPoint(Vector2 startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * Gets the tacadas max.
	 *
	 * @return the tacadas max
	 */
	public int getTacadasMax() {
		return tacadasMax;
	}

	/**
	 * Sets the tacadas max.
	 *
	 * @param tacadasMax the new tacadas max
	 */
	public void setTacadasMax(int tacadasMax) {
		this.tacadasMax = tacadasMax;
	}

	/**
	 * Gets the tempo max.
	 *
	 * @return the tempo max
	 */
	public int getTempoMax() {
		return tempoMax;
	}

	/**
	 * Sets the tempo max.
	 *
	 * @param tempoMax the new tempo max
	 */
	public void setTempoMax(int tempoMax) {
		this.tempoMax = tempoMax;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public static float getHeight() {
		return HEIGHT;
	}

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public static void setHeight(int height) {
		HEIGHT = height;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public static float getWidth() {
		return WIDTH;
	}

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public static void setWidth(int width) {
		WIDTH = width;
	}

	/**
	 * Checks if is random course.
	 *
	 * @return true, if is random course
	 */
	public static boolean isRandomCourse() {
		return randomCourse;
	}

	/**
	 * Random courses.
	 */
	public void randomCourses() {
 
		if(allCourses.isEmpty())
			return;
		
		selectedCourses.clear();
		
		//Shuffling all the courses
		ArrayList<Course> randomCourses = (ArrayList<Course>) allCourses.clone();
		Collections.shuffle(randomCourses);
		
		//Adding to selectedCourses only the first "nrCourses" courses of the shuffled array
		for(int i = 0 ; i < nrCourses; i++){
			selectedCourses.add(randomCourses.get(i));
		}
	}

	/**
	 * Sets the random course.
	 *
	 * @param randomCourse the new random course
	 */
	public static void setRandomCourse(boolean randomCourse) {
		MiniGolf.randomCourse = randomCourse;
	}

	/**
	 * Gets the nr courses.
	 *
	 * @return the nr courses
	 */
	public static int getNrCourses() {
		return nrCourses;
	}

	/**
	 * Sets the nr courses.
	 *
	 * @param nrCourses the new nr courses
	 */
	public static void setNrCourses(int nrCourses) {
		MiniGolf.nrCourses = nrCourses;
	}

	/**
	 * Gets the nr players.
	 *
	 * @return the nr players
	 */
	public int getNrPlayers() {
		return nrPlayers;
	}

	/**
	 * Sets the nr players.
	 *
	 * @param nrPlayers the new nr players
	 */
	public void setNrPlayers(int nrPlayers) {
		this.nrPlayers = nrPlayers;
	}

	/**
	 * Gets the selected courses.
	 *
	 * @return the selected courses
	 */
	public ArrayList<Course> getSelectedCourses() {
		return selectedCourses;
	}

	/**
	 * Sets the selected courses.
	 *
	 * @param selectedCourses the new selected courses
	 */
	public void setSelectedCourses(ArrayList<Course> selectedCourses) {
		this.selectedCourses = selectedCourses;
	}

	/**
	 * Adds the to selected courses.
	 *
	 * @param course the course
	 */
	public void addToSelectedCourses(Course course) {
		selectedCourses.add(course);
	}

	/**
	 * Gets the all courses.
	 *
	 * @return the all courses
	 */
	public static ArrayList<Course> getAllCourses() {
		return allCourses;
	}

	/**
	 * Sets the all courses.
	 *
	 * @param allCourses the new all courses
	 */
	public void setAllCourses(ArrayList<Course> allCourses) {
		MiniGolf.allCourses = allCourses;
	}

	/**
	 * Adds the to all courses.
	 *
	 * @param course the course
	 */
	public void addToAllCourses(Course course) {
		allCourses.add(course);
	}

	/**
	 * Gets the load save.
	 *
	 * @return the load save
	 */
	public GameIO getLoadSave() {
		return loadSave;
	}

	/**
	 * Sets the load save.
	 *
	 * @param loadSave the new load save
	 */
	public void setLoadSave(GameIO loadSave) {
		this.loadSave = loadSave;
	}

}
