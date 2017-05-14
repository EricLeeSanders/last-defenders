package util;


import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.foxholedefense.game.model.Player;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyFlameThrower;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyHumvee;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyMachineGun;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyRifle;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyRocketLauncher;
import com.foxholedefense.game.model.actor.combat.enemy.EnemySniper;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyTank;
import com.foxholedefense.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.foxholedefense.game.model.actor.combat.event.EventManagerImpl;
import com.foxholedefense.game.model.actor.combat.event.interfaces.EventManager;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.TowerFlameThrower;
import com.foxholedefense.game.model.actor.combat.tower.TowerMachineGun;
import com.foxholedefense.game.model.actor.combat.tower.TowerRifle;
import com.foxholedefense.game.model.actor.combat.tower.TowerRocketLauncher;
import com.foxholedefense.game.model.actor.combat.tower.TowerSniper;
import com.foxholedefense.game.model.actor.combat.tower.TowerTank;
import com.foxholedefense.game.model.actor.combat.tower.TowerTurret;
import com.foxholedefense.game.model.actor.combat.tower.state.TowerStateManager;
import com.foxholedefense.game.model.actor.effects.label.ArmorDestroyedEffect;
import com.foxholedefense.game.model.actor.effects.label.LabelEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.BloodSplatter;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect;
import com.foxholedefense.game.model.actor.effects.texture.animation.death.DeathEffect.DeathEffectType;
import com.foxholedefense.game.model.actor.projectile.Bullet;
import com.foxholedefense.game.service.factory.CombatActorFactory;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.game.service.factory.EffectFactory;
import com.foxholedefense.game.service.factory.HealthFactory;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.util.FHDAudio;
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
    private static ProjectileFactory projectileFactoryMock = createProjectileFactoryMock();
    private static Player playerMock = mock(Player.class);
    private static FHDAudio audioMock = mock(FHDAudio.class);

    private static Resources createResourcesMock(){
        Array<AtlasRegion> atlasRegion = new Array<AtlasRegion>();
        atlasRegion.add(null);

        Resources resources = mock(Resources.class);
        when(resources.getAtlasRegion(anyString())).thenReturn(atlasRegion);
        when(resources.getTexture(anyString())).thenReturn(null);

        return resources;
    }

    private static ProjectileFactory createProjectileFactoryMock(){
        ProjectileFactory projectileFactoryMock = mock(ProjectileFactory.class);

        Bullet bullet = mock(Bullet.class);
        doReturn(bullet).when(projectileFactoryMock).loadBullet();

        return projectileFactoryMock;
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

    public static Tower createTower(String name, boolean spy){
        Tower tower = null;
        if(name.equals("Rifle")){
            tower = new TowerRifle(null, null, new Group(), null, null, projectileFactoryMock, audioMock);
        } else if(name.equals("MachineGun")){
            tower = new TowerMachineGun(null, null, new Group(), null, null, projectileFactoryMock, audioMock);
        } else if(name.equals("Sniper")){
            tower = new TowerSniper(null, null, new Group(), null, null, projectileFactoryMock, audioMock);
        } else if(name.equals("RocketLauncher")){
            tower = new TowerRocketLauncher(null, null, new Group(), null, null, projectileFactoryMock, audioMock);
        } else if(name.equals("FlameThrower")){
            tower = new TowerFlameThrower(null, null, new Group(), null, null, projectileFactoryMock, audioMock);
        } else if(name.equals("Tank")){
            tower = new TowerTank(null, null, null, new Group(), null, null, projectileFactoryMock, audioMock);
        } else if(name.equals("Turret")){
            tower = new TowerTurret(null, null, null, new Group(), null, null, projectileFactoryMock, audioMock);
        }

        if(spy){
            tower = spy(tower);
        }

        CombatActorPool<? extends CombatActor> pool = mock(CombatActorPool.class);
        tower.setPool(pool);

        TowerStateManager stateManager = new TowerStateManager(tower, effectFactoryMock);
        tower.setStateManager(stateManager);

        EventManager eventManager = new EventManagerImpl(tower, effectFactoryMock);
        tower.setEventManager(eventManager);

        tower.init();

        return tower;
    }


    public static Enemy createEnemy(String name){

        Enemy enemy = null;
        Array<AtlasRegion> atlasRegion = new Array<AtlasRegion>();
        atlasRegion.add(null);
        TextureRegion[] animatedRegions = atlasRegion.toArray(TextureRegion.class);
        if(name.equals("EnemyRifle")){
            enemy = new EnemyRifle(null, animatedRegions, null,  new Group(), projectileFactoryMock, audioMock);
        } else if(name.equals("EnemyMachineGun")){
            enemy = new EnemyMachineGun(null, animatedRegions, null,  new Group(), projectileFactoryMock, audioMock);
        } else if(name.equals("EnemySniper")){
            enemy = new EnemySniper(null, animatedRegions, null,  new Group(), projectileFactoryMock, audioMock);
        } else if(name.equals("EnemyFlameThrower")){
            enemy = new EnemyFlameThrower(null, animatedRegions, null,  new Group(), projectileFactoryMock, audioMock);
        } else if(name.equals("EnemyHumvee")){
            enemy = new EnemyHumvee(null, animatedRegions, null);
        } else if(name.equals("EnemyRocketLauncher")){
            enemy = new EnemyRocketLauncher(null, animatedRegions, null,  new Group(), projectileFactoryMock, audioMock);
        } else if(name.equals("EnemyTank")){
            enemy = new EnemyTank(null, null, animatedRegions, null,  new Group(), projectileFactoryMock, audioMock);
        }

        CombatActorPool<? extends CombatActor> pool = mock(CombatActorPool.class);
        enemy.setPool(pool);

        EnemyStateManager stateManager = new EnemyStateManager(enemy, effectFactoryMock, playerMock);
        enemy.setStateManager(stateManager);

        EventManager eventManager = new EventManagerImpl(enemy, effectFactoryMock);
        enemy.setEventManager(eventManager);

        enemy.init();

        return enemy;
    }



}
