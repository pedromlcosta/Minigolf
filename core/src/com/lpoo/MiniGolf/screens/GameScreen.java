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
import com.lpoo.MiniGolf.logic.Ball;
import com.lpoo.MiniGolf.logic.Element;
import com.lpoo.MiniGolf.logic.ElementType;
import com.lpoo.MiniGolf.logic.MiniGolf;
import com.lpoo.MiniGolf.logic.Player;

//WHEN SPLASH SCREEN IS MADE, PASS ASSETS AND SKINS TO THERE
public class GameScreen implements Screen, InputProcessor {

	public static final float GRASS_DRAG = 0.25f;
	public static final float SAND_DRAG = 6.0f;

	static final float W_STEP = 1f / 60f;
	static final float FORCE_AUGMENT = 3.0f;

	public final float units = 1.0f / 32.0f;

	private Matrix4 debugMatrix;
	private Box2DDebugRenderer debugRenderer;
	private ShapeRenderer shapeRenderer;
	private World w = MiniGolf.getW();
	private OrthographicCamera cam = MiniGolf.cam;

	private ArrayList<Element> courseElements = MiniGolf.getCourseElements();
	private static ArrayList<Player> players = MiniGolf.getPlayers();
	private static ArrayList<Body> ballRemovalList = new ArrayList<Body>();
	private static Player currentPlayer = players.get(0);
	private static int currentPlayerIndex = 0;
	static boolean allBallsStopped = true;

	/*
	 * TESTING SKIN STUFF private void createBasicSkin(){ //Create a font
	 * BitmapFont font = new BitmapFont(); Skin skin = new Skin();
	 * skin.add("default", font);
	 * 
	 * //Create a texture Pixmap pixmap = new
	 * Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10,
	 * Pixmap.Format.RGB888); pixmap.setColor(Color.WHITE); pixmap.fill();
	 * skin.add("background",new Texture(pixmap));
	 * 
	 * //Create a button style TextButton.TextButtonStyle textButtonStyle = new
	 * TextButton.TextButtonStyle(); textButtonStyle.up =
	 * skin.newDrawable("background", Color.GRAY); textButtonStyle.down =
	 * skin.newDrawable("background", Color.DARK_GRAY); textButtonStyle.checked
	 * = skin.newDrawable("background", Color.DARK_GRAY); textButtonStyle.over =
	 * skin.newDrawable("background", Color.LIGHT_GRAY); textButtonStyle.font =
	 * skin.getFont("default"); skin.add("default", textButtonStyle);
	 * 
	 * }
	 */

	// ///////////////////////////////////////////
	// Screen Functions //
	// ///////////////////////////////////////////

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();

		MiniGolf.batch.setProjectionMatrix(cam.combined);
		debugMatrix = MiniGolf.batch.getProjectionMatrix().cpy().scale(MiniGolf.BOX_TO_WORLD, MiniGolf.BOX_TO_WORLD, 0);
		debugRenderer.render(w, debugMatrix);

		// BATCH FOR DRAWING
		MiniGolf.batch.begin();
		// Draw Course Elements

		for (int i = 0; i < MiniGolf.getCourseElements().size(); i++) {
			Element e = courseElements.get(i);
			e.draw();
		}

		// Draw Players' balls
		for (int i = 0; i < players.size(); i++) {
			Ball b = players.get(i).getBall();
			// System.out.println(b.steppingOn.toString());
			b.draw();
		}

