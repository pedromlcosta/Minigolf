package com.lpoo.MiniGolf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lpoo.MiniGolf.logic.MiniGolf;

public class OptionsScreen implements Screen {
	private SpriteBatch batch;
	private Skin skin;
	private Stage stage;
	private Slider maxTimeSlider;
	private Slider maxTacadasSlider;
	private Sprite background;
	private CheckBox userPicksCheck;
	private Label userPicksLabel;
	private MiniGolf game;
	 
	private final float SLIDER_WIDTH = 200f;
	private final float SLIDER_HEIGHT = 20f;

	public OptionsScreen(MiniGolf game) {
		this.game = game;
	 
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();
		background = new Sprite(new Texture("course.jpg"));

		createMenuElements();

		stage.setViewport(new FitViewport(MiniGolf.WIDTH, MiniGolf.HEIGHT));
		OrthographicCamera secondaryCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		secondaryCamera.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		stage.getViewport().setCamera(secondaryCamera);

		Gdx.input.setInputProcessor(stage);
	}

	private void createMenuElements() {
		maxTimeSlider = new Slider(10f, 60f, 1f, false, skin);
		maxTimeSlider.setPosition(Gdx.graphics.getWidth() / 2 - 100f, Gdx.graphics.getHeight() / 2 - 10f);
		maxTimeSlider.setWidth(SLIDER_WIDTH);
		maxTimeSlider.setHeight(SLIDER_HEIGHT);
		maxTimeSlider.setPosition(Gdx.graphics.getWidth() / 2 - 100f, Gdx.graphics.getHeight() / 2 - 10f);

		maxTacadasSlider = new Slider(1f, 30f, 1f, false, skin);
		maxTacadasSlider.setPosition(Gdx.graphics.getWidth() / 2 - 100f, Gdx.graphics.getHeight() / 2 - 10f);
		maxTacadasSlider.setWidth(SLIDER_WIDTH);
		maxTacadasSlider.setHeight(SLIDER_HEIGHT);
		maxTacadasSlider.setPosition(Gdx.graphics.getWidth() / 2 - 100f, Gdx.graphics.getHeight() / 2 - 30f);

		userPicksCheck = new CheckBox("", skin, "default");
		userPicksCheck.setWidth(SLIDER_WIDTH);
		userPicksCheck.setHeight(SLIDER_HEIGHT);
		userPicksCheck.setPosition(Gdx.graphics.getWidth() / 2 - 100f, Gdx.graphics.getHeight() / 2 + 50f);

		userPicksLabel = new Label("Pick Maps", skin);
		userPicksLabel.setWidth(SLIDER_WIDTH);
		userPicksLabel.setHeight(SLIDER_HEIGHT);
		userPicksLabel.setPosition(Gdx.graphics.getWidth() / 2 - 100f, Gdx.graphics.getHeight() / 2 + 50f);

		addListeners();

		stage.addActor(maxTimeSlider);
		stage.addActor(maxTacadasSlider);
		stage.addActor(userPicksLabel);
		stage.addActor(userPicksCheck);
	}

	private void addListeners() {
		maxTimeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				game.setTempoMax((int) maxTimeSlider.getValue());
			}
		});
		maxTacadasSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				game.setTacadasMax((int) maxTimeSlider.getValue());
			}
		});

		userPicksCheck.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				if (userPicksCheck.isChecked()) {
					// add elements
				} else {
				}
			}
		});
	}

	@Override
	public void dispose() {
		batch.dispose();
		skin.dispose();
		stage.dispose();
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void render(float arg0) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		batch.end();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void resume() {
	}

}