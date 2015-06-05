package geometry;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.lpoo.MiniGolf.logic.Element;

public class Geometrey {

	private final static float tolerance = 0.1f;

	public Geometrey() {

	}

	public static float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public static boolean overlapCircles(CircleShape c1, Vector2 posC1, CircleShape c2, Vector2 posC2) {

		float radiusC1 = c1.getRadius();
		float radiusC2 = c2.getRadius();
		float dist = distance(posC1.x, posC1.y, posC2.x, posC2.y);
		float absSum, absDiff;
		absSum = Math.abs(radiusC1 + radiusC2 + tolerance);
		absDiff = Math.abs(radiusC1 - radiusC2 - tolerance);

		if (dist <= absSum) {
			if (absDiff <= dist) {
				return true;
			}
		}
		return false;
	}

	public static boolean overlap(Element circle, Vector2 pos1, float radius, Element rectangule, Vector2 pos2) {

		float dist = distance(pos1.x, pos1.y, pos2.x, pos2.y);

		float maxDist=distance(rectangule.getWidth(),0,0,rectangule.getHeight());
		System.out.println("Dist: " + dist + "  radius: " + radius*2 + "  Width: " + rectangule.getWidth()*2 + "  Height: " + rectangule.getHeight()*2);
		if (dist <= radius*2 || dist <= rectangule.getWidth()*2 || dist <= rectangule.getHeight()*2) {
			return true;
		}
		return false;

	}

	public static boolean overlapPloygons(Element element, Vector2 posSq1, Element eleToBeAdded, Vector2 posSq2) {

		Rectangle r1 = new Rectangle(posSq1.x, posSq1.y, element.getWidth(), element.getHeight());
		Rectangle r2 = new Rectangle(posSq2.x, posSq2.y, eleToBeAdded.getWidth(), eleToBeAdded.getHeight());
		return r1.overlaps(r2);
	}
}
