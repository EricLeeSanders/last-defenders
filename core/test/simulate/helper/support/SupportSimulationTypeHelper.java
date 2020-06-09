package simulate.helper.support;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.GameStage;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyTank;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.support.Apache;
import com.lastdefenders.game.model.actor.support.LandMine;
import com.lastdefenders.game.model.actor.support.SupplyDropCrate;
import com.lastdefenders.game.service.actorplacement.SupplyDropPlacement;
import com.lastdefenders.game.service.actorplacement.SupportActorPlacement;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import simulate.state.WaveState;
import simulate.state.support.ApacheState;
import simulate.state.support.LandMineState;
import simulate.state.support.SupplyDropState;
import testutil.TestUtil;

public class SupportSimulationTypeHelper {

    private GameStage gameStage;
    private Player player;
    private SupplyDropPlacement supplyDropPlacement;
    private SupportActorPlacement supportActorPlacement;

    private static final int MAX_LANDMINES = 1;
    // How close an enemy must be to the end of the path to place a mine
    private static final float ENEMY_DISTANCE_FOR_LANDMINE_MOD = (1/6f);
    private static final float LANDMINE_COOLDOWN_RESET = 5; // 5 seconds

    private static final int MIN_TOWERS_FOR_CRATE_DROP = 7;
    private static final float SUPPLY_DROP_COOLDOWN_RESET = 5; // 5 seconds

    private static final int MIN_NUM_OF_ENEMIES_FOR_APACHE = 5;
    private static final float ENEMY_DISTANCE_FOR_APACHE_MOD = (2/3f);
    private static final float APACHE_COOLDOWN_RESET = 10; // 10 seconds

    private float apacheCooldownCounter = APACHE_COOLDOWN_RESET;
    private float landmineCooldownCounter = LANDMINE_COOLDOWN_RESET;
    private float supplyDropCooldownCounter = SUPPLY_DROP_COOLDOWN_RESET;


    private final float minEnemyDistanceForLandmine;
    private final float minEnemyDistanceForApache;

    private AirStrikeSimulationHelper airStrikeSimulationHelper;


    private WaveState currentWaveState;

    public SupportSimulationTypeHelper(GameStage gameStage, Player player) {

        this.gameStage = gameStage;
        this.player = player;
        this.supplyDropPlacement = gameStage.getSupplyDropPlacement();
        this.supportActorPlacement = gameStage.getSupportActorPlacement();

        minEnemyDistanceForLandmine = gameStage.getMap().getPathDistance() * ENEMY_DISTANCE_FOR_LANDMINE_MOD;
        minEnemyDistanceForApache = gameStage.getMap().getPathDistance() * ENEMY_DISTANCE_FOR_APACHE_MOD;

        airStrikeSimulationHelper = new AirStrikeSimulationHelper(gameStage, player);

        System.out.println("Map Distance: " + gameStage.getMap().getPathDistance());
        System.out.println("minEnemyDistanceForLandmine: " + minEnemyDistanceForLandmine);
        System.out.println("minEnemyDistanceForApache: " + minEnemyDistanceForApache);
    }

    public void handleSupportLandmine(float delta){
        landmineCooldownCounter -= delta;

        if (canPlaceLandmine() && shouldPlaceLandmine()) {
            placeLandmine();
            player.spendMoney(LandMine.COST);
            landmineCooldownCounter = LANDMINE_COOLDOWN_RESET;
        }
    }

    public void handleSupportSupplyDrop(float delta){

        supplyDropCooldownCounter -= delta;

        if(canUseSupplyDrop() && shouldCheckBestSupplyDropLocation()){
            Vector2 supplyDropLoc = getBestSupplyDropLocation();
            if(supplyDropLoc != null){
                System.out.println("Placing Supply Drop at: " + supplyDropLoc);
                supplyDropPlacement.setLocation(supplyDropLoc);
                currentWaveState.addSupportState(new SupplyDropState(supportActorPlacement.getCurrentSupportActor().getPositionCenter()));
                supplyDropPlacement.placeSupplyDrop();
                player.spendMoney(SupplyDropCrate.COST);
                supplyDropCooldownCounter = SUPPLY_DROP_COOLDOWN_RESET;
            }
        }
    }

    public void handleSupportApache(float delta){
        apacheCooldownCounter -= delta;

        if(canUseApache() && shouldUseApache()){
            Vector2 apacheLoc = getBestApacheLocation();
            for(Enemy enemy : gameStage.getActorGroups().getEnemyGroup().getCastedChildren()){
                System.out.println("Enemy pos: " + enemy.getPositionCenter() + "; dist: " + enemy.getLengthToEnd());
            }
            if(apacheLoc != null){
                System.out.println("Placing Apache at: " + apacheLoc);
                supportActorPlacement.moveSupportActor(apacheLoc);
                currentWaveState.addSupportState(new ApacheState(supportActorPlacement.getCurrentSupportActor().getPositionCenter()));
                supportActorPlacement.placeSupportActor();
                player.spendMoney(Apache.COST);
                apacheCooldownCounter = APACHE_COOLDOWN_RESET;
            }
        }
    }

    public void handleSupport(float delta) {
        handleSupportSupplyDrop(delta);
        handleSupportApache(delta);
        handleSupportLandmine(delta);
        handleAirStrike(delta);

    }

