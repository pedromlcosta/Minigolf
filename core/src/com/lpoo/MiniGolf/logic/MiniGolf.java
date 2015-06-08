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
 * The Class MiniGolf, which starts the game up, contains some of the singletons
 * and other important elements.
 */
public class MiniGolf extends Game {

	/** The batch for drawing, used wherever necessary. */
	public static SpriteBatch batch;

	/** The libgdx physics world. */
	private static World W;

	/** The controller for loading and saving courses. */
	private GameIO loadSave;

	/** The number of players, can be changed in the menu */
	private int nrPlayers = 3;

	/** The selected courses, out of all the courses. */
	private ArrayList<Course> selectedCourses = new ArrayList<Course>();

	/** The all courses. */
	private static ArrayList<Course> allCourses = new ArrayList<Course>();

	/** The nr. of courses to be selected. */
	private static int nrCourses = 1;

	/** The Constant MAX_PLAYERS. Max number of players allowed in a game. */
	public static final int MAX_PLAYERS = 4;

	/** Indicates whether the courses are chosen randomly or not. */
	private static boolean randomCourse;

	/** The maximum shots per turn per player. Not implemented yet. */
	private int tacadasMax = 3;

	/** The max time of a turn. */
	private int tempoMax = 10;

	/**
	 * The Constant BOX_TO_WORLD. Transforms numbers from BOX2D coordinates to
	 * WORLD/SCREEN coordinates when multiplied by it.
	 */
	public static final float BOX_TO_WORLD = 100f;

	/** The Constant TITLE. Name of the game. */
	public static final String TITLE = "Game Project";

	/** The internal width of the game. */
	public static float WIDTH = 1920;

	/** The internal height of the game. */
	public static float HEIGHT = 1080;

	/** The Constant BUTTON_WIDTH. */
	public static final float BUTTON_WIDTH = 200f;

	/** The Constant BUTTON_HEIGHT. */
	public static final float BUTTON_HEIGHT = 50f;

	/**
	 * Instantiates a new mini golf.
	 */
	public MiniGolf() {

	}

	/*
	 * Initializing all the things. Loading the courses (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.ApplicationListener#create()
	 */
	public void create() {

		// INITIALIZING SINGLETONS

		batch = new SpriteBatch();
		W = new World(new Vector2(0, 0), false);
		W.setContinuousPhysics(true);
		W.setWarmStarting(true);

		loadCourses();

		this.setScreen(new LoadScreen(this));

		createEdge(0.0f, 0.0f, WIDTH / BOX_TO_WORLD, 0.0f);
		createEdge(WIDTH / BOX_TO_WORLD, 0.0f, WIDTH / BOX_TO_WORLD, HEIGHT / BOX_TO_WORLD);
		createEdge(WIDTH / BOX_TO_WORLD, HEIGHT / BOX_TO_WORLD, 0.0f, HEIGHT / BOX_TO_WORLD);
		createEdge(0.0f, HEIGHT / BOX_TO_WORLD, 0.0f, 0.0f);

	}

