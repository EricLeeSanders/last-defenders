package com.lastdefenders.game.model.actor.combat;

/**
 * Created by Eric on 12/26/2019.
 */

public class CombatActorAttributes {
    private float health;
    private float attack;
    private float attackSpeed;
    private float range;
    private float armor;

    public CombatActorAttributes(AttributeBuilder builder) {

        this.health = builder.health;
        this.attack = builder.attack;
        this.attackSpeed = builder.attackSpeed;
        this.range = builder.range;
        this.armor = builder.armor;
    }

    public float getHealth() {

        return health;
    }

    public float getAttack() {

        return attack;
    }

    public float getAttackSpeed() {

        return attackSpeed;
    }

    public float getRange() {

        return range;
    }

    public float getArmor() {
        return armor;
    }


    @Override
    public String toString() {

        return "CombatActorAttributes{" +
            "health=" + health +
            ", attack=" + attack +
            ", attackSpeed=" + attackSpeed +
            ", range=" + range +
            ", armor=" + armor +
            '}';
    }

    public static abstract class AttributeBuilder <T extends AttributeBuilder<T>>{

        private float health;
        private float attack;
        private float attackSpeed;
        private float range;
        private float armor;

        public abstract T getBuilder();

        public T setHealth(float health) {

            this.health = health;
            return getBuilder();
        }

        public T setAttack(float attack) {

            this.attack = attack;
            return getBuilder();
        }

        public T setAttackSpeed(float attackSpeed) {

            this.attackSpeed = attackSpeed;
            return getBuilder();
        }

        public T setRange(float range) {

            this.range = range;
            return getBuilder();
        }

        public T setArmor(float armor) {

            this.armor = armor;
            return getBuilder();
        }
    }
}
