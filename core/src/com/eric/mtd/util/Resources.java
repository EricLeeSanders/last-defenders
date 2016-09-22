package com.eric.mtd.util;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
//-agentlib:hprof=heap=dump,format=b
public class Resources {
	public static final String MENU_ATLAS = "menu/menu.atlas";
	public static final String ACTOR_ATLAS = "game/actors/actors.atlas";
	public static final String LEVEL_SELECT_ATLAS = "level_select/level_select.atlas";
	public static final String SKIN_ATLAS = "skin/uiskin.atlas";
	public static final String SKIN_JSON = "skin/uiskin.json";

	public static final float VIRTUAL_WIDTH = 640; // 16:9
	public static final float VIRTUAL_HEIGHT = 360;
	public static final float TILED_MAP_SCALE = 0.5f;

	public static final float NORMAL_SPEED = 1.0f;
	public static final float DOUBLE_SPEED = 2.0f;

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
	private static ShapeRenderer shapeRenderer = new ShapeRenderer();
	private UserPreferences userPreferences;
	private AssetManager manager = new AssetManager();
	private java.util.Map<String, BitmapFont> fonts;
	
	public Resources(UserPreferences userPreferences){
		this.userPreferences = userPreferences;
		loadFonts();
		loadSkin(Resources.SKIN_JSON, Resources.SKIN_ATLAS );
		Pixmap.setBlending(Blending.None);
	}
	public void dispose() {
		if (Logger.DEBUG)
			System.out.println("Resources dispose");
		manager.dispose();
		shapeRenderer.dispose();

	}
	
	/**
	 * Creates the default font for the skin.json
	 * Creates a copy of default-font-46.
	 * @return
	 */
	public ObjectMap<String, Object> createDefaultFont(){
		// Creates a copy instead of using default-font-46 directly because
		// using it directly causes on exception to be thrown. The exception is thrown because
		// the font is disposed in two places
		ObjectMap<String, Object> map = new ObjectMap<String, Object>();
		BitmapFont font_46 = getFont("default-font-46");
		BitmapFont defaultFont = new BitmapFont(font_46.getData(),font_46.getRegions(), false);
		map.put("default-font", defaultFont); 
		return map;
	}
	private void loadFonts(){

		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, new FreetypeFontLoader(resolver));
		FreeTypeFontLoaderParameter parameter_16 = createFontParam("font/palamecia titling.ttf", 16, Color.BLACK, 1f, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		manager.load("default-font-16", BitmapFont.class, parameter_16 );
		FreeTypeFontLoaderParameter parameter_22 = createFontParam("font/palamecia titling.ttf", 22, Color.BLACK, 1.3f, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		manager.load("default-font-22", BitmapFont.class, parameter_22 );
		FreeTypeFontLoaderParameter parameter_46 = createFontParam("font/palamecia titling.ttf", 46, Color.BLACK, 3f, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		manager.load("default-font-46", BitmapFont.class, parameter_46 );
		manager.finishLoading(); 
		fonts = new HashMap<String, BitmapFont>();
		fonts.put("default-font-16", getFont("default-font-16"));
		fonts.put("default-font-22", getFont("default-font-22"));
		fonts.put("default-font-46", getFont("default-font-46"));
	}
	
	private FreeTypeFontLoaderParameter createFontParam(String fontFileName, int size, Color borderColor, float borderWidth, TextureFilter minFilter, TextureFilter magFilter){
		FreeTypeFontLoaderParameter parameter = new FreeTypeFontLoaderParameter();
		parameter.fontParameters.size = size;
		parameter.fontParameters.borderColor = borderColor;
		parameter.fontParameters.borderWidth = borderWidth;
		parameter.fontParameters.genMipMaps = true;
		parameter.fontParameters.minFilter = minFilter;
		parameter.fontParameters.magFilter = magFilter;
		parameter.fontFileName = fontFileName;
		
		return parameter;
	}
	public void loadMap(int level) {
		try {
			manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
			manager.load("game/levels/level" + level + "/level" + level + ".tmx", TiledMap.class);
			manager.finishLoading();
			if (Logger.DEBUG)
				System.out.println("Map Loaded: " + "game/levels/level" + level + "/level" + level + ".tmx");
		} catch (GdxRuntimeException e) {
			if (Logger.DEBUG)
				System.out.println("Map load error " + e);
		}
	}

	public void loadUIMap() {
		try {
			manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
			manager.load("game/ui/ui.tmx", TiledMap.class);
			manager.finishLoading();
			if (Logger.DEBUG)
				System.out.println("UI Map Loaded");
		} catch (GdxRuntimeException e) {
			if (Logger.DEBUG)
				System.out.println("UI Map load error " + e);
		}
	}

	public void loadSkin(String skinJson, String atlas) {
		try {
			if (Logger.DEBUG)
				System.out.println(atlas);
			if (Logger.DEBUG)
				System.out.println(skinJson);
			manager.load(skinJson, Skin.class, new SkinLoader.SkinParameter(atlas, createDefaultFont()));
			manager.finishLoading();
			if (Logger.DEBUG)
				System.out.println("Skin Loaded");
		} catch (GdxRuntimeException e) {
			if (Logger.DEBUG)
				System.out.println("Load Skin Error " + e);
		}
	}
	public void loadAtlas(String file) {
		try {
			manager.load(file, TextureAtlas.class);
			manager.finishLoading();
			if (Logger.DEBUG)
				System.out.println(file + " Atlas Loaded");
		} catch (GdxRuntimeException e) {
			if (Logger.DEBUG)
				System.out.println("Load Atlas Error " + e);
		}
	}

	public void loadTexture(String file) {
		try {
			manager.load(file, Texture.class);
			manager.finishLoading();
		} catch (GdxRuntimeException e) {
			if (Logger.DEBUG)
				System.out.println("Load Texture Error " + e);
		}
	}

	public TiledMap getMap(int level) {
		if(!manager.isLoaded("game/levels/level" + level + "/level" + level + ".tmx")){
			loadMap(level);
		}
		return manager.get("game/levels/level" + level + "/level" + level + ".tmx", TiledMap.class);
	}

	public TiledMap getUIMap() {
		return manager.get("game/ui/ui.tmx", TiledMap.class);
	}

	public TextureAtlas getAtlas(String file) {
		if(!manager.isLoaded(file)){
			if(Logger.DEBUG){
				System.out.println("File not loaded");
			}
			loadAtlas(file);
		}
		return manager.get(file, TextureAtlas.class);
	}

	public Texture getTexture(String file) {
		return manager.get(file, Texture.class);
	}

	public Skin getSkin(String file) {
		if(!manager.isLoaded(file)){
			loadSkin(Resources.SKIN_JSON, Resources.SKIN_ATLAS );
		}
		return manager.get(file, Skin.class);
	}
	public java.util.Map<String, BitmapFont> getFonts(){
		if(fonts == null){
			loadFonts();
		}
		return fonts;
	}
	public BitmapFont getFont(String font){
		return manager.get(font, BitmapFont.class);
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
		if(manager.isLoaded(file)){
			manager.unload(file);
			if(Logger.DEBUG){
				System.out.println(file + " unloaded");
			}
		}
	}
	public void unloadMap(int level){
		if(manager.isLoaded("game/levels/level" + level + "/level" + level + ".tmx")){
			manager.unload("game/levels/level" + level + "/level" + level + ".tmx");
			if(Logger.DEBUG){
				System.out.println("level " + level + " unloaded");
			}
		}
	}
}
