package com.lpoo.MiniGolf.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.logic.Ball;
import com.lpoo.MiniGolf.logic.Course;
import com.lpoo.MiniGolf.logic.Element;
import com.lpoo.MiniGolf.logic.ElementType;
import com.lpoo.MiniGolf.logic.MiniGolf;
import com.lpoo.MiniGolf.logic.Player;

//WHEN SPLASH SCREEN IS MADE, PASS ASSETS AND SKINS TO THERE
public class GameScreen implements Screen, InputProcessor {

	public static final float GRASS_DRAG = 0.8f;
	public static final float SAND_DRAG = 6.0f;

	static final float W_STEP = 1f / 60f;
	static final float FORCE_AUGMENT = 3.0f;

	public final float units = 1.0f / 32.0f;

	private MiniGolf game;
	private Matrix4 debugMatrix;
	private Box2DDebugRenderer debugRenderer;
	private ShapeRenderer shapeRenderer;
	private World w = MiniGolf.getW();
	private OrthographicCamera cam = MiniGolf.cam;

	// TODO eliminate bodies after a game
	private ArrayList<Course> selectedCourses;
	private Course currentCourse;
	ArrayList<Element> currentCourseElements;
	private int courseIndex = 0;

	private static ArrayList<Player> players = new ArrayList<Player>();
	private static ArrayList<Player> actualPlayers = new ArrayList<Player>();
	private static ArrayList<Player> playerRemovalList = new ArrayList<Player>();
	private static Player currentPlayer;

	static boolean allBallsStopped = true;
	float mouseX, mouseY;

	// ///////////////////////////////////////////
	// Screen Functions //
	// ///////////////////////////////////////////

	public GameScreen(MiniGolf game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (!actualPlayers.isEmpty()) { // no players on a course means it is
										// over
			// CURRENT COURSE RENDER CYCLE

			cam.update();

			MiniGolf.batch.setProjectionMatrix(cam.combined);
			debugMatrix = MiniGolf.batch.getProjectionMatrix().cpy().scale(MiniGolf.BOX_TO_WORLD, MiniGolf.BOX_TO_WORLD, 0);
			debugRenderer.render(w, debugMatrix);

			// BATCH FOR DRAWING
			MiniGolf.batch.begin();
			// Draw Course Elements

			courseDraw();

			MiniGolf.batch.end();

			renderLine();

			debugRenderer.render(w, debugMatrix);

			// PHYSICS UPDATE
			dragHandler();
			w.step(W_STEP, 6, 2);

			// After the step, removes the bodies and fixtures of the balls
			// outside
			// the game -> they will have to be recreated first
			if (!playerRemovalList.isEmpty())
				removeBalls();
		} else {
			
			// END OF COURSE
			resetPlayers();
			resetCourse(selectedCourses.get(courseIndex-1));
			System.out.println("Reseting Course nr. " + (courseIndex-1));
			
			if (courseIndex == selectedCourses.size()) {
				//WAS THE LAST COURSE - ENDING GAME
				game.setScreen(new MenuScreen(game));
			} else {
				// CHANGING COURSE
				initializeCourse(selectedCourses.get(courseIndex));
				System.out.println("Initializing Course nr. " + courseIndex);
				courseIndex++;
			}
		}

	}

	@Override
	public void resize(int width, int height) {

		MiniGolf.viewport.update(width, height);
		cam.update();

	}

	@Override
	public void show() {
		System.out.println("Derp");
		debugRenderer = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();

		initializePlayers();
		selectedCourses = game.getSelectedCourses();
		initializeCourse(selectedCourses.get(courseIndex)); // At this point,
															// courseIndex is 0
		courseIndex++;

		Gdx.input.setInputProcessor(this);
		// Setting body contact handling functions
		MiniGolf.getW().setContactListener(new ContactListener() {

			public void beginContact(Contact arg0) {

				ElementType elementA = (ElementType) arg0.getFixtureA().getBody().getUserData();
				ElementType elementB = (ElementType) arg0.getFixtureB().getBody().getUserData();

				if (elementA == null || elementB == null) {
					return;
				}

				if ((elementA.type == Element.elementType.ball && elementB.type != Element.elementType.ball)) {
					if (arg0.getFixtureA().isSensor()) {
						// accesses players because it is a ball that has the id
						// refferent to a index of that array
						elementA.player.getBall().steppingOn = elementB.type;
						elementA.accel = elementB.accel;

						if (elementB.type == Element.elementType.hole) {
							elementA.player.setOver(true);
							playerRemovalList.add(elementA.player);
							elementA.player.getBall().getBody().setLinearVelocity(0f, 0f);

						}

					}

				} else if ((elementB.type == Element.elementType.ball && elementA.type != Element.elementType.ball)) {

					if (arg0.getFixtureB().isSensor()) {
						// accesses players because it is a ball that has the id
						// refferent to a index of that array
						elementB.player.getBall().steppingOn = elementA.type;
						elementB.accel = elementA.accel;

						if (elementA.type == Element.elementType.hole) {
							elementB.player.setOver(true);
							playerRemovalList.add(elementB.player);
							elementB.player.getBall().getBody().setLinearVelocity(0f, 0f);

						}
					}

				}

			}

			public void endContact(Contact arg0) {
				ElementType elementA = (ElementType) arg0.getFixtureA().getBody().getUserData();
				ElementType elementB = (ElementType) arg0.getFixtureB().getBody().getUserData();

				if (elementA == null || elementB == null) {
					return;
				}
				// Ending Contact: Ball returns to stepping on grass
				if (elementA.type == Element.elementType.ball && elementB.type != Element.elementType.ball) {
					elementA.player.getBall().steppingOn = Element.elementType.grassFloor;
					elementA.accel = GRASS_DRAG; // Always goes to grass after
													// ending a contact
				} else if (elementB.type == Element.elementType.ball && elementA.type != Element.elementType.ball) {
					elementB.player.getBall().steppingOn = Element.elementType.grassFloor;
					elementB.accel = GRASS_DRAG;
				}
			}

			public void postSolve(Contact arg0, ContactImpulse arg1) {
			}

			public void preSolve(Contact arg0, Manifold arg1) {
			}

		});

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		// texture.dispose();
		// stage.dispose();
	}

