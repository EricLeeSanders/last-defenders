package com.foxholedefense.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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


	public void loadMapSync(int level) {
		Logger.info("Resources: sync loading map: " + level);
		loadMap(level);
		manager.finishLoading();
		Logger.info("Resources: sync loaded map: " + level);
	}
	
	public void loadMap(int level) {
		Logger.info("Resources: loading map: " + level);
		try {
			manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
			manager.load("game/levels/level" + level + "/level" + level + ".tmx", TiledMap.class);
			Logger.info("Resources: map loaded: " + "game/levels/level" + level + "/level" + level + ".tmx");
		} catch (GdxRuntimeException e) {
			Logger.error("Resources: map load error", e);
		}
	}

	public void loadSkinSync(String skinJson, String atlas) {
		Logger.info("Resources: sync loading skin");
		loadSkin(skinJson, atlas);
		manager.finishLoading();
		Logger.info("Resources: sync loaded skin");
	}
	
	public void loadSkin(String skinJson, String atlas) {
		Logger.info("Resources: loading skin");
		try {
			manager.load(skinJson, Skin.class, new SkinLoader.SkinParameter(atlas));
			Logger.info("Resources: skin loaded");
		} catch (GdxRuntimeException e) {
			Logger.error("Resources: load skin error", e);
		}
	}

	public void initFont(){
		Logger.info("Resources: initializing font");
		BitmapFont font = getSkin(SKIN_JSON).getFont("default-font");
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
	public void loadAtlasSync(String file) {
		Logger.info("Resources: sync loading atlas: " + file);
		loadAtlas(file);
		manager.finishLoading();
		Logger.info("Resources: sync loaded atlas: " + file);
	}
	
	public void loadAtlas(String file) {
		Logger.info("Resources: loading atlas: " + file);
		if(!manager.isLoaded(file)){
			try {
				manager.load(file, TextureAtlas.class);
				Logger.info("Resources: atlas loaded: " + file);
			} catch (GdxRuntimeException e) {
				Logger.error("Resources: atlas load error", e);
			}
		}
	}

	public void loadTextureSync(String file) {
		Logger.info("Resources: sync loading texture: " + file);
		loadTexture(file);
		manager.finishLoading();
		Logger.info("Resources: sync loaded texture: " + file);
	}
	
	public void loadTexture(String file) {
		Logger.info("Resources: loading texture: " + file);
		if(!manager.isLoaded(file)){
			try {
				manager.load(file, Texture.class);
				manager.finishLoading();
				Logger.info("Resources: texture loaded: " + file);
			} catch (GdxRuntimeException e) {
				Logger.error("Resources: texture load error", e);
			}
		}
	}
	public TiledMap getMap(int level) {
		if(!manager.isLoaded("game/levels/level" + level + "/level" + level + ".tmx")){
			Logger.info("Map " + level + " is not loaded. Loading...");
			loadMapSync(level);
		}
		return manager.get("game/levels/level" + level + "/level" + level + ".tmx", TiledMap.class);
	}

	public TiledMap getUIMap() {
		return manager.get("game/ui/ui.tmx", TiledMap.class);
	}

	public TextureAtlas getAtlas(String file) {
		if(!manager.isLoaded(file)){
			Logger.info(file + " not loaded. Loading...");
			loadAtlasSync(file);
		}
		return manager.get(file, TextureAtlas.class);
	}

	public Texture getTexture(String file) {
		return manager.get(file, Texture.class);
	}

	public Skin getSkin(String file) {
		if(!manager.isLoaded(file)){
			Logger.info(file + " (skin) not loaded. Loading");
			loadSkinSync(Resources.SKIN_JSON, Resources.SKIN_ATLAS );
		}
		return manager.get(file, Skin.class);
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
	public void unloadAsset(String file){
		Logger.info("Resources: unloaded: " + file);
		if(manager.isLoaded(file)){
			manager.unload(file);
			Logger.info("Resources: " + file + " unloaded");
		}
	}
	public void unloadMap(int level){
		Logger.info("Resources: unloading map: " + level);
		if(manager.isLoaded("game/levels/level" + level + "/level" + level + ".tmx")){
			manager.unload("game/levels/level" + level + "/level" + level + ".tmx");
			Logger.info("Map " + level + " unloaded");
		}
	}
	public AssetManager getManager(){
		return manager;
	}
	public void finishLoading(){
		manager.finishLoading();
	}

	
}
