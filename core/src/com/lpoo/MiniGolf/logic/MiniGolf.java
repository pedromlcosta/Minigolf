package com.lpoo.MiniGolf.logic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MiniGolf extends Game {
	SpriteBatch batch;
	Sprite img;
	static int ballN = 0;

	public void create() {
		batch = new SpriteBatch();
		img = new Sprite(new Texture("hero.png"));
	}

	// testes
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		int xi = 0;
		int yi = 0;
		for (int i = 0; i < 15; i++) {
			batch.draw(img.getTexture(), xi, yi, 10, 10);
			xi += 20;
			yi += 20;
		}
		batch.end();
	}

}
