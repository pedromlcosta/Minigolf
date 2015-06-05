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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.lpoo.MiniGolf.logic.Ball;
import com.lpoo.MiniGolf.logic.Course;
import com.lpoo.MiniGolf.logic.Element;
import com.lpoo.MiniGolf.logic.ElementType;
import com.lpoo.MiniGolf.logic.MiniGolf;
import com.lpoo.MiniGolf.logic.Player;

//WHEN SPLASH SCREEN IS MADE, PASS ASSETS AND SKINS TO THERE
public class GameScreen implements Screen, InputProcessor {

	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
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
	public static ArrayList<Player> playerRemovalList = new ArrayList<Player>();
	private static Player currentPlayer;
	private long turnStart = System.currentTimeMillis();
	private Table score;
	public static int ballsInsideIllusion = 0;
	static boolean allBallsStopped = true;
	float mouseX, mouseY;

	public GameScreen(MiniGolf game) {
		this.game = game;
	}

	// ///////////////////////////////////////////
	// SCREEN FUNCTIONS //
	// ///////////////////////////////////////////

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ElementType element;

		 if (currentPlayer.getBallBody() != null){
		 if ((element = (ElementType)
		 currentPlayer.getBallBody().getUserData()) != null){
		 System.out.println("Ball acceleration is: " + element.accel);
		
		 }
		 }

