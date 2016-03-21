package com.eric.mtd.helper;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.eric.mtd.MTDGame;

//-agentlib:hprof=heap=dump,format=b
public abstract class Resources {
    public static final String IMAGES_ATLAS = "images/images.atlas";
    public static final String MENU_ATLAS = "menu/menu.atlas";
    public static final String ENEMY_ATLAS = "actors/enemy/EnemyActors.atlas";   
    public static final String LEVEL_SELECT_ATLAS = "level_select/level_select.atlas";
    public static final String HUD_ATLAS = "ui/hud/hud.atlas";
    public static final String ENLIST_ATLAS = "ui/enlist/enlist.atlas";
    public static final String PERKS_ATLAS = "ui/perks/perks.atlas";
    public static final String OPTIONS_ATLAS = "ui/options/options.atlas";
    public static final String GAME_OVER_ATLAS = "ui/gameover/gameover.atlas";
    public static final String INSPECT_ATLAS = "ui/inspect/inspect.atlas";
    public static final String SKIN_JSON =  "skin/uiskin.json";
    public static final String TOWER_ATLAS = "actors/tower/TowerActors.atlas";
    public static final String EXPLOSION_ATLAS = "misc/Explosion/Explosion.atlas";
    public static final String FLAMES_ATLAS = "misc/Flames/Flames.atlas";
    
    public static final int VIEWPORT_WIDTH = 640; //16:9
    public static final int VIEWPORT_HEIGHT = 360; 
    
    public static final float NORMAL_SPEED = 1.0f;
    public static final float DOUBLE_SPEED = 2.0f;

    
    //Question: Bad?
    private static final AssetManager MANAGER = new AssetManager();

	public static void dispose() {
		if(Logger.DEBUG)System.out.println("Resources dispose");
		MANAGER.dispose();
		
	}
	public static void loadGraphics(){
		if(Logger.DEBUG)System.out.println("Loading Graphics");
		Resources.loadUIMap();
		Resources.loadSkin(Resources.SKIN_JSON);
		Resources.loadAtlas(Resources.ENEMY_ATLAS);
		Resources.loadAtlas(Resources.TOWER_ATLAS);
		Resources.loadAtlas(Resources.IMAGES_ATLAS);
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
	//Question: Do I need to dispose the TiledMaps somehow? or is that taken care of in the assetmanager dispose?s
    public static void loadMap(int level) {
        try {
        	//Question: Not sure why I need the setLoader.
            MANAGER.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
            MANAGER.load("levels/level"+level+"/level" + level + ".tmx", TiledMap.class);
            MANAGER.finishLoading();
            if(Logger.DEBUG)System.out.println("Map Loaded: " + "levels/level"+level+"/level" + level + ".tmx");
        } catch (GdxRuntimeException e) {
        	 if(Logger.DEBUG)System.out.println("Map load error " + e);
            //game.setScreen(new ErrorScreen(game, "Could not load map for level: " + level, e));
        }
    }
    public static void loadUIMap() {
        try {
        	//Question: Not sure why I need the setLoader.
            MANAGER.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
            MANAGER.load("ui/ui.tmx",TiledMap.class);
            MANAGER.finishLoading();
            if(Logger.DEBUG)System.out.println("UI Map Loaded");
        } catch (GdxRuntimeException e) {
        	 if(Logger.DEBUG)System.out.println("UI Map load error " + e);
            //game.setScreen(new ErrorScreen(game, "Could not load map for level: " + level, e));
        }
    }
    public static void loadSkin(String skinJson) {
    	String atlas = skinJson.substring(0, skinJson.lastIndexOf(".")) + ".atlas";
        try {
        	if(Logger.DEBUG)System.out.println(atlas);
        	if(Logger.DEBUG)System.out.println(skinJson);
        	MANAGER.load(skinJson, Skin.class, new SkinLoader.SkinParameter(atlas));
            MANAGER.finishLoading();
            if(Logger.DEBUG)System.out.println("Skin Loaded");
        } catch (GdxRuntimeException e) {
        	if(Logger.DEBUG)System.out.println("Load Skin Error " + e);
            //game.setScreen(new ErrorScreen(game, "Could not load skin " + file, e));
        }
    }
    public static void loadAtlas(String file) {
        try {
            MANAGER.load(file, TextureAtlas.class);
            MANAGER.finishLoading();
            if(Logger.DEBUG)System.out.println(file + " Atlas Loaded");
        } catch (GdxRuntimeException e) {
        	if(Logger.DEBUG)System.out.println("Load Atlas Error " + e);
           // game.setScreen(new ErrorScreen(game, "Not loaded: " + file, e));
        }
    }

    public static void loadTexture(String file) {
        try {
            MANAGER.load(file, Texture.class);
            MANAGER.finishLoading();
        } catch (GdxRuntimeException e) {
        	if(Logger.DEBUG)System.out.println("Load Texture Error " + e);
           // game.setScreen(new ErrorScreen(game, "Not loaded: " + file, e));
        }
    }
    public static TiledMap getMap(int level) {
        return MANAGER.get("levels/level"+level+"/level" + level + ".tmx", TiledMap.class);
    }
    public static TiledMap getUIMap(){
    	return MANAGER.get("ui/ui.tmx",TiledMap.class);
    }
    public static TextureAtlas getAtlas(String file) {
        return MANAGER.get(file, TextureAtlas.class);
    }

    public static Texture getTexture(String file) {
        return MANAGER.get(file, Texture.class);
    }
	public static Skin getSkin(String file){
		return MANAGER.get(file, Skin.class);
	}

}
