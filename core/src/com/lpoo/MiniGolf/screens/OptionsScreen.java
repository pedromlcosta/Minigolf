package com.lpoo.MiniGolf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.logic.MiniGolf;

public class OptionsScreen implements Screen {
	private SpriteBatch batch;
	private Skin skin;
	private Stage stage;
	private Slider maxTimeSlider;
	private Label maxTimeLabel;
	private Sprite background;
	private TextField numberOfPlayers;
	private TextField numberOfCourses;
	private TextButton goBackButton;
	private Table gameOptionsTable;
	private MiniGolf game;

	/**
	 * Constructor of the screen
	 * 
	 * @param game
	 */
	public OptionsScreen(MiniGolf game) {
		this.game = game;

	}

	/**
	 * Instantiates all elements that will be used through out the screen
	 */
	@Override
	public void show() {
		batch = new SpriteBatch();
		skin = Assets.manager.get("uiskin.json", Skin.class);
		stage = new Stage();
		background = new Sprite(Assets.manager.get("golfCourseBeach.jpg", Texture.class));

		createMenuElements();

		stage.setViewport(new FitViewport(MiniGolf.WIDTH, MiniGolf.HEIGHT));
		OrthographicCamera secondaryCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		secondaryCamera.translate(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		stage.getViewport().setCamera(secondaryCamera);

		Gdx.input.setInputProcessor(stage);
	}

	/**
	 * In charge of initializing the actors
	 */
	private void createMenuElements() {

		maxTimeSlider = new Slider(10f, 60f, 1f, false, skin);
		maxTimeSlider.setValue(game.getTempoMax());
		numberOfPlayers = new TextField("Number of Players", skin);
		numberOfCourses = new TextField("Number of Courses", skin);

		goBackButton = new TextButton("Back", skin);
		goBackButton.setWidth(MiniGolf.BUTTON_WIDTH);
		goBackButton.setHeight(MiniGolf.BUTTON_HEIGHT);
		goBackButton.setPosition(MiniGolf.WIDTH / 2 - 150f, MiniGolf.HEIGHT / 2 - 100f);
		stage.addActor(goBackButton);
		addListeners();
		createTable();

	}

	/**
	 * creates the tables that maintains the Menu
	 */
	private void createTable() {
		gameOptionsTable = new Table();

		Label maxNameTimeLabel = new Label("Max Time:", skin);
		maxTimeLabel = new Label("Time: " + game.getTempoMax(), skin);

		Label numberOfPlayersLabel = new Label(" Number Of Players: ", skin);
		Label numberOfCoursesLabel = new Label(" Number Of Courses: ", skin);
		Label spaceLabel = new Label("", skin);
		numberOfCoursesLabel.setColor(Color.BLACK);
		numberOfPlayersLabel.setColor(Color.BLACK);
		maxNameTimeLabel.setColor(Color.BLACK);
		maxTimeLabel.setColor(Color.BLACK);
		gameOptionsTable.defaults().width(200);

		gameOptionsTable.add(maxNameTimeLabel);
		gameOptionsTable.add(maxTimeSlider);
		gameOptionsTable.add(maxTimeLabel);
		gameOptionsTable.add(spaceLabel);
		gameOptionsTable.row();

		gameOptionsTable.add(numberOfPlayersLabel);
		gameOptionsTable.add(numberOfPlayers);
		gameOptionsTable.add(spaceLabel);
		gameOptionsTable.add(spaceLabel);
		gameOptionsTable.add(numberOfCoursesLabel);
		gameOptionsTable.add(numberOfCourses);
		gameOptionsTable.row();

		// TODO MiniGolf.WIDTH
		// TODO MiniGolf.HEIGHT
		gameOptionsTable.setPosition(MiniGolf.WIDTH / 2, MiniGolf.HEIGHT / 2 + 60f);

		stage.addActor(gameOptionsTable);

		// General table

	}

	/**
	 * adds listeners to the actors
	 */
	private void addListeners() {

		/**
		 * Validates if the value given by the user is acceptable A value is
		 * considered acceptable when it´s bigger than 0 but not bigger than the
		 * number of MAX_Player
		 */
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
					game.setNrPlayers(valor);
					return true;
				}
			}
		});
		/**
		 * if the value on the text isn´t valid it´s erased
		 */
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
		/**
		 * it aceptes a value when it is a number and said number is not bigger
		 * than the number of All Courses available
		 */
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
				// TODO change for MAX_COURSES
				if (valor > MiniGolf.getAllCourses().size() || valor <= 0)
					return false;
				else {
					MiniGolf.setNrCourses(valor);
					return true;
				}
			}
		});
		/**
		 * if the value not valid it erases the value
		 */
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
		/**
		 * updates the variable TempoMax in MiniGolf and changes the Label
		 * accordingly
		 */
		maxTimeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				game.setTempoMax((int) maxTimeSlider.getValue());
				maxTimeLabel.setText(new StringBuilder(" Time Max: " + (int) maxTimeSlider.getValue()));
			}
		});

		/**
		 * Returns to the MenuScreen
		 */
		goBackButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});

	}

	/**
	 * Disposes of the screen Elements
	 */
	@Override
	public void dispose() {
		batch.dispose();

		stage.dispose();
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	/**
	 * Redraws the screen
	 */
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

	}

	@Override
	public void resume() {
	}
}