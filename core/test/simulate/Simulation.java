package simulate;

import static com.badlogic.gdx.Application.LOG_DEBUG;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.HeadlessFiles;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.backends.headless.mock.graphics.MockGraphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.ads.AdControllerHelper;
import com.lastdefenders.game.GameStage;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.ActorGroups;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.model.level.state.LevelStateManager.LevelState;
import com.lastdefenders.game.service.actorplacement.TowerPlacement;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.UserPreferences;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import simulate.state.GameBeginState;
import simulate.state.GameEndState;
import simulate.state.GameState;
import simulate.state.StateWriter;
import testutil.TestUtil;

/**
 * Created by Eric on 12/14/2019.
 */

public class Simulation {

    private static final int PATH_SIZE = (16*3);
    private static final float GAME_STEP_SIZE = (1f/60); // 60 FPS

    private GameStage gameStage;
    private ActorGroups actorGroups;
    private Player player;
    private LevelStateManager levelStateManager;
    private String [] towerTypes = {"FlameThrower", "Humvee", "MachineGun", "Rifle", "RocketLauncher", "Sniper", "Tank"};
    private List<GameState> gameStates = new ArrayList<>();

    @Before
    public void initSimulation() {

        HeadlessNativesLoader.load();
        //Gdx.graphics = new MockGraphics();
        //Gdx.files = new HeadlessFiles();
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();

        new HeadlessApplication(new ApplicationListener() {
            public void create() {}
            public void resize(int width, int height) {}
            public void render() {}
            public void pause() {}
            public void resume() {}
            public void dispose() {}
        }, config);
        GL20 gl20 = mock(GL20.class);
        Gdx.gl = gl20;
        Gdx.gl20 = gl20;

        Gdx.app.setLogLevel(LOG_DEBUG);

        this.gameStage = createGameStage();
    }

    @Test
    public void run(){
        while(!levelStateManager.getState().equals(LevelState.GAME_OVER)) {
            addTowers();
            startWave();
            // Create begin state
            GameState gameState = new GameState();
            GameBeginState startState = new GameBeginState(player, actorGroups.getTowerGroup().getChildren(), gameStage.getLevel().getSpawningEnemyQueue());
            gameState.setBeginState(startState);


            while (levelStateManager.getState().equals(LevelState.WAVE_IN_PROGRESS)) {
                gameStage.act(GAME_STEP_SIZE);
            }

            // Create end state
            GameEndState endState = new GameEndState(player, actorGroups.getTowerGroup().getChildren());
            gameState.setEndState(endState);

            gameStates.add(gameState);
        }
        StateWriter.save(gameStates);

    }


    private void startWave(){
        levelStateManager.setState(LevelState.WAVE_IN_PROGRESS);
    }


    private void addTowers(){

        TowerPlacement towerPlacement = gameStage.getTowerPlacement();

        while(player.getMoney() >= 200){

            boolean towerPlaced = false;
            do {
                int rndIdx = TestUtil.getRandomNumberInRange(0, towerTypes.length - 1, true);
                String rndTowerType = towerTypes[rndIdx];
                towerPlacement.createTower(rndTowerType);
                Tower tower = towerPlacement.getCurrentTower();

                if (tower.getCost() <= player.getMoney()) {
                    towerPlaced = placeTower(towerPlacement);
                    if(towerPlaced) {
                        player.spendMoney(tower.getCost());
                        towerPlacement.removeCurrentTower(false);
                    } else {
                        towerPlacement.removeCurrentTower(true);
                    }

                } else {
                    towerPlacement.removeCurrentTower(true);
                }
            } while(!towerPlaced);

        }
    }

//    public void placeTower(){
//
//        TowerPlacement towerPlacement = gameStage.getTowerPlacement();
//
//        boolean towerPlaced = false;
//        do{
//            towerPlaced = placeTower(towerPlacement);
//        } while(!towerPlaced);
//    }

