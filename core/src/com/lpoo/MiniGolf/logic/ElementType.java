package com.lpoo.MiniGolf.logic;


public class ElementType {

	public Element.elementType type;
	public float accel = 0;
	public Player player;
	
	
	ElementType( Element.elementType type, float acceleration){
		this.type = type;
		accel = acceleration;
	}
	
	ElementType( Element.elementType type, float acceleration, Player player){
		this.type = type;
		accel = acceleration;
		this.player = player;
	}
	
}
