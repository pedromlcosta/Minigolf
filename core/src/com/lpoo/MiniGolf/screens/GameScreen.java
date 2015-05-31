package com.lpoo.MiniGolf.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.logic.Element;
import com.lpoo.MiniGolf.logic.MiniGolf;

//WHEN SPLASH SCREEN IS MADE, PASS ASSETS AND SKINS TO THERE
public class GameScreen implements Screen {
	
	public final float units = 1.0f/32.0f;
	private Texture texture = new Texture(Gdx.files.internal("bola0.png"));
	//private Image splashImage = new Image(texture);
	
	//private Stage stage = new Stage();
	//Gdx.input.setInputProcessor(stage);
	
	private Matrix4 debugMatrix;
	private Box2DDebugRenderer debugRenderer;
	private World w = MiniGolf.getW();
	private OrthographicCamera cam = MiniGolf.getCam();
	
	private Sprite grass = new Sprite (new Texture("grass.png"));
	private Sprite ball = new Sprite (new Texture("bola0.png"));
	private ArrayList<Element> ele = MiniGolf.getEle();
	
	
	/* TESTING SKIN STUFF
	private void createBasicSkin(){
	  //Create a font
	  BitmapFont font = new BitmapFont();
	  Skin skin = new Skin();
	  skin.add("default", font);
	 
	  //Create a texture
	  Pixmap pixmap = new Pixmap((int)Gdx.graphics.getWidth()/4,(int)Gdx.graphics.getHeight()/10, Pixmap.Format.RGB888);
	  pixmap.setColor(Color.WHITE);
	  pixmap.fill();
	  skin.add("background",new Texture(pixmap));
	 
	  //Create a button style
	  TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
	  textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
	  textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
	  textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
	  textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
	  textButtonStyle.font = skin.getFont("default");
	  skin.add("default", textButtonStyle);
	 
	}
	*/
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		cam.update();
        MiniGolf.batch.setProjectionMatrix(cam.combined);
		debugMatrix =MiniGolf.batch.getProjectionMatrix().cpy().scale(MiniGolf.BOX_TO_WORLD , MiniGolf.BOX_TO_WORLD , 0);
		if(Assets.update()){ // check if all files are loaded
			/*
            if(animationDone){ // when the animation is finished, go to MainMenu()
                Assets.setMenuSkin(); // uses files to create menuSkin
                ((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenu());
            	Basicamente Minigolf.setScreen(nextShit);
            }
            */
        }
	
		MiniGolf.batch.begin();
		for(int i = 0; i < ele.size(); i++){
			Element e = ele.get(i);
			e.draw();
		}
		
		MiniGolf.batch.end();
		
		debugMatrix = cam.combined.cpy().scale(MiniGolf.BOX_TO_WORLD , MiniGolf.BOX_TO_WORLD , 0);
        debugRenderer.render(w, debugMatrix);
		w.step(1f / 60, 6, 2);
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		Assets.queueLoading();
		System.out.println("AssetsQueueLoading");
		 
		debugMatrix = new Matrix4(cam.combined);
		debugMatrix.scale(100f, 100f, 0f);
		debugRenderer = new Box2DDebugRenderer();

		//splashImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f), Actions.delay(2)));
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
		//texture.dispose();
		//stage.dispose();
	}

}