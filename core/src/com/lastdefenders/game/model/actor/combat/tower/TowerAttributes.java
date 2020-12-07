package com.lastdefenders.game.model.actor.combat.tower;

import com.lastdefenders.game.model.actor.combat.CombatActorAttributes;

/**
 * Created by Eric on 12/26/2019.
 */

public class TowerAttributes extends CombatActorAttributes {

    private int cost;

    public TowerAttributes(Builder builder) {

        super(builder);

        this.cost = builder.cost;
    }

    public int getCost() {

        return cost;
    }


    public static class Builder extends AttributeBuilder<Builder>{

        private int cost;

        public Builder setCost(int cost){
            this.cost = cost;
            return this;
        }

        public TowerAttributes build(){

            return new TowerAttributes(this);
        }

        @Override
        public Builder getBuilder() {

            return this;
        }
    }
}
