package com.lpoo.MiniGolf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.logic.MiniGolf;

/**
 * The Class LoadScreen.
 */
public class LoadScreen implements Screen {
	
	/** The loading img. */
	private Image loadingImg;
	
	/** The stage. */
	private Stage stage;
	
	/** The texture. */
	private Texture texture;
	
	/** The game. */
	private MiniGolf game;

	/**
	 * Instantiates a new load screen.
	 *
	 * @param game the game
	 */
	public LoadScreen(MiniGolf game) {
		this.game = game;
	}

	/**
	 * Render.
	 *
	 * @param delta the delta
	 */
	@Override
	/**
	 * is responsible for drawing all elements in the screen
	 * @param delta   time in seconds since the last render.
	 */
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		if (Assets.update()) {
			game.setScreen(new MenuScreen(game));
		}
	}

	/**
	 * Resize.
	 *
	 * @param width the width
	 * @param height the height
	 */
	@Override
	/**
	 * it´s called when any of the window dimensions are changed, updates the Viewport accordingly
	 * @param width
	 * @param height
	 */
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		stage.getCamera().update();
	}

	/**
	 * Show.
	 * Creates the elements that will be used in the screen
	 */
	@Override
	public void show() {

		texture = new Texture(Gdx.files.internal("loading.png"));
		stage = new Stage();
		loadingImg = new Image(texture);

		stage.setViewport(new FitViewport(MiniGolf.WIDTH, MiniGolf.HEIGHT));
		OrthographicCamera secondaryCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		secondaryCamera.translate(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 300f);
		stage.getViewport().setCamera(secondaryCamera);

		loadingImg.setPosition((MiniGolf.WIDTH - texture.getWidth()) / 2, MiniGolf.HEIGHT / 2);
		stage.addActor(loadingImg);
		Assets.queueLoading();
	}

	/**
	 * Hide.
	 */
	@Override
	public void hide() {
		dispose();
	}

	/**
	 * Pause.
	 */
	@Override
	public void pause() {
	}

	/**
	 * Resume.
	 */
	@Override
	public void resume() {
	}

	/**
	 * Dispose.
	 */
	@Override
	public void dispose() {
		texture.dispose();
		stage.dispose();
	}

}
