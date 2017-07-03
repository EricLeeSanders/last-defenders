package com.foxholedefense.game.service.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.effects.label.ArmorDestroyedEffect;
import com.foxholedefense.game.model.actor.effects.label.LabelEffect;
import com.foxholedefense.game.model.actor.effects.label.TowerHealEffect;
import com.foxholedefense.game.model.actor.effects.label.WaveOverCoinEffect;
import com.foxholedefense.game.model.actor.effects.texture.TextureEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.AnimationEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.BloodSplatter;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.VehicleExplosion;
import com.foxholedefense.util.Resources;

/**
 * Created by Eric on 3/31/2017.
 */

public class EffectFactory {
    private DeathEffectPool<VehicleExplosion> vehicleExplosionPool = new DeathEffectPool<>(VehicleExplosion.class);
    private DeathEffectPool<BloodSplatter> bloodPool = new DeathEffectPool<>(BloodSplatter.class);
    private LabelEffectPool<ArmorDestroyedEffect> armorDestroyedEffectPool = new LabelEffectPool<>(ArmorDestroyedEffect.class);
    private LabelEffectPool<TowerHealEffect> towerHealEffectPool = new LabelEffectPool<>(TowerHealEffect.class);
    private LabelEffectPool<WaveOverCoinEffect> waveOverCoinEffectPool = new LabelEffectPool<>(WaveOverCoinEffect.class);
    private AnimationEffectPool<EnemyCoinEffect> enemyCoinEffectPool = new AnimationEffectPool<>(EnemyCoinEffect.class);

    private ActorGroups actorGroups;
    private Resources resources;

    public EffectFactory(ActorGroups actorGroups, Resources resources) {
        this.actorGroups = actorGroups;
        this.resources = resources;
    }

    /**
     * Obtains a Death Effect from the pool
     *
     * @param type
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public <T extends DeathEffect> T loadDeathEffect(DeathEffectType type) {

        T deathEffect = null;
        switch (type) {
            case BLOOD:
                deathEffect = (T) bloodPool.obtain();
                break;
            case VEHCILE_EXPLOSION:
                deathEffect = (T) vehicleExplosionPool.obtain();
                break;
        }

        actorGroups.getDeathEffectGroup().addActor(deathEffect);
        return deathEffect;
    }

    /**
     * Obtains a Label Effect from the pool
     *
     * @param type
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public <T extends LabelEffect> T loadLabelEffect(Class<T> type) {

        T labelEffect;
        if (type.equals(ArmorDestroyedEffect.class)) {
            labelEffect = (T) armorDestroyedEffectPool.obtain();
        } else if (type.equals(TowerHealEffect.class)) {
            labelEffect = (T) towerHealEffectPool.obtain();
        } else if (type.equals(WaveOverCoinEffect.class)) {
            labelEffect = (T) waveOverCoinEffectPool.obtain();
        } else {
            throw new IllegalArgumentException("No type: " + type.getSimpleName() + " exists");
        }
        actorGroups.getEffectGroup().addActor(labelEffect);
        return labelEffect;
    }

    /**
     * Obtains an Animation Effect from the pool
     *
     * @param type
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public <T extends AnimationEffect> T loadAnimationEffect(Class<T> type) {

        T animationEffect;
        if (type.equals(EnemyCoinEffect.class)) {
            animationEffect = (T) enemyCoinEffectPool.obtain();
        } else {
            throw new IllegalArgumentException("No type: " + type.getSimpleName() + " exists");
        }

        actorGroups.getEffectGroup().addActor(animationEffect);
        return animationEffect;
    }

    /**
     * Create a Death Effect
     *
     * @return DeathEffect
     */
    private DeathEffect createDeathEffect(Class<? extends TextureEffect> type) {

        if (type.equals(BloodSplatter.class)) {
            Array<AtlasRegion> atlasRegions = resources.getAtlasRegion("blood-splatter");
            return new BloodSplatter(bloodPool, atlasRegions);
        } else if (type.equals(VehicleExplosion.class)) {
            Array<AtlasRegion> atlasRegions = resources.getAtlasRegion("smoke-ring");
            return new VehicleExplosion(vehicleExplosionPool, atlasRegions);
        } else {
            throw new NullPointerException("Effect Factory couldn't create: " + type.getSimpleName());
        }

    }

    private LabelEffect createLabelEffect(Class<? extends LabelEffect> type) {
        if (type.equals(ArmorDestroyedEffect.class)) {
            Array<AtlasRegion> atlasRegions = resources.getAtlasRegion("shield-destroyed");
            return new ArmorDestroyedEffect(atlasRegions, armorDestroyedEffectPool, resources.getSkin());
        } else if (type.equals(TowerHealEffect.class)) {
            return new TowerHealEffect(towerHealEffectPool, resources.getSkin());
        } else if (type.equals(WaveOverCoinEffect.class)) {
            Array<AtlasRegion> atlasRegions = resources.getAtlasRegion("coin");
            return new WaveOverCoinEffect(waveOverCoinEffectPool, resources.getSkin(), atlasRegions);
        } else {
            throw new NullPointerException("Effect Factory couldn't create: " + type.getSimpleName());
        }
    }

    private AnimationEffect createAnimationEffect(Class<? extends AnimationEffect> type) {
        if (type.equals(EnemyCoinEffect.class)) {
            Array<AtlasRegion> atlasRegions = resources.getAtlasRegion("coin");
            return new EnemyCoinEffect(enemyCoinEffectPool, atlasRegions);
        } else {
            throw new NullPointerException("Effect Factory couldn't create: " + type.getSimpleName());
        }
    }


    public class DeathEffectPool<T extends DeathEffect> extends Pool<TextureEffect> {
        private final Class<? extends DeathEffect> type;

        public DeathEffectPool(Class<? extends DeathEffect> type) {
            this.type = type;
        }

        @Override
        protected DeathEffect newObject() {
            return createDeathEffect(type);
        }

    }

    public class LabelEffectPool<T extends LabelEffect> extends Pool<LabelEffect> {
        private final Class<? extends LabelEffect> type;

        public LabelEffectPool(Class<? extends LabelEffect> type) {
            this.type = type;
        }

        @Override
        protected LabelEffect newObject() {
            return createLabelEffect(type);
        }
    }

    public class AnimationEffectPool<T extends AnimationEffect> extends Pool<TextureEffect> {
        private final Class<? extends AnimationEffect> type;

        public AnimationEffectPool(Class<? extends AnimationEffect> type) {
            this.type = type;
        }

        @Override
        protected AnimationEffect newObject() {
            return createAnimationEffect(type);
        }
    }
}
