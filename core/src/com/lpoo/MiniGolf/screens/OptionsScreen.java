package com.lpoo.MiniGolf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
	private TextField numberOfPlayers;
	private TextField numberOfCourses;
	private TextButton goBackButton;
	private SelectBox selectGame;
	private SplitPane pane;
	
	private MiniGolf game;
	private final float DELTA_WIDTH = 200f;
	private final float SLIDER_WIDTH = 200f;
	private final float SLIDER_HEIGHT = 20f;
	private static final float BUTTON_WIDTH = 200f;
	private static final float BUTTON_HEIGHT = 50f;

	public OptionsScreen(MiniGolf game) {
		this.game = game;

	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();
		background = new Sprite(new Texture("golfCourseBeach.jpg"));

		createMenuElements();

		stage.setViewport(new FitViewport(MiniGolf.WIDTH, MiniGolf.HEIGHT));
		OrthographicCamera secondaryCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		secondaryCamera.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		stage.getViewport().setCamera(secondaryCamera);

		Gdx.input.setInputProcessor(stage);
	}

	// Create template
	private void createMenuElements() {
		maxTimeSlider = new Slider(10f, 60f, 1f, false, skin);
		maxTimeSlider.setWidth(SLIDER_WIDTH);
		maxTimeSlider.setHeight(SLIDER_HEIGHT);
		maxTimeSlider.setPosition(Gdx.graphics.getWidth() / 2 + DELTA_WIDTH, Gdx.graphics.getHeight() / 2 - 50f);

		maxTacadasSlider = new Slider(1f, 30f, 1f, false, skin);
		maxTacadasSlider.setWidth(SLIDER_WIDTH);
		maxTacadasSlider.setHeight(SLIDER_HEIGHT);
		maxTacadasSlider.setPosition(Gdx.graphics.getWidth() / 2 + DELTA_WIDTH, Gdx.graphics.getHeight() / 2 - 10f);

		userPicksCheck = new CheckBox("", skin, "default");
		userPicksCheck.setWidth(SLIDER_WIDTH);
		userPicksCheck.setHeight(SLIDER_HEIGHT);
		userPicksCheck.setPosition(Gdx.graphics.getWidth() / 2 + DELTA_WIDTH, Gdx.graphics.getHeight() / 2 + 50f);

		userPicksLabel = new Label("Pick Maps", skin);
		userPicksLabel.setWidth(SLIDER_WIDTH);
		userPicksLabel.setHeight(SLIDER_HEIGHT);
		userPicksLabel.setPosition(Gdx.graphics.getWidth() / 2 + DELTA_WIDTH, Gdx.graphics.getHeight() / 2 + 50f);

		numberOfPlayers = new TextField("Number of Players", skin);
		numberOfPlayers.setWidth(SLIDER_WIDTH);
		numberOfPlayers.setHeight(SLIDER_HEIGHT);
		numberOfPlayers.setPosition(Gdx.graphics.getWidth() / 2 + DELTA_WIDTH, Gdx.graphics.getHeight() / 2 + 80f);

		goBackButton = new TextButton("Exit", skin, "default");
		goBackButton.setWidth(BUTTON_WIDTH);
		goBackButton.setHeight(BUTTON_HEIGHT);
		goBackButton.setPosition(Gdx.graphics.getWidth() / 2 + DELTA_WIDTH, Gdx.graphics.getHeight() / 2 - 100f);

		numberOfCourses = new TextField("Number of Courses", skin);
		numberOfCourses.setWidth(SLIDER_WIDTH);
		numberOfCourses.setHeight(SLIDER_HEIGHT);
		numberOfCourses.setPosition(Gdx.graphics.getWidth() / 2 + DELTA_WIDTH, Gdx.graphics.getHeight() / 2 + 60f);

		addListeners();

		stage.addActor(maxTimeSlider);
		stage.addActor(maxTacadasSlider);
		stage.addActor(userPicksLabel);
		stage.addActor(userPicksCheck);
		stage.addActor(numberOfPlayers);
		stage.addActor(goBackButton);
		stage.addActor(numberOfCourses);

	}

	private void addListeners() {

		numberOfPlayers.setTextFieldFilter(new TextFieldFilter() {

			@Override
			public boolean acceptChar(TextField textField, char c) {
				int valor;
				StringBuilder builder;
				try {
					builder = new StringBuilder(numberOfPlayers.getText() + c);
					valor = Integer.parseUnsignedInt(builder.toString());
				} catch (NumberFormatException e) {

					numberOfPlayers.setText("");
					return false;
				}

				if (valor > MiniGolf.MAX_PLAYERS || valor <= 0)
					return false;
				else {
					MiniGolf.setNrPlayers(valor);
					return true;

				}
			}
		});
		numberOfPlayers.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					Integer.parseUnsignedInt(numberOfPlayers.getText());
				} catch (NumberFormatException e) {
					numberOfPlayers.setText("");
				}

			}
		});

		numberOfCourses.setTextFieldFilter(new TextFieldFilter() {

			@Override
			public boolean acceptChar(TextField textField, char c) {
				int valor;
				StringBuilder builder;
				try {
					builder = new StringBuilder(numberOfCourses.getText() + c);
					valor = Integer.parseUnsignedInt(builder.toString());
				} catch (NumberFormatException e) {

					numberOfCourses.setText("");
					return false;
				}

				if (valor > MiniGolf.MAX_PLAYERS || valor <= 0)
					return false;
				else {
					MiniGolf.setNrCourses(valor);
					return true;

				}
			}
		});

		numberOfCourses.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				try {
					Integer.parseUnsignedInt(numberOfCourses.getText());
				} catch (NumberFormatException e) {
					numberOfCourses.setText("");
				}

			}
		});

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
					MiniGolf.setRandomCourse(false);
				} else {
					MiniGolf.setRandomCourse(true);
				}
			}
		});

		goBackButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
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
		// Gdx.gl.glViewport(arg0, arg1, arg2, arg3);
	}

	@Override
	public void resume() {
	}

}