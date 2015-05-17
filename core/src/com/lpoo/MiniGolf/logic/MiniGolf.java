package com.lpoo.MiniGolf.logic;

import java.util.ArrayList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class MiniGolf extends Game {
	SpriteBatch batch;
	Sprite img;
	static int ballN = 0;
	ArrayList<Ball> balls;
	ArrayList<Obstacle> obstacles;
	Point endPoint;
	Point startPoint;
	int tacadasMax;
	int tempoMax;
	int height;
	int width;
	World W;

	public enum obstacleType {
		normalSquareWall, normalTriangularWall, teleporter, squareOne, regularFloor, accFloor, accWall, startSquareWall, startTriangularWall,
	};

	public MiniGolf() {

	}

	public void create() {
		batch = new SpriteBatch();
		img = new Sprite(new Texture("hero.png"));
	}

	// testes
	public void render() {
		W = new World(new Vector2(0, -10), true);
		 
		//
		// Gdx.gl.glClearColor(1, 0, 0, 1);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// batch.begin();
		// int xi = 0;
		// int yi = 0;
		// for (int i = 0; i < 15; i++) {
		// batch.draw(img.getTexture(), xi, yi, 20, 20);
		// xi += 20;
		// yi += 20;
		// }
		// batch.end();
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't
		// move we would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(100, 300);

		// Create our body in the world using our body definition
		Body body = W.createBody(bodyDef);

		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(6f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);

		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public void setBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public Sprite getImg() {
		return img;
	}

	public void setImg(Sprite img) {
		this.img = img;
	}

	public ArrayList<Ball> getBalls() {
		return balls;
	}

	public void setBalls(ArrayList<Ball> balls) {
		this.balls = balls;
	}

	public Ball getBall(int i) {

		if (i < balls.size())
			return balls.get(i);
		else
			return null;
	}

	public int getNBalls() {
		return balls.size();
	}

	public int getNObstacles() {
		return obstacles.size();
	}

	public Obstacle getObstacle(int i) {

		if (i < obstacles.size())
			return obstacles.get(i);
		else
			return null;
	}

	public ArrayList<Obstacle> getObstacles() {
		return obstacles;
	}

	public void setObstacles(ArrayList<Obstacle> obstacles) {
		this.obstacles = obstacles;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public int getTacadasMax() {
		return tacadasMax;
	}

	public void setTacadasMax(int tacadasMax) {
		this.tacadasMax = tacadasMax;
	}

	public int getTempoMax() {
		return tempoMax;
	}

	public void setTempoMax(int tempoMax) {
		this.tempoMax = tempoMax;
	}

	public static int getBallN() {
		return ballN;
	}

	public static void setBallN(int ballN) {
		MiniGolf.ballN = ballN;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}
