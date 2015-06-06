package com.lpoo.MiniGolf.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.logic.MiniGolf;

//TODO bug do reset com bolas
public class LoadScreen implements Screen {
	Image loadingImg;
	Stage stage;
	private Texture texture;
	private MiniGolf game;

	public LoadScreen(MiniGolf game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		if (Assets.update()) {
			game.setScreen(new MenuScreen(game));
		}

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		

		texture = new Texture(Gdx.files.internal("loading.png"));
		loadingImg = new Image(texture);
		loadingImg.setPosition(MiniGolf.WIDTH / 4, MiniGolf.HEIGHT / 4 + 100f);
		stage = new Stage();
		stage.addActor(loadingImg);

		loadingImg.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(1.5f), Actions.run(new Runnable() {
			@Override
			public void run() {
			//	((Game) Gdx.app.getApplicationListener()).setScreen(new LoadScreen(game));
			}
		})));
		Assets.queueLoading();
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
