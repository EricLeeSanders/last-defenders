package com.lastdefenders.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ResolutionFileResolver.Resolution;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyAttributes;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyFlameThrower;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyHumvee;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyMachineGun;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRocketLauncher;
import com.lastdefenders.game.model.actor.combat.enemy.EnemySniper;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyTank;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerAttributes;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.TowerHumvee;
import com.lastdefenders.game.model.actor.combat.tower.TowerMachineGun;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.TowerRocketLauncher;
import com.lastdefenders.game.model.actor.combat.tower.TowerSniper;
import com.lastdefenders.game.model.actor.combat.tower.TowerTank;
import com.lastdefenders.levelselect.LevelName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//-agentlib:hprof=heap=dump,format=b

public class Resources {

    public static final String LOAD_ATLAS = "load/load.atlas";
    public static final String MENU_ATLAS = "menu/menu.atlas";
    public static final String ACTOR_ATLAS = "game/actors/actors.atlas";
    public static final String LEVEL_SELECT_ATLAS = "level_select/level_select.atlas";
    public static final String TUTORIAL_ATLAS = "tutorial/tutorial.atlas";
    public static final float VIRTUAL_WIDTH = 640; // 16:9
    public static final float VIRTUAL_HEIGHT = 360;
    public static final float MAX_GAME_SPEED = 2.0f;

    private static final String ENEMY_WEIGHTS_LOC = "game/levels/wave_generator/enemy-weights.json";
    private static final String WAVE_GENERATOR_METADATA_LOC = "game/levels/wave_generator/wave-generator-metadata.json";

    private static ShapeRenderer shapeRenderer;
    private float gameSpeed = 1;
    private UserPreferences userPreferences;
    private AssetManager manager = new AssetManager();
    private String assetFolder;
    private float tiledMapScale;
    private float fontScale;
    private String skinAtlas;
    private String skinJson;

    private Map<String, TextureRegion> loadedTextures = new HashMap<>();
    private Map<String, Array<AtlasRegion>> loadedAtlasRegions = new HashMap<>();
    private Map<Class, TowerAttributes> towerAttributes = new HashMap<>();
    private Map<Class, EnemyAttributes> enemyAttributes = new HashMap<>();

    public Resources() {

    }

    public Resources(UserPreferences userPreferences, ShapeRenderer shapeRenderer) {

        this.userPreferences = userPreferences;
        this.shapeRenderer = shapeRenderer;

        Resolution[] resolutions = {new Resolution(1, 1, "lo"),
            new Resolution(361, 641, "med"),
            new Resolution(721, 1281, "hi")};

        manager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        ResolutionFileResolver fileResolver = new ResolutionFileResolver(
            new InternalFileHandleResolver(), resolutions);
        manager.setLoader(TextureAtlas.class, new TextureAtlasLoader(fileResolver));
        assetFolder = fileResolver.choose(resolutions).folder;
        Logger.info("Using " + assetFolder + " assets");
        skinAtlas = "skin/" + assetFolder + "/uiskin.atlas";
        skinJson = "skin/" + assetFolder + "/uiskin.json";

        //generateFonts();

        switch(assetFolder){
            case "hi":
                tiledMapScale = 1f/3;
                fontScale = 1f/3;
                break;
            case "med":
                tiledMapScale = 1f/2;
                fontScale = 1f/2;
                break;
            case "lo":
                tiledMapScale = 1;
                fontScale = 1f;
                break;
        }

        Texture.setAssetManager(manager);

        loadCombatActorAttributes();
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
        loadedTextures.put("range-black", actorAtlas.findRegion("range-black"));
        loadedTextures.put("airstrike", actorAtlas.findRegion("airplane"));
        loadedTextures.put("bullet", actorAtlas.findRegion("bullet"));
        loadedTextures.put("rocket", actorAtlas.findRegion("rocket"));
        loadedTextures.put("healthbar-frame", actorAtlas.findRegion("healthbar-frame"));
        loadedTextures.put("healthbar-gray", actorAtlas.findRegion("healthbar-gray"));
        loadedTextures.put("healthbar-green", actorAtlas.findRegion("healthbar-green"));
        loadedTextures.put("healthbar-orange", actorAtlas.findRegion("healthbar-orange"));
        loadedTextures.put("healthbar-red", actorAtlas.findRegion("healthbar-red"));
        loadedTextures.put("healthbar-unfilled", actorAtlas.findRegion("healthbar-unfilled"));
        loadedTextures.put("humvee", actorAtlas.findRegion("humvee"));
        loadedTextures.put("landmine", actorAtlas.findRegion("landmine"));
        loadedTextures.put("tower-rifle", actorAtlas.findRegion("tower-rifle"));
        loadedTextures.put("tower-machine-gun", actorAtlas.findRegion("tower-machine-gun"));
        loadedTextures.put("tower-sniper", actorAtlas.findRegion("tower-sniper"));
        loadedTextures.put("tower-flame-thrower", actorAtlas.findRegion("tower-flame-thrower"));
        loadedTextures.put("tower-rocket-launcher", actorAtlas.findRegion("tower-rocket-launcher"));
        loadedTextures.put("tower-humvee-turret", actorAtlas.findRegion("tower-humvee-turret"));
        loadedTextures.put("tower-humvee", actorAtlas.findRegion("tower-humvee"));
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
        loadedTextures.put("enemy-humvee-turret", actorAtlas.findRegion("enemy-humvee-turret"));
        loadedTextures.put("supply-drop-plane", actorAtlas.findRegion("airplane"));
        loadedTextures.put("supply-drop-crate", actorAtlas.findRegion("supply-drop-crate"));
        loadedTextures.put("apache-stationary", actorAtlas.findRegion("apache", 1));
        loadedTextures.put("shield", actorAtlas.findRegion("shield"));
        loadedTextures.put("shield-destroyed", actorAtlas.findRegion("shield-destroyed"));
        loadedTextures.put("pathDisplayCircle", actorAtlas.findRegion("path-display-circle"));
        loadedTextures.put("blood", actorAtlas.findRegion("blood"));

        loadedAtlasRegions.put("explosion", actorAtlas.findRegions("explosion"));
        loadedAtlasRegions.put("flame", actorAtlas.findRegions("flame"));
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
        loadedAtlasRegions.put("coin", actorAtlas.findRegions("coin"));



        Logger.info("Resources: textures initialized");
    }

