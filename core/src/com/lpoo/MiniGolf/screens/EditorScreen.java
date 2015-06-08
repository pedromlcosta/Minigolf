package com.lpoo.MiniGolf.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lpoo.MiniGolf.data.Assets;
import com.lpoo.MiniGolf.geometry.Geometry;
import com.lpoo.MiniGolf.geometry.Geometry.shapes;
import com.lpoo.MiniGolf.logic.Course;
import com.lpoo.MiniGolf.logic.Element;
import com.lpoo.MiniGolf.logic.Element.elementType;
import com.lpoo.MiniGolf.logic.Floor;
import com.lpoo.MiniGolf.logic.Hole;
import com.lpoo.MiniGolf.logic.MiniGolf;
import com.lpoo.MiniGolf.logic.Teleporter;
import com.lpoo.MiniGolf.logic.Wall;

/**
 * The Class EditorScreen.
 */
public class EditorScreen implements Screen {

	/** The skin. */
	private Skin skin;

	/** The stage. */
	private Stage stage;

	/** The select element. */
	private SelectBox2<String> selectElement;

	/** The scene. */
	private Table scene;

	/** The reset button. */
	private TextButton goBackButton, doneButton, revertLastMoveButton, resetButton;

	/** The game. */
	private MiniGolf game;

	/** The created. */
	private static Course created;

	/** The element to add. */
	private Element elementToAdd;

	/** The Constant HOLE_RADIUS. */
	private static final float HOLE_RADIUS = 0.3f;

	/**
	 * The pos init. this is the inicial position when we start to add an
	 * Element
	 */
	Vector2 posInit;

	/** boolean flags that manage the diferent changes in the editor. */
	private boolean drawElement, pressedLeftButton, pressedRightButton, pressedResetbutton, changedItem, circleElement;

	/** The shape renderer. */
	private ShapeRenderer shapeRenderer;

	/** The cam. */
	private OrthographicCamera cam;

	/**
	 * The current position of the Mouse -> (CursosPosX,cursorPosY) and the
	 * position when the user releases the Mouse Left Button -> (leftX,leftY)
	 */
	private float cursorPosX, cursorPosY, leftX, leftY;

	/** The n players placed. */
	private int nPlayersPlaced = 0;

	/** The n totalt. */
	private int nTotalt = 0;

	/** The n teleporters. must be 2 bigger than nTotalt */
	private int nTeleporters = 0;

	/** The grass floor. */
	Floor grassFloor;

	/** The r key pressed. */
	public static boolean middleMousebutton, rKeyPressed;