    public void handleAirStrike(float delta){
        airStrikeSimulationHelper.handleAirStrike(delta);
    }

    private boolean canUseApache(){
        return player.getMoney() >= Apache.COST && apacheCooldownCounter <= 0;
    }

    private boolean shouldUseApache(){

        Array<Enemy> enemies = gameStage.getActorGroups().getEnemyGroup().getCastedChildren();
        int numOfEnemies = 0;
        for(Enemy enemy : enemies){
            if(enemy.isActive() && enemy.getLengthToEnd() <= minEnemyDistanceForApache){
                numOfEnemies++;
            }
        }

        return numOfEnemies >= MIN_NUM_OF_ENEMIES_FOR_APACHE;
    }

    private Vector2 getBestApacheLocation(){
        supportActorPlacement.createSupportActor(Apache.class);
        Vector2 bestLoc = null;
        float closestEnemyDist = Float.MAX_VALUE;

        for(Enemy enemy : gameStage.getActorGroups().getEnemyGroup().getCastedChildren()){

            if(enemy.getLengthToEnd() < closestEnemyDist){
                closestEnemyDist = enemy.getLengthToEnd();
                bestLoc = enemy.getPositionCenter();
            }
        }
        supportActorPlacement.moveSupportActor(bestLoc);
        System.out.println("Returning best apache loc: " + bestLoc + " with amount: " + closestEnemyDist);
        return bestLoc;
    }

    private boolean canUseSupplyDrop(){
        return player.getMoney() >= SupplyDropCrate.COST && supplyDropCooldownCounter <= 0;
    }

    private boolean shouldCheckBestSupplyDropLocation(){
        Array<Tower> towers = gameStage.getActorGroups().getTowerGroup().getCastedChildren();
        int numOfWeakTowers = 0;
        for (Tower tower : towers) {
            if (tower.getHealthPercent() <= .5) {
                numOfWeakTowers++;
            }
        }

        return numOfWeakTowers >= MIN_TOWERS_FOR_CRATE_DROP;
    }

    private Vector2 getBestSupplyDropLocation(){
        Array<Tower> towers = gameStage.getActorGroups().getTowerGroup().getCastedChildren();
        supplyDropPlacement.createSupplyDrop();
        Vector2 bestLoc = null;
        int bestLocAmount = 0;
        for(int x = 0; x <= Resources.VIRTUAL_WIDTH; x++) {
            for (int y = 0; y <= Resources.VIRTUAL_HEIGHT; y++) {
                Vector2 place = new Vector2(x, y);
                supplyDropPlacement.setLocation(place);
                int locAmount = 0;
                for (Tower tower : towers) {
                    boolean towerInRange = CollisionDetection.shapesIntersect(
                        supplyDropPlacement.getCurrentSupplyDropCrate().getRangeShape(),
                        tower.getBody());
                    if (tower.getHealthPercent() <= .5 && towerInRange) {
                        locAmount++;
                    }
                }

                if (locAmount > bestLocAmount) {
                    bestLocAmount = locAmount;
                    bestLoc =place;
                }
            }
        }

        if(bestLocAmount >= MIN_TOWERS_FOR_CRATE_DROP){
            return bestLoc;
        } else {
            return null;
        }
    }

    private boolean canPlaceLandmine(){

        return player.getMoney() >= LandMine.COST && landmineCooldownCounter <= 0;
    }

    private boolean shouldPlaceLandmine(){

        if( gameStage.getActorGroups().getLandmineGroup().getChildren().size >= MAX_LANDMINES){
            return false;
        }

        // Check Enemy distances
        Array<Enemy> enemies = gameStage.getActorGroups().getEnemyGroup().getCastedChildren();
        for(Enemy enemy : enemies){
            if(enemy.getLengthToEnd() < minEnemyDistanceForLandmine){
                if(enemy instanceof EnemyTank) {
                    return enemy.getHealthPercent() <= .25;
                }
                return true;
            }
        }

        return false;
    }

    private void placeLandmine(){
        SupportActorPlacement placementService = gameStage.getSupportActorPlacement();
        placementService.createSupportActor(LandMine.class);

        Array<LDVector2> path = gameStage.getMap().getPath();
        LDVector2 firstPoint = path.get(path.size - 1);
        LDVector2 secondPoint = path.get(path.size - 2);

        LDVector2 landMinePos = new LDVector2();
        int direction = TestUtil.lineDirection(firstPoint, secondPoint);
        if(direction == TestUtil.HORIZONTAL){
            landMinePos.x = (firstPoint.x + secondPoint.x)/2;
            landMinePos.y = firstPoint.y;
        } else {
            landMinePos.y = (firstPoint.y + secondPoint.y)/2;
            landMinePos.x = firstPoint.x;
        }
        System.out.println("Adding landmine at: " +landMinePos);
        placementService.moveSupportActor(landMinePos);
        currentWaveState.addSupportState(new LandMineState(supportActorPlacement.getCurrentSupportActor().getPositionCenter()));
        placementService.placeSupportActor();
    }

    public void setCurrentWaveState(WaveState currentWaveState) {

        this.currentWaveState = currentWaveState;
        // Also set it on dependencies
        airStrikeSimulationHelper.setCurrentWaveState(currentWaveState);
    }
}
