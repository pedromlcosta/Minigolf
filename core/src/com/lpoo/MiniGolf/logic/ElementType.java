package com.lpoo.MiniGolf.logic;


// TODO: Auto-generated Javadoc
/**
 * The Class ElementType.
 */
public class ElementType {

	/** The type. */
	public Element.elementType type;
	
	/** The accel. */
	public float accel = 0;
	
	/** The player. */
	public Player player;
	
	/** The element. */
	public Element element;
	
	/** The end contacts. */
	public int endContacts = 0;
	
	/** The ready to check end. */
	public boolean readyToCheckEnd = false;
	
	/** The type to check. */
	public Element.elementType typeToCheck = null;
	
	
	/**
	 * Instantiates a new element type.
	 *
	 * @param type the type
	 * @param acceleration the acceleration
	 */
	ElementType( Element.elementType type, float acceleration){
		this.type = type;
		accel = acceleration;
	}
	
	/**
	 * Instantiates a new element type.
	 *
	 * @param type the type
	 * @param acceleration the acceleration
	 * @param element the element
	 */
	ElementType( Element.elementType type, float acceleration, Element element){
		this.type = type;
		accel = acceleration;
		this.element = element;
	}
	
	/**
	 * Instantiates a new element type.
	 *
	 * @param type the type
	 * @param acceleration the acceleration
	 * @param player the player
	 */
	ElementType( Element.elementType type, float acceleration, Player player){
		this.type = type;
		accel = acceleration;
		this.player = player;
	}
	
}