    public GameStage createGameStage(){


        UserPreferences userPreferences = TestUtil.createUserPreferencesMock();
        Resources resources = new Resources(userPreferences);

        resources.loadActorAtlasRegions();

        actorGroups = new ActorGroups();
        LDAudio audio = mock(LDAudio.class);
        player = new Player();
        levelStateManager = new LevelStateManager();
        GameUIStateManager gameUIStateManager = new GameUIStateManager(levelStateManager);
        OrthographicCamera gameCamera = new OrthographicCamera();
        Viewport gameViewport = new StretchViewport(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, gameCamera);
        SpriteBatch spriteBatch = mock(SpriteBatch.class);
        GooglePlayServices googlePlayServices = new GooglePlayServicesHelper();
        AdControllerImpl adController = new AdControllerImpl();
        AdControllerHelper adControllerHelper = new AdControllerHelper(adController, Integer.MAX_VALUE);

        GameStage gameStage = new GameStage(LevelName.SERPENTINE_RIVER, player, actorGroups, audio,
            levelStateManager, gameUIStateManager, gameViewport, resources, spriteBatch,
            googlePlayServices, adControllerHelper);

        gameStage.loadFirstWave();

        return gameStage;

    }

    public boolean placeTower(TowerPlacement towerPlacement){

        float towerHeight = 0;
        float towerWidth = 0;
        Tower tower = towerPlacement.getCurrentTower();
        if(tower.getBody() instanceof Circle){
            towerHeight = ((Circle) tower.getBody()).radius;
            towerWidth = towerHeight;
        } else if(tower.getBody() instanceof Rectangle) {
            towerHeight = ((Polygon) tower.getBody()).getBoundingRectangle().height;
            towerWidth = ((Polygon) tower.getBody()).getBoundingRectangle().width;
        }

        Vector2 position = getRandomPosition();

        // Try up
        Vector2 upPos = new LDVector2(position.x, position.y + (PATH_SIZE/2) + towerHeight);
        towerPlacement.moveTower(upPos);
        boolean placed = towerPlacement.placeTower();

        if(!placed) {
            // Try down
            Vector2 downPos = new LDVector2(position.x, position.y - (PATH_SIZE/2) - towerHeight);
            towerPlacement.moveTower(downPos);
            placed = towerPlacement.placeTower();
        }

        if(!placed) {
            // Try right
            Vector2 rightPos = new LDVector2(position.x + (PATH_SIZE/2) + towerWidth, position.y);
            towerPlacement.moveTower(rightPos);
            placed = towerPlacement.placeTower();
        }

        if(!placed) {
            // Try left
            Vector2 leftPos = new LDVector2(position.x - (PATH_SIZE/2) - towerWidth, position.y);
            towerPlacement.moveTower(leftPos);
            placed = towerPlacement.placeTower();
        }


        return placed;


    }

    public Vector2 getRandomPosition(){
        // Get a random spot near the line
        Array<LDVector2> path = gameStage.getMap().getPath();

        int rndVectorIdx = TestUtil.getRandomNumberInRange(0, path.size-2, true);
        LDVector2 rndVector1 = path.get(rndVectorIdx);
        LDVector2 rndVector2 = path.get(rndVectorIdx+1);

        System.out.println(rndVector1);

        LDVector2 rndPosition;
        if(rndVector1.y == rndVector2.y){
            // Find a random point horizontally
            int rndX = TestUtil.getRandomNumberInRange((int)rndVector1.x, (int)rndVector2.x, false);
            rndPosition = new LDVector2(rndX, rndVector1.y);
        } else {
            // Find a random point vertically
            int rndY = TestUtil.getRandomNumberInRange((int)rndVector1.y, (int)rndVector2.y, false);
            rndPosition = new LDVector2(rndVector1.x, rndY);
        }


        System.out.println(path);


        System.out.println(rndPosition);

        return rndPosition;

    }



}
