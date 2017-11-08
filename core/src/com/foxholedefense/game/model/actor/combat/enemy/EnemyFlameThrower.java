package com.foxholedefense.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.interfaces.IFlame;
import com.foxholedefense.game.model.actor.interfaces.Targetable;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.FHDAudio.FHDSound;
import com.foxholedefense.util.datastructures.Dimension;
import com.foxholedefense.util.datastructures.pool.UtilPool;

/**
 * Represents an Enemy FlameThrower
 *
 * @author Eric
 */
public class EnemyFlameThrower extends Enemy implements IFlame {

    private static final float HEALTH = 8;
    private static final float ARMOR = 3;
    private static final float ATTACK = 1.75f;
    private static final float ATTACK_SPEED = 1.2f;
    private static final float RANGE = 70;
    private static final float SPEED = 70f;
    private static final int KILL_REWARD = 15;

    private static final Vector2 GUN_POS = UtilPool.getVector2(26, 4);
    private static final Dimension TEXTURE_SIZE = new Dimension(56, 36);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.BLOOD;

    private Circle body;
    private Dimension flameSize = new Dimension(RANGE - 26, 20);
    private FHDAudio audio;
    private ProjectileFactory projectileFactory;

    public EnemyFlameThrower(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions,
        CombatActorPool<EnemyFlameThrower> pool, Group targetGroup,
        ProjectileFactory projectileFactory, FHDAudio audio) {

        super(stationaryTextureRegion, animatedRegions, TEXTURE_SIZE, pool, targetGroup, GUN_POS,
            SPEED, HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, KILL_REWARD, DEATH_EFFECT_TYPE);
        this.audio = audio;
        this.projectileFactory = projectileFactory;
        this.body = new Circle(this.getPositionCenter(), 10);
    }

    @Override
    public Dimension getFlameSize() {

        flameSize.set(this.getRange() - GUN_POS.x, this.getHeight());
        return flameSize;
    }

    @Override
    public void attackTarget(Targetable target) {

        if (target != null) {
            audio.playSound(FHDSound.FLAME_BURST);
            projectileFactory.loadFlame().initialize(this, getTargetGroup(), getFlameSize());
        }
    }

    @Override
    public Circle getBody() {

        body.setPosition(getPositionCenter().x, getPositionCenter().y);
        return body;
    }

}
