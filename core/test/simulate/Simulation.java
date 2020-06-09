package simulate;

import static com.badlogic.gdx.Application.LOG_DEBUG;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.ads.AdControllerHelper;
import com.lastdefenders.game.GameStage;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.TowerHumvee;
import com.lastdefenders.game.model.actor.combat.tower.TowerMachineGun;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.TowerRocketLauncher;
import com.lastdefenders.game.model.actor.combat.tower.TowerSniper;
import com.lastdefenders.game.model.actor.combat.tower.TowerTank;
import com.lastdefenders.game.model.actor.interfaces.IRotatable;
import com.lastdefenders.game.model.level.Map;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map.Entry;
import org.junit.Before;
import org.junit.Test;
import simulate.helper.support.SupportSimulationTypeHelper;
import simulate.helper.UpgradeSimulationTypeHelper;
import simulate.positionweights.TowerPositionWeight;
import simulate.state.WaveState;
import simulate.state.writer.StateWriter;
import testutil.TestUtil;

/**
 * Created by Eric on 12/14/2019.
 */

public class Simulation {

    private static final int PATH_SIZE = (16*3);
    private static final float GAME_STEP_SIZE = (1f/60); // 60 FPS

    private static final int WAVE_LIMIT = 50;

    private GameStage gameStage;
    private ActorGroups actorGroups;
    private Player player;
    private Resources resources;
    private LevelStateManager levelStateManager;
    private HashMap<String, Integer> towerTypes;

    private SupportSimulationTypeHelper supportHelper;

    @Before
    public void initSimulation() {
        towerTypes = new HashMap<>();

        HeadlessNativesLoader.load();
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

        towerTypes.put("Rifle", resources.getTowerAttribute(TowerRifle.class).getCost());
        towerTypes.put("MachineGun", resources.getTowerAttribute(TowerMachineGun.class).getCost());
        towerTypes.put("Sniper", resources.getTowerAttribute(TowerSniper.class).getCost());
        towerTypes.put("FlameThrower", resources.getTowerAttribute(TowerFlameThrower.class).getCost());
        towerTypes.put("RocketLauncher", resources.getTowerAttribute(TowerRocketLauncher.class).getCost());
        towerTypes.put("Tank", resources.getTowerAttribute(TowerTank.class).getCost());
        towerTypes.put("Humvee", resources.getTowerAttribute(TowerHumvee.class).getCost());

        supportHelper = new SupportSimulationTypeHelper(gameStage, player);
    }

    @Test
    public void run() throws IOException {
        //runAggregate(1);
        simulate(SimulationRunType.ALL, true);
//        initSimulation();
//        simulate(SimulationRunType.UPGRADES_ALL);
//        initSimulation();
//        simulate(SimulationRunType.UPGRADE_ARMOR);
//        initSimulation();
//        simulate(SimulationRunType.UPGRADE_ATTACK);
//        initSimulation();
//        simulate(SimulationRunType.UPGRADE_ATTACK_SPEED);
//        initSimulation();
//        simulate(SimulationRunType.UPGRADE_RANGE);
//        runAggregate(100, new SimulationRunType[]{ SimulationRunType.SUPPORT_AIR_STRIKE, SimulationRunType.SUPPORT_ALL, SimulationRunType.ALL});

//        runAggregate(50, new SimulationRunType[]{SimulationRunType.TOWER_ONLY, SimulationRunType.UPGRADES_ALL, SimulationRunType.SUPPORT_ALL, SimulationRunType.ALL});
//        runAggregate(10);
    }

    public void runAggregate(int count, SimulationRunType [] runTypes)  throws IOException{
        HashMap<SimulationRunType, HashMap<Integer, Integer>> waveCounts = new HashMap<>();

        for(int i = 0; i < count; i++){
            for(SimulationRunType runType : runTypes){
                List<WaveState> waveStates = simulate(runType, false);
                HashMap<Integer, Integer> waveCount = waveCounts.get(runType);
                if(waveCount == null){
                    waveCount = new HashMap<>();
                }
                waveCount.put(i+1, waveStates.size());
                waveCounts.put(runType,waveCount);
                initSimulation();
            }
        }

        for (HashMap.Entry<SimulationRunType, HashMap<Integer, Integer>> entry : waveCounts.entrySet()) {
            SimulationRunType runType = entry.getKey();
            HashMap<Integer, Integer> waveCount = entry.getValue();

            IntSummaryStatistics stats = new IntSummaryStatistics();
            for(HashMap.Entry<Integer, Integer> waveEntry : waveCount.entrySet()){
                stats.accept(waveEntry.getValue());
            }

            System.out.println("Stats for " + runType);
            System.out.println("Min: " + stats.getMin());
            System.out.println("Max: " + stats.getMax());
            System.out.println("Avg: " + stats.getAverage());
            System.out.println(waveCount);
        }
    }

