package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.lpoo.MiniGolf.geometry.Geometry;

public class Teste {

	@Test
	public void teste() {

		CircleShape c1 = new CircleShape();
		CircleShape c2 = new CircleShape();
		c1.setRadius(2);
		c2.setRadius(4);
		c1.setPosition(new Vector2(5, 5));
		c2.setPosition(new Vector2(7, 7));
		// assertEquals(true, Geometry.overlapCircles(c1, new Vector2(5, 5), c2,
		// new Vector2(7, 7)));
		// Hole hole = new Hole();
		// Hole hole1 = new Hole(new Vector2(5f, 5f), 0.3f);
		//
		// Floor sand1 = new Floor(new Vector2(3 * (MiniGolf.WIDTH / 12f /
		// MiniGolf.BOX_TO_WORLD), 9 * (MiniGolf.HEIGHT / 12f /
		// MiniGolf.BOX_TO_WORLD)), MiniGolf.WIDTH / 6f / MiniGolf.BOX_TO_WORLD,
		// MiniGolf.HEIGHT / 2f / MiniGolf.BOX_TO_WORLD, elementType.sandFloor);
		//
		// Wall glue1 = new Wall(new Vector2(5 * (MiniGolf.WIDTH / 12f /
		// MiniGolf.BOX_TO_WORLD), 9 * (MiniGolf.HEIGHT / 12f /
		// MiniGolf.BOX_TO_WORLD)), MiniGolf.WIDTH / 6f / MiniGolf.BOX_TO_WORLD,
		// MiniGolf.HEIGHT / 2f / MiniGolf.BOX_TO_WORLD, elementType.glueWall);
		// Teleporter teleporter1 = new Teleporter(new Vector2(7f, 5f), new
		// Vector2(7f, 3f), 0.3f, 1);

	}
}
