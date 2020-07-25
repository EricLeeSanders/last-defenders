package com.lastdefenders.game.model.level.wave.impl;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.level.Level;
import com.lastdefenders.game.model.level.Map;
import com.lastdefenders.game.model.level.SpawningEnemy;
import com.lastdefenders.game.service.factory.CombatActorFactory;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Logger;
import com.lastdefenders.util.Resources;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Created by Eric on 5/25/2017.
 */

public class DynamicWaveLoader extends AbstractWaveLoader {

    private Array<EnemyWeight> enemyWeights;
    private WaveGeneratorMetadata metadata;
    private float enemyWeight;

    public DynamicWaveLoader(CombatActorFactory combatActorFactory, Map map, Resources resources,
        LevelName levelName) {

        super(combatActorFactory, map);

        enemyWeights = resources.getEnemyWeights();
        metadata = resources.getWaveGeneratorMetadataByLevelName(levelName);
        enemyWeight = metadata.dynamicEnemyStartingWeight;
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

        enemyWeight += enemyWeight * (metadata.enemyWeightModifier/(wave*metadata.enemyWeightModifier2));
        return enemyQueue;
    }


    private Array<SpawningEnemy> getEnemies(float weight, int waveNum){
        Array<SpawningEnemy> wave = new Array<>();

        float remainingWeight = weight;
        int tankCount = 0;
        int numOfTanksAllowed = waveNum / metadata.wavesPerTank;

        while(remainingWeight >= 4){
            int randomIdx = MathUtils.random(enemyWeights.size - 1);
            EnemyWeight rndEnemy = enemyWeights.get(randomIdx);

            if(rndEnemy.weight <= remainingWeight){

                if(rndEnemy.name.equals("Tank")){
                    tankCount++;
                }

                if(rndEnemy.name.equals("Tank") && tankCount > numOfTanksAllowed){
                    continue;
                }

                remainingWeight -= rndEnemy.weight;
                float delay = MathUtils.random(100,1000)/1000f;

                SpawningEnemy enemy = loadSpawningEnemy(rndEnemy.name, rndEnemy.armor, delay);

                wave.add(enemy);
            }
        }

        return wave;
    }


    public static class WaveGeneratorMetadata {
        private LevelName levelName;
        private float dynamicEnemyStartingWeight;
        private float enemyWeightModifier;
        private float enemyWeightModifier2;
        private int wavesPerTank;

        public LevelName getLevelName() {

            return levelName;
        }

        public void setLevelName(LevelName levelName) {

            this.levelName = levelName;
        }


        public float getDynamicEnemyStartingWeight() {

            return dynamicEnemyStartingWeight;
        }

        public void setDynamicEnemyStartingWeight(float dynamicEnemyStartingWeight) {

            this.dynamicEnemyStartingWeight = dynamicEnemyStartingWeight;
        }

        public float getEnemyWeightModifier() {

            return enemyWeightModifier;
        }

        public void setEnemyWeightModifier(float enemyWeightModifier) {

            this.enemyWeightModifier = enemyWeightModifier;
        }

        public float getEnemyWeightModifier2() {

            return enemyWeightModifier2;
        }

        public void setEnemyWeightModifier2(float enemyWeightModifier2) {

            this.enemyWeightModifier2 = enemyWeightModifier2;
        }

        public int getWavesPerTank() {

            return wavesPerTank;
        }

        public void setWavesPerTank(int wavesPerTank) {

            this.wavesPerTank = wavesPerTank;
        }
    }

    public static class EnemyWeight {
        private int weight;
        private boolean armor;
        private String name;


        public int getWeight() {

            return weight;
        }

        public void setWeight(int weight) {

            this.weight = weight;
        }

        public boolean isArmor() {

            return armor;
        }

        public void setArmor(boolean armor) {

            this.armor = armor;
        }

        public String getName() {

            return name;
        }

        public void setName(String name) {

            this.name = name;
        }
    }
}

