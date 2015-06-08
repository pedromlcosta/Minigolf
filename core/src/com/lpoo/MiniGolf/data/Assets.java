package com.lpoo.MiniGolf.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

// TODO: Auto-generated Javadoc
/**
 * The Class Assets.
 */
public class Assets {

	/** The manager. */
	public static AssetManager manager = new AssetManager();

	/** The menu skin. */
	public static Skin menuSkin;

	/**
	 * In here we'll put everything that needs to be loaded in this format:
	 *
	 * manager.load("file location in assets", fileType.class);
	 *
	 * libGDX AssetManager currently supports: Pixmap, Texture, BitmapFont,
	 * TextureAtlas, TiledAtlas, TiledMapRenderer, Music and Sound.
	 */
	public static void queueLoading() {
		// manager.load("grass.png", Texture.class);
		manager.load("bola1.png", Texture.class);
		manager.load("bola2.png", Texture.class);
		manager.load("bola3.png", Texture.class);
		manager.load("bola4.png", Texture.class);
		manager.load("acceleratorFloor.png", Texture.class);
		manager.load("bouncyWall.png", Texture.class);
		manager.load("glueWall.png", Texture.class);
		manager.load("golfCourseBeach.jpg", Texture.class);
		manager.load("grassFloor.png", Texture.class);
		manager.load("hole.png", Texture.class);
		manager.load("iceFloor.png", Texture.class);
		manager.load("illusionWall.png", Texture.class);
		manager.load("loading.png", Texture.class);
		manager.load("regularWall.png", Texture.class);
		manager.load("sandFloor.png", Texture.class);
		manager.load("teleporter.png", Texture.class);
		manager.load("teleporterDestination.png", Texture.class);
		manager.load("uiskin.png", Texture.class);
		manager.load("voidFloor.png", Texture.class);
		manager.load("regularWall.png", Texture.class);
		manager.load("waterFloor.png", Texture.class);
		manager.load("teleporter.png", Texture.class);
		Assets.manager.load("uiskin.atlas", TextureAtlas.class);
		Assets.manager.load("uiskin.json", Skin.class);

	}

	/**
	 * In here we'll create our skin, so we only have to create it once. Sets
	 * the menu skin.
	 */
	public static void setMenuSkin() {
		if (menuSkin == null)
			menuSkin = new Skin(Gdx.files.internal("uiskin.json"));
	}

	/**
	 * This function gets called every render() and the AssetManager pauses the
	 * so we can still run menus and loading screens smoothly loading each frame
	 * Update.
	 *
	 * @return true, if successful
	 */
	public static boolean update() {
		return manager.update();
	}
}
