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
	 * @param circle1
	 * @param posCircle1
	 * @param circle2
	 * @param posCircle2
	 * @return true if two circles Overlap
	 */
	public static boolean overlapCircles(CircleShape circle1, Vector2 posCircle1, CircleShape circle2, Vector2 posCircle2) {

		float radiusC1 = circle1.getRadius();
		float radiusC2 = circle2.getRadius();
		float dist = distance(posCircle1.x, posCircle1.y, posCircle2.x, posCircle2.y);
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
	 * @param posCircle
	 * @param radius
	 * @param rectangule
	 * @param posRectangule
	 * @return returns if there is an overlap between a circle and a rectangle
	 */
	public static boolean overlap(Element circle, Vector2 posCircle, float radius, Element rectangule, Vector2 posRectangule) {

		Circle c = new Circle(posCircle.x, posCircle.y, radius);
		Rectangle r = new Rectangle(posRectangule.x, posRectangule.y, rectangule.getWidth(), rectangule.getHeight());
		return Intersector.overlaps(c, r);

	}

	/**
	 * 
	 * @param element
	 * @param posRectangle1
	 * @param eleToBeAdded
	 * @param posRectangle2
	 * @return return true if there is an overlap between two rectangle
	 */
	public static boolean overlapPloygons(Element element, Vector2 posRectangle1, Element eleToBeAdded, Vector2 posRectangle2) {

		Rectangle r1 = new Rectangle(posRectangle1.x, posRectangle1.y, element.getWidth(), element.getHeight());
		Rectangle r2 = new Rectangle(posRectangle2.x, posRectangle2.y, eleToBeAdded.getWidth(), eleToBeAdded.getHeight());
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
