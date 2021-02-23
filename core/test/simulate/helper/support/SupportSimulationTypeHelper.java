package simulate.helper.support;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.GameStage;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyTank;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.support.AirStrike;
import com.lastdefenders.game.model.actor.support.Apache;
import com.lastdefenders.game.model.actor.support.LandMine;
import com.lastdefenders.game.model.actor.support.SupportActor;
import com.lastdefenders.game.model.actor.support.SupportActorCooldown;
import com.lastdefenders.game.model.actor.support.supplydrop.SupplyDrop;
import com.lastdefenders.game.service.actorplacement.SupportActorPlacement;
import com.lastdefenders.game.service.validator.SupportActorValidator;
import com.lastdefenders.game.service.validator.ValidationResponseEnum;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import java.util.HashMap;
import java.util.Map;
import simulate.state.WaveState;
import simulate.state.support.ApacheState;
import simulate.state.support.LandMineState;
import simulate.state.support.SupplyDropState;
import testutil.TestUtil;

public class SupportSimulationTypeHelper {

    private GameStage gameStage;
    private Player player;
    private SupportActorPlacement supportActorPlacement;

    private static final int MAX_LANDMINES = 1;
    // How close an enemy must be to the end of the path to place a mine
    private static final float ENEMY_DISTANCE_FOR_LANDMINE_MOD = (1/6f);

    private static final int MIN_TOWERS_FOR_CRATE_DROP = 7;

    private static final int MIN_NUM_OF_ENEMIES_FOR_APACHE = 5;
    private static final float ENEMY_DISTANCE_FOR_APACHE_MOD = (2/3f);

    private Map<Class<? extends SupportActor>, SupportActorValidator> supportActorValidatorMap = new HashMap<>();


    private final float minEnemyDistanceForLandmine;
    private final float minEnemyDistanceForApache;

    private AirStrikeSimulationHelper airStrikeSimulationHelper;


    private WaveState currentWaveState;

    public SupportSimulationTypeHelper(GameStage gameStage, Player player) {

        this.gameStage = gameStage;
        this.player = player;
        this.supportActorPlacement = gameStage.getSupportActorPlacement();

        minEnemyDistanceForLandmine = gameStage.getMap().getPathDistance() * ENEMY_DISTANCE_FOR_LANDMINE_MOD;
        minEnemyDistanceForApache = gameStage.getMap().getPathDistance() * ENEMY_DISTANCE_FOR_APACHE_MOD;

        airStrikeSimulationHelper = new AirStrikeSimulationHelper(gameStage, player);

        initSupportActorValidators();
    }

    private void initSupportActorValidators(){

        SupportActorCooldown apacheCooldown = createSupportActorCooldown(Apache.COOLDOWN_TIME);
        SupportActorCooldown airStrikeCooldown = createSupportActorCooldown(AirStrike.COOLDOWN_TIME);
        SupportActorCooldown landMineCooldown = createSupportActorCooldown(LandMine.COOLDOWN_TIME);
        SupportActorCooldown supplyDropCooldown = createSupportActorCooldown(SupplyDrop.COOLDOWN_TIME);

        supportActorValidatorMap.put(Apache.class, createSupportActorValidator(Apache.COST, apacheCooldown));
        supportActorValidatorMap.put(
            AirStrike.class, createSupportActorValidator(AirStrike.COST, airStrikeCooldown));
        supportActorValidatorMap.put(LandMine.class, createSupportActorValidator(LandMine.COST, landMineCooldown));
        supportActorValidatorMap.put(SupplyDrop.class, createSupportActorValidator(SupplyDrop.COST, supplyDropCooldown));

        this.gameStage.getActorGroups().getCooldownGroup().addActor(apacheCooldown);
        this.gameStage.getActorGroups().getCooldownGroup().addActor(airStrikeCooldown);
        this.gameStage.getActorGroups().getCooldownGroup().addActor(landMineCooldown);
        this.gameStage.getActorGroups().getCooldownGroup().addActor(supplyDropCooldown);
    }

    private SupportActorCooldown createSupportActorCooldown(float cooldownTime){
        SupportActorCooldown cooldown = new SupportActorCooldown(cooldownTime);

        return cooldown;
    }

    private SupportActorValidator createSupportActorValidator(int cost, SupportActorCooldown cooldown){

        SupportActorValidator validator = new SupportActorValidator(cost, cooldown, player);

        return validator;
    }

    public void handleSupportLandmine(){

        SupportActorValidator validator = supportActorValidatorMap.get(LandMine.class);

        if (validator.canCreateSupportActor() == ValidationResponseEnum.OK && shouldPlaceLandmine()) {
            placeLandmine();
            player.spendMoney(LandMine.COST);
            validator.beginCooldown();
        }
    }

    public void handleSupportSupplyDrop(){

        SupportActorValidator validator = supportActorValidatorMap.get(SupplyDrop.class);

        if (validator.canCreateSupportActor() == ValidationResponseEnum.OK && shouldCheckBestSupplyDropLocation()){
            Vector2 supplyDropLoc = getBestSupplyDropLocation();
            if(supplyDropLoc != null){
                supportActorPlacement.setPlacement(new LDVector2(supplyDropLoc));
                currentWaveState.addSupportState(new SupplyDropState(new Vector2(supplyDropLoc)));
                supportActorPlacement.finish();
                player.spendMoney(SupplyDrop.COST);
                validator.beginCooldown();
            }
        }
    }

    public void handleSupportApache(){

        SupportActorValidator validator = supportActorValidatorMap.get(Apache.class);

        if (validator.canCreateSupportActor() == ValidationResponseEnum.OK && shouldUseApache()){
            Vector2 apacheLoc = getBestApacheLocation();

            if(apacheLoc != null){
                supportActorPlacement.setPlacement(new LDVector2(apacheLoc));
                currentWaveState.addSupportState(new ApacheState(new Vector2(apacheLoc)));
                supportActorPlacement.finish();
                player.spendMoney(Apache.COST);
                validator.beginCooldown();
            }
        }
    }

    public void handleSupport() {
        handleSupportSupplyDrop();
        handleSupportApache();
        handleSupportLandmine();
        handleAirStrike();

    }

    public void handleAirStrike(){
        airStrikeSimulationHelper.handleAirStrike();
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
        supportActorPlacement.setPlacement(new LDVector2(bestLoc));

        return bestLoc;
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
        supportActorPlacement.createSupportActor(SupplyDrop.class);
        Vector2 bestLoc = null;
        int bestLocAmount = 0;
        for(int x = 0; x <= Resources.VIRTUAL_WIDTH; x++) {
            for (int y = 0; y <= Resources.VIRTUAL_HEIGHT; y++) {
                Vector2 place = new Vector2(x, y);
                supportActorPlacement.setPlacement(new LDVector2(place));
                int locAmount = 0;
                for (Tower tower : towers) {
                    boolean towerInRange = CollisionDetection.shapesIntersect(
                        supportActorPlacement.getCurrentSupportActor().getRangeShape(),
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

        return bestLoc;
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

        placementService.setPlacement(landMinePos);
        currentWaveState.addSupportState(new LandMineState(new Vector2(supportActorPlacement.getCurrentSupportActor().getPositionCenter())));
        placementService.finish();
    }

    public void setCurrentWaveState(WaveState currentWaveState) {

        this.currentWaveState = currentWaveState;
        // Also set it on dependencies
        airStrikeSimulationHelper.setCurrentWaveState(currentWaveState);
    }
}
