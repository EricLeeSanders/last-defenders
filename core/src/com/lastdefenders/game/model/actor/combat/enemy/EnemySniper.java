package com.lastdefenders.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.groups.GenericGroup;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.game.service.factory.CombatActorFactory.EnemyPool;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.sound.LDSound;
import com.lastdefenders.sound.SoundPlayer;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.UtilPool;

/**
 * Represents an Enemy Sniper
 *
 * @author Eric
 */
public class EnemySniper extends Enemy {

    private static final Dimension BULLET_SIZE = new Dimension(5, 5);
    private static final Vector2 GUN_POS = UtilPool.getVector2(37, -10);
    private static final Dimension TEXTURE_SIZE = new Dimension(78, 41);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.BLOOD;

    private Circle body;
    private SoundPlayer soundPlayer;
    private ProjectileFactory projectileFactory;

    public EnemySniper(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions,
        EnemyPool<EnemySniper> pool, GenericGroup<Tower> targetGroup, ProjectileFactory projectileFactory,
        SoundPlayer soundPlayer, EnemyAttributes attributes) {

        super(stationaryTextureRegion, animatedRegions, TEXTURE_SIZE, pool, targetGroup, GUN_POS,
            DEATH_EFFECT_TYPE, attributes);
        this.soundPlayer = soundPlayer;
        this.projectileFactory = projectileFactory;
        this.body = new Circle(this.getPositionCenter(), 10);
    }

    @Override
    public void attackTarget(Targetable target) {

        if (target != null) {
            soundPlayer.play(LDSound.Type.SNIPER_SHOT);
            projectileFactory.loadProjectile(Bullet.class).initialize(this, target, BULLET_SIZE);
        }
    }

    @Override
    public Circle getBody() {

        body.setPosition(getPositionCenter().x, getPositionCenter().y);
        return body;
    }
}