    public void runAggregate(int count) throws IOException {
        runAggregate(count, SimulationRunType.values());
    }

    private List<WaveState> simulate(SimulationRunType simulationRunType, boolean writeState) throws IOException{

        System.out.println("Running simulation for " + simulationRunType);
        List<WaveState> waveStates = new ArrayList<>();
        int wave = 1;
        while(!levelStateManager.getState().equals(LevelState.GAME_OVER) && wave <= WAVE_LIMIT) {
            addTowers();
            System.out.println("Wave: " + wave + "; remaining lives: " + player.getLives());
            startWave();

            // Create begin state
//            GameState gameState = new GameState();
//            GameBeginState startState = new GameBeginState(player, actorGroups.getTowerGroup().getCastedChildren(), gameStage.getLevel().getSpawningEnemyQueue());
//            gameState.setBeginState(startState);
            WaveState waveState = new WaveState(wave, player.getLives(), player.getMoney(), actorGroups.getTowerGroup().getCastedChildren(), gameStage.getLevel().getSpawningEnemyQueue());
            supportHelper.setCurrentWaveState(waveState);
            while (levelStateManager.getState().equals(LevelState.WAVE_IN_PROGRESS)) {
                gameStage.act(GAME_STEP_SIZE);
                switch(simulationRunType){
                    case SUPPORT_ALL:
                    case ALL:
                        supportHelper.handleSupport(GAME_STEP_SIZE);
                        break;
                    case SUPPORT_LANDMINE:
                        supportHelper.handleSupportLandmine(GAME_STEP_SIZE);
                        break;
                    case SUPPORT_CRATE_DROP:
                        supportHelper.handleSupportSupplyDrop(GAME_STEP_SIZE);
                        break;
                    case SUPPORT_APACHE:
                        supportHelper.handleSupportApache(GAME_STEP_SIZE);
                        break;
                    case SUPPORT_AIR_STRIKE:
                        supportHelper.handleAirStrike(GAME_STEP_SIZE);
                        break;
                }
            }

            // Create end state
//            GameEndState endState = new GameEndState(player, actorGroups.getTowerGroup().getChildren());
//            gameState.setEndState(endState);
//
//            gameStates.add(gameState);

            waveState.setLivesEnd(player.getLives());
            waveState.setMoneyEnd(player.getMoney());

            waveStates.add(waveState);


            if(simulationRunType.equals(SimulationRunType.UPGRADES_ALL)
                || simulationRunType.equals(SimulationRunType.UPGRADE_RANGE)
                || simulationRunType.equals(SimulationRunType.UPGRADE_ATTACK_SPEED)
                || simulationRunType.equals(SimulationRunType.UPGRADE_ATTACK)
                || simulationRunType.equals(SimulationRunType.UPGRADE_ARMOR)
                || simulationRunType.equals(SimulationRunType.ALL)
                ) {
                UpgradeSimulationTypeHelper.upgradeTowers(simulationRunType, player, actorGroups.getTowerGroup().getChildren());
            }

            wave++;

        }

        if(writeState) {
            StateWriter.save(waveStates, gameStage.getLevel().getActiveLevel(), simulationRunType);
        }

        WaveState lastRound = waveStates.get(waveStates.size() - 1);
        System.out.println("Completed simulation for " + simulationRunType);
        System.out.println("Last wave: " + wave);
        System.out.println("Lives left: " + lastRound.getLivesEnd());


        return waveStates;

    }



    private void placeTower(List<TowerPositionWeight> positionWeights){
        for(TowerPositionWeight position : positionWeights){
            gameStage.getTowerPlacement().moveTower(new LDVector2(position.getX(), position.getY()));
            if(gameStage.getTowerPlacement().placeTower()){
                return;
            } else if(gameStage.getTowerPlacement().getCurrentTower() instanceof IRotatable){
                gameStage.getTowerPlacement().rotateTower(90);
                if(gameStage.getTowerPlacement().placeTower()){
                    return;
                }
            }
        }
    }

    public static float intersectSegmentCircleDisplace(Vector2 start, Vector2 end, Vector2 point, float radius, Vector2 displacement) {

        Vector2 tmp = new Vector2();
        Vector2 tmp2 = new Vector2();

        float u = (point.x - start.x) * (end.x - start.x) + (point.y - start.y) * (end.y - start.y);
        float d = start.dst(end);
        u /= d * d;
        if (u < 0)
            tmp2.set(start);
        else if (u > 1)
            tmp2.set(end);
        else {
            tmp.set(end.x, end.y).sub(start.x, start.y);
            tmp2.set(start.x, start.y).add(tmp.scl(u));
        }
        d = tmp2.dst(point.x, point.y);
        if (d < radius) {
            displacement.set(tmp2).sub(point).nor();
            return radius - d;
        } else
            return Float.POSITIVE_INFINITY;
    }

