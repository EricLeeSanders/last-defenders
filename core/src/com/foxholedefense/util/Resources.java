package com.foxholedefense.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;

//-agentlib:hprof=heap=dump,format=b

public class Resources {
	public static final String LOAD_ATLAS = "load/load.atlas";
	public static final String MENU_ATLAS = "menu/menu.atlas";
	public static final String ACTOR_ATLAS = "game/actors/actors.atlas";
	public static final String LEVEL_SELECT_ATLAS = "level_select/level_select.atlas";
	public static final String SKIN_ATLAS = "skin/uiskin.atlas";
	public static final String SKIN_JSON = "skin/uiskin.json";

	public static final float VIRTUAL_WIDTH = 640; // 16:9
	public static final float VIRTUAL_HEIGHT = 360;
	public static final float TILED_MAP_SCALE = 0.5f;

	public static final float MAX_GAME_SPEED = 2.0f;

	public static final String MENU_MUSIC = "audio/big_action_trailer.mp3";

	public static final String RPG_EXPLOSION_SOUND = "audio/rpg_explosion.mp3";
	public static final String ROCKET_LAUNCH_SOUND = "audio/rocket_launch.mp3";
	public static final String FLAME_SOUND = "audio/flame_burst.mp3";
	public static final String RIFLE_SHOT_SOUND = "audio/rifle_shot.mp3";
	public static final String MACHINE_GUN_SHOT_SOUND = "audio/machine_gun_shot.mp3";
	public static final String SNIPER_SHOT_SOUND = "audio/sniper_shot.mp3";

	
	public static final String ACTOR_PLACE_SOUND = "audio/actor_place.mp3";
	public static final String SELL_SOUND = "audio/sell.mp3";
	public static final String SMALL_CLICK = "audio/button_small_click.mp3";
	public static final String LARGE_CLICK = "audio/button_large_click.mp3";
	public static final String VEHICLE_EXPLOSION_SOUND = "audio/vehicle_explosion.mp3";
	private float gameSpeed = 1;
	private static ShapeRenderer shapeRenderer;
	private UserPreferences userPreferences;
	private AssetManager manager = new AssetManager();

	public Resources(UserPreferences userPreferences){
		this.userPreferences = userPreferences;
		shapeRenderer = new ShapeRenderer();
		manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
	}
	public void dispose() {
		Logger.info("Resources: dispose");
		manager.dispose();
		shapeRenderer.dispose();
	}

	public void activityResume(){
		shapeRenderer.dispose();
		shapeRenderer = new ShapeRenderer();
	}
	
	public UserPreferences getUserPreferences(){
		return userPreferences;
	}


	public void initFont(){
		Logger.info("Resources: initializing font");
		BitmapFont font = getSkin().getFont("default-font");
		font.setUseIntegerPositions(false);
		font.getData().setLineHeight(55);
		font.getData().ascent = 11;
		font.getData().capHeight = 30;
		font.getData().descent = -14;
		font.getData().scaleX = 1.0f;
		font.getData().scaleY = 1.0f;
		font.getData().spaceWidth = 12.0f;
		font.getData().xHeight = 30.0f;
		Logger.info("Resources: font initialized");
	}

	public void loadAsset(String file, Class<?> type){
		Logger.info("Resources: loading asset: " + file);
		if(!manager.isLoaded(file)){
			try {
				manager.load(file, type);
				Logger.info("Resources: asset loaded: " + file);
			} catch (GdxRuntimeException e) {
				Logger.error("Resources: asset load error", e);
			}
		}
	}

	public void loadAssetSync(String file, Class<?> type){
		Logger.info("Resources: sync loading asset: " + file);
		loadAsset(file, type);
		manager.finishLoading();
		Logger.info("Resources: sync loaded asset: " + file);
	}

	public <T> T getAsset(String file, Class<T> type){
		if(!manager.isLoaded(file)){
			Logger.info(file + " not loaded. Loading...");
			loadAssetSync(file, type);
		}
		return manager.get(file, type);
	}

	public void unloadAsset(String file){
		Logger.info("Resources: unloading: " + file);
		if(manager.isLoaded(file)){
			manager.unload(file);
			Logger.info("Resources: " + file + " unloaded");
		}
	}


	public void loadMap(int level) {
		loadAsset("game/levels/level" + level + "/level" + level + ".tmx", TiledMap.class);
	}

	public TiledMap getMap(int level) {
		return getAsset("game/levels/level" + level + "/level" + level + ".tmx", TiledMap.class);
	}

	public void unloadMap(int level){
		unloadAsset("game/levels/level" + level + "/level" + level + ".tmx");
	}


	public void loadSkinSync() {
		Logger.info("Resources: sync loading skin");
		loadSkin();
		manager.finishLoading();
		Logger.info("Resources: sync loaded skin");
	}

	public void loadSkin() {
		Logger.info("Resources: loading skin");
		try {
			manager.load(SKIN_JSON, Skin.class, new SkinLoader.SkinParameter(SKIN_ATLAS));
			Logger.info("Resources: skin loaded");
		} catch (GdxRuntimeException e) {
			Logger.error("Resources: load skin error", e);
		}
	}

	public Skin getSkin() {
		if(!manager.isLoaded(SKIN_JSON)){
			Logger.info(SKIN_JSON + " (skin) not loaded. Loading");
			loadSkinSync();
		}
		return manager.get(SKIN_JSON, Skin.class);
	}

	public static ShapeRenderer getShapeRenderer(){
		return shapeRenderer;
	}

	public float getGameSpeed() {
		return gameSpeed;
	}

	public void setGameSpeed(float gameSpeed) {
		this.gameSpeed = gameSpeed;
	}

	public AssetManager getManager(){
		return manager;
	}

	
}
