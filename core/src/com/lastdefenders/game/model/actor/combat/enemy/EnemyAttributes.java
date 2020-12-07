package com.lastdefenders.game.model.actor.combat.enemy;

import com.lastdefenders.game.model.actor.combat.CombatActorAttributes;
import com.lastdefenders.game.model.actor.combat.tower.TowerAttributes;

/**
 * Created by Eric on 12/26/2019.
 */

public class EnemyAttributes extends CombatActorAttributes {

    private float speed;
    private int killReward;

    public EnemyAttributes(Builder builder) {

        super(builder);

        this.speed = builder.speed;
        this.killReward = builder.killReward;
    }

    public float getSpeed() {

        return speed;
    }

    public int getKillReward(){
        return killReward;
    }

    public static class Builder extends AttributeBuilder<Builder>{

        private float speed;
        private int killReward;

        public Builder setSpeed(float speed){
            this.speed = speed;
            return this;
        }

        public Builder setKillReward(int killReward){
            this.killReward = killReward;
            return this;
        }

        public EnemyAttributes build(){

            return new EnemyAttributes(this);
        }

        @Override
        public Builder getBuilder() {

            return this;
        }
    }
}
