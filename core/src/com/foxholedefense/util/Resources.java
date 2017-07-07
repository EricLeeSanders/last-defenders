package com.foxholedefense.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import java.util.HashMap;
import java.util.Map;

//-agentlib:hprof=heap=dump,format=b

public class Resources {

    public static final String LOAD_ATLAS = "load/load.atlas";
    public static final String MENU_ATLAS = "menu/menu.atlas";
    public static final String ACTOR_ATLAS = "game/actors/actors.atlas";
    public static final String LEVEL_SELECT_ATLAS = "level_select/level_select.atlas";
    public static final String SKIN_ATLAS = "skin/uiskin.atlas";
    public static final float VIRTUAL_WIDTH = 640; // 16:9
    public static final float VIRTUAL_HEIGHT = 360;
    public static final float TILED_MAP_SCALE = 0.5f;
    public static final float MAX_GAME_SPEED = 2.0f;

    private static final String SKIN_JSON = "skin/uiskin.json";
    private static ShapeRenderer shapeRenderer;
    private float gameSpeed = 1;
    private UserPreferences userPreferences;
    private AssetManager manager = new AssetManager();

    private Map<String, TextureRegion> loadedTextures = new HashMap<>();
    private Map<String, Array<AtlasRegion>> loadedAtlasRegions = new HashMap<>();

    public Resources() {

    }

    public Resources(UserPreferences userPreferences) {

        this.userPreferences = userPreferences;
        shapeRenderer = new ShapeRenderer();
        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        Texture.setAssetManager(manager);
    }

    public static ShapeRenderer getShapeRenderer() {

        return shapeRenderer;
    }

    public void dispose() {

        Logger.info("Resources: dispose");
        manager.dispose();
        shapeRenderer.dispose();
        loadedTextures.clear();
        loadedAtlasRegions.clear();
    }

    public void loadActorAtlasRegions() {

        Logger.info("Resources: initializing textures");
        TextureAtlas actorAtlas = getAsset(ACTOR_ATLAS, TextureAtlas.class);

        loadedTextures.put("range-red", actorAtlas.findRegion("range-red"));
        loadedTextures.put("range", actorAtlas.findRegion("range"));
        loadedTextures.put("range-red-turret", actorAtlas.findRegion("range-red-turret"));
        loadedTextures.put("range-turret", actorAtlas.findRegion("range-turret"));
        loadedTextures.put("range-black", actorAtlas.findRegion("range-black"));
        loadedTextures.put("airstrike", actorAtlas.findRegion("airstrike"));
        loadedTextures.put("bullet", actorAtlas.findRegion("bullet"));
        loadedTextures.put("rocket", actorAtlas.findRegion("rocket"));
        loadedTextures.put("healthbar-armor", actorAtlas.findRegion("healthbar-armor"));
        loadedTextures.put("healthbar-bg", actorAtlas.findRegion("healthbar-bg"));
        loadedTextures.put("healthbar-life", actorAtlas.findRegion("healthbar-life"));
        loadedTextures.put("humvee", actorAtlas.findRegion("humvee"));
        loadedTextures.put("landmine", actorAtlas.findRegion("landmine"));
        loadedTextures.put("tower-rifle", actorAtlas.findRegion("tower-rifle"));
        loadedTextures.put("tower-machine-gun", actorAtlas.findRegion("tower-machine-gun"));
        loadedTextures.put("tower-sniper", actorAtlas.findRegion("tower-sniper"));
        loadedTextures.put("tower-flame-thrower", actorAtlas.findRegion("tower-flame-thrower"));
        loadedTextures.put("tower-rocket-launcher", actorAtlas.findRegion("tower-rocket-launcher"));
        loadedTextures.put("tower-turret-turret", actorAtlas.findRegion("tower-turret-turret"));
        loadedTextures.put("tower-turret-bags", actorAtlas.findRegion("tower-turret-bags"));
        loadedTextures.put("tower-tank-body", actorAtlas.findRegion("tower-tank-body"));
        loadedTextures.put("tower-tank-turret", actorAtlas.findRegion("tower-tank-turret"));
        loadedTextures
            .put("enemy-rifle-stationary", actorAtlas.findRegion("enemy-rifle-stationary"));
        loadedTextures.put("enemy-machine-gun-stationary",
            actorAtlas.findRegion("enemy-machine-gun-stationary"));
        loadedTextures
            .put("enemy-sniper-stationary", actorAtlas.findRegion("enemy-sniper-stationary"));
        loadedTextures.put("enemy-flame-thrower-stationary",
            actorAtlas.findRegion("enemy-flame-thrower-stationary"));
        loadedTextures.put("enemy-rocket-launcher-stationary",
            actorAtlas.findRegion("enemy-rocket-launcher-stationary"));
        loadedTextures
            .put("enemy-sprinter-stationary", actorAtlas.findRegion("enemy-sprinter-stationary"));
        loadedTextures.put("enemy-tank-body", actorAtlas.findRegion("enemy-tank-body"));
        loadedTextures.put("enemy-tank-turret", actorAtlas.findRegion("enemy-tank-turret"));
        loadedTextures.put("enemy-humvee", actorAtlas.findRegion("enemy-humvee"));
        loadedTextures.put("supply-drop", actorAtlas.findRegion("supply-drop"));
        loadedTextures.put("supply-drop-crate", actorAtlas.findRegion("supply-drop-crate"));
        loadedTextures.put("apache-stationary", actorAtlas.findRegion("apache", 1));
        loadedTextures.put("shield", actorAtlas.findRegion("shield"));

        loadedAtlasRegions.put("explosion", actorAtlas.findRegions("explosion"));
        loadedAtlasRegions.put("flame", actorAtlas.findRegions("flame"));
        loadedAtlasRegions.put("blood-splatter", actorAtlas.findRegions("blood-splatter"));
        loadedAtlasRegions.put("smoke-ring", actorAtlas.findRegions("smoke-ring"));
        loadedAtlasRegions.put("enemy-rifle", actorAtlas.findRegions("enemy-rifle"));
        loadedAtlasRegions
            .put("enemy-flame-thrower", actorAtlas.findRegions("enemy-flame-thrower"));
        loadedAtlasRegions.put("enemy-sniper", actorAtlas.findRegions("enemy-sniper"));
        loadedAtlasRegions.put("enemy-machine-gun", actorAtlas.findRegions("enemy-machine-gun"));
        loadedAtlasRegions
            .put("enemy-rocket-launcher", actorAtlas.findRegions("enemy-rocket-launcher"));
        loadedAtlasRegions.put("enemy-sprinter", actorAtlas.findRegions("enemy-sprinter"));
        loadedAtlasRegions.put("apache", actorAtlas.findRegions("apache"));
        loadedAtlasRegions.put("shield-destroyed", actorAtlas.findRegions("shield-destroyed"));
        loadedAtlasRegions.put("coin", actorAtlas.findRegions("coin"));

        Logger.info("Resources: textures initialized");
    }

