package com.lpoo.MiniGolf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lpoo.MiniGolf.logic.MiniGolf;

public class MenuScreen implements Screen {
	private SpriteBatch batch;
	private Skin skin;
	private Stage stage;
	private TextButton playButton;
	private TextButton OptionsButton;
	private TextButton exitButton;
	private TextButton editorButton;
	private static final float BUTTON_WIDTH = 200f;
	private static final float BUTTON_HEIGHT = 50f;
	private MiniGolf game;
	private Sprite background;

	private final float DELTA_WIDTH = 100f;
	private final float DELTA_HEIGHT = 50f;

	// private final static Logger LOGGER =
	// Logger.getLogger(MenuScreen.class.getName());

	public MenuScreen(MiniGolf game) {
		this.game = game;
	}

	public void show() {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();
		background = new Sprite(new Texture("golfCourseBeach.jpg"));
		ObjectSet<Texture> objs = skin.getAtlas().getTextures();

		for (Texture t : objs) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		createButtons();

		stage.setViewport(new FitViewport(MiniGolf.WIDTH, MiniGolf.HEIGHT));
		OrthographicCamera secondaryCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		secondaryCamera.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		stage.getViewport().setCamera(secondaryCamera);

		stage.addActor(playButton);
		stage.addActor(OptionsButton);
		stage.addActor(editorButton);
		stage.addActor(exitButton);

		Gdx.input.setInputProcessor(stage);

	}

	/**
	 * it´s responsible for the creation and positioning of all the buttons in
	 * the stage
	 */
	private void createButtons() {
		playButton = new TextButton("Play", skin);
		OptionsButton = new TextButton("Options", skin);
		exitButton = new TextButton("Exit", skin);
		editorButton = new TextButton("Edit", skin);

		playButton.setWidth(BUTTON_WIDTH);
		playButton.setHeight(BUTTON_HEIGHT);

		OptionsButton.setWidth(BUTTON_WIDTH);
		OptionsButton.setHeight(BUTTON_HEIGHT);

		exitButton.setWidth(BUTTON_WIDTH);
		exitButton.setHeight(BUTTON_HEIGHT);

		editorButton.setWidth(BUTTON_WIDTH);
		editorButton.setHeight(BUTTON_HEIGHT);

		playButton.setPosition(MiniGolf.WIDTH / 2 - DELTA_WIDTH, MiniGolf.HEIGHT / 2 + DELTA_HEIGHT * 3);
		editorButton.setPosition(MiniGolf.WIDTH / 2 - DELTA_WIDTH, MiniGolf.HEIGHT / 2 + DELTA_HEIGHT);
		OptionsButton.setPosition(MiniGolf.WIDTH / 2 - DELTA_WIDTH, MiniGolf.HEIGHT / 2 - DELTA_HEIGHT);
		exitButton.setPosition(MiniGolf.WIDTH / 2 - DELTA_WIDTH, MiniGolf.HEIGHT / 2 - DELTA_HEIGHT * 3);

		addListener();
	}

	/**
	 * it ´s responsible for defining the behaviors of all the buttons that will
	 * on the stage
	 */
	private void addListener() {
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new LoadScreen());
			}
		});

		OptionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new OptionsScreen(game));
			}
		});

		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		editorButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new EditorScreen(game));
			}
		});
	}

	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		batch.end();
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		dispose();

	}

	@Override
	public void dispose() {
		batch.dispose();
		skin.dispose();
		stage.dispose();
		background.getTexture().dispose();
	}

}
