package com.lastdefenders.game.model.level.wave.impl;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Logger;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Created by Eric on 5/25/2017.
 */

public class DynamicWaveLoader extends AbstractWaveLoader {

    private static final float ENEMY_STARTING_WEIGHT = 575;
    private static final float ENEMY_WEIGHT_MODIFIER = 0.35f;
    private static final float ENEMY_WEIGHT_MODIFIER_2 = 0.2f;
    private static final int WAVES_PER_TANK = 10;

    private Array<EnemyWeight> enemies = new Array<>();
    private float enemyWeight = ENEMY_STARTING_WEIGHT;

    public DynamicWaveLoader(CombatActorFactory combatActorFactory, Map map) {

        super(combatActorFactory, map);

        EnemyWeight rifle = new EnemyWeight("Rifle",4, false);
        EnemyWeight rifleArmor = new EnemyWeight("Rifle",6, true);
        EnemyWeight machineGun = new EnemyWeight("MachineGun",4, false);
        EnemyWeight machineGunArmor = new EnemyWeight("MachineGun",6, true);
        EnemyWeight sniper = new EnemyWeight("Sniper",7, false);
        EnemyWeight sniperArmor = new EnemyWeight("Sniper",10, true);
        EnemyWeight flameThrower = new EnemyWeight("FlameThrower",9, false);
        EnemyWeight flameThrowerArmor = new EnemyWeight("FlameThrower",13, true);
        EnemyWeight rocketLauncher = new EnemyWeight("RocketLauncher",11, false);
        EnemyWeight rocketLauncherArmor = new EnemyWeight("RocketLauncher",16, true);
        EnemyWeight humvee = new EnemyWeight("Humvee",13, false);
        EnemyWeight humveeArmor = new EnemyWeight("Humvee",19, true);
        EnemyWeight tank = new EnemyWeight("Tank",50, false);
        EnemyWeight tankArmor = new EnemyWeight("Tank",75, true);

        enemies.add(rifle);
        enemies.add(rifleArmor);
        enemies.add(machineGun);
        enemies.add(machineGunArmor);
        enemies.add(sniper);
        enemies.add(sniperArmor);
        enemies.add(flameThrower);
        enemies.add(flameThrowerArmor);
        enemies.add(rocketLauncher);
        enemies.add(rocketLauncherArmor);
        enemies.add(humvee);
        enemies.add(humveeArmor);
        enemies.add(tank);
        enemies.add(tankArmor);
    }

    @Override
    public Queue<SpawningEnemy> loadWave(LevelName levelName, int wave) {

        Logger.info("DynamicWaveGenerator: Generating Wave: " + wave + "; weight: " + enemyWeight);

        Array<SpawningEnemy> enemies = getEnemies(enemyWeight, wave);

        enemies.shuffle();

        Queue<SpawningEnemy> enemyQueue = new Queue<>();
        for(SpawningEnemy e : enemies){
            enemyQueue.addFirst(e);
        }

        enemyWeight += enemyWeight * (ENEMY_WEIGHT_MODIFIER/(wave*ENEMY_WEIGHT_MODIFIER_2));
        return enemyQueue;
    }


    private Array<SpawningEnemy> getEnemies(float weight, int waveNum){
        Array<SpawningEnemy> wave = new Array<>();

        float remainingWeight = weight;
        int tankCount = 0;
        int numOfTanksAllowed = waveNum / WAVES_PER_TANK;

        while(remainingWeight >= 4){
            int randomIdx = MathUtils.random(enemies.size - 1);
            EnemyWeight rndEnemy = enemies.get(randomIdx);

            if(rndEnemy.weight <= remainingWeight){

                if(rndEnemy.name.equals("Tank")){
                    tankCount++;
                }

                if(rndEnemy.name.equals("Tank") && tankCount > numOfTanksAllowed){
                    continue;
                }

                remainingWeight -= rndEnemy.weight;
                float delay = MathUtils.random(1,10)/10f;

                SpawningEnemy enemy = loadSpawningEnemy(rndEnemy.name, rndEnemy.armor, delay);

                wave.add(enemy);
            }
        }

        return wave;
    }

    private static class EnemyWeight {
        int weight;
        boolean armor;
        String name;

        EnemyWeight(String name, int weight, boolean armor) {

            this.weight = weight;
            this.armor = armor;
            this.name = name;
        }

    }
}

