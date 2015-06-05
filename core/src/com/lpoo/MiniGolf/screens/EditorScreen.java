package com.lpoo.MiniGolf.screens;

import java.util.ArrayList;

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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
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
import com.lpoo.MiniGolf.logic.ElementType;
import com.lpoo.MiniGolf.logic.Floor;
import com.lpoo.MiniGolf.logic.MiniGolf;

//TODO array the shape2d e vou testando
public class EditorScreen implements Screen {

	private SpriteBatch batch;
	private Skin skin;
	private Stage stage;
	private SelectBox<String> selectElement;
	private Table scene;
	private Sprite background;
	private TextButton goBackButton, doneButton, revertLastMoveButton, reseteButton;
	private static final float BUTTON_WIDTH = 200f;
	private static final float BUTTON_HEIGHT = 50f;
	private MiniGolf game;
	private Course created;
	private Element elementToAdd;

	Vector2 posInit;
	private boolean drawElement, pressedLeftButton, pressedRightButton, pressedResetbutton;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera cam;
	private float cursorPosX, cursorPosY, mouseX, mouseY, leftX, leftY;

	public EditorScreen() {
	}

	public EditorScreen(MiniGolf game) {
		this.game = game;
	}

	@Override
	public void show() {

		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();
		background = new Sprite(new Texture("golfCourseBeach.jpg"));

		posInit = new Vector2();
		shapeRenderer = new ShapeRenderer();
		elementToAdd = new Floor(Element.elementType.bouncyWall);
		// first value
		// of drop
		// down and
		// default
		// value for
		// elementToAdd
		pressedResetbutton = drawElement = pressedLeftButton = pressedRightButton = false;
		createActors();
		addListeners();

		stage.setViewport(new FitViewport(MiniGolf.WIDTH, MiniGolf.HEIGHT));
		OrthographicCamera secondaryCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		secondaryCamera.translate(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 300f);
		stage.getViewport().setCamera(secondaryCamera);
		cam = (OrthographicCamera) stage.getViewport().getCamera();
		stage.addActor(scene);
		Gdx.input.setInputProcessor(stage);

		shapeRenderer.setColor(Color.RED);

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		// background.draw(batch);
		batch.end();

		// MiniGolf.batch.begin(); for (int i = 0; i <
		// created.getElementos().size(); i++) {
		// created.getElement(i).draw(MiniGolf.batch, 0); if (i == 1)
		// System.out.println("i: " + i + " Height: " +
		// created.getElement(i).getHeight() + "  Width: " +
		// created.getElement(i).getWidth() + "   PosX: " +
		// created.getElement(i).getPosX() + "   PosY: " +
		// created.getElement(i).getPosY()); } MiniGolf.batch.end();

		stage.act(delta);
		stage.draw();
		// System.out.println(pressedLeftButton);
		if (pressedLeftButton && !pressedResetbutton) {
			// Vector3 mouseVec = stage.getViewport().unproject(new
			// Vector3((float) cursorPosX, (float) cursorPosY, 0));
			// Vector3 leftButtonVec = stage.getViewport().unproject(new
			// Vector3((float) posInit.x, (float) posInit.y, 0));
			mouseX = cursorPosX;// mouseVec.x;
			mouseY = cursorPosY;// mouseVec.y;
			leftX = posInit.x;// leftButtonVec.x;
			leftY = posInit.y;// leftButtonVec.y;

			shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
			shapeRenderer.begin(ShapeType.Filled);

			// System.out.println(leftX + "   " + leftY + "   " + mouseX + "  "
			// + mouseY);

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

		cam.update();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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

	public static float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	// TODO
	private void removeActorFromEditor(ArrayList<Element> ele, int index, Element removed) {
		ele.remove(index);
		removed.destroyBody();
		removed.remove();
	}

	private void addListeners() {
		doneButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				for (Element ele : created.getElementos()) {
					ele.destroyBody();
				}

				game.addToSelectedCourses(created);
				// add course to thingy
				// game.setScreen(new MenuScreen(game));
			}
		});

		goBackButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});

		revertLastMoveButton.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				pressedResetbutton = true;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				pressedResetbutton = false;
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				pressedResetbutton = true;
				pressedLeftButton = false;
				ArrayList<Element> ele = created.getElementos();
				int index = ele.size() - 1;
				// System.out.println(index);
				if (index == 0)
					return;
				Element removed = ele.get(index);
				removeActorFromEditor(ele, index, removed);

			}

		});

		reseteButton.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				pressedResetbutton = true;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				pressedResetbutton = false;
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				pressedResetbutton = true;
				pressedLeftButton = false;
				elementToAdd = null;
				ArrayList<Element> elementsArray = created.getElementos();
				int index = elementsArray.size() - 1;
				if (index == 0)
					return;
				for (; index >= 1; index--) {

					Element eleRemoved = elementsArray.get(index);
					eleRemoved.destroyBody();
					eleRemoved.remove();
					elementsArray.remove(index);

				}

			}
		});
	}

	// TODO passar para Element método static??
	public void getElement(String selected) {
		switch (selected) {
		case "BouncyWall":
			elementToAdd = new Floor(Element.elementType.bouncyWall);
			break;
		case "GlueWall":
			elementToAdd = new Floor(Element.elementType.glueWall);
			break;
		case "Hole":
			elementToAdd = new Floor(Element.elementType.hole);
			break;
		case "IceFloor":
			// elementToAdd = new Floor(Element.elementType.iceFloor);
			break;
		case "IllusionWall":
			elementToAdd = new Floor(Element.elementType.illusionWall);
			break;
		case "WaterFloor":
			// elementToAdd = new Floor(Element.elementType.sandFloor);
			break;
		case "RegularWall":
			elementToAdd = new Floor(Element.elementType.regularWall);
			break;
		case "SquareOne":
			elementToAdd = new Floor(Element.elementType.squareOne);
			break;
		case "SandFloor":
			elementToAdd = new Floor(Element.elementType.sandFloor);
			break;
		case "Teleporter":
			// elementToAdd = new Floor(Element.elementType.teleporter);
			break;
		case "VoidFloor":
			elementToAdd = new Floor(Element.elementType.voidFloor);
			break;
		default:
			break;
		}

	}

	// TODO area minima 0.2*0.2 em quadrado
	private void createActors() {
		created = new Course();
		overrideStageListener();
		Label tableLabel = new Label("Editor", skin);

		Label spaceLabel = new Label("", skin);
		doneButton = new TextButton("Done", skin);
		doneButton.setWidth(BUTTON_WIDTH);
		doneButton.setHeight(BUTTON_HEIGHT);

		revertLastMoveButton = new TextButton("Revert Last Placement", skin);
		revertLastMoveButton.setWidth(BUTTON_WIDTH);
		revertLastMoveButton.setHeight(BUTTON_HEIGHT);

		reseteButton = new TextButton("Reset", skin);
		reseteButton.setWidth(BUTTON_WIDTH);
		reseteButton.setHeight(BUTTON_HEIGHT);

		goBackButton = new TextButton("Back", skin);
		goBackButton.setWidth(BUTTON_WIDTH);
		goBackButton.setHeight(BUTTON_HEIGHT);

		selectElement = new SelectBox<String>(skin);
		selectElement.setItems(new String[] { "BouncyWall", "GlueWall", "Hole", "IceFloor", "IllusionWall", "WaterFloor", "RegularWall", "SandFloor", "SquareOne", "Teleporter", "VoidFloor" });
		selectElement.setMaxListCount(0);
		scene = new Table();
		scene.add(reseteButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
		scene.add(spaceLabel).width(50f);
		scene.add(revertLastMoveButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
		scene.add(spaceLabel).width(50f);
		scene.add(doneButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
		scene.add(spaceLabel).width(50f);
		scene.add(goBackButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT);
		scene.add(spaceLabel).width(50);
		scene.add(tableLabel);
		scene.add(selectElement).width(200f);
		scene.row();
		scene.setPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 300f);

		Floor grass1 = new Floor(new Vector2(0, 0), MiniGolf.getWidth() / MiniGolf.BOX_TO_WORLD, MiniGolf.getHeight() / MiniGolf.BOX_TO_WORLD, elementType.grassFloor);
		grass1.createBody(MiniGolf.getW());
		// Floor grass1 = new Floor(new Vector2((MiniGolf.WIDTH / 2f /
		// MiniGolf.BOX_TO_WORLD), (MiniGolf.HEIGHT / 2f /
		// MiniGolf.BOX_TO_WORLD)), MiniGolf.WIDTH / MiniGolf.BOX_TO_WORLD,
		// MiniGolf.HEIGHT
		// / MiniGolf.BOX_TO_WORLD, elementType.grassFloor);
		grass1.createBody(MiniGolf.getW());
		created.addCourseElement(grass1);
		stage.addActor(grass1);

		// Floor sand2 = new Floor(new Vector2(3 * (MiniGolf.WIDTH / 4f /
		// MiniGolf.BOX_TO_WORLD), 3 * (MiniGolf.HEIGHT / 4f /
		// MiniGolf.BOX_TO_WORLD)), MiniGolf.WIDTH / 2f / MiniGolf.BOX_TO_WORLD,
		// MiniGolf.HEIGHT / 2f / MiniGolf.BOX_TO_WORLD, MiniGolf.getW(),
		// elementType.sandFloor);
		// created.addCourseElement(sand2);
	}

	private boolean notOverlapping() {

		for (Element ele : created.getElementos()) {
			if (ele.overlap(elementToAdd)) {
				return false;
			}
		}
		return true;
	}

	private void addElement() {
		if (drawElement && !pressedRightButton) {
			getElement(selectElement.getSelected());

			if (elementToAdd == null)
				return;
			float width, height, posInicialX, posInicialY;
			width = (float) (1 * distance(leftX, leftY, mouseX, leftY) / MiniGolf.BOX_TO_WORLD);
			height = (float) (1 * distance(leftX, leftY, leftX, mouseY) / MiniGolf.BOX_TO_WORLD);

			if (mouseX - leftX < 0) {
				posInicialX = mouseX;
			} else
				posInicialX = leftX;

			if (mouseY - leftY < 0) {
				posInicialY = mouseY;
			} else
				posInicialY = leftY;

			elementToAdd.setOldPos(new Vector2(posInicialX, posInicialY));

			posInicialX /= MiniGolf.BOX_TO_WORLD;
			posInicialY /= MiniGolf.BOX_TO_WORLD;
			if (notOverlapping()) {
				elementToAdd.setOldPos(new Vector2(posInicialX, posInicialY));
				elementToAdd.setHeight(height);
				elementToAdd.setWidth(width);
				elementToAdd.createBody(MiniGolf.getW());
				stage.addActor(elementToAdd);
				this.created.addEle(elementToAdd);
			}
			pressedLeftButton = false;
			drawElement = false;
			pressedRightButton = false;
		}
		pressedRightButton = false;
	}

	public void overrideStageListener() {

		stage.addListener(new InputListener() {

			@Override
			public void touchUp(InputEvent Event, float screenX, float screenY, int pointer, int button) {
				// Event.getRelatedActor().getClass().getName();
				if (button == Buttons.LEFT && !pressedResetbutton) {
					drawElement = true;
					// System.out.println("Left released");
					addElement();
				}
			}

			@Override
			public void touchDragged(InputEvent Event, float posX, float posY, int button) {
				if (button == Buttons.LEFT && !pressedResetbutton) {
					cursorPosX = posX;
					cursorPosY = posY;
				}
			}

			@Override
			public boolean touchDown(InputEvent Event, float posX, float posY, int arg2, int button) {

				if (button == Buttons.LEFT && !pressedLeftButton) {

					// System.out.println("Left pressed");
					posInit.x = posX;
					posInit.y = posY;
					pressedLeftButton = true;

					pressedRightButton = false;
					return true;
				} else if (button == Buttons.RIGHT) {
					pressedLeftButton = false;
					elementToAdd = null;
					drawElement = false;
					pressedRightButton = true;
				}
				return false;
			}

			@Override
			public boolean mouseMoved(InputEvent Event, float posX, float posY) {
				cursorPosX = posX;
				cursorPosY = posY;
				if (!pressedLeftButton && !pressedResetbutton) {
					// System.out.println("Moved");
					posInit.x = posX;
					posInit.y = posY;
				}
				return false;
			}

		});

	}

	@Override
	public void dispose() {
		batch.dispose();
		skin.dispose();
		stage.dispose();
		background.getTexture().dispose();

	}

	@Override
	public void hide() {
		dispose();

	}

}
