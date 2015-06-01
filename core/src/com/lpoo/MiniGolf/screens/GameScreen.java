package com.lpoo.MiniGolf.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
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
public class GameScreen implements Screen {

	static final float GRASS_DRAG = 0.1f;
	static final float SAND_DRAG = 0.5f;

	public final float units = 1.0f / 32.0f;

	private Matrix4 debugMatrix;
	private Box2DDebugRenderer debugRenderer;
	private World w = MiniGolf.getW();
	private OrthographicCamera cam = MiniGolf.getCam();

	private Sprite grass = new Sprite(new Texture("grass.png"));
	private Sprite ball = new Sprite(new Texture("bola0.png"));
	private ArrayList<Element> ele = MiniGolf.getEle();
	private ArrayList<Player> players = MiniGolf.getPlayers();

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

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cam.update();
		MiniGolf.batch.setProjectionMatrix(cam.combined);
		debugMatrix = MiniGolf.batch.getProjectionMatrix().cpy().scale(MiniGolf.BOX_TO_WORLD, MiniGolf.BOX_TO_WORLD, 0);
		if (Assets.update()) { // check if all files are loaded
			/*
			 * if(animationDone){ // when the animation is finished, go to
			 * MainMenu() Assets.setMenuSkin(); // uses files to create menuSkin
			 * ((Game)Gdx.app.getApplicationListener()).setScreen(new
			 * MainMenu()); Basicamente Minigolf.setScreen(nextShit); }
			 */
		}

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

		// MiniGolf.batch.draw(grass,100,100,100,100);

		MiniGolf.batch.end();

		debugRenderer.render(w, debugMatrix);

		for (int i = 0; i < players.size(); i++) {
			
			// Getting Ball Data
			Body ballBody = players.get(i).getBall().getBody();
			ElementType elementA = (ElementType) ballBody.getUserData();

			// if(ballBody.getLinearVelocity().len()> 0.001){
			//ballBody.applyForceToCenter(-elementA.accel * (ballBody.getLinearVelocity().x / ballBody.getLinearVelocity().len()), -elementA.accel* (ballBody.getLinearVelocity().y / ballBody.getLinearVelocity().len()), true);
			//Calculate speed after the force -> float newXSpeed = currXSpeed - 0.005f*currXSpeed-0.002f;
			//0.005 may be the grass accell constant and 0.001 = 0.005/5;

			// Deciding if there is something to change in force or velocity
			if ((players.get(i).getBall().steppingOn != Element.elementType.nothing) && (ballBody.getLinearVelocity().len() != 0f)) {
								
				//Calculate new speed
				float currXSpeed = ballBody.getLinearVelocity().x;
				float currYSpeed = ballBody.getLinearVelocity().y;
				float speedIntensity = ballBody.getLinearVelocity().len();

				//Drag Force intensity = 10 * SURFACE_DRAG
				//Transform velX and velY such that vel.len is unitary. Then multiply by the drag force intensity and get dragForceX and dragForceY
				float dragForceX = (currXSpeed/speedIntensity)*(10*elementA.accel);
				float dragForceY = (currYSpeed/speedIntensity)*(10*elementA.accel);
				
				
				//System.out.println("Speed Intensity from len " + speedIntensity);
				//System.out.println("Speed Intensity calculated " + Math.sqrt((currXSpeed * currXSpeed ) + (currYSpeed * currYSpeed)));

				//System.out.println("X Speed Normalized " + (currXSpeed/speedIntensity));
				//System.out.println("Y Speed Normalized "  + (currYSpeed/speedIntensity));
				System.out.println("Drag Force Intensity: " + Math.sqrt((dragForceX * dragForceX ) + (dragForceY * dragForceY)));
				
				float newXSpeed = currXSpeed - (dragForceX/60f);  // Acceleration = Force, because mass = 1kg
				float newYSpeed = currYSpeed - (dragForceY/60f); // a = delta v / delta t  <=>  delta v = a * delta t <=> delta v = force * (1/60) -> DELTA T SAME AS IN STEP
				
				
				if(currXSpeed * newXSpeed < 0){
					newXSpeed = 0f;
				}
				
				if(currYSpeed * newYSpeed < 0){
					newYSpeed = 0f;
				}
				
				ballBody.setLinearVelocity(newXSpeed,newYSpeed);
			}
		}

		w.step(1f / 60, 6, 2);

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		Assets.queueLoading();
		System.out.println("AssetsQueueLoading");

		// debugMatrix = new Matrix4(cam.combined);
		// debugMatrix.scale(100f, 100f, 0f);
		debugRenderer = new Box2DDebugRenderer();

		MiniGolf.getW().setContactListener(new ContactListener() {

			public void beginContact(Contact arg0) {

				ElementType elementA = (ElementType) arg0.getFixtureA().getBody().getUserData();
				ElementType elementB = (ElementType) arg0.getFixtureB().getBody().getUserData();

				if ((elementA.type == Element.elementType.ball && elementB.type == Element.elementType.regularFloor)) {
					System.out.println("elementA is a ball and elementB is the floor.");

					// Sets ball as stepping on grassFloor -> id was needed to
					// know what ball to change
					players.get(elementA.id).getBall().steppingOn = Element.elementType.regularFloor;
					elementA.accel = GRASS_DRAG;

				} else if ((elementA.type == Element.elementType.regularFloor && elementB.type == Element.elementType.ball)) {
					System.out.println("elementB is a ball and elementA is the floor.");

					players.get(elementB.id).getBall().steppingOn = Element.elementType.regularFloor;
					elementB.accel = GRASS_DRAG;
				}

			}

			public void endContact(Contact arg0) {
				ElementType elementA = (ElementType) arg0.getFixtureA().getBody().getUserData();
				ElementType elementB = (ElementType) arg0.getFixtureB().getBody().getUserData();

				if ((elementA.type == Element.elementType.ball && elementB.type == Element.elementType.regularFloor)) {
					System.out.println("elementA is a ball and elementB is the floor.");

					// Sets ball as not in any kind of floor
					players.get(elementA.id).getBall().steppingOn = Element.elementType.nothing;
				} else if ((elementA.type == Element.elementType.regularFloor && elementB.type == Element.elementType.ball)) {
					System.out.println("elementB is a ball and elementA is the floor.");

					players.get(elementB.id).getBall().steppingOn = Element.elementType.nothing;
				}
			}

			public void postSolve(Contact arg0, ContactImpulse arg1) {
			}

			public void preSolve(Contact arg0, Manifold arg1) {
			}

		});

		// splashImage.addAction(Actions.sequence(Actions.alpha(0),
		// Actions.fadeIn(0.5f), Actions.delay(2)));
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

}