		if (!actualPlayers.isEmpty()) { // no players on a course means it is
										// over
			// CURRENT COURSE RENDER CYCLE

			long elapsedTimeSeconds = (System.currentTimeMillis() - turnStart) / 1000;

			if (elapsedTimeSeconds > game.getTempoMax() && allBallsStopped) {
				updateCurrentPlayer();
			}

			checkFallenBalls();

			cam.update();

			MiniGolf.batch.setProjectionMatrix(cam.combined);
			debugMatrix = MiniGolf.batch.getProjectionMatrix().cpy().scale(MiniGolf.BOX_TO_WORLD, MiniGolf.BOX_TO_WORLD, 0);
			debugRenderer.render(w, debugMatrix);

			// BATCH FOR DRAWING
			MiniGolf.batch.begin();
			// Draw Course Elements

			courseDraw();

			MiniGolf.batch.end();

			// Checks if balls are stopped and render lines by polling
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
			// System.out.println("Reseting Course nr. " + (courseIndex - 1));

			if (courseIndex == selectedCourses.size()) {
				// WAS THE LAST COURSE - ENDING GAME
				// resetPlayers();
				resetCourse(selectedCourses.get(courseIndex - 1));
				game.setScreen(new MenuScreen(game));
				this.dispose();
			} else {
				// CHANGING COURSE
				resetPlayers(selectedCourses.get(courseIndex));
				resetCourse(selectedCourses.get(courseIndex - 1));
				initializeCourse(selectedCourses.get(courseIndex));

				// System.out.println("Initializing Course nr. " + courseIndex);
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
		debugRenderer = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();
		score = new Table();

		selectedCourses = game.getSelectedCourses();

		initializePlayers(selectedCourses.get(courseIndex));
		initializeCourse(selectedCourses.get(courseIndex)); // At this point,
															// courseIndex is 0
		courseIndex++;
		turnStart = System.currentTimeMillis();

		Gdx.input.setInputProcessor(this);
		// Setting body contact handling functions
		MiniGolf.getW().setContactListener(new ContactListener() {

			public void beginContact(Contact arg0) {

				ElementType elementA = (ElementType) arg0.getFixtureA().getBody().getUserData();
				ElementType elementB = (ElementType) arg0.getFixtureB().getBody().getUserData();

				// Check if the body has no user data
				if (elementA == null || elementB == null) {
					return;
				}

				// Collisions between ball and something else
				if ((elementA.type == Element.elementType.ball && elementB.type != Element.elementType.ball)) {

					elementA.player.getBall().beginContactListener(elementA, elementB, arg0.getFixtureA().isSensor());

				} else if ((elementB.type == Element.elementType.ball && elementA.type != Element.elementType.ball)) {

					elementB.player.getBall().beginContactListener(elementB, elementA, arg0.getFixtureB().isSensor());

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

					elementA.player.getBall().endContactListener(elementA, elementB, arg0.getFixtureA().isSensor());

				} else if (elementB.type == Element.elementType.ball && elementA.type != Element.elementType.ball) {

					elementB.player.getBall().endContactListener(elementB, elementA, arg0.getFixtureB().isSensor());
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
			// System.out.println(currentPlayer.getBallPosX() *
			// MiniGolf.BOX_TO_WORLD + "    " + currentPlayer.getBallPosY() *
			// MiniGolf.BOX_TO_WORLD + "  " + mouseX + "   " + mouseY);
			shapeRenderer.rectLine(currentPlayer.getBallPosX() * MiniGolf.BOX_TO_WORLD, currentPlayer.getBallPosY() * MiniGolf.BOX_TO_WORLD, mouseX, mouseY, 5);
			// shapeRenderer.rectLine(p1, p2, width);

			shapeRenderer.end();
		}
	}

	private void dragHandler() {

		for (int i = 0; i < actualPlayers.size(); i++) {

			// Getting Ball Data
			Ball ball = actualPlayers.get(i).getBall();
			Body ballBody = ball.getBody();
			ElementType elementA = (ElementType) ballBody.getUserData();
			// Deciding if there is something to change in force or velocity
			if ((actualPlayers.get(i).getBall().steppingOn != Element.elementType.nothing) && (actualPlayers.get(i).getBallBody().getLinearVelocity().len() != 0f)) {

				// Calculate new speed
				float currXSpeed = ballBody.getLinearVelocity().x;
				float currYSpeed = ballBody.getLinearVelocity().y;
				float speedIntensity = ballBody.getLinearVelocity().len();

				// Drag Force intensity = 10 * SURFACE_DRAG
				// -> Divide velX and velY by vel. length, such that vel.len is
				// unitary.
				// -> Then multiply by the drag force intensity and get
				// dragForceX and dragForceY
				double dragForceX = 0f;
				double dragForceY = 0f;

				if (!ball.isOnSpeedPad()) { // When the object suffers drag
					dragForceX = (currXSpeed / speedIntensity) * (10 * elementA.accel);
					dragForceY = (currYSpeed / speedIntensity) * (10 * elementA.accel);
				} else { // When the object suffers acceleration in a certain
							// direction
					
					dragForceX = Math.cos(ball.accelAngle) * (10 * elementA.accel);
					dragForceY = Math.sin(ball.accelAngle) * (10 * elementA.accel);
				}
				//System.out.println("Second: " + elementA.accel);
				// Acceleration = Force, because mass = 1kg
				// F = delta v / delta t <=> DELTA V = F * DELTA T
				// FINAL VEL = INITIAL VEL - DELTA V <=> FINAL VEL = INITIAL VEL
				// - F * DELTA T
				double newXSpeed = currXSpeed - (dragForceX * W_STEP);
				double newYSpeed = currYSpeed - (dragForceY * W_STEP);

				//System.out.println("Third: " + elementA.accel);
				if (!ball.isOnSpeedPad()) {
					// Only for the real drag/attrition/friction, it will stop
					// acting if the speed is nullified.
					// This happens because drag only exists while there exists speed.
					//System.out.println("Fourth: " + elementA.accel);
					if (currXSpeed * newXSpeed < 0) {
						newXSpeed = 0f;
						// System.out.println("newXSpeed is 0");
					}

					if (currYSpeed * newYSpeed < 0) {
						newYSpeed = 0f;
						// System.out.println("newYSpeed is 0");
					}
					// && ball.steppingOn != Element.elementType.waterFloor

					if (newXSpeed == 0f && newYSpeed == 0f) {
						ball.setLastPos(ballBody.getPosition());
					}
				}
				ballBody.setLinearVelocity((float) newXSpeed, (float) newYSpeed);
			}
		}
	}

	private void checkFallenBalls() {
		for (int i = 0; i < actualPlayers.size(); i++) {
			actualPlayers.get(i).getBall().checkElementsTouched();
		}
	}

	private void updateCurrentPlayer() {
		ArrayList<Player> playerDecisionList = new ArrayList<Player>();

		for (int i = 0; i < players.size(); i++) {
			// System.out.println("Ball nr. " + i + " Over = " +
			// players.get(i).isOver());

			if ((players.get(i).isOver() == false) || (players.get(i).isOver() && players.get(i).isJustPlayed())) {
				playerDecisionList.add(players.get(i));
			}

		}

		for (int i = 0; i < playerDecisionList.size(); i++) {
			if (playerDecisionList.get(i).isJustPlayed()) {

				playerDecisionList.get(i).setJustPlayed(false);

				if (i != playerDecisionList.size() - 1) {
					currentPlayer = playerDecisionList.get(i + 1);
				} else {
					currentPlayer = playerDecisionList.get(0);
				}
				currentPlayer.setJustPlayed(true);
				turnStart = System.currentTimeMillis();
				currentPlayer.setPlayTime(0);
				break;
			}

		}
		allBallsStopped = true;
	}

	public void removeBalls() {
		for (int i = 0; i < playerRemovalList.size(); i++) {
			// Destroys ball and removes player from the course
			playerRemovalList.get(i).getBall().destroyBody();

			actualPlayers.remove(playerRemovalList.get(i));
		}

		playerRemovalList.clear();
	}

	// ///////////////////////////////////////////
	// INITIALIZATION AND RESET FUNCTIONS //
	// ///////////////////////////////////////////

	/*
	 * Called when the show() method is called, initializing the player
	 * variables for the first time
	 */
	public void initializePlayers(Course course) {
		// INITIALIZE PLAYERS

		players = new ArrayList<Player>();

		// System.out.println("Initialize - Nr. Players is " + players.size());

		for (int i = 0; i < game.getNrPlayers(); i++) {

			// Vector2 ballPos = courseBallPos.get(i);
			Player player = new Player(i + 1);
			player.createBall(course.getPositions().get(i), w, 0.25f);
			Label playerID = new Label("ID: " + (i + 1), skin);
			Label tacadas = new Label("Tacadas: " + 0, skin);

			players.add(player);

			score.defaults().width(100);
			score.add(playerID, tacadas);
			score.row();
		}

		players.get(0).setJustPlayed(true);
		currentPlayer = players.get(0);
		actualPlayers = players;

	}

	public void resetPlayers(Course course) {

		// Restoring booleans and the body to each ball
		for (int i = 0; i < game.getNrPlayers(); i++) {
			players.get(i).setOver(false);
			players.get(i).setJustPlayed(false);
			players.get(i).getBall().setStartPos(course.getPositions().get(i));
			players.get(i).getBall().createBody(w, players.get(i), course.getPositions().get(i));

		}
		players.get(0).setJustPlayed(true);

		// Reseting the actual an current players
		currentPlayer = players.get(0);
		System.out.println("Current player reset to 0, with player id = " + currentPlayer.getPlayerID());
		actualPlayers = players;
	}

	@SuppressWarnings("unchecked")
	public void initializeCourse(Course course) {

		// Sets current course
		currentCourse = course;

		// Sets current course elements and initializes them
		currentCourseElements = course.getElementos();

		for (int i = 0; i < currentCourseElements.size(); i++) {
			// Creates this elements body -> gives form to it
			currentCourseElements.get(i).createBody(w);
		}

		// Clone the actualPlayers array.
		actualPlayers = (ArrayList<Player>) players.clone();

	}

	public void resetCourse(Course course) {

		for (int i = 0; i < currentCourseElements.size(); i++) {
			// Creates this elements body -> gives form to it
			currentCourseElements.get(i).destroyBody();
		}
		allBallsStopped = true;
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

				Vector3 vec = MiniGolf.viewport.unproject(new Vector3((float) Gdx.input.getX(), (float) Gdx.input.getY(), 0));

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