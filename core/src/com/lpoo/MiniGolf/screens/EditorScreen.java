package com.lpoo.MiniGolf.screens;

import geometry.Geometrey;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
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
import com.lpoo.MiniGolf.logic.Hole;
import com.lpoo.MiniGolf.logic.MiniGolf;
import com.lpoo.MiniGolf.logic.Teleporter;

public class EditorScreen implements Screen {
	private Skin skin;
	private Stage stage;
	private SelectBox<String> selectElement;
	private Table scene;

	private TextButton goBackButton, doneButton, revertLastMoveButton, reseteButton;
	private static final float BUTTON_WIDTH = 200f;
	private static final float BUTTON_HEIGHT = 50f;
	private MiniGolf game;
	private Course created;
	private Element elementToAdd;
	private static final float HOLE_RADIUS = 0.3f;
	Vector2 posInit;
	private boolean drawElement, pressedLeftButton, pressedRightButton, pressedResetbutton;
	private ShapeRenderer shapeRenderer;
	private OrthographicCamera cam;
	private float cursorPosX, cursorPosY, leftX, leftY;
	private int nPlayersPlaced = 0;
	private int nTeleporters = 0;

	public EditorScreen(MiniGolf game) {
		this.game = game;
	}

	@Override
	public void show() {

		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage();

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

		stage.act(delta);
		stage.draw();

		if (pressedLeftButton && !pressedResetbutton) {
			// Vector3 mouseVec = stage.getViewport().unproject(new
			// Vector3((float) cursorPosX, (float) cursorPosY, 0));
			// Vector3 leftButtonVec = stage.getViewport().unproject(new
			// Vector3((float) posInit.x, (float) posInit.y, 0));

			leftX = posInit.x;// leftButtonVec.x;
			leftY = posInit.y;// leftButtonVec.y;

			shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.rectLine(leftX, leftY, leftX, cursorPosY, 5);
			shapeRenderer.rectLine(leftX, leftY, cursorPosX, leftY, 5);
			shapeRenderer.rectLine(cursorPosX, leftY, cursorPosX, cursorPosY, 5);
			shapeRenderer.rectLine(leftX, cursorPosY, cursorPosX, cursorPosY, 5);
			shapeRenderer.end();
		}

		cam.update();
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

				if (nPlayersPlaced == 4) {
					for (Element ele : created.getElementos()) {
						ele.destroyBody();
					}

					game.addToSelectedCourses(created);
				}
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
				nTeleporters = 0;

			}
		});

		selectElement.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				getElement(selectElement.getSelected());
			}

		});
		selectElement.addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				pressedResetbutton = true;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				pressedResetbutton = false;
			}

		});
		scene.addListener(new ClickListener() {
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
				pressedRightButton = true;

			}
		});
	}

	public void getElement(String selected) {
		switch (selected) {
		case "Ball":
			elementToAdd = new Element(Element.elementType.ball);
			break;
		case "BouncyWall":
			elementToAdd = new Floor(Element.elementType.bouncyWall);
			break;
		case "GlueWall":
			elementToAdd = new Floor(Element.elementType.glueWall);
			break;
		case "Hole":
			elementToAdd = new Hole();
			elementToAdd.setType(elementType.hole);
			break;
		case "IceFloor":
			elementToAdd = new Floor(elementType.iceFloor);
			break;
		case "IllusionWall":
			elementToAdd = new Floor(Element.elementType.illusionWall);
			break;
		case "WaterFloor":
			elementToAdd = new Floor(elementType.waterFloor);
			break;
		case "RegularWall":
			elementToAdd = new Floor(Element.elementType.regularWall);
			break;
		case "SquareOne":
			// elementToAdd = new Floor(Element.elementType.squareOne);
			break;
		case "SandFloor":
			elementToAdd = new Floor(Element.elementType.sandFloor);
			break;
		case "Teleporter":
			elementToAdd = new Teleporter(Element.elementType.teleporter);
			elementToAdd.changeColor(nTeleporters + 1);

			break;
		case "VoidFloor":
			elementToAdd = new Floor(Element.elementType.voidFloor);
			break;
		default:
			elementToAdd = null;
			break;
		}

	}

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
		selectElement.setItems(new String[] { "Ball", "BouncyWall", "GlueWall", "Hole", "IceFloor", "IllusionWall", "WaterFloor", "RegularWall", "SandFloor", "SquareOne", "Teleporter", "VoidFloor" });
		selectElement.setMaxListCount(1);

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

			float width = 0;
			float height = 0;
			float posInicialX = 0;
			float posInicialY = 0;
			elementType eleType = elementToAdd.getType();

			if (eleType == elementType.hole || eleType == elementType.teleporter || eleType == elementType.squareOne) {
				width = 2 * HOLE_RADIUS;
				height = width;
				elementToAdd.setRadius(HOLE_RADIUS);

			} else {
				width = (float) (Geometrey.distance(leftX, leftY, cursorPosX, leftY) / MiniGolf.BOX_TO_WORLD);
				height = (float) (Geometrey.distance(leftX, leftY, leftX, cursorPosY) / MiniGolf.BOX_TO_WORLD);
				if (width * height <= 0.04) {

					pressedLeftButton = false;
					drawElement = false;
					pressedRightButton = false;
					return;
				}
			}

			if (cursorPosX - leftX < 0) {
				posInicialX = cursorPosX;
			} else
				posInicialX = leftX;

			if (cursorPosY - leftY < 0) {
				posInicialY = cursorPosY;
			} else
				posInicialY = leftY;

			posInicialX /= MiniGolf.BOX_TO_WORLD;
			posInicialY /= MiniGolf.BOX_TO_WORLD;

			if (eleType == elementType.ball) {
				if (nPlayersPlaced < 4) {
					created.addPosition(new Vector2(posInicialX, posInicialY));
					nPlayersPlaced++;

				}

			} else if (nTeleporters % 2 != 0 && elementToAdd.getType() == elementType.teleporter) {
				System.out.println("Here");
				elementToAdd.setDestination(new Vector2(posInicialX, posInicialY));
				System.out.println(nTeleporters);
				nTeleporters++;

			} else {
				if (elementToAdd == null)
					return;
				elementToAdd.createElement(posInicialX, posInicialY, width, height);

				if (notOverlapping()) {
					if (elementToAdd.getType() == elementType.teleporter)
						nTeleporters++;

					stage.addActor(elementToAdd);
					this.created.addEle(elementToAdd);

				} else {
					elementToAdd.destroyBody();
				}
			}

			pressedLeftButton = false;
			drawElement = false;
			pressedRightButton = false;
			elementToAdd = null;
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

				if (button == Buttons.LEFT && !pressedLeftButton && !pressedResetbutton) {
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
					posInit.x = posX;
					posInit.y = posY;
				}
				return false;
			}

		});

	}

	@Override
	public void dispose() {
		skin.dispose();
		stage.dispose();

	}

	@Override
	public void hide() {
		dispose();

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

}
