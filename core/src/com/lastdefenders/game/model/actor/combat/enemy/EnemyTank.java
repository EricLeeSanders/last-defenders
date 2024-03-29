package com.lastdefenders.game.model.actor.combat.enemy;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.groups.GenericGroup;
import com.lastdefenders.game.model.actor.health.interfaces.PlatedArmor;
import com.lastdefenders.game.model.actor.interfaces.IRocket;
import com.lastdefenders.game.model.actor.interfaces.IVehicle;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.model.actor.projectile.Rocket;
import com.lastdefenders.game.service.factory.CombatActorFactory.EnemyPool;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.sound.LDSound;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.UtilPool;

/**
 * Represents an Enemy Tank.
 *
 * @author Eric
 */
public class EnemyTank extends EnemyTurret implements PlatedArmor, IVehicle, IRocket {

    private static final float AOE_RADIUS = 40f;

    private static final Dimension ROCKET_SIZE = new Dimension(23, 6);
    private static final Vector2 GUN_POS = UtilPool.getVector2(57, 0);
    private static final Dimension TEXTURE_SIZE_BODY = new Dimension(75, 57);
    private static final Dimension TEXTURE_SIZE_TURRET = new Dimension(138, 36);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.VEHCILE_EXPLOSION;
    private static final float[] BODY_POINTS = {0, 0, 0, 56, 75, 56, 75, 0};

    private ProjectileFactory projectileFactory;
    private SoundPlayer soundPlayer;

    public EnemyTank(TextureRegion bodyRegion, TextureRegion turretRegion,
        TextureRegion[] animatedRegions, EnemyPool<EnemyTank> pool, GenericGroup<Tower> targetGroup,
        ProjectileFactory projectileFactory, SoundPlayer soundPlayer, EnemyAttributes attributes) {

        super(turretRegion, animatedRegions, TEXTURE_SIZE_TURRET, pool, targetGroup, GUN_POS,
            DEATH_EFFECT_TYPE, TEXTURE_SIZE_BODY, bodyRegion, BODY_POINTS, attributes);
        this.projectileFactory = projectileFactory;
        this.soundPlayer = soundPlayer;
    }

    @Override
    public void attackTarget(Targetable target) {

        if (target != null) {
            soundPlayer.play(LDSound.Type.ROCKET_LAUNCH);
            projectileFactory.loadProjectile(Rocket.class)
                .initialize(this, target.getPositionCenter(), ROCKET_SIZE, AOE_RADIUS);
        }
    }
}
