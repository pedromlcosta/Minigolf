package com.lpoo.MiniGolf.geometry;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.lpoo.MiniGolf.logic.Element;

public class Geometry {

	private final static float tolerance = 0.2f;
	/**
	 * Converters degrees into radians
	 */
	public static final float DEG_TO_RAD = (float) (Math.PI / 180);

	/**
	 * 
	 * Type of shapes supported
	 */
	public enum shapes {
		circle, line
	};

	public Geometry() {

	}

	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the distance between two points
	 */
	public static float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	/**
	 * 
	 * @param c1
	 * @param posC1
	 * @param c2
	 * @param posC2
	 * @return true if two circles Overlap
	 */
	public static boolean overlapCircles(CircleShape c1, Vector2 posC1, CircleShape c2, Vector2 posC2) {

		float radiusC1 = c1.getRadius();
		float radiusC2 = c2.getRadius();
		float dist = distance(posC1.x, posC1.y, posC2.x, posC2.y);
		float absSum, absDiff;
		absSum = Math.abs(radiusC1 + radiusC2 + tolerance);
		absDiff = Math.abs(radiusC1 - radiusC2);

		if (dist <= absSum) {
			if (absDiff <= dist) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param circle
	 * @param pos1
	 * @param radius
	 * @param rectangule
	 * @param pos2
	 * @return returns if there is an overlap between a circle and a rectangle
	 */
	public static boolean overlap(Element circle, Vector2 pos1, float radius, Element rectangule, Vector2 pos2) {

		Circle c = new Circle(pos1.x, pos1.y, radius);
		Rectangle r = new Rectangle(pos2.x, pos2.y, rectangule.getWidth(), rectangule.getHeight());
		return Intersector.overlaps(c, r);

	}

	/**
	 * 
	 * @param element
	 * @param posSq1
	 * @param eleToBeAdded
	 * @param posSq2
	 * @return return true if there is an overlap between two rectangle
	 */
	public static boolean overlapPloygons(Element element, Vector2 posSq1, Element eleToBeAdded, Vector2 posSq2) {

		Rectangle r1 = new Rectangle(posSq1.x, posSq1.y, element.getWidth(), element.getHeight());
		Rectangle r2 = new Rectangle(posSq2.x, posSq2.y, eleToBeAdded.getWidth(), eleToBeAdded.getHeight());
		return r1.overlaps(r2);
	}

	/**
	 * 
	 * @param centerX
	 * @param centerY
	 * @param x
	 * @param y
	 * @param radius
	 * @return true if a point is inside a circle
	 */
	public static boolean insideCircle(float centerX, float centerY, float x, float y, float radius) {
		return Geometry.distance(x, y, centerX, centerY) <= radius;

	}

	/**
	 * 
	 * @param center
	 * @param point
	 * @param radius
	 * @return true if a line is inside a circle
	 */
	public static Vector2 intersectLineCircle(Vector2 center, Vector2 point, float radius) {

		Vector2 centerToPoint = new Vector2(point.x - center.x, point.y - center.y);
		float intersectPointX = center.x + (centerToPoint.x / centerToPoint.len()) * radius;
		float intersectPointY = center.y + (centerToPoint.y / centerToPoint.len()) * radius;

		return new Vector2(intersectPointX, intersectPointY);
	}
}
