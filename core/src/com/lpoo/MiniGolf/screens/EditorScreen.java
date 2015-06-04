package com.lpoo.MiniGolf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lpoo.MiniGolf.logic.Course;
import com.lpoo.MiniGolf.logic.Element;
import com.lpoo.MiniGolf.logic.Element.elementType;
import com.lpoo.MiniGolf.logic.Floor;
import com.lpoo.MiniGolf.logic.MiniGolf;

public class EditorScreen implements Screen, InputProcessor {

	private SpriteBatch batch;
	private Skin skin;
	private Stage stage;
	private SelectBox<String> selectElement;
	private Table scene;
	private Sprite background;
	private TextButton goBackButton;
	private static final float BUTTON_WIDTH = 200f;
	private static final float BUTTON_HEIGHT = 50f;
	private MiniGolf game;
	private Course created;
	private Element elementToAdd;
	private Actor endStage1;
	Vector2 posInit, posFim;
	private boolean drawElement, pressedLeftButton;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera cam = MiniGolf.cam;
	private int cursorPosX, cursorPosY;

	public EditorScreen() {
	}

	public EditorScreen(MiniGolf game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		background.draw(batch);
		batch.end();
		MiniGolf.batch.begin();
		for (int i = 0; i < created.getElementos().size(); i++) {
			created.getElement(i).draw();
		}
		MiniGolf.batch.end();
		if (pressedLeftButton) {
			Vector3 vec = MiniGolf.viewport.unproject(new Vector3((float) cursorPosX, (float) cursorPosY, 0));

			float mouseX = vec.x;
			float mouseY = vec.y;
			shapeRenderer.setProjectionMatrix(cam.combined);
			shapeRenderer.begin(ShapeType.Filled);
	 
			//Square of influence
			shapeRenderer.rectLine(posInit.x, posInit.y, posInit.x, mouseY, 5);
			shapeRenderer.rectLine(posInit.x, posInit.y, mouseX, posInit.y, 5);
			shapeRenderer.rectLine(mouseX, posInit.y, mouseX, mouseY, 5);
			shapeRenderer.rectLine(posInit.x, mouseY, mouseX, mouseY, 5);
			shapeRenderer.end();
		}

		// stage.act(delta);
		// stage.draw();

		cam.update();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		MiniGolf.viewport.update(width, height);
		cam.update();

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();
		background = new Sprite(new Texture("golfCourseBeach.jpg"));
		endStage1 = new Actor();
		posInit = new Vector2();
		posFim = new Vector2();
		shapeRenderer = new ShapeRenderer();
		createElements();
		addListeners();

		/*
		 * stage.setViewport(new FitViewport(MiniGolf.WIDTH, MiniGolf.HEIGHT));
		 * OrthographicCamera secondaryCamera = new
		 * OrthographicCamera(Gdx.graphics.getWidth(),
		 * Gdx.graphics.getHeight());
		 * secondaryCamera.translate(Gdx.graphics.getWidth(),
		 * Gdx.graphics.getHeight() + 300f);
		 * stage.getViewport().setCamera(secondaryCamera);
		 */

		stage.addActor(scene);
		stage.addActor(goBackButton);
		Gdx.input.setInputProcessor(this);

		shapeRenderer.setColor(Color.RED);

	}

	private void addListeners() {

		goBackButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});

		selectElement.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				elementToAdd = getElement(selectElement.getSelected());
			}

		});
	}

	public Element getElement(String selected) {

		switch (selected) {
		case "SandFloor":
			return null;
		default:
			return null;
		}

	}

	private void createElements() {
		created = new Course();

		Label tableLabel = new Label("Editor", skin);

		selectElement = new SelectBox<String>(skin);
		selectElement.setItems(new String[] { "SandFloor", "hole", "ball" });
		selectElement.setMaxListCount(0);
		scene = new Table();
		scene.defaults().width(200f);
		scene.add(tableLabel);
		scene.add(selectElement);
		scene.row();
		scene.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 300f);

		goBackButton = new TextButton("Back", skin);
		goBackButton.setWidth(BUTTON_WIDTH);
		goBackButton.setHeight(BUTTON_HEIGHT);
		goBackButton.setPosition(Gdx.graphics.getWidth() - 500f, Gdx.graphics.getHeight() + 285f);

		Floor grass1 = new Floor(new Vector2(0, 0), 2 * MiniGolf.getWidth() / MiniGolf.BOX_TO_WORLD, 2 * MiniGolf.getHeight() / MiniGolf.BOX_TO_WORLD, elementType.grassFloor);
		grass1.createBody(MiniGolf.getW());
		created.addCourseElement(grass1);
		//
		// Floor sand2 = new Floor(new Vector2(3 * (MiniGolf.WIDTH / 4f /
		// MiniGolf.BOX_TO_WORLD), 3 * (MiniGolf.HEIGHT / 4f /
		// MiniGolf.BOX_TO_WORLD)), MiniGolf.WIDTH / 2f / MiniGolf.BOX_TO_WORLD,
		// MiniGolf.HEIGHT / 2f / MiniGolf.BOX_TO_WORLD, MiniGolf.getW(),
		// elementType.sandFloor);
		// created.addCourseElement(sand2);

	}

	private void addElement() {
		// posInit;
		// posFim;
		// this.elementToAdd
		// this.drawElement

	}

	@Override
	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int posX, int posY) {
		cursorPosX = posX;
		cursorPosY = posY;
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int posX, int posY, int arg2, int button) {

		if (button == Buttons.LEFT) {
			System.out.println("Left pressed");
			posInit.x = posX;
			posInit.y = posY;
			pressedLeftButton = true;
			return true;
		} else if (button == Buttons.RIGHT)
			pressedLeftButton = false;
		return false;
	}

	@Override
	public boolean touchUp(int posX, int posY, int arg2, int button) {
		if (button == Buttons.LEFT) {
			posFim.x = posX;
			posFim.y = posY;
			drawElement = true;
			System.out.println("Left released");
			addElement();
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int posX, int posY, int button) {
		if (button == Buttons.LEFT) {
			cursorPosX = posX;
			cursorPosY = posY;
		}
		return false;
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

}
