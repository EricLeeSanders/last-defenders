package com.lastdefenders.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.interfaces.IRotatable;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.game.service.factory.CombatActorFactory.CombatActorPool;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.DebugOptions;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.UtilPool;

/**
 * Represents a Tower Turret. Turret is different in that its shape is a
 * triangle instead of a circle
 *
 * @author Eric
 */
public class TowerHumvee extends TowerTurret implements IRotatable {

    public static final int COST = 1300;
    private static final float HEALTH = 14;
    private static final float ARMOR = 8;
    private static final float ATTACK = 3;
    private static final float ATTACK_SPEED = .2f;
    private static final float RANGE = 70;
    private static final int ARMOR_COST = 900;
    private static final int RANGE_INCREASE_COST = 500;
    private static final int SPEED_INCREASE_COST = 500;
    private static final int ATTACK_INCREASE_COST = 500;

    private static final Dimension BULLET_SIZE = new Dimension(5, 5);
    private static final Vector2 GUN_POS = UtilPool.getVector2(29, 0);
    private static final Dimension TEXTURE_SIZE_BODY = new Dimension(75, 40);
    private static final Dimension TEXTURE_SIZE_TURRET = new Dimension(75, 23);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.VEHCILE_EXPLOSION;
    private static final float[] BODY_POINTS = {0, 0, 0, 40, 75, 40, 75, 0};

    private LDAudio audio;
    private ProjectileFactory projectileFactory;

    public TowerHumvee(TextureRegion bodyRegion, TextureRegion turretRegion,
        CombatActorPool<TowerHumvee> pool, Group targetGroup, TextureRegion rangeRegion,
        TextureRegion collidingRangeRegion, ProjectileFactory projectileFactory, LDAudio audio) {

        super(turretRegion, TEXTURE_SIZE_TURRET, pool, targetGroup, GUN_POS, rangeRegion,
            collidingRangeRegion, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, COST, ARMOR_COST,
            RANGE_INCREASE_COST, SPEED_INCREASE_COST, ATTACK_INCREASE_COST, DEATH_EFFECT_TYPE,
            TEXTURE_SIZE_BODY, bodyRegion, BODY_POINTS);

        this.audio = audio;
        this.projectileFactory = projectileFactory;
    }


    @Override
    public void attackTarget(Targetable target) {

        if (target != null) {
            audio.playSound(LDSound.MACHINE_GUN);
            projectileFactory.loadProjectile(Bullet.class).initialize(this, target, BULLET_SIZE);
        }
    }

    @Override
    public String getName() {

        return "Humvee";
    }
}