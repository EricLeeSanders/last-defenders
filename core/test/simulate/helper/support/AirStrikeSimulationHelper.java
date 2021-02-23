package simulate.helper.support;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.GameStage;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.support.AirStrike;
import com.lastdefenders.game.model.actor.support.SupportActorCooldown;
import com.lastdefenders.game.service.actorplacement.SupportActorPlacement;
import com.lastdefenders.game.service.validator.SupportActorValidator;
import com.lastdefenders.game.service.validator.ValidationResponseEnum;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.UtilPool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import simulate.state.WaveState;
import simulate.state.support.AirStrikeState;

public class AirStrikeSimulationHelper{

    private static final int MIN_NUM_OF_ENEMIES_FOR_AIR_STRIKE = 5;
    private static final int MIN_TOTAL_NUM_OF_ENEMIES_FOR_AIR_STRIKE = 15;
    private static final float MIN_DISTANCE_FOR_AIR_STRIKE_LOCATIONS = AirStrike.AIRSTRIKE_RADIUS * (3f/5f);
    private static final float ENEMY_DISTANCE_FOR_AIR_STRIKE_MOD = (2/3f);

    private final float minEnemyDistanceForAirStrike;

    private SupportActorCooldown cooldown = new SupportActorCooldown(AirStrike.COOLDOWN_TIME);
    private SupportActorValidator validator;

    private GameStage gameStage;
    private Player player;
    private SupportActorPlacement supportActorPlacement;

    private WaveState currentWaveState;

    public AirStrikeSimulationHelper(GameStage gameStage, Player player){
        minEnemyDistanceForAirStrike = gameStage.getMap().getPathDistance() * ENEMY_DISTANCE_FOR_AIR_STRIKE_MOD;

        this.gameStage = gameStage;
        this.player = player;
        this.supportActorPlacement = gameStage.getSupportActorPlacement();
        this.validator = new SupportActorValidator(AirStrike.COST, cooldown, player);

    }

    public void handleAirStrike(){

        if(validator.canCreateSupportActor() == ValidationResponseEnum.OK && shouldUseAirStrike()){
            List<Vector2> bestLocations = getBestLocationsForAirStrike();
            if(bestLocations.size() == 3){

                for(int i = 0; i < 3; i++){
                    supportActorPlacement.setPlacement(UtilPool.getVector2(bestLocations.get(i)));
                }
                supportActorPlacement.finish();
                player.spendMoney(AirStrike.COST);
                currentWaveState.addSupportState(new AirStrikeState(bestLocations));
                validator.beginCooldown();
            }
        }
    }

    private boolean shouldUseAirStrike(){

        Array<Enemy> enemies = gameStage.getActorGroups().getEnemyGroup().getCastedChildren();
        int numOfEnemies = 0;
        for(Enemy enemy : enemies){
            if(enemy.isActive() && enemy.getLengthToEnd() <= minEnemyDistanceForAirStrike){
                numOfEnemies++;
            }
        }

        return numOfEnemies >= MIN_NUM_OF_ENEMIES_FOR_AIR_STRIKE && enemies.size >= MIN_TOTAL_NUM_OF_ENEMIES_FOR_AIR_STRIKE;
    }

    private List<AirStrikeLocation> getLocationsForAirStrike(){
        Array<Enemy> enemies = gameStage.getActorGroups().getEnemyGroup().getCastedChildren();
        supportActorPlacement.createSupportActor(AirStrike.class);
        List<AirStrikeLocation> locations = new ArrayList<>();
        Circle airStrikePos = new Circle(0, 0, AirStrike.AIRSTRIKE_RADIUS);
        for(int x = 0; x <= Resources.VIRTUAL_WIDTH; x++) {
            for (int y = 0; y <= Resources.VIRTUAL_HEIGHT; y++) {
                airStrikePos.setPosition(x,y);
                int locAmount = 0;
                for (Enemy enemy : enemies) {
                    boolean enemyInRange = CollisionDetection.shapesIntersect(
                        airStrikePos, enemy.getBody());
                    if (enemyInRange) {
                        locAmount++;
                    }
                }

                locations.add(new AirStrikeLocation(new Vector2(x, y), locAmount));

            }
        }

        return locations;

    }

    private List<Vector2> getBestLocationsForAirStrike( ){
        List<Vector2> bestLocations = new ArrayList<>();

        List<AirStrikeLocation>  locations = getLocationsForAirStrike();
        Collections.sort(locations);
        for(int i = 0; i < locations.size(); i++){
            Vector2 location = locations.get(i).location;
            boolean isLocationValid = true;
            for(Vector2 bestLocation: bestLocations){
                if(areLocationsTooClose(location, bestLocation)){
                    isLocationValid = false;
                    break;
                }
            }

            if(isLocationValid){
                bestLocations.add(location);
            }

            if(bestLocations.size() == 3){
                break;
            }
        }

        return bestLocations;
    }

    private boolean areLocationsTooClose(Vector2 loc1, Vector2 loc2){
        return loc1.dst(loc2) < MIN_DISTANCE_FOR_AIR_STRIKE_LOCATIONS;
    }

    void setCurrentWaveState(WaveState currentWaveState){
        this.currentWaveState = currentWaveState;
    }


    private class AirStrikeLocation implements Comparable<AirStrikeLocation>{
        private Vector2 location;
        private int numOfEnemies;

        private AirStrikeLocation(Vector2 location, int numOfEnemies) {

            this.location = location;
            this.numOfEnemies = numOfEnemies;
        }

        public int getNumOfEnemies(){
            return numOfEnemies;
        }


        @Override
        public int compareTo(AirStrikeLocation o) {

            return o.numOfEnemies - this.numOfEnemies;
        }

        @Override
        public String toString(){
            return "Location: " + location + "; Enemies: " + numOfEnemies;
        }
    }

}
