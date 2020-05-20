package com.lastdefenders.game.model.actor.combat.tower;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.groups.GenericGroup;
import com.lastdefenders.game.model.actor.interfaces.IFlame;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.model.actor.projectile.Flame;
import com.lastdefenders.game.service.factory.CombatActorFactory.CombatActorPool;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.LDAudio.LDSound;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.UtilPool;

/**
 * Represents a Tower FlameThrower
 *
 * @author Eric
 */
public class TowerFlameThrower extends Tower implements IFlame {

    private static final Vector2 GUN_POS = UtilPool.getVector2(26, 4);
    private static final Dimension TEXTURE_SIZE = new Dimension(56, 26);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.BLOOD;

    private Circle body;
    private Dimension flameSize;
    private LDAudio audio;
    private ProjectileFactory projectileFactory;

    public TowerFlameThrower(TextureRegion actorRegion, CombatActorPool<TowerFlameThrower> pool,
        GenericGroup<Enemy> targetGroup, TextureRegion rangeRegion, TextureRegion collidingRangeRegion,
        ProjectileFactory projectileFactory, LDAudio audio, TowerAttributes attributes) {

        super(actorRegion, TEXTURE_SIZE, pool, targetGroup, GUN_POS, rangeRegion,
            collidingRangeRegion, DEATH_EFFECT_TYPE, attributes);
        flameSize = new Dimension(attributes.getRange() - 26, 20);
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
            audio.playSound(LDSound.FLAME_BURST);
            projectileFactory.loadProjectile(Flame.class).initialize(this, getTargetGroup(), getFlameSize());
        }
    }

    @Override
    public String getName() {

        return "Flame Thrower";
    }

    @Override
    public Circle getBody() {

        body.setPosition(getPositionCenter().x, getPositionCenter().y);
        return body;
    }
}