    private boolean invalidPlacement(Map map, Tower tower){
        return CollisionDetection.collisionWithPath(map.getPathBoundaries(), tower) ||
            CollisionDetection.outOfMapBoundary(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, tower);
    }

    public static List<Vector2> getCircleLineIntersectionPoint(Vector2 pointA,
        Vector2 pointB, Vector2 center, float radius) {
        float baX = pointB.x - pointA.x;
        float baY = pointB.y - pointA.y;
        float caX = center.x - pointA.x;
        float caY = center.y - pointA.y;

        float a = baX * baX + baY * baY;
        float bBy2 = baX * caX + baY * caY;
        float c = caX * caX + caY * caY - radius * radius;

        float pBy2 = bBy2 / a;
        float q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return Collections.emptyList();
        }
        // if disc == 0 ... dealt with later
        float tmpSqrt = (float)Math.sqrt(disc);
        float abScalingFactor1 = -pBy2 + tmpSqrt;
        float abScalingFactor2 = -pBy2 - tmpSqrt;

        Vector2 p1 = new Vector2(pointA.x - baX * abScalingFactor1, pointA.y
            - baY * abScalingFactor1);
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return Collections.singletonList(p1);
        }
        Vector2 p2 = new Vector2(pointA.x - baX * abScalingFactor2, pointA.y
            - baY * abScalingFactor2);
        return Arrays.asList(p1, p2);
    }

    public static boolean inLine(Vector2 A, Vector2 B, Vector2 C) {
        if(C.x > A.x && C.x > B.x){
            return false;
        }
        if(C.x < A.x && C.x < B.x){
            return false;
        }

        if(C.y > A.y && C.y > B.y){
            return false;
        }
        if(C.y < A.y && C.y < B.y){
            return false;
        }

        return true;
    }

    private List<TowerPositionWeight> findStrongestPoint(Tower tower){
        Array<LDVector2> waypoints = gameStage.getMap().getPath();
//        Array<LDVector2> unformattedWaypoints = gameStage.getMap().getPath();
//
//        Array<LDVector2> waypoints = new Array<>();
//
//        for(int i = 0; i < unformattedWaypoints.size - 1; i++){
//            LDVector2 start = unformattedWaypoints.get(i);
//            LDVector2 end = unformattedWaypoints.get(i+1);
//
//            if(start.y == end.y){
//                // Horizontal
//                waypoints.add(new LDVector2(start.x, start.y+10));
//                waypoints.add(new LDVector2(start.x, start.y-10));
//            } else {
//                // Vertical
//                waypoints.add(new LDVector2(start.x+10, start.y));
//                waypoints.add(new LDVector2(start.x-10, start.y));
//            }
//
//        }
//
//        System.out.println(waypoints);
        List<TowerPositionWeight> positionWeights = new ArrayList<>();
        Vector2 displacement = new Vector2();

        for(int x = 0; x <= Resources.VIRTUAL_WIDTH; x++) {
            for (int y = 0; y <= Resources.VIRTUAL_HEIGHT; y++) {
                Vector2 center = new Vector2(x, y);
                tower.setPositionCenter(center);
                if(invalidPlacement(gameStage.getMap(), tower)){

                    continue;
                }
                //                for (int i = 0; i < waypoints.size - 1; i++) {
//                    Vector2 start = waypoints.get(i);
//                    Vector2 end = waypoints.get(i + 1);
//                    float d = intersectSegmentCircleDisplace(start, end, location, 50,
//                        displacement);
//                    if(d != Float.POSITIVE_INFINITY ) {
//                        locationWeight += d;
//                    }
//                }
                Array<Vector2> intersections = new Array<>();
                int depth =Integer.MAX_VALUE;
                float locationLength = 0;
                for (int i = 0; i < waypoints.size - 1; i++) {

                    Vector2 start = waypoints.get(i);
                    Vector2 end = waypoints.get(i + 1);

                    float d = intersectSegmentCircleDisplace(start, end, center, tower.getRangeShape().radius,
                            displacement);
                    if(d != Float.POSITIVE_INFINITY ) {
                        locationLength += d;
                    }

                    Boolean intersects = Intersector
                        .intersectSegmentCircle(start, end, center, tower.getRangeShape().radius * tower.getRangeShape().radius);
                    if (!intersects) {
                        continue;
                    }
                    if(i < depth){
                        depth = i;
                    }
                    List<Vector2> points = getCircleLineIntersectionPoint(start, end, center, tower.getRangeShape().radius);

                    if(points.size() > 0 ) {
                        Vector2 firstPoint = points.get(0);

                        if (inLine(start, end, firstPoint)) {
                            intersections.add(firstPoint);
                        }

//                        if (!inLine(start, end, firstPoint)) {
//                            firstPoint = start;
//                        }
//
//                        intersections.add(firstPoint);
                    }

                    if(points.size() > 1 ) {
                        Vector2 secondPoint = points.get(1);

                        if (inLine(start, end, secondPoint)) {
                            intersections.add(secondPoint);
                        }
//                        if (!inLine(start, end, secondPoint)) {
//                            secondPoint = end;
//                        }
//
//                        intersections.add(secondPoint);
                    }
                }
                positionWeights.add(new TowerPositionWeight(x, y, intersections.size, actorGroups.getTowerGroup().getChildren(), locationLength, depth, tower));
            }
        }

        Collections.sort(positionWeights);
        Collections.reverse(positionWeights);

        return positionWeights;


    }

    private void startWave(){
        levelStateManager.setState(LevelState.WAVE_IN_PROGRESS);
    }


    private void addTowers(){

        TowerPlacement towerPlacement = gameStage.getTowerPlacement();
        String bestTowerName = getBestTower();
        while(bestTowerName != null){

            towerPlacement.createTower(bestTowerName);
            Tower tower = towerPlacement.getCurrentTower();
            List<TowerPositionWeight> positionWeights = findStrongestPoint(tower);
            placeTower(positionWeights);
            player.spendMoney(tower.getCost());
            towerPlacement.removeCurrentTower(false);
            bestTowerName = getBestTower();

        }
    }

    private String getBestTower(){
        List<String> bestTowers = new ArrayList<>();
        Integer highestCost = 0;
        for(Entry<String, Integer> entry : towerTypes.entrySet()){
            String towerName = entry.getKey();
            Integer cost = entry.getValue();

            if(cost <= player.getMoney() && cost >= highestCost){
                if(cost > highestCost){
                    bestTowers.clear();
                }
                bestTowers.add(towerName);
                highestCost = cost;
            }
        }
        if(bestTowers.size() == 1){
            return bestTowers.get(0);
        } else if(bestTowers.size() > 1) {
            int rndIdx = TestUtil.getRandomNumberInRange(0, bestTowers.size() - 1);

            return bestTowers.get(rndIdx);
        } else {
            return null;
        }

    }


    private GameStage createGameStage(){


        UserPreferences userPreferences = TestUtil.createUserPreferencesMock();
        resources = new Resources(userPreferences);

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
        Vector2 upPos = new LDVector2(position.x, position.y + (PATH_SIZE/2.0f) + towerHeight);
        towerPlacement.moveTower(upPos);
        boolean placed = towerPlacement.placeTower();

        if(!placed) {
            // Try down
            Vector2 downPos = new LDVector2(position.x, position.y - (PATH_SIZE/2.0f) - towerHeight);
            towerPlacement.moveTower(downPos);
            placed = towerPlacement.placeTower();
        }

        if(!placed) {
            // Try right
            Vector2 rightPos = new LDVector2(position.x + (PATH_SIZE/2.0f) + towerWidth, position.y);
            towerPlacement.moveTower(rightPos);
            placed = towerPlacement.placeTower();
        }

        if(!placed) {
            // Try left
            Vector2 leftPos = new LDVector2(position.x - (PATH_SIZE/2.0f) - towerWidth, position.y);
            towerPlacement.moveTower(leftPos);
            placed = towerPlacement.placeTower();
        }


        return placed;


    }

    public Vector2 getRandomPosition(){
        // Get a random spot near the line
        Array<LDVector2> path = gameStage.getMap().getPath();

        int rndVectorIdx = TestUtil.getRandomNumberInRange(0, path.size-2);
        LDVector2 rndVector1 = path.get(rndVectorIdx);
        LDVector2 rndVector2 = path.get(rndVectorIdx+1);

        LDVector2 rndPosition;
        if(rndVector1.y == rndVector2.y){
            // Find a random point horizontally
            int rndX = TestUtil.getRandomNumberInRange((int)rndVector1.x, (int)rndVector2.x);
            rndPosition = new LDVector2(rndX, rndVector1.y);
        } else {
            // Find a random point vertically
            int rndY = TestUtil.getRandomNumberInRange((int)rndVector1.y, (int)rndVector2.y);
            rndPosition = new LDVector2(rndVector1.x, rndY);
        }

        return rndPosition;

    }
}
