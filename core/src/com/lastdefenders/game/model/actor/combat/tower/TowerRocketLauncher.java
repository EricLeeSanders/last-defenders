package com.lastdefenders.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.interfaces.IRocket;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.service.factory.CombatActorFactory.CombatActorPool;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.UtilPool;

/**
 * Represents a Tower RocketLauncher
 *
 * @author Eric
 */
public class TowerRocketLauncher extends Tower implements IRocket {

    public static final int COST = 800;
    private static final float HEALTH = 8;
    private static final float ARMOR = 4;
    private static final float ATTACK = 10;
    private static final float ATTACK_SPEED = 1f;
    private static final float RANGE = 60;
    private static final float AOE_RADIUS = 50f;
    private static final int ARMOR_COST = 500;
    private static final int RANGE_INCREASE_COST = 450;
    private static final int SPEED_INCREASE_COST = 450;
    private static final int ATTACK_INCREASE_COST = 450;

    private static final Dimension ROCKET_SIZE = new Dimension(23, 6);
    private static final Vector2 GUN_POS = UtilPool.getVector2(25, -10);
    private static final Dimension TEXTURE_SIZE = new Dimension(56, 31);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.BLOOD;

    private Circle body;
    private LDAudio audio;
    private ProjectileFactory projectileFactory;

    public TowerRocketLauncher(TextureRegion actorRegion, CombatActorPool<TowerRocketLauncher> pool,
        Group targetGroup, TextureRegion rangeRegion, TextureRegion collidingRangeRegion,
        ProjectileFactory projectileFactory, LDAudio audio) {

        super(actorRegion, TEXTURE_SIZE, pool, targetGroup, GUN_POS, rangeRegion,
            collidingRangeRegion, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST,
            RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST, DEATH_EFFECT_TYPE);
        this.audio = audio;
        this.projectileFactory = projectileFactory;
        this.body = new Circle(this.getPositionCenter(), 10);
    }

    @Override
    public void attackTarget(Targetable target) {

        if (target != null) {
            audio.playSound(LDSound.ROCKET_LAUNCH);
            projectileFactory.loadRocket()
                .initialize(this, target.getPositionCenter(), ROCKET_SIZE, AOE_RADIUS);
        }
    }

    @Override
    public String getName() {

        return "Rocket Launcher";
    }

    @Override
    public Circle getBody() {

        body.setPosition(getPositionCenter().x, getPositionCenter().y);
        return body;
    }
}