	/**
	 * Load courses, from the folder where they were saved individually.
	 */
	public void loadCourses() {
		loadSave = new GameIO();

		// LOADING MAPS
		try {
			allCourses = loadSave.loadAllIndividualCourses();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Gets the World.
	 *
	 * @return the World
	 */
	public static World getW() {
		return W;
	}

	/**
	 * Sets the World.
	 *
	 * @param w
	 *            the new World.
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
	 * @param batch
	 *            the new batch
	 */
	public static void setBatch(SpriteBatch batch) {
		MiniGolf.batch = batch;
	}

	/**
	 * Creates an edge.
	 *
	 * @param x1
	 *            the x1 Starting x of the edge.
	 * @param y1
	 *            the y1 Starting y of the edge.
	 * @param x2
	 *            the x2 Final x of the edge.
	 * @param y2
	 *            the y2 Final y of the edge.
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
	 * Gets the number of max shots.
	 *
	 * @return the number of max shots.
	 */
	public int getTacadasMax() {
		return tacadasMax;
	}

	/**
	 * Sets the number of max shots.
	 *
	 * @param tacadasMax
	 *            the new number of max shots.
	 */
	public void setTacadasMax(int tacadasMax) {
		this.tacadasMax = tacadasMax;
	}

	/**
	 * Gets the max. turn time.
	 *
	 * @return the max. turn time.
	 */
	public int getTempoMax() {
		return tempoMax;
	}

	/**
	 * Sets the max. turn time.
	 *
	 * @param tempoMax
	 *            the new max. turn time
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
	 * @param height
	 *            the new height
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
	 * @param width
	 *            the new width
	 */
	public static void setWidth(int width) {
		WIDTH = width;
	}

	/**
	 * Checks if courses are selected randomly.
	 *
	 * @return true, if the courses are to be selected randomly.
	 */
	public static boolean isRandomCourse() {
		return randomCourse;
	}

	/**
	 * Picks a nrCourses courses from all the courses available and puts them in
	 * the selectedCourses.
	 */
	public void randomCourses() {

		if (allCourses.isEmpty())
			return;

		selectedCourses.clear();

		// Shuffling all the courses
		ArrayList<Course> randomCourses = (ArrayList<Course>) allCourses.clone();
		Collections.shuffle(randomCourses);

		// Adding to selectedCourses only the first "nrCourses" courses of the
		// shuffled array
		for (int i = 0; i < nrCourses; i++) {
			selectedCourses.add(randomCourses.get(i));
		}
	}

	/**
	 * Sets if the courses will be selected randomly
	 *
	 * @param randomCourse
	 *            the new random course value
	 */
	public static void setRandomCourse(boolean randomCourse) {
		MiniGolf.randomCourse = randomCourse;
	}

	/**
	 * Gets the nr courses to be selected.
	 *
	 * @return the nr courses to be selected.
	 */
	public static int getNrCourses() {
		return nrCourses;
	}

	/**
	 * Sets the nr courses to be selected.
	 *
	 * @param nrCourses
	 *            the new nr courses to be selected.
	 */
	public static void setNrCourses(int nrCourses) {
		MiniGolf.nrCourses = nrCourses;
	}

	/**
	 * Gets the nr players.
	 *
	 * @return the nr players.
	 */
	public int getNrPlayers() {
		return nrPlayers;
	}

	/**
	 * Sets the nr players.
	 *
	 * @param nrPlayers
	 *            the new nr players.
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
	 * @param selectedCourses
	 *            the new selected courses
	 */
	public void setSelectedCourses(ArrayList<Course> selectedCourses) {
		this.selectedCourses = selectedCourses;
	}

	/**
	 * Adds the to selected courses.
	 *
	 * @param course
	 *            the course to add.
	 */
	public void addToSelectedCourses(Course course) {
		selectedCourses.add(course);
	}

	/**
	 * Gets all the courses.
	 *
	 * @return the array with all the courses
	 */
	public static ArrayList<Course> getAllCourses() {
		return allCourses;
	}

	/**
	 * Sets the new array with all courses.
	 *
	 * @param allCourses
	 *            the new array with all the courses
	 */
	public void setAllCourses(ArrayList<Course> allCourses) {
		MiniGolf.allCourses = allCourses;
	}

	/**
	 * Adds a course to the array which stores them all.
	 *
	 * @param course
	 *            the course to be added
	 */
	public void addToAllCourses(Course course) {
		allCourses.add(course);
	}

	/**
	 * Gets the gameIO controller.
	 *
	 * @return the gameIO controller.
	 */
	public GameIO getLoadSave() {
		return loadSave;
	}

	/**
	 * Sets the gameIO controller.
	 *
	 * @param loadSave
	 *            the gameIO controller.
	 */
	public void setLoadSave(GameIO loadSave) {
		this.loadSave = loadSave;
	}

}