	/**
	 * Instantiates a new editor screen.
	 *
	 * @param game
	 *            the game
	 */
	public EditorScreen(MiniGolf game) {
		this.game = game;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#show()
	 */
	@Override
	public void show() {

		skin = Assets.manager.get("uiskin.json", Skin.class);
		stage = new Stage();
		posInit = new Vector2();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		elementToAdd = new Element(Element.elementType.acceleratorFloor);
		// first value
		// of drop
		// down and
		// default
		// value for
		// elementToAdd
		pressedResetbutton = middleMousebutton = circleElement = drawElement = pressedLeftButton = pressedRightButton = false;
		changedItem = true;
		createActors();
		addListeners();

		initStageCamera();

		Gdx.input.setInputProcessor(stage);

	}

	/**
	 * Initiates the stage camera.
	 */
	public void initStageCamera() {
		stage.setViewport(new FitViewport(MiniGolf.WIDTH, MiniGolf.HEIGHT));
		OrthographicCamera secondaryCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		secondaryCamera.translate(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 300f);
		stage.getViewport().setCamera(secondaryCamera);
		cam = (OrthographicCamera) stage.getViewport().getCamera();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#render(float)
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

		drawBall();

		if (pressedLeftButton && !pressedResetbutton) {

			leftX = posInit.x;// leftButtonVec.x;
			leftY = posInit.y;// leftButtonVec.y;
			// TODO
			shapeRenderer.begin(ShapeType.Filled);
			createAuxiliarSquare();
			shapeRenderer.end();

		}

		cam.update();
	}

	/**
	 * Creates the auxiliar square.
	 */
	public void createAuxiliarSquare() {
		if (!circleElement) {
			shapeRendererDraw(Color.RED, shapes.line, leftX, leftY, leftX, cursorPosY, true);
			shapeRendererDraw(Color.RED, shapes.line, leftX, leftY, cursorPosX, leftY, true);
			shapeRendererDraw(Color.RED, shapes.line, cursorPosX, leftY, cursorPosX, cursorPosY, true);
			shapeRendererDraw(Color.RED, shapes.line, leftX, cursorPosY, cursorPosX, cursorPosY, true);
		}
	}

	// TODO
	/**
	 * Removes the actor from editor.
	 *
	 * @param ele
	 *            the ele
	 * @param index
	 *            the index
	 * @param removed
	 *            the removed
	 */
	private void removeActorFromEditor(ArrayList<Element> ele, int index, Element removed) {
		ele.remove(index);
		removed.destroyBody();
		removed.remove();
	}

	/**
	 * Adds the listeners.
	 */
	private void addListeners() {
		/**
		 * It only let´s the user leave after he placed MAX_PLAYERS starting
		 * points Saves the course and adds it to the allCourses
		 */
		doneButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				if (nPlayersPlaced == MiniGolf.MAX_PLAYERS) {
					for (Element ele : created.getElementos()) {
						ele.destroyBody();
					}

					created.setNome("Course" + MiniGolf.getAllCourses().size());
					game.addToAllCourses(created);
					game.getLoadSave().saveIndividualCourse(created);
					game.setScreen(new MenuScreen(game));
				}
			}
		});

		/**
		 * Allows the user to leave if he has no more desires to complete the
		 * Map
		 */
		goBackButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new MenuScreen(game));
			}
		});

		/**
		 * Attempt to prevent the user from placing Elements in the buttons Also
		 * allows user to revert Last Change
		 */
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
				pressedRightButton = false;

				if (elementToAdd.getType() != elementType.ball)
					removeLastElement(created.getElementos());
				else
					removeLastBall(created.getPositions());

			}

		});

		/**
		 * Destroys all Elements and allows user to start fresh With the
		 * exception of grass floor that´s the default floor type
		 */
		resetButton.addListener(new ClickListener() {
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
				pressedRightButton = false;
				nTeleporters = 0;
				nPlayersPlaced = 0;

				// deletes all the starting positions
				created.getPositions().clear();
				ArrayList<Element> elementsArray = created.getElementos();
				int index = elementsArray.size() - 1;
				if (index <= 0)
					return;
				// deletes all elements with the exepction of the grass Floor,
				// the default floor
				for (; index >= 1; index--) {

					Element eleRemoved = elementsArray.get(index);
					if (eleRemoved.getType() != elementType.grassFloor) {
						eleRemoved.destroyBody();
						eleRemoved.remove();
						elementsArray.remove(index);

					}
				}
			}
		});

		/**
		 * Drop down that selects the Element to be placed
		 */
		selectElement.addListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {

				getElement(selectElement.getSelected());
				changedItem = true;
				drawElement = false;
			}

		});
		selectElement.addListener(new InputListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				pressedResetbutton = true;
				drawElement = false;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				pressedResetbutton = false;
				drawElement = false;
			}

		});
		/**
		 * Table where the buttons are placed
		 */
		scene.addListener(new ClickListener() {
			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				pressedResetbutton = true;
				drawElement = false;
				pressedLeftButton = false;
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				pressedResetbutton = false;
				drawElement = false;
				pressedLeftButton = false;
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				pressedResetbutton = true;
				pressedLeftButton = false;
				drawElement = false;
			}
		});
	}

	// replaces the current elementToAdd with the one selected in the drop down
	/**
	 * Gets the element selected in the dropDown.
	 *
	 * @param selected
	 *            the selected
	 * @return the element
	 */
	public void getElement(String selected) {
		switch (selected) {
		case "AcceleratorFloor":
			elementToAdd = new Floor(Element.elementType.acceleratorFloor);
			break;
		case "Ball":
			elementToAdd = new Element(Element.elementType.ball);
			break;
		case "BouncyWall":
			elementToAdd = new Wall(Element.elementType.bouncyWall);
			break;
		case "GlueWall":
			elementToAdd = new Wall(Element.elementType.glueWall);
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
			elementToAdd = new Wall(Element.elementType.regularWall);
			break;
		case "SandFloor":
			elementToAdd = new Floor(Element.elementType.sandFloor);
			break;
		case "Teleporter":
			elementToAdd = new Teleporter(Element.elementType.teleporter);
			break;
		case "VoidFloor":
			elementToAdd = new Floor(Element.elementType.voidFloor);
			break;
		default:
			elementToAdd = null;
			break;
		}

	}

	// creates the actor of the screen, buttons,table
	/**
	 * Creates the actors.
	 */
	private void createActors() {
		created = new Course();
		overrideStageListener();
		Label tableLabel = new Label("Editor", skin);

		Label spaceLabel = new Label("", skin);
		doneButton = new TextButton("Done", skin);
		doneButton.setWidth(MiniGolf.BUTTON_WIDTH);
		doneButton.setHeight(MiniGolf.BUTTON_HEIGHT);

		revertLastMoveButton = new TextButton("Revert Last Placement", skin);
		revertLastMoveButton.setWidth(MiniGolf.BUTTON_WIDTH);
		revertLastMoveButton.setHeight(MiniGolf.BUTTON_HEIGHT);

		resetButton = new TextButton("Reset", skin);
		resetButton.setWidth(MiniGolf.BUTTON_WIDTH);
		resetButton.setHeight(MiniGolf.BUTTON_HEIGHT);

		goBackButton = new TextButton("Back", skin);
		goBackButton.setWidth(MiniGolf.BUTTON_WIDTH);
		goBackButton.setHeight(MiniGolf.BUTTON_HEIGHT);

		selectElement = new SelectBox2<String>(skin);
		selectElement.setItems(new String[] { "AcceleratorFloor", "Ball", "BouncyWall", "GlueWall", "Hole", "IceFloor", "IllusionWall", "WaterFloor", "RegularWall", "SandFloor", "Teleporter",
				"VoidFloor" });
		selectElement.setMaxListCount(0);

		// TODO ADD PLAYER STATUS TO GAME; CHANGE LETTER COLOR
		scene = new Table();
		scene.add(resetButton).width(MiniGolf.BUTTON_WIDTH).height(MiniGolf.BUTTON_HEIGHT);
		scene.add(spaceLabel).width(50f);
		scene.add(revertLastMoveButton).width(MiniGolf.BUTTON_WIDTH).height(MiniGolf.BUTTON_HEIGHT);
		scene.add(spaceLabel).width(50f);
		scene.add(doneButton).width(MiniGolf.BUTTON_WIDTH).height(MiniGolf.BUTTON_HEIGHT);
		scene.add(spaceLabel).width(50f);
		scene.add(goBackButton).width(MiniGolf.BUTTON_WIDTH).height(MiniGolf.BUTTON_HEIGHT);
		scene.add(spaceLabel).width(60);
		scene.add(tableLabel);
		scene.add(spaceLabel).width(10);
		scene.add(selectElement).width(200f);
		scene.row();
		scene.setPosition(MiniGolf.WIDTH - 700f, MiniGolf.HEIGHT - 50f);

		grassFloor = new Floor(new Vector2((MiniGolf.WIDTH / 2f / MiniGolf.BOX_TO_WORLD), (MiniGolf.HEIGHT / 2f / MiniGolf.BOX_TO_WORLD)), MiniGolf.WIDTH / MiniGolf.BOX_TO_WORLD, MiniGolf.HEIGHT
				/ MiniGolf.BOX_TO_WORLD, elementType.grassFloor);
		grassFloor.createBody(MiniGolf.getW());

		grassFloor.createBody(MiniGolf.getW());
		created.addCourseElement(grassFloor);
		stage.addActor(grassFloor);
		stage.addActor(scene);
	}

	/**
	 * Not overlapping. see if the elementToAdd is not overlapping any of the
	 * already placed elements
	 * 
	 * @return true, if successful
	 */
	private boolean notOverlapping() {
		for (Element ele : created.getElementos()) {
			if (ele.overlap(elementToAdd)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * return the a new value be it for width or height depending whether if the
	 * element is a circle and/or of the points selected by the user Gets the
	 * new element pos value.
	 *
	 * @param circle
	 *            the circle
	 * @param pos1
	 *            the pos1
	 * @param pos2
	 *            the pos2
	 * @param pos3
	 *            the pos3
	 * @param pos4
	 *            the pos4
	 * @return the new element pos value
	 */
	public float getNewElementPosValue(boolean circle, float pos1, float pos2, float pos3, float pos4) {
		if (circle) {
			if (changedItem) {
				changedItem = false;
				drawElement = false;
				pressedLeftButton = false;
			}
			return 2 * HOLE_RADIUS;
		}
		return Geometry.distance(pos1, pos2, pos3, pos4);
	}

	// add an element to the course if he meets the requirements:
	// Not being null
	// Not overlapping any of the current Elements
	/**
	 * add an element to the course if he meets the requirements: Not being null
	 * Not overlapping any of the current Elements
	 */
	private void addElement() {
		if (elementToAdd == null)
			return;
		if (drawElement && !pressedRightButton) {
			elementType eleType = elementToAdd.getType();
			if (eleType != elementType.teleporter)
				getElement(selectElement.getSelected());

			float width = 0;
			float height = 0;
			float posInicialX = 0;
			float posInicialY = 0;
			eleType = elementToAdd.getType();

			if (eleType == elementType.hole || eleType == elementType.teleporter || eleType == elementType.squareOne) {

				width = getNewElementPosValue(true, 0, 0, 0, 0);
				height = width;
				elementToAdd.setRadius(HOLE_RADIUS);
				circleElement = true;

			} else {
				circleElement = false;
				width = getNewElementPosValue(false, leftX, leftY, cursorPosX, leftY) / MiniGolf.BOX_TO_WORLD;
				height = getNewElementPosValue(false, leftX, leftY, leftX, cursorPosY) / MiniGolf.BOX_TO_WORLD;
				if (width * height <= 0.04) {

					pressedLeftButton = false;
					drawElement = false;
					pressedRightButton = false;
					return;
				}
			}

			posInicialX = getPosInitial(cursorPosX, leftX);

			posInicialY = getPosInitial(cursorPosY, leftY);

			posInicialX /= MiniGolf.BOX_TO_WORLD;
			posInicialY /= MiniGolf.BOX_TO_WORLD;

			if (nTeleporters % 2 != 0 && elementToAdd.getType() == elementType.teleporter && nTotalt <= 10) {
				elementToAdd.setDestination(new Vector2(posInicialX + width / 2, posInicialY + height / 2));
				nTeleporters++;
				elementToAdd.initializeDestImage();
				getElement(selectElement.getSelected());

			} else {

				elementToAdd.createElement(posInicialX, posInicialY, width, height);

				if (notOverlapping()) {

					if (elementToAdd.getType() != elementType.teleporter) {

						createElement();
					} else {
						if (nTotalt <= 9) {
							nTeleporters++;
							nTotalt++;
							createElement();
							elementToAdd.changeColor(nTotalt);
						}
					}
				} else {
					elementToAdd.destroyBody();
				}
			}

			pressedLeftButton = false;
			drawElement = false;
			pressedRightButton = false;

		}
		pressedRightButton = false;
	}

	/**
	 * Creates the element.
	 */
	public void createElement() {
		stage.addActor(elementToAdd);
		created.addEle(elementToAdd);
		elementToAdd.initializeImage();

	}

	/**
	 * returns a new Pos depending on the placement of the element
	 *
	 * @param cursor
	 * @param left
	 * @return the pos initial
	 */
	public float getPosInitial(float cursor, float left) {
		if (cursor - left < 0) {
			return cursor;
		} else
			return left;
	}

	/**
	 * Overrides the stage listeners: touchUp touchDown touchDragged mouseMoved
	 *
	 * Override stage listener.
	 */
	public void overrideStageListener() {

		stage.addListener(new InputListener() {

			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.R) {
					rKeyPressed = true;
				} else
					rKeyPressed = false;

				return true;
			}

			@Override
			public void touchUp(InputEvent Event, float screenX, float screenY, int pointer, int button) {
				if (button == Buttons.LEFT && !pressedResetbutton) {

					if (elementToAdd != null) {

						if (elementToAdd.getType() == elementType.ball) {
							if (nPlayersPlaced < MiniGolf.MAX_PLAYERS) {
								created.addPosition(new Vector2((screenX + elementToAdd.getWidth() / 2) / MiniGolf.BOX_TO_WORLD, (screenY + elementToAdd.getHeight() / 2) / MiniGolf.BOX_TO_WORLD));
								nPlayersPlaced++;

							}
							pressedLeftButton = false;
							drawElement = false;
							pressedRightButton = true;

							return;
						}
					}
					drawElement = true;
					addElement();
					return;
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

				middleMousebutton = false;
				if (button == Buttons.BACK) {
				} else if (button == Buttons.MIDDLE) {
					middleMousebutton = true;
					return false;
				} else if (button == Buttons.LEFT && !pressedLeftButton) {
					posInit.x = posX;
					posInit.y = posY;
					pressedLeftButton = true;
					pressedRightButton = false;

					return true;
				} else if (button == Buttons.RIGHT) {
					pressedLeftButton = false;
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

	// draws the number of starting positions already placed
	/**
	 * Draw ball.
	 */
	public void drawBall() {

		for (int i = 0; i < nPlayersPlaced; i++) {
			Vector2 pos = created.getPositions().get(i);
			shapeRenderer.begin();
			shapeRendererDraw(Color.GREEN, shapes.circle, pos.x * MiniGolf.BOX_TO_WORLD, pos.y * MiniGolf.BOX_TO_WORLD, 0, 0, true);
			shapeRenderer.end();
		}
	}

	/**
	 * draw using the shape Renderer, must be given color, an enum shap , and
	 * the positions Shape renderer draw.
	 *
	 * @param color
	 *            the color
	 * @param shape
	 *            the shape
	 * @param pos1
	 *            the pos1
	 * @param pos2
	 *            the pos2
	 * @param pos3
	 *            the pos3
	 * @param pos4
	 *            the pos4
	 * @param autoShape
	 *            the auto shape
	 */
	public void shapeRendererDraw(Color color, shapes shape, float pos1, float pos2, float pos3, float pos4, boolean autoShape) {
		shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
		shapeRenderer.setColor(color);
		shapeRenderer.setAutoShapeType(autoShape);
		switch (shape) {
		case line:
			shapeRenderer.rectLine(pos1, pos2, pos3, pos4, 5);
			break;
		case circle:
			shapeRenderer.circle(pos1, pos2, 0.5f * MiniGolf.BOX_TO_WORLD);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#dispose()
	 */
	@Override
	public void dispose() {
		stage.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#hide()
	 */
	@Override
	public void hide() {
		dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#resize(int, int)
	 */
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		cam.update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#pause()
	 */
	@Override
	public void pause() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.badlogic.gdx.Screen#resume()
	 */
	@Override
	public void resume() {
	}

	/**
	 * Removes the last Element from the Elements Array from course, used by
	 * button revertLastMoveButton
	 *
	 * @param ele
	 * 
	 */
	public void removeLastElement(ArrayList<Element> ele) {

		int index = ele.size() - 1;
		if (index == 0)
			return;
		Element removed = ele.get(index);
		removeActorFromEditor(ele, index, removed);
	}

	/**
	 * Removes the last ball.
	 *
	 * @param posVec
	 *            the pos vec
	 */
	public void removeLastBall(ArrayList<Vector2> posVec) {
		int index = posVec.size() - 1;
		if (index < 0)
			return;
		posVec.remove(index);
		nPlayersPlaced--;
	}

	/**
	 * Removes the from array element.
	 *
	 * @param ele
	 *            the ele
	 */
	public static void removeFromArrayElement(Element ele) {
		created.getElementos().remove(ele);
	}
}
