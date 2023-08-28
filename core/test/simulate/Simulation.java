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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.ads.AdControllerHelper;
import com.lastdefenders.game.GameStage;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.level.state.LevelStateManager;
import com.lastdefenders.game.model.level.state.LevelStateManager.LevelState;
import com.lastdefenders.game.ui.state.GameUIStateManager;
import com.lastdefenders.googleplay.GooglePlayServices;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.log.EventLogger;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.UserPreferences;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulate.helper.SimulationTowerHelper;
import simulate.helper.support.SupportSimulationTypeHelper;
import simulate.helper.UpgradeSimulationTypeHelper;
import simulate.state.AggregateSimulationState;
import simulate.state.SingleSimulationState;
import simulate.state.WaveState;
import simulate.state.writer.StateWriter;
import testutil.TestUtil;
import testutil.UserPreferencesMock;

/**
 * Created by Eric on 12/14/2019.
 */

public class Simulation {

    private static final float GAME_STEP_SIZE = (1f/60); // 60 FPS

    private static final int WAVE_LIMIT = 80;

    private final LevelName levelName = LevelName.SERPENTINE_RIVER;

    private GameStage gameStage;
    private ActorGroups actorGroups;
    private Player player;
    private Resources resources;
    private LevelStateManager levelStateManager;

    private SupportSimulationTypeHelper supportHelper;
    private SimulationTowerHelper towerHelper;

    private StateWriter stateWriter = new StateWriter();
    @BeforeEach
    public void initSimulation() {

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
        createHelpers();

    }

    public void resetGame(){
        gameStage = createGameStage();
        createHelpers();
    }

    @Test
    public void run() throws IOException {
//        runAggregate(15);
  runSingle(SimulationRunType.ALL);
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
//        runAggregate(80, new SimulationRunType[]{  SimulationRunType.ALL});
////
 // runAggregate(10);
//        runAggregate(15);
    }

    public void runAggregate(int count, SimulationRunType [] runTypes)  throws IOException{

        System.out.println("Running Aggregate Simulation " + count + " times");

        Set<AggregateSimulationState> aggregateSimulationStateSet = new HashSet<>();

        for(SimulationRunType runType : runTypes){
            System.out.println("Simulation Run Type: " + runType);
            AggregateSimulationState aggregateSimulationState = new AggregateSimulationState(runType);
            for(int i = 0; i < count; i++){
                System.out.println("Iteration Count: " + i);
                SingleSimulationState singleSimulationState = simulate(runType);
                aggregateSimulationState.addSingleSimulationState(singleSimulationState);
                resetGame();
            }

            aggregateSimulationStateSet.add(aggregateSimulationState);
        }

        stateWriter.saveSimulationAggregate(aggregateSimulationStateSet, gameStage.getLevel().getActiveLevel());
    }

    public void runSingle(SimulationRunType simulationRunType) throws IOException {
        SingleSimulationState singleSimulationState = simulate(SimulationRunType.ALL);
        stateWriter.saveSimulation(singleSimulationState, gameStage.getLevel().getActiveLevel(), simulationRunType);
    }

    public void runAggregate(int count) throws IOException {
        runAggregate(count, SimulationRunType.values());
    }

    private SingleSimulationState simulate(SimulationRunType simulationRunType) throws IOException{
        System.out.println("Simulation: " + simulationRunType.name());
        SingleSimulationState singleSimulationState = new SingleSimulationState();
        int wave = 1;
        while(!levelStateManager.getState().equals(LevelState.GAME_OVER) && wave <= WAVE_LIMIT) {
            System.out.println("Wave: " + wave);

            int startMoney = player.getMoney();

            towerHelper.addTowers();

            startWave();

            WaveState waveState = new WaveState(wave, player.getLives(), startMoney,
                actorGroups.getTowerGroup().getCastedChildren(), gameStage.getLevel().getSpawningEnemyQueue());

            supportHelper.setCurrentWaveState(waveState);
            float waveTimeLength = 0;
            while (levelStateManager.getState().equals(LevelState.WAVE_IN_PROGRESS)) {
                gameStage.act(GAME_STEP_SIZE);
                waveTimeLength += GAME_STEP_SIZE;
                switch (simulationRunType) {
                    case SUPPORT_ALL:
                    case ALL:
                        supportHelper.handleSupport();
                        break;
                    case SUPPORT_LANDMINE:
                        supportHelper.handleSupportLandmine();
                        break;
                    case SUPPORT_CRATE_DROP:
                        supportHelper.handleSupportSupplyDrop();
                        break;
                    case SUPPORT_APACHE:
                        supportHelper.handleSupportApache();
                        break;
                    case SUPPORT_AIR_STRIKE:
                        supportHelper.handleAirStrike();
                        break;
                }
            }

            if(levelStateManager.getState().equals(LevelState.GAME_OVER)){
                waveState.gameOver();
            }

            waveState.setLivesEnd(player.getLives());
            waveState.setMoneyEnd(player.getMoney());

            singleSimulationState.addWaveState(waveState);


            if(simulationRunType.equals(SimulationRunType.UPGRADES_ALL)
                || simulationRunType.equals(SimulationRunType.UPGRADE_RANGE)
                || simulationRunType.equals(SimulationRunType.UPGRADE_ATTACK_SPEED)
                || simulationRunType.equals(SimulationRunType.UPGRADE_ATTACK)
                || simulationRunType.equals(SimulationRunType.UPGRADE_ARMOR)
                || simulationRunType.equals(SimulationRunType.ALL)
                ) {
                UpgradeSimulationTypeHelper.upgradeTowers(simulationRunType, player, actorGroups.getTowerGroup().getChildren());
            }

            System.out.println("Wave Time Length: " + waveTimeLength);
            wave++;

        }

        System.out.println("Waves Reached = " + singleSimulationState.getWaveStates().size());

        return singleSimulationState;

    }

    private void startWave(){
        levelStateManager.setState(LevelState.WAVE_IN_PROGRESS);
    }

    private GameStage createGameStage() {

        UserPreferences userPreferences = UserPreferencesMock.create();
        ShapeRenderer shapeRendererMock = mock(ShapeRenderer.class);
        resources = new Resources(userPreferences, shapeRendererMock);

        resources.loadActorAtlasRegions();

        actorGroups = new ActorGroups();
        LDAudio audio = mock(LDAudio.class);
        player = new Player();
        levelStateManager = new LevelStateManager();
        GameUIStateManager gameUIStateManager = new GameUIStateManager(levelStateManager);
        OrthographicCamera gameCamera = new OrthographicCamera();
        Viewport gameViewport = new StretchViewport(Resources.VIRTUAL_WIDTH,
            Resources.VIRTUAL_HEIGHT, gameCamera);
        SpriteBatch spriteBatch = mock(SpriteBatch.class);
        GooglePlayServices googlePlayServices = new GooglePlayServicesHelper();
        AdControllerImpl adController = new AdControllerImpl();
        AdControllerHelper adControllerHelper = new AdControllerHelper(adController, userPreferences,
            Integer.MAX_VALUE);

        EventLogger eventLogger = mock(EventLogger.class);

        GameStage gameStage = new GameStage(levelName, player, actorGroups, audio,
            levelStateManager, gameUIStateManager, gameViewport, resources, spriteBatch,
            googlePlayServices, adControllerHelper, eventLogger);

        gameStage.loadFirstWave();

        return gameStage;

    }

    private void createHelpers(){
        supportHelper = new SupportSimulationTypeHelper(gameStage, player);
        towerHelper = new SimulationTowerHelper(gameStage, resources, actorGroups, player);
    }

}
