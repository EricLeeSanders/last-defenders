package com.lastdefenders.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.health.interfaces.PlatedArmor;
import com.lastdefenders.game.model.actor.interfaces.IRocket;
import com.lastdefenders.game.model.actor.interfaces.IVehicle;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.model.actor.projectile.Rocket;
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
 * Represents an Enemy Tank.
 *
 * @author Eric
 */
public class EnemyTank extends EnemyTurret implements PlatedArmor, IVehicle, IRocket {

    private static final float HEALTH = 20;
    private static final float ARMOR = 10;
    private static final float ATTACK = 6;
    private static final float ATTACK_SPEED = 3f;
    private static final float RANGE = 60;
    private static final float SPEED = 45;
    private static final float AOE_RADIUS = 40f;
    private static final int KILL_REWARD = 15;

    private static final Dimension ROCKET_SIZE = new Dimension(23, 6);
    private static final Vector2 GUN_POS = UtilPool.getVector2(57, 0);
    private static final Dimension TEXTURE_SIZE_BODY = new Dimension(75, 57);
    private static final Dimension TEXTURE_SIZE_TURRET = new Dimension(138, 36);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.VEHCILE_EXPLOSION;
    private static final float[] BODY_POINTS = {0, 0, 0, 56, 75, 56, 75, 0};

    private ProjectileFactory projectileFactory;
    private LDAudio audio;

    public EnemyTank(TextureRegion bodyRegion, TextureRegion turretRegion,
        TextureRegion[] animatedRegions, CombatActorPool<EnemyTank> pool, Group targetGroup,
        ProjectileFactory projectileFactory, LDAudio audio) {

        super(turretRegion, animatedRegions, TEXTURE_SIZE_TURRET, pool, targetGroup, GUN_POS, SPEED,
            HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, KILL_REWARD, DEATH_EFFECT_TYPE, TEXTURE_SIZE_BODY,
            bodyRegion, BODY_POINTS);
        this.projectileFactory = projectileFactory;
        this.audio = audio;
    }

    @Override
    public void attackTarget(Targetable target) {

        if (target != null) {
            audio.playSound(LDSound.ROCKET_LAUNCH);
            projectileFactory.loadProjectile(Rocket.class)
                .initialize(this, target.getPositionCenter(), ROCKET_SIZE, AOE_RADIUS);
        }
    }
}
