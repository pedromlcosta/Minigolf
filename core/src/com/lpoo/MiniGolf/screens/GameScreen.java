package com.lpoo.MiniGolf.screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lpoo.MiniGolf.geometry.Geometry;
import com.lpoo.MiniGolf.logic.Ball;
import com.lpoo.MiniGolf.logic.Course;
import com.lpoo.MiniGolf.logic.Element;
import com.lpoo.MiniGolf.logic.ElementType;
import com.lpoo.MiniGolf.logic.MiniGolf;
import com.lpoo.MiniGolf.logic.Player;
import com.lpoo.MiniGolf.logic.Teleporter;

//WHEN SPLASH SCREEN IS MADE, PASS ASSETS AND SKINS TO THERE
public class GameScreen implements Screen, InputProcessor {

	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	static final float W_STEP = 1f / 60f;
	static final float FORCE_AUGMENT = 6.0f;
	static final float PLAY_RADIUS = MiniGolf.HEIGHT / 2f;
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
	long elapsedTimeSeconds = 0;
	public static int ballsInsideIllusion = 0;
	static boolean allBallsStopped = true;
	float mouseX, mouseY;
	private Stage stage;
	boolean invertedPointMode = false;
	private Label playerID;
	private ArrayList<Label> tacadas;
	private boolean changeProcessor;
	private Label timeLabel;

	public GameScreen(MiniGolf game) {
		this.game = game;

		this.game.randomCourses();
	}

	// ///////////////////////////////////////////
	// SCREEN FUNCTIONS //
	// ///////////////////////////////////////////

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (changeProcessor) {
			Gdx.input.setInputProcessor(this);
		}

		
		
		if (!actualPlayers.isEmpty()) { // no players on a course means it is
										// over

			// CURRENT COURSE RENDER CYCLE

			// TIME CHECK
			elapsedTimeSeconds = (System.currentTimeMillis() - turnStart) / 1000;
			timeLabel.setText("Time Elapsed: " + elapsedTimeSeconds);
			if (elapsedTimeSeconds > game.getTempoMax() && allBallsStopped) {
				// Didn't play in time -> still gets a "tacada added"
				// Because the winner is the one with the less "tacadas"
				currentPlayer.addTacadaTotal();
				int playerID = currentPlayer.getPlayerID() - 1;
				tacadas.get(playerID).setText("Tacadas: " + currentPlayer.getTacadaTotal());
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
			checkBallsStopped();
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
				// resetPlayers takes the next course Index
				resetPlayers(selectedCourses.get(courseIndex + 1));
				resetCourse(selectedCourses.get(courseIndex));
				initializeCourse(selectedCourses.get(courseIndex + 1));

				// System.out.println("Initializing Course nr. " + courseIndex);
				courseIndex++;
			}
		}
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {

		MiniGolf.viewport.update(width, height);
		stage.getViewport().update(width, height, true);
		stage.getCamera().update();
		cam.update();

	}

	@Override
	public void show() {
		stage = new Stage();
		debugRenderer = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();
		score = new Table();

		stage.addListener(new InputListener() {

			@Override
			public boolean mouseMoved(InputEvent Event, float screenX, float screenY) {
				// if (!(screenX >= BUTTON_WIDTH * 2 && screenX <= BUTTON_WIDTH
				// * 3)) {
				// if (!(screenY >= MiniGolf.HEIGHT - BUTTON_HEIGHT && screenY
				// <= HEIGHT))
				// changeProcessor = true;
				// }
				return false;
			}
		});
		// GameIO saveGame = new GameIO();

		stage.setViewport(new FitViewport(MiniGolf.WIDTH, MiniGolf.HEIGHT));
		OrthographicCamera secondaryCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		secondaryCamera.translate(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 300f);
		stage.getViewport().setCamera(secondaryCamera);

		selectedCourses = game.getSelectedCourses();

		if (!selectedCourses.isEmpty()) {

			initializePlayers(selectedCourses.get(courseIndex));
			initializeButtons();
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
			e.draw();

		}

		// Draw Players' balls
		for (int i = 0; i < actualPlayers.size(); i++) {
			Ball b = actualPlayers.get(i).getBall();
			b.draw();
		}
	}

