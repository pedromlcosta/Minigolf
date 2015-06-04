package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.lpoo.MiniGolf.logic.Element.elementType;

public class RegularWall extends Element {

	RegularWall() {
	}
	
	public RegularWall(Vector2 pos, int height, int width, elementType type) {
		super(pos, height, width, type);

	}
}
