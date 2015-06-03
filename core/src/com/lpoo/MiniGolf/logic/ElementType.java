package com.lpoo.MiniGolf.logic;


public class ElementType {

	public Element.elementType type;
	public float accel = 1 ;
	public int id;
	
	
	ElementType( Element.elementType type, int id){
		this.type = type;
		this.id = id;
	}
	
	ElementType( Element.elementType type, float acceleration, int id){
		this.type = type;
		accel = acceleration;
		this.id = id;
	}
	
}
