package com.lpoo.MiniGolf.logic;


public class ElementType {

	public Element.elementType type;
	public float accel = 0;
	public Player player;
	public Element element;
	public int endContacts = 0;
	public boolean readyToCheckEnd = false;
	public Element.elementType typeToCheck = null;
	
	
	ElementType( Element.elementType type, float acceleration){
		this.type = type;
		accel = acceleration;
	}
	
	ElementType( Element.elementType type, float acceleration, Element element){
		this.type = type;
		accel = acceleration;
		this.element = element;
	}
	
	ElementType( Element.elementType type, float acceleration, Player player){
		this.type = type;
		accel = acceleration;
		this.player = player;
	}
	
}
