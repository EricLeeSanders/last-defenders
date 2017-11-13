package com.lastdefenders.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.service.factory.CombatActorFactory.CombatActorPool;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.datastructures.pool.UtilPool;

/**
 * Represents an Enemy Sniper
 *
 * @author Eric
 */
public class EnemySniper extends Enemy {

    private static final float HEALTH = 8;
    private static final float ARMOR = 4;
    private static final float ATTACK = 5;
    private static final float ATTACK_SPEED = 1;
    private static final float RANGE = 100;
    private static final float SPEED = 70f;
    private static final int KILL_REWARD = 15;

    private static final Dimension BULLET_SIZE = new Dimension(5, 5);
    private static final Vector2 GUN_POS = UtilPool.getVector2(37, -10);
    private static final Dimension TEXTURE_SIZE = new Dimension(78, 41);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.BLOOD;

    private Circle body;
    private LDAudio audio;
    private ProjectileFactory projectileFactory;

    public EnemySniper(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions,
        CombatActorPool<EnemySniper> pool, Group targetGroup, ProjectileFactory projectileFactory,
        LDAudio audio) {

        super(stationaryTextureRegion, animatedRegions, TEXTURE_SIZE, pool, targetGroup, GUN_POS,
            SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, KILL_REWARD, DEATH_EFFECT_TYPE);
        this.audio = audio;
        this.projectileFactory = projectileFactory;
        this.body = new Circle(this.getPositionCenter(), 10);
    }

    @Override
    public void attackTarget(Targetable target) {

        if (target != null) {
            audio.playSound(LDSound.SNIPER);
            projectileFactory.loadBullet().initialize(this, target, BULLET_SIZE);
        }
    }

    @Override
    public Circle getBody() {

        body.setPosition(getPositionCenter().x, getPositionCenter().y);
        return body;
    }
}
