package com.lpoo.MiniGolf.screens;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.logic.Ball;
import com.lpoo.MiniGolf.logic.Element;
import com.lpoo.MiniGolf.logic.ElementType;
import com.lpoo.MiniGolf.logic.MiniGolf;
import com.lpoo.MiniGolf.logic.Player;

//WHEN SPLASH SCREEN IS MADE, PASS ASSETS AND SKINS TO THERE
public class GameScreen implements Screen, InputProcessor {

	static final float GRASS_DRAG = 0.3f;
	static final float SAND_DRAG = 0.5f;

	static final float W_STEP = 1f/60f;
	static final float FORCE_AUGMENT = 2f;

	public final float units = 1.0f / 32.0f;

	private Matrix4 debugMatrix;
	private Box2DDebugRenderer debugRenderer;
	private ShapeRenderer shapeRenderer;
	private World w = MiniGolf.getW();
	private OrthographicCamera cam = MiniGolf.getCam();

	private Sprite grass = new Sprite(new Texture("grass.png"));
	private Sprite ball = new Sprite(new Texture("bola0.png"));
	private ArrayList<Element> ele = MiniGolf.getEle();
	private ArrayList<Player> players = MiniGolf.getPlayers();
	private Player currentPlayer = players.get(0);

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
		for (int i = 0; i < ele.size(); i++) {
			Element e = ele.get(i);
			e.draw();
		}
		// Draw Players' balls
		for (int i = 0; i < players.size(); i++) {
			Ball b = players.get(i).getBall();
			b.draw();
		}
		// MiniGolf.batch.draw(grass,5*MiniGolf.BOX_TO_WORLD,5*MiniGolf.BOX_TO_WORLD,5*MiniGolf.BOX_TO_WORLD,5*MiniGolf.BOX_TO_WORLD);
		MiniGolf.batch.end();

		// RENDER A LINE BETWEEN MOUSE POSITION AND BALL
		if (currentPlayer.getBallSpeedLen() == 0f) {
			shapeRenderer.setColor(Color.BLACK);
			shapeRenderer.setProjectionMatrix(cam.combined);

			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.rectLine(currentPlayer.getBallPosX() * MiniGolf.BOX_TO_WORLD, currentPlayer.getBallPosY() * MiniGolf.BOX_TO_WORLD, Gdx.input.getX(), MiniGolf.HEIGHT - Gdx.input.getY(), 5);
			shapeRenderer.end();
		}

		// PHYSICS UPDATE
		dragHandler();
		w.step(W_STEP, 6, 2);

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {

		debugRenderer = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();

		// Setting body contact handling functions
		MiniGolf.getW().setContactListener(new ContactListener() {

			public void beginContact(Contact arg0) {

				ElementType elementA = (ElementType) arg0.getFixtureA().getBody().getUserData();
				ElementType elementB = (ElementType) arg0.getFixtureB().getBody().getUserData();

				if (elementA == null || elementB == null) {
					return;
				}

				if ((elementA.type == Element.elementType.ball && elementB.type == Element.elementType.regularFloor)) {

					if (arg0.getFixtureA().isSensor()) {
						// Inner part of the ball stepping on the grass
						players.get(elementA.id).getBall().steppingOn = Element.elementType.regularFloor;
						elementA.accel = GRASS_DRAG; // Ball gets the drag from
														// the grass
					}

				} else if ((elementA.type == Element.elementType.regularFloor && elementB.type == Element.elementType.ball)) {
					// System.out.println("elementB is a ball and elementA is the floor.");
					if (arg0.getFixtureB().isSensor()) {
						players.get(elementB.id).getBall().steppingOn = Element.elementType.regularFloor;
						elementB.accel = GRASS_DRAG;
					}
				}

			}

			public void endContact(Contact arg0) {
				ElementType elementA = (ElementType) arg0.getFixtureA().getBody().getUserData();
				ElementType elementB = (ElementType) arg0.getFixtureB().getBody().getUserData();

				if (elementA == null || elementB == null) {
					return;
				}

				if ((elementA.type == Element.elementType.ball && elementB.type == Element.elementType.regularFloor)) {
					// System.out.println("elementA is a ball and elementB is the floor. Ball id = "
					// + elementB.id );

					// Sets ball as not in any kind of floor
					players.get(elementA.id).getBall().steppingOn = Element.elementType.nothing;
				} else if ((elementA.type == Element.elementType.regularFloor && elementB.type == Element.elementType.ball)) {
					// System.out.println("elementB is a ball and elementA is the floor.");

					players.get(elementB.id).getBall().steppingOn = Element.elementType.nothing;
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
			if ((players.get(i).getBall().steppingOn != Element.elementType.nothing) && (ballBody.getLinearVelocity().len() != 0f)) {

				// Calculate new speed
				float currXSpeed = ballBody.getLinearVelocity().x;
				float currYSpeed = ballBody.getLinearVelocity().y;
				float speedIntensity = ballBody.getLinearVelocity().len();

				// Drag Force intensity = 10 * SURFACE_DRAG
				// -> Divide velX and velY by vel. length, such that vel.len is unitary. 
				// -> Then multiply by the drag force intensity and get dragForceX and dragForceY
				float dragForceX = (currXSpeed / speedIntensity) * (10 * elementA.accel);
				float dragForceY = (currYSpeed / speedIntensity) * (10 * elementA.accel);

				
				// Acceleration = Force, because mass = 1kg
				// F = delta v / delta t <=> DELTA V = F * DELTA T         
				// FINAL VEL = INITIAL VEL - DELTA V  <=> FINAL VEL = INITIAL VEL - F * DELTA T
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
		if (button == Buttons.LEFT)
			if (currentPlayer.getBallSpeedLen() == 0) {

				// Vector Ball->Mouse = Vector Mouse - Vector Ball
				// forceX = Box_scale_mouse_X - Box_scale_ball_X   -> everything must be scaled to the box size first
				// forceY =	Box_scale_mouse_Y - Box_scale_ball_Y

				float forceX = ((float)Gdx.input.getX() / MiniGolf.BOX_TO_WORLD) - currentPlayer.getBallPosX();
				float forceY = ((MiniGolf.HEIGHT - Gdx.input.getY())/MiniGolf.BOX_TO_WORLD) - currentPlayer.getBallPosY();

				System.out.println("Box_scale_mouse_x: " + (float)(Gdx.input.getX() / (float)MiniGolf.BOX_TO_WORLD));
				System.out.println("Box_scale_ball_x: " + currentPlayer.getBallPosX());
				System.out.println("Box_scale_mouse_y: " +  ((MiniGolf.HEIGHT - Gdx.input.getY())/MiniGolf.BOX_TO_WORLD));
				System.out.println("Box_scale_ball_y: " + currentPlayer.getBallPosY());
				
				System.out.println("Force X: " + forceX);
				System.out.println("Force Y: " + forceY);
				
				// forceX/W_STEP is as if the force where applied during 1 second instead of 1/60 seconds
				currentPlayer.getBallBody().applyForceToCenter((forceX /W_STEP)*FORCE_AUGMENT  , (forceY / W_STEP)*FORCE_AUGMENT , true);
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