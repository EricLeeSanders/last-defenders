package simulate.helper;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import simulate.SimulationRunType;
import simulate.UpgradeTypes;
import testutil.TestUtil;

/**
 * Created by Eric on 1/20/2020.
 */

public class UpgradeSimulationTypeHelper {

    private static final float UPGRADE_BUDGET_RATIO = .25f;


    public static void upgradeTowers(SimulationRunType simulationRunType, Player player, SnapshotArray<Actor> towers){

        float upgradeBudget = player.getMoney() * UPGRADE_BUDGET_RATIO;

        TreeSet<Tower> towersOrderedSet = new TreeSet<>(new Comparator<Tower>() {
            @Override
            public int compare(Tower o1, Tower o2) {

                if(o1.getCost() > o2.getCost()){
                    return 1;
                } else if(o1.getCost() < o2.getCost()){
                    return -1;
                } else {
                    return o1.getNumOfKills() - o2.getNumOfKills();
                }
            }
        });

        for(Actor actor : towers){
            towersOrderedSet.add((Tower) actor);
        }
        List<Tower> towersOrdered = new ArrayList<>(towersOrderedSet);
        Collections.reverse(towersOrdered);

        // Apply upgrade
        float remainingMoney = upgradeBudget;
        for(Tower tower : towersOrdered){

            List<UpgradeTypes> availableUpgrades = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                if(!tower.hasArmor() && (tower.getArmorCost() <= remainingMoney)
                    && (simulationRunType.equals(SimulationRunType.UPGRADE_ARMOR) || simulationRunType.equals(
                    SimulationRunType.UPGRADES_ALL))){
                    availableUpgrades.add(UpgradeTypes.ARMOR);
                }
                if(!tower.hasIncreasedAttack() && (tower.getAttackIncreaseCost() <= remainingMoney)
                    && (simulationRunType.equals(SimulationRunType.UPGRADE_ATTACK) || simulationRunType.equals(
                    SimulationRunType.UPGRADES_ALL))){
                    availableUpgrades.add(UpgradeTypes.ATTACK);
                }
                if(!tower.hasIncreasedSpeed() && (tower.getSpeedIncreaseCost() <= remainingMoney)
                     && (simulationRunType.equals(SimulationRunType.UPGRADE_ATTACK_SPEED) || simulationRunType.equals(
                    SimulationRunType.UPGRADES_ALL))){
                    availableUpgrades.add(UpgradeTypes.ATTACK_SPEED);
                }
                if(!tower.hasIncreasedRange() && (tower.getRangeIncreaseCost() <= remainingMoney)
                    && (simulationRunType.equals(SimulationRunType.UPGRADE_RANGE) || simulationRunType.equals(
                    SimulationRunType.UPGRADES_ALL))){
                    availableUpgrades.add(UpgradeTypes.RANGE);
                }
            }

            if(availableUpgrades.size() <= 0){
                continue;
            }
            int rndUpgradeIdx = TestUtil.getRandomNumberInRange(0, availableUpgrades.size() - 1);
            UpgradeTypes rndUpgrade = availableUpgrades.get(rndUpgradeIdx);
            if(rndUpgrade.equals(UpgradeTypes.ARMOR)){
                // Armor
                tower.setHasArmor(true);
                remainingMoney -= tower.getArmorCost();
                player.spendMoney(tower.getArmorCost());
            } else if(rndUpgrade.equals(UpgradeTypes.ATTACK)){
                // Attack
                tower.increaseAttack();
                remainingMoney -= tower.getAttackIncreaseCost();
                player.spendMoney(tower.getAttackIncreaseCost());
            }  else if(rndUpgrade.equals(UpgradeTypes.ATTACK_SPEED)){
                // Attack Speed
                tower.increaseSpeed();
                remainingMoney -= tower.getSpeedIncreaseCost();
                player.spendMoney(tower.getSpeedIncreaseCost());
            }  else if(rndUpgrade.equals(UpgradeTypes.RANGE)) {
                // Range
                tower.increaseRange();
                remainingMoney -= tower.getRangeIncreaseCost();
                player.spendMoney(tower.getRangeIncreaseCost());
            }

        }


    }

}