	private void courseDraw() {
		ArrayList<Element> courseElements = currentCourse.getElementos();

		for (int i = 0; i < courseElements.size(); i++) {
			Element e = courseElements.get(i);
			e.draw();
		}

		// Draw Players' balls
		for (int i = 0; i < actualPlayers.size(); i++) {
			Ball b = actualPlayers.get(i).getBall();
			b.draw();
		}
	}

	private void renderLine() {
		for (int i = 0; i < actualPlayers.size(); i++) {

			if (actualPlayers.get(i).getBallSpeedLen() != 0) {
				allBallsStopped = false;
				// allBallsStopped2 = false;
				break;
			} else if (i == actualPlayers.size() - 1) {
				// Chegou ao ultimo e todos tinham velocidade 0
				if (allBallsStopped == false) {
					allBallsStopped = true;
					updateCurrentPlayer();
				}
			}
		}

		// RENDER A LINE BETWEEN MOUSE POSITION AND BALL
		if (allBallsStopped) {

			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.setProjectionMatrix(cam.combined);

			shapeRenderer.begin(ShapeType.Filled);

			// Transforms mouse screen coordinates to camera World coordinates
			// (using camera width and height)
			Vector3 vec = MiniGolf.viewport.unproject(new Vector3((float) Gdx.input.getX(), (float) Gdx.input.getY(), 0));

			float mouseX = vec.x;
			float mouseY = vec.y;

			shapeRenderer.rectLine(currentPlayer.getBallPosX() * MiniGolf.BOX_TO_WORLD, currentPlayer.getBallPosY() * MiniGolf.BOX_TO_WORLD, mouseX, mouseY, 5);
			shapeRenderer.end();
		}
	}

	private void dragHandler() {

		for (int i = 0; i < actualPlayers.size(); i++) {

			// Getting Ball Data
			Body ballBody = actualPlayers.get(i).getBallBody();
			ElementType elementA = (ElementType) ballBody.getUserData();
			// Deciding if there is something to change in force or velocity
			if ((actualPlayers.get(i).getBall().steppingOn != Element.elementType.nothing) && (actualPlayers.get(i).getBall().getBody().getLinearVelocity().len() != 0f)) {

				// Calculate new speed
				float currXSpeed = ballBody.getLinearVelocity().x;
				float currYSpeed = ballBody.getLinearVelocity().y;
				float speedIntensity = ballBody.getLinearVelocity().len();

				// Drag Force intensity = 10 * SURFACE_DRAG
				// -> Divide velX and velY by vel. length, such that vel.len is
				// unitary.
				// -> Then multiply by the drag force intensity and get
				// dragForceX and dragForceY
				float dragForceX = (currXSpeed / speedIntensity) * (10 * elementA.accel);
				float dragForceY = (currYSpeed / speedIntensity) * (10 * elementA.accel);

				// Acceleration = Force, because mass = 1kg
				// F = delta v / delta t <=> DELTA V = F * DELTA T
				// FINAL VEL = INITIAL VEL - DELTA V <=> FINAL VEL = INITIAL VEL
				// - F * DELTA T
				float newXSpeed = currXSpeed - (dragForceX * W_STEP);
				float newYSpeed = currYSpeed - (dragForceY * W_STEP);

				if (currXSpeed * newXSpeed < 0) {
					newXSpeed = 0f;
					// System.out.println("newXSpeed is 0");
				}

				if (currYSpeed * newYSpeed < 0) {
					newYSpeed = 0f;
					// System.out.println("newYSpeed is 0");
				}

				ballBody.setLinearVelocity(newXSpeed, newYSpeed);
			}
		}
	}

