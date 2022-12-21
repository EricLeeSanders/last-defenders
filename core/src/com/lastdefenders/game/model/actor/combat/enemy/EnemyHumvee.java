package com.lastdefenders.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.groups.GenericGroup;
import com.lastdefenders.game.model.actor.interfaces.IVehicle;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.game.service.factory.CombatActorFactory.EnemyPool;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.sound.LDAudio;
import com.lastdefenders.sound.LDAudio.LDSound;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.UtilPool;

/**
 * Represents an Enemy Jeep.
 *
 * @author Eric
 */
public class EnemyHumvee extends EnemyTurret implements IVehicle {

    private static final Dimension BULLET_SIZE = new Dimension(5, 5);
    private static final Vector2 GUN_POS = UtilPool.getVector2(29, 0);
    private static final Dimension TEXTURE_SIZE_BODY = new Dimension(75, 40);
    private static final Dimension TEXTURE_SIZE_TURRET = new Dimension(75, 23);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.VEHCILE_EXPLOSION;
    private static final float[] BODY_POINTS = {0, 0, 0, 40, 75, 40, 75, 0};

    private LDAudio audio;
    private ProjectileFactory projectileFactory;

    public EnemyHumvee(TextureRegion bodyRegion, TextureRegion turretRegion,
        TextureRegion[] animatedRegions, EnemyPool<EnemyHumvee> pool, GenericGroup<Tower> targetGroup,
        ProjectileFactory projectileFactory, LDAudio audio, EnemyAttributes attributes) {

        super(turretRegion, animatedRegions, TEXTURE_SIZE_TURRET, pool, targetGroup, GUN_POS,
            DEATH_EFFECT_TYPE, TEXTURE_SIZE_BODY, bodyRegion, BODY_POINTS,attributes);
        this.projectileFactory = projectileFactory;
        this.audio = audio;
    }


    @Override
    public void attackTarget(Targetable target) {

        if (target != null) {
            audio.playSound(LDSound.ROCKET_LAUNCH);
            projectileFactory.loadProjectile(Bullet.class).initialize(this, target, BULLET_SIZE);
        }
    }

}
