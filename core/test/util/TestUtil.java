package util;


import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.effects.label.ArmorDestroyedEffect;
import com.foxholedefense.game.model.actor.effects.label.LabelEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.BloodSplatter;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.service.factory.CombatActorFactory;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.game.service.factory.HealthFactory;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.Resources;


import org.mockito.Matchers;


import static org.mockito.Mockito.*;

/**
 * Created by Eric on 4/23/2017.
 */

public class TestUtil {
    public static final double DELTA = 1e-15;
    private static ActorGroups actorGroups = new ActorGroups();
    private static Resources resourcesMock = createResourcesMock();
    private static EffectFactory effectFactoryMock = createEffectFactoryMock();
    private static HealthFactory healthFactoryMock = mock(HealthFactory.class);
    private static ProjectileFactory projectileFactoryMock = mock(ProjectileFactory.class);
    private static Player playerMock = mock(Player.class);
    private static CombatActorFactory combatActorFactory = new CombatActorFactory(actorGroups, null, resourcesMock, effectFactoryMock, healthFactoryMock, projectileFactoryMock, playerMock);

    private static Resources createResourcesMock(){
        Array<AtlasRegion> atlasRegion = new Array<AtlasRegion>();
        atlasRegion.add(null);

        Resources resources = mock(Resources.class);
        when(resources.getAtlasRegion(anyString())).thenReturn(atlasRegion);
        when(resources.getTexture(anyString())).thenReturn(null);

        return resources;
    }

    private static EffectFactory createEffectFactoryMock(){
        EffectFactory effectFactoryMock = mock(EffectFactory.class);

        ArmorDestroyedEffect armorDestroyedEffect = mock(ArmorDestroyedEffect.class);
        doReturn(armorDestroyedEffect).when(effectFactoryMock).loadLabelEffect(ArmorDestroyedEffect.class);

        DeathEffect deathEffect = mock(BloodSplatter.class);
        doReturn(deathEffect).when(effectFactoryMock).loadDeathEffect(any(DeathEffectType.class));

        EnemyCoinEffect enemyCoinEffect = mock(EnemyCoinEffect.class);
        doReturn(enemyCoinEffect).when(effectFactoryMock).loadAnimationEffect(EnemyCoinEffect.class);

        return effectFactoryMock;
    }

    public static Tower createTower(String name){

        Tower tower = combatActorFactory.loadTower(name);
        tower.init();
        CombatActorPool<? extends CombatActor> pool = mock(CombatActorPool.class);
        tower.setPool(pool);

        Tower towerSpy = spy(tower);
        doReturn(new Group()).when(towerSpy).getTargetGroup();

        return towerSpy;
    }


    public static Enemy createEnemy(String name){

        Enemy enemy = combatActorFactory.loadEnemy(name);
        enemy.init();
        CombatActorPool<? extends CombatActor> pool = mock(CombatActorPool.class);


        Enemy enemySpy = spy(enemy);
        doReturn(new Group()).when(enemySpy).getTargetGroup();
        doNothing().when(enemySpy).setTextureRegion(any(TextureRegion.class));
        enemySpy.setPool(pool);


        return enemySpy;
    }



}