		MiniGolf.batch.end();

		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getBallSpeedLen() != 0) {
				allBallsStopped = false;
				break;
			} else if (i == players.size() - 1)
				allBallsStopped = true;
		}
		// RENDER A LINE BETWEEN MOUSE POSITION AND BALL
		if (allBallsStopped) {
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.setProjectionMatrix(cam.combined);

			shapeRenderer.begin(ShapeType.Filled);

			// Transforms mouse screen coordinates to camera World coordinates
			// (using camera width and height)
			Vector3 vec = cam.unproject(new Vector3((float) Gdx.input.getX(), (float) Gdx.input.getY(), 0));

			float mouseX = vec.x;
			float mouseY = vec.y;
			shapeRenderer.rectLine(currentPlayer.getBallPosX() * MiniGolf.BOX_TO_WORLD, currentPlayer.getBallPosY() * MiniGolf.BOX_TO_WORLD, mouseX, mouseY, 5);
			shapeRenderer.end();
		}

		debugRenderer.render(w, debugMatrix);

		// PHYSICS UPDATE
		dragHandler();
		w.step(W_STEP, 6, 2);
	}

	@Override
	public void resize(int width, int height) {
		// Gdx.gl.glViewport(height, height, width, height);

		// float oldRatio = MiniGolf.WIDTH/MiniGolf.HEIGHT;
		// float newRatio = width/height;
		// float viewWidth = width;
		// float viewHeight = height;
		//
		// if(width < height){
		//
		// }else{
		//
		// }
		//
		// cam.setToOrtho(false, viewportWidth, viewportHeight);
		MiniGolf.viewport.update(width, height);
		cam.update();

	}

	@Override
	public void show() {

		debugRenderer = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();

		currentPlayerIndex = 0;
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
						System.out.println("elementType: " + elementB.type);

						players.get(elementA.id).getBall().steppingOn = elementB.type;
						elementA.accel = elementB.accel;

						if (elementB.type == Element.elementType.hole) {
							players.get(elementA.id).getBall().getBody().setLinearVelocity(0f, 0f);
						}

					}

				} else if ((elementB.type == Element.elementType.ball && elementA.type != Element.elementType.ball)) {

					if (arg0.getFixtureB().isSensor()) {
						System.out.println("elementType: " + elementA.type);

						players.get(elementB.id).getBall().steppingOn = elementA.type;
						elementB.accel = elementA.accel;

						if (elementA.type == Element.elementType.hole) {
							players.get(elementB.id).getBall().getBody().setLinearVelocity(0f, 0f);
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
					players.get(elementA.id).getBall().steppingOn = Element.elementType.grassFloor;
					elementA.accel = GRASS_DRAG; // Always goes to grass after
													// ending a contact
				} else if (elementB.type == Element.elementType.ball && elementA.type != Element.elementType.ball) {
					players.get(elementB.id).getBall().steppingOn = Element.elementType.grassFloor;
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

	private void dragHandler() {

		for (int i = 0; i < players.size(); i++) {

			// Getting Ball Data
			Body ballBody = players.get(i).getBallBody();
			ElementType elementA = (ElementType) ballBody.getUserData();
			// Deciding if there is something to change in force or velocity
			if ((players.get(i).getBall().steppingOn != Element.elementType.nothing) && (players.get(i).getBall().getBody().getLinearVelocity().len() != 0f)) {

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

			buttonPressed();
		}
		return false;
	}

	private void buttonPressed() {

		if (allBallsStopped) {
			allBallsStopped = false;

			// Vector Ball->Mouse = Vector Mouse - Vector Ball
			// forceX = Box_scale_mouse_X - Box_scale_ball_X -> everything
			// must be scaled to the box size first
			// forceY = Box_scale_mouse_Y - Box_scale_ball_Y

			Vector3 vec = cam.unproject(new Vector3((float) Gdx.input.getX(), (float) Gdx.input.getY(), 0));

			float mouseX = vec.x;
			float mouseY = vec.y;

			float forceX = (mouseX / MiniGolf.BOX_TO_WORLD - currentPlayer.getBallPosX());
			float forceY = (mouseY / MiniGolf.BOX_TO_WORLD) - currentPlayer.getBallPosY();

			// System.out.println("Box_scale_mouse_x: " + (float)
			// (Gdx.input.getX() / (float) MiniGolf.BOX_TO_WORLD));
			// System.out.println("Box_scale_ball_x: " +
			// currentPlayer.getBallPosX());
			// System.out.println("Box_scale_mouse_y: " + ((MiniGolf.HEIGHT -
			// Gdx.input.getY()) / MiniGolf.BOX_TO_WORLD));
			// System.out.println("Box_scale_ball_y: " +
			// currentPlayer.getBallPosY());
			//
			// System.out.println("Force X: " + forceX);
			// System.out.println("Force Y: " + forceY);

			// (forceX/W_STEP) is as if the force where applied during 1
			// second instead of 1/60 seconds
			allBallsStopped = false;
			currentPlayer.getBallBody().applyForceToCenter((forceX / W_STEP) * FORCE_AUGMENT, (forceY / W_STEP) * FORCE_AUGMENT, true);

			if (currentPlayerIndex != players.size() - 1) {
				currentPlayer = players.get(currentPlayerIndex + 1);
				currentPlayerIndex++;
			} else {
				currentPlayer = players.get(0);
				currentPlayerIndex = 0;
			}
		}
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