	private void renderLine() {

		// RENDER A LINE BETWEEN MOUSE POSITION AND BALL
		if (allBallsStopped) {

			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.setProjectionMatrix(cam.combined);

			// GETTING PROPER VARIABLES
			// Transforms mouse screen coordinates to camera World coordinates
			// (using camera width and height)
			Vector2 mouse = MiniGolf.viewport.unproject(new Vector2((float) Gdx.input.getX(), (float) Gdx.input.getY()));

			float mouseX = mouse.x;
			float mouseY = mouse.y;

			Vector2 ball = new Vector2(currentPlayer.getBallPosX() * MiniGolf.BOX_TO_WORLD, currentPlayer.getBallPosY() * MiniGolf.BOX_TO_WORLD);

			float ballX = currentPlayer.getBallPosX() * MiniGolf.BOX_TO_WORLD;
			float ballY = currentPlayer.getBallPosY() * MiniGolf.BOX_TO_WORLD;

			// DRAWING PLAY LINE

			// NORMAL MOUSE MODE
			if (!invertedPointMode) {
				if (inside(ballX, ballY, mouseX, mouseY, PLAY_RADIUS)) {
					shapeRenderer.rectLine(ball.x, ball.y, mouse.x, mouse.y, 5);
				} else {
					Vector2 intersection = intersectLineCircle(ball, mouse, PLAY_RADIUS);
					shapeRenderer.rectLine(ball.x, ball.y, intersection.x, intersection.y, 5);
				}

			} else { // INVERTED MODE (RIGHT CLICK TO CHANGE)


				if (inside(ballX, ballY, mouseX, mouseY, PLAY_RADIUS)) {
					shapeRenderer.rectLine(ballX, ballY, ballX + (ballX - mouseX), ballY + (ballY - mouseY), 5);
					shapeRenderer.setColor(Color.RED);
					shapeRenderer.end();
					drawDottedLine(shapeRenderer, 10, ball.x, ball.y, mouse.x, mouse.y);
				} else {
					Vector2 intersection = intersectLineCircle(ball, mouse, PLAY_RADIUS);

					shapeRenderer.rectLine(ballX, ballY, ballX - (intersection.x - ballX), ballY - (intersection.y - ballY), 5);
					shapeRenderer.setColor(Color.RED);
					shapeRenderer.end();
					drawDottedLine(shapeRenderer, 10, ball.x, ball.y, intersection.x, intersection.y);

				}
			}

			// DRAWING RADIUS CIRCLE
			shapeRenderer.end();
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.circle(ballX, ballY, PLAY_RADIUS);
			shapeRenderer.end();
		}
	}

	private void checkBallsStopped() {
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
	}

	private boolean inside(float ballX, float ballY, float mouseX, float mouseY, float radius) {
		return Geometry.distance(mouseX, mouseY, ballX, ballY) <= radius;

	}

	private Vector2 intersectLineCircle(Vector2 ball, Vector2 mouse, float radius) {

		Vector2 ballToMouse = new Vector2(mouse.x - ball.x, mouse.y - ball.y);
		float intersectPointX = ball.x + (ballToMouse.x / ballToMouse.len()) * radius;
		float intersectPointY = ball.y + (ballToMouse.y / ballToMouse.len()) * radius;

		return new Vector2(intersectPointX, intersectPointY);
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
				}else{
//					float rad = (float) Math.atan2(newXSpeed, newYSpeed);
//					Vector2 newVel = new Vector2((float)newXSpeed, (float)newYSpeed);
//					System.out.println("The X speed is: " + currentPlayer.getBallBody().getLinearVelocity().x);
//					System.out.println("The Y speed is: " + currentPlayer.getBallBody().getLinearVelocity().y);
//					System.out.println("The total speed is: " + currentPlayer.getBallBody().getLinearVelocity().len());
//					System.out.println("The angle of the speed is: " + ball.accelAngle);
//					if (rad == ball.accelAngle && newVel.len() < 0.5f){
//						newXSpeed = 0f;
//						newYSpeed = 0f;
//					}
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
		actualPlayers = players;
	}