    private void loadCombatActorAttributes(){
        Logger.info("Resources: loading combat actor attributes");

        Json json = new Json();

        Class [] towerClasses = {TowerFlameThrower.class, TowerHumvee.class, TowerMachineGun.class,
            TowerRifle.class, TowerRocketLauncher.class, TowerSniper.class, TowerTank.class};

        Class [] enemyClasses = {EnemyFlameThrower.class, EnemyHumvee.class, EnemyMachineGun.class, EnemyRifle.class,
            EnemyRocketLauncher.class, EnemySniper.class, EnemyTank.class};
        String path = "game/actors/attributes/%s.json";
        for(Class clazz : towerClasses){
            FileHandle file = Gdx.files.internal(String.format(path, clazz.getSimpleName()));
            TowerAttributes.Builder builder = json.fromJson(TowerAttributes.Builder.class, file);

            towerAttributes.put(clazz, builder.build());
        }

        for(Class clazz : enemyClasses){
            FileHandle file = Gdx.files.internal(String.format(path, clazz.getSimpleName()));
            EnemyAttributes.Builder builder = json.fromJson(EnemyAttributes.Builder.class, file);

            enemyAttributes.put(clazz, builder.build());
        }

        Logger.info("Resources: combat actor attributes loaded");
    }

    public TowerAttributes getTowerAttribute(Class<? extends Tower> clazz){
        return towerAttributes.get(clazz);
    }

    public EnemyAttributes getEnemyAttributes(Class<? extends Enemy> clazz){
        return enemyAttributes.get(clazz);
    }

    public TextureRegion getTexture(String texture) {

        TextureRegion retrievedTexture = loadedTextures.get(texture);
        if(retrievedTexture == null){
            throw new IllegalArgumentException("Texture: " + texture + " does not exist");
        }

        return retrievedTexture;
    }

    public Array<AtlasRegion> getAtlasRegion(String region) {

        Array<AtlasRegion> retrievedRegions = loadedAtlasRegions.get(region);
        if(retrievedRegions == null){
            throw new IllegalArgumentException("Region: " + region + " does not exist");
        }

        return retrievedRegions;
    }

    public void reload() {

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

        font.getData().setScale(getFontScale());
        //font.getData().lineHeight = 55;
        //font.getData().ascent = 6;
        //font.getData().capHeight = 28;
//        font.getData().descent = -16;
//        font.getData().spaceXadvance = 20;
//        font.getData().xHeight = 30;


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

    public void loadMap(LevelName level) {

        loadAsset("game/levels/" + level.toString() + "/" + assetFolder + "/" + level.toString() + ".tmx",
            TiledMap.class);
    }

    public TiledMap getMap(LevelName level) {

        return getAsset("game/levels/" + level.toString() + "/" + assetFolder + "/" + level.toString() + ".tmx",
            TiledMap.class);
    }

    public void unloadMap(LevelName level) {

        unloadAsset("game/levels/" + level.toString() + "/" + assetFolder + "/" + level.toString() + ".tmx");
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
            manager.load(skinJson, Skin.class, new SkinLoader.SkinParameter(skinAtlas));
            Logger.info("Resources: skin loaded");
        } catch (GdxRuntimeException e) {
            Logger.error("Resources: load skin error", e);
        }
    }

    public Skin getSkin() {

        if (!manager.isLoaded(skinJson)) {
            Logger.info(skinJson + " (skin) not loaded. Loading");
            loadSkinSync();
        }
        return manager.get(skinJson, Skin.class);
    }

    public float getGameSpeed() {

        return gameSpeed;
    }

    public void setGameSpeed(float gameSpeed) {

        this.gameSpeed = gameSpeed;
    }

    public float getTiledMapScale(){

        return tiledMapScale;
    }

    public float getFontScale() {

        return fontScale;
    }

    public AssetManager getManager() {

        return manager;
    }

}
