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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lpoo.MiniGolf.data.GameIO;
import com.lpoo.MiniGolf.logic.*;
import com.lpoo.MiniGolf.logic.Element.elementType;

//WHEN SPLASH SCREEN IS MADE, PASS ASSETS AND SKINS TO THERE
public class GameScreen implements Screen, InputProcessor {

	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	static final float W_STEP = 1f / 60f;
	static final float FORCE_AUGMENT = 3.0f;
	static final float BOX_TO_WORLD = MiniGolf.BOX_TO_WORLD;
	static final int WIDTH = (int) MiniGolf.WIDTH;
	static final int HEIGHT = (int) MiniGolf.HEIGHT;

	public final float units = 1.0f / 32.0f;

	private MiniGolf game;
	private Matrix4 debugMatrix;
	private Box2DDebugRenderer debugRenderer;
	private ShapeRenderer shapeRenderer;
	private World w = MiniGolf.getW();
	private OrthographicCamera cam = MiniGolf.cam;

	private TextButton goBackButton, nextMapButton;
	private static final float BUTTON_WIDTH = 200f;
	private static final float BUTTON_HEIGHT = 50f;

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
	private Table buttonTable;
	public static int ballsInsideIllusion = 0;
	static boolean allBallsStopped = true;
	float mouseX, mouseY;
	private Stage stage;
	boolean invertedPointMode = false;
	private Label playerID;
	private ArrayList<Label> tacadas;

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
		stage.act(delta);
		stage.draw();
		

		// if (currentPlayer.getBallBody() != null) {
		// if ((element = (ElementType)
		// currentPlayer.getBallBody().getUserData()) != null) {
		// System.out.println("Ball acceleration is: " + element.accel);
		//
		// }
		// }

