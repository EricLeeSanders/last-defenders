package simulate.helper;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.game.GameStage;
import com.lastdefenders.game.helper.CollisionDetection;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.TowerHumvee;
import com.lastdefenders.game.model.actor.combat.tower.TowerMachineGun;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.TowerRocketLauncher;
import com.lastdefenders.game.model.actor.combat.tower.TowerSniper;
import com.lastdefenders.game.model.actor.combat.tower.TowerTank;
import com.lastdefenders.game.model.actor.groups.ActorGroups;
import com.lastdefenders.game.model.actor.interfaces.IRotatable;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.service.actorplacement.TowerPlacement;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import simulate.SimulationMathUtil;
import simulate.positionweights.TowerPositionWeight;
import testutil.TestUtil;

public class SimulationTowerHelper {

    private GameStage gameStage;
    private ActorGroups actorGroups;
    private Player player;

    private HashMap<String, Integer> towerTypes;

    public SimulationTowerHelper(GameStage gameStage, Resources resources, ActorGroups actorGroups,
        Player player){

        this.gameStage = gameStage;
        this.actorGroups = actorGroups;
        this.player = player;

        towerTypes = new HashMap<>();

        towerTypes.put("Rifle", resources.getTowerAttribute(TowerRifle.class).getCost());
        towerTypes.put("MachineGun", resources.getTowerAttribute(TowerMachineGun.class).getCost());
        towerTypes.put("Sniper", resources.getTowerAttribute(TowerSniper.class).getCost());
        towerTypes.put("FlameThrower", resources.getTowerAttribute(TowerFlameThrower.class).getCost());
        towerTypes.put("RocketLauncher", resources.getTowerAttribute(TowerRocketLauncher.class).getCost());
        towerTypes.put("Tank", resources.getTowerAttribute(TowerTank.class).getCost());
        towerTypes.put("Humvee", resources.getTowerAttribute(TowerHumvee.class).getCost());
    }

    private List<TowerPositionWeight> findStrongestPoint(Tower tower){
        Array<LDVector2> waypoints = gameStage.getMap().getPath();
        List<TowerPositionWeight> positionWeights = new ArrayList<>();
        Vector2 displacement = new Vector2();

        for(int x = 0; x <= Resources.VIRTUAL_WIDTH; x++) {
            for (int y = 0; y <= Resources.VIRTUAL_HEIGHT; y++) {
                Vector2 center = new Vector2(x, y);
                tower.setPositionCenter(center);
                if(invalidPlacement(gameStage.getMap(), tower)){
                    continue;
                }
                Array<Vector2> intersections = new Array<>();
                int depth =Integer.MAX_VALUE;
                float locationLength = 0;
                for (int i = 0; i < waypoints.size - 1; i++) {

                    Vector2 start = waypoints.get(i);
                    Vector2 end = waypoints.get(i + 1);

                    float d = SimulationMathUtil
                        .intersectSegmentCircleDisplace(start, end, center, tower.getRangeShape().radius,
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
                    List<Vector2> points = SimulationMathUtil.getCircleLineIntersectionPoint(start, end, center, tower.getRangeShape().radius);

                    if(points.size() > 0 ) {
                        Vector2 firstPoint = points.get(0);

                        if (SimulationMathUtil.inLine(start, end, firstPoint)) {
                            intersections.add(firstPoint);
                        }

                    }

                    if(points.size() > 1 ) {
                        Vector2 secondPoint = points.get(1);

                        if (SimulationMathUtil.inLine(start, end, secondPoint)) {
                            intersections.add(secondPoint);
                        }
                    }
                }
                positionWeights.add(new TowerPositionWeight(x, y, intersections.size,
                    actorGroups.getTowerGroup().getChildren(), locationLength, depth, tower));
            }
        }

        Collections.sort(positionWeights);
        Collections.reverse(positionWeights);

        return positionWeights;

    }

    private boolean invalidPlacement(Map map, Tower tower){
        return CollisionDetection.collisionWithPath(map.getPathBoundaries(), tower) ||
            CollisionDetection.outOfMapBoundary(Resources.VIRTUAL_WIDTH, Resources.VIRTUAL_HEIGHT, tower);
    }

    public void addTowers(){

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

    private void placeTower(List<TowerPositionWeight> positionWeights){
        for(TowerPositionWeight position : positionWeights){
            gameStage.getTowerPlacement().moveTower(new LDVector2(position.getX(), position.getY()));

            if(gameStage.getTowerPlacement().placeTower()){
                return;
            }
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
}
