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
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.logic.MiniGolf;

// TODO: Auto-generated Javadoc
/**
 * The Class MenuScreen.
 */
public class MenuScreen implements Screen {
	
	/** The batch. */
	private SpriteBatch batch;
	
	/** The skin. */
	private Skin skin;
	
	/** The stage. */
	private Stage stage;
	
	/** The play button. */
	private TextButton playButton;
	
	/** The Options button. */
	private TextButton OptionsButton;
	
	/** The exit button. */
	private TextButton exitButton;
	
	/** The editor button. */
	private TextButton editorButton;
	
	/** The game. */
	private MiniGolf game;
	
	/** The background. */
	private Sprite background;

	/** The delta width. */
	private final float DELTA_WIDTH = 100f;
	
	/** The delta height. */
	private final float DELTA_HEIGHT = 50f;

	// private final static Logger LOGGER =
	// Logger.getLogger(MenuScreen.class.getName());

	/**
	 * Instantiates a new menu screen.
	 *
	 * @param game the game
	 */
	public MenuScreen(MiniGolf game) {
		this.game = game;
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#show()
	 */
	public void show() {
		batch = new SpriteBatch();
		skin = Assets.manager.get("uiskin.json",Skin.class);
		stage = new Stage();
		background = new Sprite(Assets.manager.get("golfCourseBeach.jpg", Texture.class));
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
	 * the stage.
	 */
	private void createButtons() {
		playButton = new TextButton("Play", skin);
		OptionsButton = new TextButton("Options", skin);
		exitButton = new TextButton("Exit", skin);
		editorButton = new TextButton("Edit", skin);

		playButton.setWidth(MiniGolf.BUTTON_WIDTH);
		playButton.setHeight(MiniGolf.BUTTON_HEIGHT);

		OptionsButton.setWidth(MiniGolf.BUTTON_WIDTH);
		OptionsButton.setHeight(MiniGolf.BUTTON_HEIGHT);

		exitButton.setWidth(MiniGolf.BUTTON_WIDTH);
		exitButton.setHeight(MiniGolf.BUTTON_HEIGHT);

		editorButton.setWidth(MiniGolf.BUTTON_WIDTH);
		editorButton.setHeight(MiniGolf.BUTTON_HEIGHT);

		playButton.setPosition(MiniGolf.WIDTH / 2 - DELTA_WIDTH, MiniGolf.HEIGHT / 2 + DELTA_HEIGHT * 3);
		editorButton.setPosition(MiniGolf.WIDTH / 2 - DELTA_WIDTH, MiniGolf.HEIGHT / 2 + DELTA_HEIGHT);
		OptionsButton.setPosition(MiniGolf.WIDTH / 2 - DELTA_WIDTH, MiniGolf.HEIGHT / 2 - DELTA_HEIGHT);
		exitButton.setPosition(MiniGolf.WIDTH / 2 - DELTA_WIDTH, MiniGolf.HEIGHT / 2 - DELTA_HEIGHT * 3);

		addListener();
	}

	/**
	 * it ´s responsible for defining the behaviors of all the buttons that will
	 * on the stage.
	 */
	private void addListener() {
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameScreen(game));
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

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		batch.end();
		stage.act(delta);
		stage.draw();
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		dispose();

	}

	/* (non-Javadoc)
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		batch.dispose();
	 
		stage.dispose();
	 
	}

}
