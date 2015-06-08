package com.lpoo.MiniGolf.logic;

// TODO: Auto-generated Javadoc
/**
 * The Class ElementType. Used as bodyUserData for the libgdx bodies, which is
 * basically a data storage to be used in the contact listeners,
 * when you can only access the bodies that collided
 */
public class ElementType {

	/** The type of the element the body belongs to. */
	public Element.elementType type;

	/** The acceleration/drag this element gives, if the ball steps on it. */
	public float accel = 0;

	/** The player the body belongs to. Only applied to balls, else is null */
	public Player player;

	/** The element the body belongs to. */
	public Element element;

	/**
	 * The end contacts. Variable to check if a body leaves 2 surfaces without
	 * entering a new one, meaning it entered the grass (which isn't detectable
	 * otherwise)
	 */
	public int endContacts = 0;

	/**
	 * Variable to check if there is a beggining and ending of the same type in
	 * a row. Example: Ball began/entered surface A -> Ball ended/left surface
	 * A. Meant to detect cases when the ball goes from grass to another surface
	 * and back to grass.
	 */
	public boolean readyToCheckEnd = false;

	/** The type to check, auxiliar to the boolean readyToCheckEnd */
	public Element.elementType typeToCheck = null;

	/**
	 * Instantiates a new element type.
	 *
	 * @param type
	 *            the type of the element the body belongs to
	 * @param acceleration
	 *            the acceleration it will give the ball when stepping on the
	 *            body this data belongs to
	 */
	ElementType(Element.elementType type, float acceleration) {
		this.type = type;
		accel = acceleration;
	}

	/**
	 * Instantiates a new element type.
	 *
	 * @param type
	 *            the type of the element the body belongs to
	 * @param acceleration
	 *            the acceleration it will give the ball when stepping on the
	 *            body this data belongs to
	 * @param element
	 *            the element the body belongs to
	 */
	ElementType(Element.elementType type, float acceleration, Element element) {
		this.type = type;
		accel = acceleration;
		this.element = element;
	}

	/**
	 * Instantiates a new element type.
	 *
	 * @param type
	 *            the type of the element the body belongs to
	 * @param acceleration
	 *            the acceleration it will give the ball when stepping on the
	 *            body this data belongs to
	 * @param player
	 *            the player the body belongs to (applicable to balls)
	 */
	ElementType(Element.elementType type, float acceleration, Player player) {
		this.type = type;
		accel = acceleration;
		this.player = player;
	}

}
