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
import com.badlogic.gdx.scenes.scene2d.InputListener;
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
	private TextButton goBackButton, doneButton;
	private static final float BUTTON_WIDTH = 200f;
	private static final float BUTTON_HEIGHT = 50f;
	private MiniGolf game;
	private Course created;
	private Element elementToAdd;
	private Actor endStage1;
	Vector2 posInit;
	private boolean drawElement, pressedLeftButton;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera cam = MiniGolf.cam;
	private int cursorPosX, cursorPosY;
	private float mouseX;
	private float mouseY;
	private float leftX, leftY;

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
			if (i == 1){
				
			}
//				System.out.println("i: " + i + " Height: " + created.getElement(i).getHeight() + "  Width: " + created.getElement(i).getWidth() + "   PosX: " + created.getElement(i).getPosX()
//						+ "   PosY: " + created.getElement(i).getPosY());
		}
		MiniGolf.batch.end();
		if (pressedLeftButton) {
			Vector3 mouseVec = MiniGolf.viewport.unproject(new Vector3((float) cursorPosX, (float) cursorPosY, 0));
			Vector3 leftButtonVec = MiniGolf.viewport.unproject(new Vector3((float) posInit.x, (float) posInit.y, 0));
			mouseX = mouseVec.x;
			mouseY = mouseVec.y;
			leftX = leftButtonVec.x;
			leftY = leftButtonVec.y;

			shapeRenderer.setProjectionMatrix(cam.combined);
			shapeRenderer.begin(ShapeType.Filled);

//			System.out.println(leftX + "   " + leftY + "   " + mouseX + "  " + mouseY);

			// Square of influence
			/* 1 2 */
			// System.out.println("Height: " + distance(leftX, leftY, leftX,
			// mouseY) + "   Width: " + distance(leftX, leftY, mouseX, leftY));
			shapeRenderer.rectLine(leftX, leftY, leftX, mouseY, 5);
			shapeRenderer.rectLine(leftX, leftY, mouseX, leftY, 5);
			shapeRenderer.rectLine(mouseX, leftY, mouseX, mouseY, 5);
			shapeRenderer.rectLine(leftX, mouseY, mouseX, mouseY, 5);
			shapeRenderer.end();
		}

		stage.act(delta);
		stage.draw();

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

	public float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	@Override
	public void show() {

		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();
		background = new Sprite(new Texture("golfCourseBeach.jpg"));
		endStage1 = new Actor();
		posInit = new Vector2();
		shapeRenderer = new ShapeRenderer();
		createElements();
		addListeners();

		stage.setViewport(new FitViewport(MiniGolf.WIDTH, MiniGolf.HEIGHT));
		OrthographicCamera secondaryCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		secondaryCamera.translate(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 300f);
		stage.getViewport().setCamera(secondaryCamera);

		stage.addActor(scene);
		Gdx.input.setInputProcessor(stage);

		shapeRenderer.setColor(Color.RED);

		 stage.addListener(new InputListener() {
		
		 public boolean mouseMoved(int posX, int posY ) {
		
		 return false;
		
		 }});

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

		Label spaceLabel = new Label("", skin);
		doneButton = new TextButton("Done", skin);
		doneButton.setWidth(BUTTON_WIDTH);
		doneButton.setHeight(BUTTON_HEIGHT);

		goBackButton = new TextButton("Back", skin);
		goBackButton.setWidth(BUTTON_WIDTH);
		goBackButton.setHeight(BUTTON_HEIGHT);

		selectElement = new SelectBox<String>(skin);
		selectElement.setItems(new String[] { "SandFloor", "hole", "ball" });
		selectElement.setMaxListCount(0);
		scene = new Table();
		scene.add(doneButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
		scene.add(spaceLabel).width(50f);
		scene.add(goBackButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
		scene.add(spaceLabel).width(50);
		scene.add(tableLabel);
		scene.add(selectElement).width(200f);
		scene.row();
		scene.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 300f);

		 
		// Floor grass1 = new Floor(new Vector2(0, 0), MiniGolf.getWidth() /
		// MiniGolf.BOX_TO_WORLD, MiniGolf.getHeight() / MiniGolf.BOX_TO_WORLD,
		// elementType.grassFloor); grass1.createBody(MiniGolf.getW());
		// created.addCourseElement(grass1);
		 

		//
		// Floor sand2 = new Floor(new Vector2(3 * (MiniGolf.WIDTH / 4f /
		// MiniGolf.BOX_TO_WORLD), 3 * (MiniGolf.HEIGHT / 4f /
		// MiniGolf.BOX_TO_WORLD)), MiniGolf.WIDTH / 2f / MiniGolf.BOX_TO_WORLD,
		// MiniGolf.HEIGHT / 2f / MiniGolf.BOX_TO_WORLD, MiniGolf.getW(),
		// elementType.sandFloor);
		// created.addCourseElement(sand2);

	}

	private void addElement() {
		if (drawElement) {
			elementToAdd = new Floor(Element.elementType.sandFloor);

			float width, height, posInicialX, posInicialY;
			// width = (mouseX - leftX) / MiniGolf.BOX_TO_WORLD;
			// height = (mouseY - leftY) / MiniGolf.BOX_TO_WORLD;
			width = (float) (1 * distance(leftX, leftY, mouseX, leftY) / MiniGolf.BOX_TO_WORLD);
			height = (float) (1 * distance(leftX, leftY, leftX, mouseY) / MiniGolf.BOX_TO_WORLD);

			// System.out.println((mouseY - leftY) + "   " + (mouseY - leftY) /
			// MiniGolf.BOX_TO_WORLD);
			// width e heights negativas,funciona se o ponto inicial for 0
			if (mouseY - leftY < 0) {
				posInicialX = mouseX;
				width *= -1;
			} else
				posInicialX = leftX;

			if (mouseY - leftY < 0) {
				posInicialY = mouseY;
				height *= -1;
			} else
				posInicialY = leftY;

//			System.out.println("In add Height: " + height + "   Width: " + width + "  PosEX: " + posInicialX + " PosEY: " + posInicialY);
			posInicialX /= MiniGolf.BOX_TO_WORLD;
			posInicialY /= MiniGolf.BOX_TO_WORLD;

			// será que tenho que voltar a projectar para o screen
			// System.out.println("mouseX: " + mouseX + "  mouseY: " + mouseY +
			// " PosX: " + leftX + "  PosY: " + leftY + " Width: " + width +
			// "  Height: " + height);
			elementToAdd.setOldPos(new Vector2(posInicialX, posInicialY));
			elementToAdd.setHeight(height);
			elementToAdd.setWidth(width);
			elementToAdd.createBody(MiniGolf.getW());
			this.created.addEle(elementToAdd);
			pressedLeftButton = false;
			drawElement = false;
			System.out.println(created.getElementos().size());
		}

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
		if (!pressedLeftButton) {
			posInit.x = posX;
			posInit.y = posY;
		}

		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int posX, int posY, int arg2, int button) {

		if (button == Buttons.LEFT && !pressedLeftButton) {
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
