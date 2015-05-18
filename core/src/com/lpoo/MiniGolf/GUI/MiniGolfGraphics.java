package com.lpoo.MiniGolf.GUI;

import java.io.IOException;

import com.lpoo.MiniGolf.logic.MiniGolf;

public class MiniGolfGraphics {

	static int width;
	static int height;

	@SuppressWarnings("unused")
	public static void paintMaze(MiniGolf game, int offsetX, int offsetY, int size) throws IOException {
		int courseHeight = game.getCourseHeight();
		int courseWidth = game.getCourseWidth();

		if (width > height) {
			size = height / (courseHeight);
			offsetY = 0;
		} else {
			size = width / (courseWidth);
			offsetY = (height - (courseWidth * size)) / 2;
		}

		offsetX = (width - size * courseWidth) / 2;

		int x1 = 0 + offsetX;
		int y1 = offsetY;
		for (int i = 0; i < courseWidth; i++)
			for (int j = 0; j < courseHeight; j++) {

				y1 += offsetY;
			}
		x1 += offsetX;
		y1 = offsetY;
	}

}
