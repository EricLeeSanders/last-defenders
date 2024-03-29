package com.lastdefenders.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
//import com.badlogic.gdx.Too
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
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;
import com.badlogic.gdx.tools.hiero.Hiero;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;

import static java.awt.SystemColor.info;

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
	
	public Resources(UserPreferences userPreferences){
		this.userPreferences = userPreferences;
//		loadFonts();
//		loadSkin(Resources.SKIN_JSON, Resources.SKIN_ATLAS );
		Pixmap.setBlending(Blending.None);
	}
	public void dispose() {
		Logger.info("Resources dispose");
		//Need to reset Pixmap blending otherwise blending
		//for fonts on restart are screwed up
		Pixmap.setBlending(Blending.SourceOver);
		manager.dispose();
		shapeRenderer.dispose();

	}
	
	public UserPreferences getUserPreferences(){
		return userPreferences;
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
		createFont(defaultFont);
		map.put("default-font", defaultFont); 
		return map;
	}
	public void loadFontsSync(){
		Blending prevBlend = Pixmap.getBlending();
		Pixmap.setBlending(Blending.SourceOver);
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
//		FontInfo info = new FontInfo();
//		info.padding = new Padding(1, 1, 1, 1);
//
//		FreeTypeFontParameter param = new FreeTypeFontParameter();
//		param.size = 13;
//		param.gamma = 2f;
//		param.shadowOffsetY = 1;
//		param.renderCount = 3;
//		param.shadowColor = new Color(0, 0, 0, 0.45f);
//		param.characters = Hiero.EXTENDED_CHARS;
//		param.packer = new PixmapPacker(512, 512, Format.RGBA8888, 2, false, new SkylineStrategy());
//
//		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.absolute("some-font.ttf"));
//		FreeTypeBitmapFontData data = generator.generateData(param);
//
//		BitmapFontWriter.writeFont(data, new String[] {"font.png"},
//				Gdx.files.absolute("font.fnt"), info, 512, 512);
//		BitmapFontWriter.writePixmaps(param.packer.getPages(), Gdx.files.absolute("imageDir"), name);


		//System.exit(0);
		createFont(46, Color.BLACK, 3f, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		Pixmap.setBlending(prevBlend);
	}

	public void createFont(BitmapFont font){
		Texture texture = font.getRegion().getTexture();
		if (!texture.getTextureData().isPrepared()) {
			texture.getTextureData().prepare();
		}
		Pixmap pixmap = texture.getTextureData().consumePixmap();
//		new WriteFont(pixmap);
		BitmapFontWriter.FontInfo info = new BitmapFontWriter.FontInfo();
		BitmapFontWriter.writeFont(font.getData(), new Pixmap[]{pixmap}, Gdx.files.local("default-font.fnt"), info);
		BitmapFontWriter.writePixmaps(new Pixmap[]{pixmap}, Gdx.files.local(""), "default-font");
	}

	public void createFont(int size, Color borderColor, float borderWidth, TextureFilter minFilter, TextureFilter magFilter){
		BitmapFont font = getFont("default-font-46");
		System.out.println(font.getData().getGlyph('G').getKerning('A'));
		Texture texture = font.getRegion().getTexture();
		if (!texture.getTextureData().isPrepared()) {
			texture.getTextureData().prepare();
		}
		Pixmap pixmap = texture.getTextureData().consumePixmap();
//		new WriteFont(pixmap);
		BitmapFontWriter.FontInfo info = new BitmapFontWriter.FontInfo();
		//BitmapFontWriter.writeFont(font.getData(), new String[] {"default-font.png"}, Gdx.files.local("default-font.fnt"), info, 512, 512);
		//param.packer = new PixmapPacker(512, 512, Pixmap.Format.RGBA8888, 2, false, new PixmapPacker.SkylineStrategy());
//		BitmapFontWriter.writePixmaps(new Pixmap[]{pixmap}, Gdx.files.local(""), "default-font");
//
//		BitmapFontWriter.FontInfo info = new BitmapFontWriter.FontInfo();
////		info.padding = new BitmapFontWriter.Padding(1, 1, 1, 1);
//
//		FreeTypeFontParameter param = new FreeTypeFontParameter();
//		param.size = size;
//		param.borderColor = borderColor;
//		param.borderWidth = borderWidth;
//		param.genMipMaps = true;
//		param.minFilter = minFilter;
//		param.magFilter = magFilter;
//		param.characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!?.,/$%+=#";
//		param.packer = new PixmapPacker(512, 512, Pixmap.Format.RGBA8888, 0, false, new PixmapPacker.SkylineStrategy());
//
//		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.absolute("font/palamecia titling.ttf"));
//		FreeTypeFontGenerator.FreeTypeBitmapFontData data = generator.generateData(param);
//
//		BitmapFontWriter.writeFont(data, new String[] {"default-font.png"},
//				Gdx.files.absolute("default-font.fnt"), info, 512, 512);
//		BitmapFontWriter.writePixmaps(param.packer.getPages(), Gdx.files.absolute(""), "default-font");
	}
	public void loadFonts(){
		Blending prevBlend = Pixmap.getBlending();
		Pixmap.setBlending(Blending.SourceOver);
		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(BitmapFont.class, new FreetypeFontLoader(resolver));
		FreeTypeFontLoaderParameter parameter_16 = createFontParam("font/palamecia titling.ttf", 16, Color.BLACK, 1f, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		manager.load("default-font-16", BitmapFont.class, parameter_16 );
		FreeTypeFontLoaderParameter parameter_22 = createFontParam("font/palamecia titling.ttf", 22, Color.BLACK, 1.3f, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		manager.load("default-font-22", BitmapFont.class, parameter_22 );
		FreeTypeFontLoaderParameter parameter_46 = createFontParam("font/palamecia titling.ttf", 46, Color.BLACK, 3f, Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		manager.load("default-font-46", BitmapFont.class, parameter_46 );
		Pixmap.setBlending(prevBlend);
	}
	
	private FreeTypeFontLoaderParameter createFontParam(String fontFileName, int size, Color borderColor, float borderWidth, TextureFilter minFilter, TextureFilter magFilter){
		FreeTypeFontLoaderParameter parameter = new FreeTypeFontLoaderParameter();
		parameter.fontParameters.size = size;
		parameter.fontParameters.borderColor = borderColor;
		parameter.fontParameters.borderWidth = borderWidth;
		parameter.fontParameters.genMipMaps = true;
		parameter.fontParameters.minFilter = minFilter;
		parameter.fontParameters.magFilter = magFilter;
		//parameter.fontParameters.kerning = true;
		parameter.fontFileName = fontFileName;
		parameter.fontParameters.characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!?:.,/$%+=#";
		
		return parameter;
	}
	public void loadMapSync(int level) {
		loadMap(level);
		manager.finishLoading();
	}
	
	public void loadMap(int level) {
		try {
			manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
			manager.load("game/levels/level" + level + "/level" + level + ".tmx", TiledMap.class);
			Logger.info("Map Loaded: " + "game/levels/level" + level + "/level" + level + ".tmx");
		} catch (GdxRuntimeException e) {
			Logger.error("Map load error", e);
		}
	}

	public void loadSkinSync(String skinJson, String atlas) {
		loadSkin(skinJson, atlas);
		manager.finishLoading();
	}
	
	public void loadSkin(String skinJson, String atlas) {
		try {
			manager.load(skinJson, Skin.class, new SkinLoader.SkinParameter(atlas, createDefaultFont()));
			Logger.info("Skin Loaded");
		} catch (GdxRuntimeException e) {
			Logger.error("Load Skin Error", e);
		}
	}
	public void loadAtlasSync(String file) {
		loadAtlas(file);
		manager.finishLoading();
	}
	
	public void loadAtlas(String file) {
		if(!manager.isLoaded(file)){
			try {
				manager.load(file, TextureAtlas.class);
				Logger.info(file + " Atlas Loaded");
			} catch (GdxRuntimeException e) {
				Logger.error("Load Atlas Error", e);
			}
		}
	}

	public void loadTextureSync(String file) {
		loadTexture(file);
		manager.finishLoading();
	}
	
	public void loadTexture(String file) {
		if(!manager.isLoaded(file)){
			try {
				manager.load(file, Texture.class);
				manager.finishLoading();
			} catch (GdxRuntimeException e) {
				Logger.error("Load Texture Error", e);
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

	public BitmapFont getFont(String font){
		return manager.get(font, BitmapFont.class);
	}
	
	public java.util.Map<String, BitmapFont> getFonts(){
		java.util.Map<String, BitmapFont> fonts = new HashMap<String, BitmapFont>();
		fonts.put("default-font-16", getFont("default-font-16"));
		fonts.put("default-font-22", getFont("default-font-22"));
		fonts.put("default-font-46", getFont("default-font-46"));
		return fonts;
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
			Logger.info(file + " unloaded");
		}
	}
	public void unloadMap(int level){
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
