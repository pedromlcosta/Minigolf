package com.lpoo.MiniGolf.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
//
//public class Splash implements Screen {
//	private Texture texture = new Texture(Gdx.files.internal("badlogic.jpg"));
//	private Image splashImage = new Image(texture);
//	private Stage stage = new Stage();
//
//	@Override
//	public void hide() {
//		dispose();
//	}
//
//	@Override
//	public void dispose() {
//		texture.dispose();
//		stage.dispose();
//	}
//
//	@Override
//	public void pause() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void render(float arg0) {
//		// TODO Auto-generated method stub
//		Gdx.gl.glClearColor(1, 0, 0, 1); // sets clear color to black
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // clear the batch
//		stage.act(); // update all actors
//		stage.draw(); // draw all actors on the Stage.getBatch()
//
//	}
//
//	@Override
//	public void resize(int arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void resume() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void show() {
//		// TODO Auto-generated method stub
//		/*
//		 * If your image is not the same size as your screensplashImage.setX(?);
//		 * splashImage.setY(?);
//		 */
//		stage.addActor(splashImage); // adds the image as an actor to the stage
//		splashImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f), Actions.delay(2), Actions.run(new Runnable() {
//			@Override
//			public void run() {
//				((Game) Gdx.app.getApplicationListener()).setScreen(new Splash());
//			}
//		})));
//	}
//
//}

public class Splash implements Screen {
	private Texture texture = new Texture(Gdx.files.internal("bola.png"));
	private Image splashImage = new Image(texture);
	private Stage stage = new Stage();

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		Button b;

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		stage.addActor(splashImage);

		splashImage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(0.5f), Actions.delay(2), Actions.run(new Runnable() {
			@Override
			public void run() {
				((Game) Gdx.app.getApplicationListener()).setScreen(new Splash());
			}
		})));
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