	private void updateCurrentPlayer() {
		ArrayList<Player> playerDecisionList = new ArrayList<Player>();

		for (int i = 0; i < players.size(); i++) {
			System.out.println("Ball nr. " + i + " Over = " + players.get(i).isOver());

			if ((players.get(i).isOver() == false) || (players.get(i).isOver() && players.get(i).isJustPlayed())) {
				playerDecisionList.add(players.get(i));
			}

		}

		for (int i = 0; i < playerDecisionList.size(); i++) {
			if (playerDecisionList.get(i).isJustPlayed()) {

				playerDecisionList.get(i).setJustPlayed(false);

				if (i != playerDecisionList.size() - 1) {
					currentPlayer = playerDecisionList.get(i + 1);
					currentPlayer.setJustPlayed(true);
				} else {
					currentPlayer = playerDecisionList.get(0);
					currentPlayer.setJustPlayed(true);
				}
				break;
			}

		}
		allBallsStopped = true;
		System.out.println("BOOM");
	}

	public void removeBalls() {

		// System.out.println("BEFORE");
		//
		// for (int i = 0; i < players.size(); i++) {
		// System.out.println("Players: player nr. " + players.get(i).test);
		// }
		//
		// for (int i = 0; i < actualPlayers.size(); i++) {
		// System.out.println("Players: player nr. " +
		// actualPlayers.get(i).test);
		// }

		for (int i = 0; i < playerRemovalList.size(); i++) {
			// Destroys ball and removes player from the course
			playerRemovalList.get(i).getBall().destroyBody();

			actualPlayers.remove(playerRemovalList.get(i));
		}

		playerRemovalList.clear();

		// System.out.println("AFTER");
		//
		// for (int i = 0; i < players.size(); i++) {
		// System.out.println("Players: player nr. " + players.get(i).test);
		// }
		//
		// for (int i = 0; i < actualPlayers.size(); i++) {
		// System.out.println("Players: player nr. " +
		// actualPlayers.get(i).test);
		// }
		// System.out.println("END");
	}

	/*
	 * Called when the show() method is called, initializing the player
	 * variables for the first time
	 */
	public void initializePlayers() {
		// INITIALIZE PLAYERS
		for (int i = 0; i < game.getNrPlayers(); i++) {

			// Vector2 ballPos = courseBallPos.get(i);
			String str = "Player " + i;
			Player player = new Player(str);
			player.createBall(new Vector2(i + 1, i + 1), w, 0.25f);
			players.add(player);
		}

		players.get(0).setJustPlayed(true);
		currentPlayer = players.get(0);
		actualPlayers = players;

	}

	public void resetPlayers() {

		// Restoring booleans and the body to each ball
		for (int i = 0; i < game.getNrPlayers(); i++) {
			players.get(i).setOver(false);
			players.get(i).setJustPlayed(false);
			players.get(i).getBall().createBody(w, players.get(i));
		}
		players.get(0).setJustPlayed(true);

		// Reseting the actual an current players
		currentPlayer = players.get(0);
		actualPlayers = players;
	}

	public void initializeCourse(Course course) {

		// Sets current course
		currentCourse = course;

		// Sets current course elements and initializes them
		currentCourseElements = course.getElementos();

		for (int i = 0; i < currentCourseElements.size(); i++) {
			// Creates this elements body -> gives form to it
			currentCourseElements.get(i).createBody(w);
		}

		// Clone the actualPlayers array. This way it was empty before this
		// function was called
		actualPlayers = (ArrayList<Player>) players.clone();

	}
	
	public void resetCourse(Course course){
		
		for (int i = 0; i < currentCourseElements.size(); i++) {
			// Creates this elements body -> gives form to it
			currentCourseElements.get(i).destroyBody();
		}
		
	}

	public void resetWorld() {

	}

	// ///////////////////////////////////////////
	// InputProcessor Functions //
	// ///////////////////////////////////////////

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Keys.LEFT)
			System.out.println("Derp Left");
		if (keycode == Keys.RIGHT)
			System.out.println("Derp Right");
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int ammount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Buttons.LEFT) {

			// Auxiliar vector containing the available players, that are still
			// playing and the one who played in the last turn

			if (allBallsStopped) {

				// Vector Ball->Mouse = Vector Mouse - Vector Ball
				// forceX = Box_scale_mouse_X - Box_scale_ball_X -> everything
				// must be scaled to the box size first
				// forceY = Box_scale_mouse_Y - Box_scale_ball_Y

				Vector3 vec = cam.unproject(new Vector3((float) Gdx.input.getX(), (float) Gdx.input.getY(), 0));

				float mouseX = vec.x;
				float mouseY = vec.y;

				float forceX = (mouseX / MiniGolf.BOX_TO_WORLD - currentPlayer.getBallPosX());
				float forceY = (mouseY / MiniGolf.BOX_TO_WORLD) - currentPlayer.getBallPosY();

				// (forceX/W_STEP) is as if the force where applied during 1
				// second instead of 1/60 seconds

				currentPlayer.getBallBody().applyForceToCenter((forceX / W_STEP) * FORCE_AUGMENT, (forceY / W_STEP) * FORCE_AUGMENT, true);

			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

}