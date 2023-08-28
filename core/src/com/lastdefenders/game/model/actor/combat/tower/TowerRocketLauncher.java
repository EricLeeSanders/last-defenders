package com.lastdefenders.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.groups.GenericGroup;
import com.lastdefenders.game.model.actor.interfaces.IRocket;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.model.actor.projectile.Rocket;
import com.lastdefenders.game.service.factory.CombatActorFactory.TowerPool;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.sound.LDSound;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.UtilPool;

/**
 * Represents a Tower RocketLauncher
 *
 * @author Eric
 */
public class TowerRocketLauncher extends Tower implements IRocket {

    private static final float AOE_RADIUS = 75f;

    private static final Dimension ROCKET_SIZE = new Dimension(23, 6);
    private static final Vector2 GUN_POS = UtilPool.getVector2(25, -10);
    private static final Dimension TEXTURE_SIZE = new Dimension(56, 31);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.BLOOD;

    private Circle body;
    private SoundPlayer soundPlayer;
    private ProjectileFactory projectileFactory;

    public TowerRocketLauncher(TextureRegion actorRegion, TowerPool<TowerRocketLauncher> pool,
        GenericGroup<Enemy> targetGroup, TextureRegion rangeRegion, TextureRegion collidingRangeRegion,
        ProjectileFactory projectileFactory, SoundPlayer soundPlayer, TowerAttributes attributes) {

        super(actorRegion, TEXTURE_SIZE, pool, targetGroup, GUN_POS, rangeRegion,
            collidingRangeRegion, DEATH_EFFECT_TYPE, attributes);
        this.soundPlayer = soundPlayer;
        this.projectileFactory = projectileFactory;
        this.body = new Circle(this.getPositionCenter(), 10);
    }

    @Override
    public void attackTarget(Targetable target) {

        if (target != null) {
            soundPlayer.play(LDSound.Type.ROCKET_LAUNCH);
            projectileFactory.loadProjectile(Rocket.class)
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