    public TextureRegion getTexture(String texture) {

        return loadedTextures.get(texture);
    }

    public Array<AtlasRegion> getAtlasRegion(String region) {

        return loadedAtlasRegions.get(region);
    }

    public void activityResume() {

        shapeRenderer.dispose();
        shapeRenderer = new ShapeRenderer();
    }

    public UserPreferences getUserPreferences() {

        return userPreferences;
    }

    public void initFont() {

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

    public void loadAsset(String file, Class<?> type) {

        Logger.info("Resources: loading asset: " + file);
        if (!manager.isLoaded(file)) {
            try {
                manager.load(file, type);
                Logger.info("Resources: asset loaded: " + file);
            } catch (GdxRuntimeException e) {
                Logger.error("Resources: asset load error", e);
            }
        }
    }

    public void loadAssetSync(String file, Class<?> type) {

        Logger.info("Resources: sync loading asset: " + file);
        loadAsset(file, type);
        manager.finishLoading();
        Logger.info("Resources: sync loaded asset: " + file);
    }

    public <T> T getAsset(String file, Class<T> type) {

        if (!manager.isLoaded(file)) {
            Logger.info(file + " not loaded. Loading...");
            loadAssetSync(file, type);
        }
        return manager.get(file, type);
    }

    public void unloadAsset(String file) {

        Logger.info("Resources: unloading: " + file);
        if (manager.isLoaded(file)) {
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

    public void unloadMap(int level) {

        unloadAsset("game/levels/level" + level + "/level" + level + ".tmx");
    }

    private void loadSkinSync() {

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

        if (!manager.isLoaded(SKIN_JSON)) {
            Logger.info(SKIN_JSON + " (skin) not loaded. Loading");
            loadSkinSync();
        }
        return manager.get(SKIN_JSON, Skin.class);
    }

    public float getGameSpeed() {

        return gameSpeed;
    }

    public void setGameSpeed(float gameSpeed) {

        this.gameSpeed = gameSpeed;
    }

    public AssetManager getManager() {

        return manager;
    }

}
