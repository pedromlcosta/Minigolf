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
import com.lpoo.MiniGolf.logic.Course;
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

	private final float DELTA_WIDTH = 200f;
	private static final float BUTTON_WIDTH = 200f;
	private static final float BUTTON_HEIGHT = 50f;

	public OptionsScreen(MiniGolf game) {
		this.game = game;

	}

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

	// Create template
	private void createMenuElements() {

		maxTimeSlider = new Slider(10f, 60f, 1f, false, skin);
		numberOfPlayers = new TextField("Number of Players", skin);
		numberOfCourses = new TextField("Number of Courses", skin);

		String[] newItems;
		newItems = new String[game.getSelectedCourses().size()];

		int i = 0;
		for (Course c : game.getSelectedCourses()) {
			if (c.getNome() != null) {
				newItems[i] = c.getNome();
				i++;
			}
		}

		goBackButton = new TextButton("Back", skin);
		goBackButton.setWidth(BUTTON_WIDTH);
		goBackButton.setHeight(BUTTON_HEIGHT);
		goBackButton.setPosition(MiniGolf.WIDTH / 2 - 150f, MiniGolf.HEIGHT / 2 - 100f);
		stage.addActor(goBackButton);
		addListeners();
		createTable();

	}

	/**
	 * creates the tables that mantain the Menu
	 */
	// TODO ver fun��es sem numero de argumentos;
	private void createTable() {
		gameOptionsTable = new Table();

		Label maxNameTimeLabel = new Label("Max Time:", skin);
		maxTimeLabel = new Label("Time: 0", skin);

		Label numberOfPlayersLabel = new Label(" Number Of Players: ", skin);
		Label numberOfCoursesLabel = new Label(" Number Of Courses: ", skin);
		Label spaceLabel = new Label("", skin);

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
					game.setNrPlayers(valor);
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
				// TODO change for MAX_COURSES
				if (valor > MiniGolf.getAllCourses().size() || valor <= 0)
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
				maxTimeLabel.setText(new StringBuilder(" Time Max: " + (int) maxTimeSlider.getValue()));
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

	}

	@Override
	public void resume() {
	}
}