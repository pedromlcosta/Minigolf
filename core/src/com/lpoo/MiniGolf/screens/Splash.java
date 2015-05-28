package com.lpoo.MiniGolf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.lpoo.MiniGolf.logic.MiniGolf;

public class Splash implements Screen {
	private Texture texture = new Texture(Gdx.files.internal("bola0.png"));
	private Image splashImage = new Image(texture);
	private Stage stage = new Stage();
	private SpriteBatch batch = MiniGolf.getBatch(); 
	private Matrix4 debugMatrix;
	private Box2DDebugRenderer debugRenderer;
	private World w = MiniGolf.getW();
	private OrthographicCamera cam = MiniGolf.getCam();
	
	private Sprite grass = new Sprite(new Texture("grass.png"));
	private Sprite ball = new Sprite(new Texture("bola0.png"));
	
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//stage.act();
		//stage.draw();
		
		batch.begin();
		int xi = 200;
		int yi = 200;
		batch.draw(ball, xi, yi, 50, 50);
		batch.end();

		debugRenderer.render(w, cam.combined);
		w.step(1f / 60, 6, 2);
		debugMatrix = batch.getProjectionMatrix().cpy().scale(100, 100, 0);
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		stage.addActor(splashImage);
		
		ball.setPosition(40, 40);
		ball.setSize(5, 5);
		
		debugMatrix = new Matrix4(cam.combined);
		debugMatrix.scale(100f, 100f, 0f);
		debugRenderer = new Box2DDebugRenderer();

		splashImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f), Actions.delay(2)));
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
		texture.dispose();
		stage.dispose();
	}

}