	@SuppressWarnings("unchecked")
	public void initializeCourse(Course course) {

		ArrayList<Integer> teleporterColor = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
		Collections.shuffle(teleporterColor);
		int teleporterCounter = 0;

		// Sets current course
		currentCourse = course;

		// Sets current course elements and initializes them
		currentCourseElements = course.getElementos();

		for (int i = 0; i < currentCourseElements.size(); i++) {
			// Creates this elements body -> gives form to it
			currentCourseElements.get(i).createBody(w);
			currentCourseElements.get(i).initializeImage();
			if (currentCourseElements.get(i) instanceof Teleporter) {
				Teleporter tele1 = (Teleporter) currentCourseElements.get(i);
				System.out.println("Teleporter with destination: " + tele1.getDestination().x + " " + tele1.getDestination().y);
				System.out.println("Changed color to nr." + teleporterColor.get(teleporterCounter));
				currentCourseElements.get(i).changeColor(teleporterColor.get(teleporterCounter));
				teleporterCounter++;

			}

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
		// TODO borked don´t know enough of the way transitions between games
		// work to fix

		if (keycode == Keys.ESCAPE) {

			resetCourse(selectedCourses.get(courseIndex));
			for (Player p : actualPlayers) {
				p.getBall().destroyBody();
			}
			game.setScreen(new MenuScreen(game));

		} else if (keycode == Keys.S) {

			for (Player p : actualPlayers) {
				if (p.getBallBody() != null)
					p.getBall().destroyBody();
			}
			actualPlayers.clear();
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		// Vector3 pos = cam.unproject(new Vector3(screenX, screenY, 0));
		// System.out.println("Mouse Moved: " + pos.x + "  " + pos.y + "  " +
		// (goBackButton.getHeight()));
		// // BUTTON_WIDTH * 2 + "  " + BUTTON_WIDTH * 3);
		// if (pos.x >= BUTTON_WIDTH * 2 && pos.x <= BUTTON_WIDTH * 3) {
		// System.out.println("STEP1");
		// if (pos.y >= goBackButton.getHeight()) {
		// System.out.println("STEP2");
		// Gdx.input.setInputProcessor(stage);
		// }
		// }
		//
		return false;
	}

	@Override
	public boolean scrolled(int ammount) {
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

				float ballX = currentPlayer.getBallPosX() * MiniGolf.BOX_TO_WORLD;
				float ballY = currentPlayer.getBallPosY() * MiniGolf.BOX_TO_WORLD;

				// MOUSE<->BALL DISTANCE GIVES THE FORCE
				float forceX = (mouseX / MiniGolf.BOX_TO_WORLD - currentPlayer.getBallPosX());
				float forceY = (mouseY / MiniGolf.BOX_TO_WORLD) - currentPlayer.getBallPosY();

				Vector2 force = new Vector2(forceX, forceY);

				// LIMITS FORCE AS IF MOUSE WERE ON RADIUS LIMIT
				// Normalizes the vector (to have the direction) and multiplies
				// by the radius
				if (!inside(ballX, ballY, mouseX, mouseY, PLAY_RADIUS)) {
					forceX = (forceX / force.len()) * (PLAY_RADIUS / BOX_TO_WORLD);
					forceY = (forceY / force.len()) * (PLAY_RADIUS / BOX_TO_WORLD);
				}

				if (invertedPointMode) {
					forceX *= -1;
					forceY *= -1;
				}
				Vector2 forceTest = new Vector2(forceX, forceY);
				//System.out.println("Force lenght: " + forceTest.len());

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

		// stage.getCamera().update();

	}

	// TODO
	public void initializeButtons() {
		timeLabel = new Label("Time Elapsed: ", skin);
		timeLabel.setColor(Color.BLACK);
		timeLabel.setPosition(BUTTON_WIDTH * 2, (float) (MiniGolf.HEIGHT - BUTTON_HEIGHT*1.5));
		
		Label temp = new Label("Press ESC to quit or S to skip track", skin);
		temp.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		temp.setPosition(BUTTON_WIDTH * 2, MiniGolf.HEIGHT - BUTTON_HEIGHT);
		temp.setColor(Color.BLACK);
		goBackButton = new TextButton("Back", skin);
		goBackButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		goBackButton.setPosition(BUTTON_WIDTH, MiniGolf.HEIGHT - BUTTON_HEIGHT);

		nextMapButton = new TextButton("Skip Map", skin);
		nextMapButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		nextMapButton.setPosition(BUTTON_WIDTH * 3, MiniGolf.HEIGHT - BUTTON_HEIGHT);

		stage.addActor(timeLabel);
		stage.addActor(temp);
		// stage.addActor(goBackButton);
		// stage.addActor(nextMapButton);
		addListeners();
	}

	private void addListeners() {

		// goBackButton.addListener(new ClickListener() {
		// @Override
		// public void clicked(InputEvent event, float x, float y) {
		// resetCourse(selectedCourses.get(courseIndex));
		// game.setScreen(new MenuScreen(game));
		// }
		// });

		// nextMapButton.addListener(new ClickListener() {
		// @Override
		// public void clicked(InputEvent event, float x, float y) {
		// actualPlayers.clear();
		// resetCourse(selectedCourses.get(courseIndex));
		// }
		// });
	}

	private void drawDottedLine(ShapeRenderer shapeRenderer, float dotDist, float x1, float y1, float x2, float y2) {

		shapeRenderer.begin(ShapeType.Filled);

		Vector2 vec2 = new Vector2(x2, y2).sub(new Vector2(x1, y1));
		float length = vec2.len();
		for (int i = 0; i < length; i += dotDist) {
			vec2.clamp(length - i, length - i);
			// shapeRenderer.point(x1 + vec2.x, y1 + vec2.y, 0);
			shapeRenderer.rectLine(x1 + vec2.x, y1 + vec2.y, x1 + vec2.x + dotDist / 2, y1 + vec2.y, 5);
		}

	}
}