		if (!actualPlayers.isEmpty()) { // no players on a course means it is
										// over
			// CURRENT COURSE RENDER CYCLE

			long elapsedTimeSeconds = (System.currentTimeMillis() - turnStart) / 1000;

			if (elapsedTimeSeconds > game.getTempoMax() && allBallsStopped) {
				updateCurrentPlayer();
			}
			checkFallenBalls();

			cam.update();
			stage.getCamera().update();

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
			System.out.println(courseIndex);
			// END OF COURSE
			// System.out.println("Reseting Course nr. " + (courseIndex - 1));
			// System.out.println("Derp");
			if (courseIndex == selectedCourses.size() - 1) {
				// WAS THE LAST COURSE - ENDING GAME
				// resetPlayers();
				if (selectedCourses.isEmpty())
					game.setScreen(new MenuScreen(game));

				resetCourse(selectedCourses.get(courseIndex));
				game.setScreen(new MenuScreen(game));
				this.dispose();
			} else {
				// CHANGING COURSE
				System.out.println("Reseting players from course nr." + courseIndex);
				// resetPlayers takes the next course Index
				resetPlayers(selectedCourses.get(courseIndex + 1));
				resetCourse(selectedCourses.get(courseIndex));
				initializeCourse(selectedCourses.get(courseIndex + 1));

				// System.out.println("Initializing Course nr. " + courseIndex);
				courseIndex++;
			}
		}
		
	}

	@Override
	public void resize(int width, int height) {

		MiniGolf.viewport.update(width, height);
		stage.getViewport().update(width, height, true);
		cam.update();

	}

	@Override
	public void show() {
		stage = new Stage();
		debugRenderer = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();
		score = new Table();

		// ///////////////////////////////////////////////////////////////////
		// /// TEST COURSE /////
		// ///////////////////////////////////////////////////////////////////

		// Course 1
		//
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
		// //
		// // // Adding to all and selected
		// game.addToAllCourses(Course1);
		// game.addToAllCourses(Course2);
		// game.addToSelectedCourses(Course1);
		// game.addToSelectedCourses(Course2);

		
		
		// GameIO saveGame = new GameIO();


		// ///////////////////////////////////////////////////////////////////
		// //
		// // /// END OF TEST COURSE //
		// //
		// ///////////////////////////////////////////////////////////////////

		stage.setViewport(new FitViewport(MiniGolf.WIDTH, MiniGolf.HEIGHT));
		OrthographicCamera secondaryCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		secondaryCamera.translate(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 300f);
		stage.getViewport().setCamera(secondaryCamera);

		selectedCourses = game.getSelectedCourses();
		System.out.println("derp");
		if (!selectedCourses.isEmpty()) {

			initializePlayers(selectedCourses.get(courseIndex));
			initializeTable();
			initializeCourse(selectedCourses.get(courseIndex)); // At this
																// point,
																// courseIndex
																// is 0

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
		} else {
			game.setScreen(new MenuScreen(game));
		}
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
			if (e.getType() != elementType.illusionWall)
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

			shapeRenderer.begin(ShapeType.Filled);

			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.setProjectionMatrix(cam.combined);
			// Transforms mouse screen coordinates to camera World coordinates
			// (using camera width and height)
			Vector3 vec = MiniGolf.viewport.unproject(new Vector3((float) Gdx.input.getX(), (float) Gdx.input.getY(), 0));

			float mouseX = vec.x;
			float mouseY = vec.y;

			float ballX = currentPlayer.getBallPosX() * MiniGolf.BOX_TO_WORLD;
			float ballY = currentPlayer.getBallPosY() * MiniGolf.BOX_TO_WORLD;
			// System.out.println(currentPlayer.getBallPosX() *
			// MiniGolf.BOX_TO_WORLD + "    " + currentPlayer.getBallPosY() *
			// MiniGolf.BOX_TO_WORLD + "  " + mouseX + "   " + mouseY);

			if (!invertedPointMode) {
				shapeRenderer.rectLine(ballX, ballY, mouseX, mouseY, 5);
			} else { // Ball + (Ball-Mouse)
				shapeRenderer.rectLine(ballX, ballY, ballX + (ballX - mouseX), ballY + (ballY - mouseY), 5);
				shapeRenderer.setColor(Color.RED);
				shapeRenderer.rectLine(ballX, ballY, mouseX, mouseY, 5);
			}

			shapeRenderer.end();

			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.circle(ballX, ballY, MiniGolf.HEIGHT / 2f);
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
				// System.out.println("Second: " + elementA.accel);
				// Acceleration = Force, because mass = 1kg
				// F = delta v / delta t <=> DELTA V = F * DELTA T
				// FINAL VEL = INITIAL VEL - DELTA V <=> FINAL VEL = INITIAL VEL
				// - F * DELTA T
				double newXSpeed = currXSpeed - (dragForceX * W_STEP);
				double newYSpeed = currYSpeed - (dragForceY * W_STEP);

				// System.out.println("Third: " + elementA.accel);
				if (!ball.isOnSpeedPad()) {
					// Only for the real drag/attrition/friction, it will stop
					// acting if the speed is nullified.
					// This happens because drag only exists while there exists
					// speed.
					// System.out.println("Fourth: " + elementA.accel);
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
		invertedPointMode = false;
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

			players.add(player);

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
			currentCourseElements.get(i).initializeImage();
	 
			
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

				if (invertedPointMode) {
					forceX *= -1;
					forceY *= -1;
				}

				// (forceX/W_STEP) is as if the force where applied during 1
				// second instead of 1/60 seconds
				currentPlayer.addTacadaTotal();
				int playerID = currentPlayer.getPlayerID() - 1;
				tacadas.get(playerID).setText("Tacadas: " + currentPlayer.getTacadaTotal());

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
		if (button == Buttons.RIGHT) {
			invertedPointMode = !invertedPointMode;
		}
		return false;
	}

	public void initializeTable() {

		// INITIALIZE PLAYERS
		tacadas = new ArrayList<Label>();
		score.defaults().width(100);
		for (int i = 0; i < game.getNrPlayers(); i++) {

			playerID = new Label("ID:  " + (i + 1), skin);

			switch (i) {
			case 0:
				playerID.setColor(Color.RED);
				break;
			case 1:
				playerID.setColor(Color.CYAN);
				break;
			case 2:
				playerID.setColor(Color.YELLOW);
				break;
			case 3:
				playerID.setColor(Color.PURPLE);
				break;
			default:
				playerID.setColor(Color.BLACK);
				break;
			}

			tacadas.add(new Label("Tacadas:  " + 0, skin));
			score.add(playerID, tacadas.get(i));
			score.row();

		}

		score.setSize(100, 100);
		score.setPosition(score.getWidth(), -score.getHeight() + MiniGolf.HEIGHT);
		stage.addActor(score);
		stage.getCamera().update();

	}

	public void initializeButtons() {

		goBackButton = new TextButton("Back", skin);
		goBackButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		goBackButton.setPosition(MiniGolf.WIDTH / 2 - BUTTON_WIDTH, MiniGolf.HEIGHT / 2 - BUTTON_HEIGHT);

		nextMapButton = new TextButton("Skip Map", skin);
		nextMapButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		nextMapButton.setPosition(MiniGolf.WIDTH / 2 - (BUTTON_WIDTH * 2), MiniGolf.HEIGHT / 2 - BUTTON_HEIGHT);

		stage.addActor(goBackButton);
		stage.addActor(nextMapButton);
		addListeners();
	}

	private void addListeners() {

		goBackButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				resetCourse(selectedCourses.get(courseIndex));
				game.setScreen(new MenuScreen(game));
			}
		});

		nextMapButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				actualPlayers.clear();
			}
		});
	}
}
