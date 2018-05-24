package com.lastdefenders.game.model.actor.combat.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.interfaces.IPassiveEnemy;
import com.lastdefenders.game.model.actor.interfaces.IVehicle;
import com.lastdefenders.game.model.actor.interfaces.Targetable;
import com.lastdefenders.game.service.factory.CombatActorFactory.CombatActorPool;
import com.lastdefenders.util.ActorUtil;
import com.lastdefenders.util.datastructures.Dimension;
import com.lastdefenders.util.UtilPool;

/**
 * Represents an Enemy Humvee. A passive enemy.
 *
 * @author Eric
 */
public class EnemyHumvee extends Enemy implements IVehicle, IPassiveEnemy {

    private static final float HEALTH = 16;
    private static final float ARMOR = 8;
    private static final float ATTACK = 0;
    private static final float ATTACK_SPEED = 0f;
    private static final float RANGE = 0;
    private static final float SPEED = 140f;
    private static final int KILL_REWARD = 15;

    private static final Vector2 GUN_POS = UtilPool.getVector2();
    private static final Dimension TEXTURE_SIZE = new Dimension(100, 43);
    private static final DeathEffectType DEATH_EFFECT_TYPE = DeathEffectType.VEHCILE_EXPLOSION;
    private static final float[] BODY_POINTS = {15, 0, 15, 32, 69, 32, 69, 0};

    private Polygon body;

    public EnemyHumvee(TextureRegion stationaryTextureRegion, TextureRegion[] animatedRegions,
        CombatActorPool<EnemyHumvee> pool) {

        super(stationaryTextureRegion, animatedRegions, TEXTURE_SIZE, pool, null, GUN_POS, SPEED,
            HEALTH, ARMOR, ATTACK, ATTACK_SPEED, RANGE, KILL_REWARD, DEATH_EFFECT_TYPE);
        this.body = new Polygon(BODY_POINTS);
    }

    @Override
    public void attackTarget(Targetable target) {
        // Does not attack
    }

    @Override
    public Polygon getBody() {

        body.setOrigin((this.getWidth() / 2), (this.getHeight() / 2));
        body.setRotation(this.getRotation());
        body.setPosition(
            ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().x, this.getWidth()),
            ActorUtil.calcBotLeftPointFromCenter(getPositionCenter().y, this.getHeight()));
        return body;
    }
}
