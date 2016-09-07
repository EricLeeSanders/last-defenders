package com.eric.mtd.util;

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
	public static final String HUD_ATLAS = "game/ui/hud/hud.atlas";
	public static final String ENLIST_ATLAS = "game/ui/enlist/enlist.atlas";
	public static final String SUPPORT_UI_ATLAS = "game/ui/support/support.atlas";
	public static final String INSPECT_ATLAS = "game/ui/inspect/inspect.atlas";
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
	
	private static final ShapeRenderer SHAPE_RENDERER = new ShapeRenderer();
	
	private static Preferences prefs;
	
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
	public static void loadGameAssets() {
		loadPreferences();
		loadGraphics();
		loadAudio();
		
	}
	public static void loadPreferences(){
		prefs = Gdx.app.getPreferences("MTD_Preferences");
	}
	public static void loadGraphics(){
		if (Logger.DEBUG)
			System.out.println("Loading Graphics");
		loadFonts();
		Resources.loadUIMap();
		Resources.loadSkin(Resources.SKIN_JSON, Resources.SKIN_ATLAS );
		Resources.loadAtlas(Resources.MENU_ATLAS);
		Resources.loadAtlas(Resources.HUD_ATLAS);
		Resources.loadAtlas(Resources.ENLIST_ATLAS);
		Resources.loadAtlas(Resources.SUPPORT_UI_ATLAS);
		Resources.loadAtlas(Resources.INSPECT_ATLAS);
		Resources.loadAtlas(Resources.LEVEL_SELECT_ATLAS);
		Resources.loadAtlas(ACTOR_ATLAS);
		Pixmap.setBlending(Blending.None);
	}
	public static void loadAudio(){
		if (Logger.DEBUG)
			System.out.println("Loading Audio");
		AudioUtil.load();
	}
	
	/**
	 * Creates the default font for the skin.json
	 * Creates a copy of default-font-46.
	 * @return
	 */
	public static ObjectMap<String, Object> createDefaultFont(){
		// Creates a copy instead of using default-font-46 directly because
		// using it directly causes on exception to be thrown. The exception is thrown because
		// the font is disposed in two places
		ObjectMap<String, Object> map = new ObjectMap<String, Object>();
		BitmapFont font_46 = getFont("default-font-46");
		BitmapFont defaultFont = new BitmapFont(font_46.getData(),font_46.getRegions(), false);
		map.put("default-font", defaultFont); 
		return map;
	}
	private static void loadFonts(){

		FileHandleResolver resolver = new InternalFileHandleResolver();
		MANAGER.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		MANAGER.setLoader(BitmapFont.class, new FreetypeFontLoader(resolver));
		FreeTypeFontLoaderParameter parameter_16 = createFontParam("font/palamecia titling.ttf", 16, Color.BLACK, 1f, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		MANAGER.load("default-font-16", BitmapFont.class, parameter_16 );
		FreeTypeFontLoaderParameter parameter_22 = createFontParam("font/palamecia titling.ttf", 22, Color.BLACK, 1.3f, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		MANAGER.load("default-font-22", BitmapFont.class, parameter_22 );
		FreeTypeFontLoaderParameter parameter_46 = createFontParam("font/palamecia titling.ttf", 46, Color.BLACK, 3f, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		MANAGER.load("default-font-46", BitmapFont.class, parameter_46 );
		MANAGER.finishLoading(); 
	}
	
	private static FreeTypeFontLoaderParameter createFontParam(String fontFileName, int size, Color borderColor, float borderWidth, TextureFilter minFilter, TextureFilter magFilter){
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

	public static void loadSkin(String skinJson, String atlas) {
		try {
			if (Logger.DEBUG)
				System.out.println(atlas);
			if (Logger.DEBUG)
				System.out.println(skinJson);
			MANAGER.load(skinJson, Skin.class, new SkinLoader.SkinParameter(atlas, createDefaultFont()));
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
	
	public static BitmapFont getFont(String font){
		return MANAGER.get(font, BitmapFont.class);
	}
	public static Preferences getPreferences(){
		return prefs;
	}
	public static ShapeRenderer getShapeRenderer(){
		return SHAPE_RENDERER;
	}

}
