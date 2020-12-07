package com.lastdefenders.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.groups.GenericGroup;
import com.lastdefenders.game.model.actor.interfaces.IRotatable;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.game.service.factory.CombatActorFactory.TowerPool;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.UtilPool;

/**
 * Represents a Tower Turret. Turret is different in that its shape is a
 * triangle instead of a circle
 *
 * @author Eric
 */
public class TowerHumvee extends TowerTurret implements IRotatable {

    private static final Dimension BULLET_SIZE = new Dimension(5, 5);
    private static final Vector2 GUN_POS = UtilPool.getVector2(29, 0);
    private static final Dimension TEXTURE_SIZE_BODY = new Dimension(75, 40);
    private static final Dimension TEXTURE_SIZE_TURRET = new Dimension(75, 23);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.VEHCILE_EXPLOSION;
    private static final float[] BODY_POINTS = {0, 0, 0, 40, 75, 40, 75, 0};

    private LDAudio audio;
    private ProjectileFactory projectileFactory;

    public TowerHumvee(TextureRegion bodyRegion, TextureRegion turretRegion,
        TowerPool<TowerHumvee> pool, GenericGroup<Enemy> targetGroup, TextureRegion rangeRegion,
        TextureRegion collidingRangeRegion, ProjectileFactory projectileFactory, LDAudio audio,
        TowerAttributes attributes) {

        super(turretRegion, TEXTURE_SIZE_TURRET, pool, targetGroup, GUN_POS, rangeRegion,
            collidingRangeRegion, DEATH_EFFECT_TYPE, TEXTURE_SIZE_BODY, bodyRegion, BODY_POINTS, attributes);

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
