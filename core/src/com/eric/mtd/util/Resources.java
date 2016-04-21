package com.eric.mtd.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;

//-agentlib:hprof=heap=dump,format=b
public abstract class Resources {
	public static final String MENU_ATLAS = "menu/menu.atlas";
	public static final String ENEMY_ATLAS = "game/actors/enemy/EnemyActors.atlas";
	public static final String LEVEL_SELECT_ATLAS = "level_select/level_select.atlas";
	public static final String HUD_ATLAS = "game/ui/hud/hud.atlas";
	public static final String ENLIST_ATLAS = "game/ui/enlist/enlist.atlas";
	public static final String PERKS_ATLAS = "game/ui/perks/perks.atlas";
	public static final String OPTIONS_ATLAS = "game/ui/options/options.atlas";
	public static final String GAME_OVER_ATLAS = "game/ui/gameover/gameover.atlas";
	public static final String INSPECT_ATLAS = "game/ui/inspect/inspect.atlas";
	public static final String SKIN_JSON = "skin/uiskin.json";
	public static final String TOWER_ATLAS = "game/actors/tower/TowerActors.atlas";
	public static final String EXPLOSION_ATLAS = "game/actors/projectile/Explosion/Explosion.atlas";
	public static final String FLAMES_ATLAS = "game/actors/projectile/Flames/Flames.atlas";

	public static final int SCREEN_WIDTH = 640; // 16:9
	public static final int SCREEN_HEIGHT = 360;

	public static final float NORMAL_SPEED = 1.0f;
	public static final float DOUBLE_SPEED = 2.0f;

	public static final String MENU_MUSIC = "audio/big_action_trailer.mp3";

	public static final String RPG_EXPLOSION_SOUND = "audio/rpg_explosion.mp3";
	public static final String ROCKET_LAUNCH_SOUND = "audio/rocket_launch.mp3";
	public static final String FLAME_SOUND = "audio/flame_burst.mp3";
	public static final String RIFLE_SHOT_SOUND = "audio/rifle_shot.mp3";
	public static final String MACHINE_GUN_SHOT_SOUND = "audio/machine_gun_shot.mp3";
	public static final String SNIPER_SHOT_SOUND = "audio/sniper_shot.mp3";

	public static final String VEHICLE_EXPLOSION_SOUND = "audio/vehicle_explosion.mp3";
	
	private static final ShapeRenderer SHAPE_RENDERER = new ShapeRenderer();
	
	private static final AssetManager MANAGER = new AssetManager();

	public static void dispose() {
		if (Logger.DEBUG)
			System.out.println("Resources dispose");
		MANAGER.dispose();
		SHAPE_RENDERER.dispose();

	}
	public static void gameResume(){
		MANAGER.finishLoading();
	}
	public static void loadGraphics() {
		if (Logger.DEBUG)
			System.out.println("Loading Graphics");
		Resources.loadUIMap();
		Resources.loadSkin(Resources.SKIN_JSON);
		Resources.loadAtlas(Resources.ENEMY_ATLAS);
		Resources.loadAtlas(Resources.TOWER_ATLAS);
		Resources.loadAtlas(Resources.EXPLOSION_ATLAS);
		Resources.loadAtlas(Resources.FLAMES_ATLAS);
		Resources.loadAtlas(Resources.MENU_ATLAS);
		Resources.loadAtlas(Resources.HUD_ATLAS);
		Resources.loadAtlas(Resources.ENLIST_ATLAS);
		Resources.loadAtlas(Resources.PERKS_ATLAS);
		Resources.loadAtlas(Resources.INSPECT_ATLAS);
		Resources.loadAtlas(Resources.OPTIONS_ATLAS);
		Resources.loadAtlas(Resources.GAME_OVER_ATLAS);
		Resources.loadAtlas(Resources.LEVEL_SELECT_ATLAS);
	}

	public static void loadMap(int level) {
		try {
			MANAGER.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
			MANAGER.load("game/levels/level" + level + "/level" + level + ".tmx", TiledMap.class);
			MANAGER.finishLoading();
			if (Logger.DEBUG)
				System.out.println("Map Loaded: " + "game/levels/level" + level + "/level" + level + ".tmx");
		} catch (GdxRuntimeException e) {
			if (Logger.DEBUG)
				System.out.println("Map load error " + e);
		}
	}

	public static void loadUIMap() {
		try {
			MANAGER.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
			MANAGER.load("game/ui/ui.tmx", TiledMap.class);
			MANAGER.finishLoading();
			if (Logger.DEBUG)
				System.out.println("UI Map Loaded");
		} catch (GdxRuntimeException e) {
			if (Logger.DEBUG)
				System.out.println("UI Map load error " + e);
		}
	}

	public static void loadSkin(String skinJson) {
		String atlas = skinJson.substring(0, skinJson.lastIndexOf(".")) + ".atlas";
		try {
			if (Logger.DEBUG)
				System.out.println(atlas);
			if (Logger.DEBUG)
				System.out.println(skinJson);
			MANAGER.load(skinJson, Skin.class, new SkinLoader.SkinParameter(atlas));
			MANAGER.finishLoading();
			if (Logger.DEBUG)
				System.out.println("Skin Loaded");
		} catch (GdxRuntimeException e) {
			if (Logger.DEBUG)
				System.out.println("Load Skin Error " + e);
		}
	}

	public static void loadAtlas(String file) {
		try {
			MANAGER.load(file, TextureAtlas.class);
			MANAGER.finishLoading();
			if (Logger.DEBUG)
				System.out.println(file + " Atlas Loaded");
		} catch (GdxRuntimeException e) {
			if (Logger.DEBUG)
				System.out.println("Load Atlas Error " + e);
		}
	}

	public static void loadTexture(String file) {
		try {
			MANAGER.load(file, Texture.class);
			MANAGER.finishLoading();
		} catch (GdxRuntimeException e) {
			if (Logger.DEBUG)
				System.out.println("Load Texture Error " + e);
		}
	}

	public static TiledMap getMap(int level) {
		return MANAGER.get("game/levels/level" + level + "/level" + level + ".tmx", TiledMap.class);
	}

	public static TiledMap getUIMap() {
		return MANAGER.get("game/ui/ui.tmx", TiledMap.class);
	}

	public static TextureAtlas getAtlas(String file) {
		return MANAGER.get(file, TextureAtlas.class);
	}

	public static Texture getTexture(String file) {
		return MANAGER.get(file, Texture.class);
	}

	public static Skin getSkin(String file) {
		return MANAGER.get(file, Skin.class);
	}
	
	public static ShapeRenderer getShapeRenderer(){
		return SHAPE_RENDERER;
	}

}
