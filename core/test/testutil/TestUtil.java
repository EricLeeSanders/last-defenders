package testutil;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.backends.headless.HeadlessNativesLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lastdefenders.game.model.Player;
import com.lastdefenders.game.model.actor.combat.CombatActor;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyAttributes;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyFlameThrower;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyHumvee;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyMachineGun;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRifle;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyRocketLauncher;
import com.lastdefenders.game.model.actor.combat.enemy.EnemySniper;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyTank;
import com.lastdefenders.game.model.actor.combat.enemy.state.EnemyStateManager;
import com.lastdefenders.game.model.actor.combat.event.EventManagerImpl;
import com.lastdefenders.game.model.actor.combat.event.interfaces.EventManager;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerAttributes;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.TowerHumvee;
import com.lastdefenders.game.model.actor.combat.tower.TowerMachineGun;
import com.lastdefenders.game.model.actor.combat.tower.TowerRifle;
import com.lastdefenders.game.model.actor.combat.tower.TowerRocketLauncher;
import com.lastdefenders.game.model.actor.combat.tower.TowerSniper;
import com.lastdefenders.game.model.actor.combat.tower.TowerTank;
import com.lastdefenders.game.model.actor.combat.tower.TowerTurret;
import com.lastdefenders.game.model.actor.combat.tower.state.TowerStateManager;
import com.lastdefenders.game.model.actor.effects.label.ArmorDestroyedEffect;
import com.lastdefenders.game.model.actor.effects.texture.TextureEffect;
import com.lastdefenders.game.model.actor.effects.texture.animation.EnemyCoinEffect;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.BloodSplatter;
import com.lastdefenders.game.model.actor.effects.texture.animation.death.DeathEffectType;
import com.lastdefenders.game.model.actor.projectile.Bullet;
import com.lastdefenders.game.service.factory.CombatActorFactory.CombatActorPool;
import com.lastdefenders.game.service.factory.EffectFactory;
import com.lastdefenders.game.service.factory.ProjectileFactory;
import com.lastdefenders.util.LDAudio;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.UserPreferences;
import com.lastdefenders.util.datastructures.pool.LDVector2;
import java.util.Random;

/**
 * Created by Eric on 4/23/2017.
 */

public class TestUtil {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    public static final double DELTA = 1e-15;
    private static EffectFactory effectFactoryMock = createEffectFactoryMock();
    private static ProjectileFactory projectileFactoryMock = createProjectileFactoryMock();
    private static Player playerMock = mock(Player.class);
    private static LDAudio audioMock = mock(LDAudio.class);
    private static TowerAttributes towerAttributes = mock(TowerAttributes.class);
    private static EnemyAttributes enemyAttributes = mock(EnemyAttributes.class);

    static {

        System.out.println("TEST UTIL");
        HeadlessNativesLoader.load();
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();

        new HeadlessApplication(new ApplicationListener() {
            public void create() {}
            public void resize(int width, int height) {}
            public void render() {}
            public void pause() {}
            public void resume() {}
            public void dispose() {}
        }, config);
        GL20 gl20 = mock(GL20.class);
        Gdx.gl = gl20;
        Gdx.gl20 = gl20;

        Resources resources = new Resources(mock(UserPreferences.class));

        System.out.println(resources.getTowerAttribute(TowerRifle.class));
    }

    public static Resources createResourcesMock() {

        Array<AtlasRegion> atlasRegion = new Array<>();
        atlasRegion.add(null);

        Resources resources = mock(Resources.class);
        UserPreferences userPreferences = createUserPreferencesMock();
        when(resources.getAtlasRegion(anyString())).thenReturn(atlasRegion);
        when(resources.getTexture(anyString())).thenReturn(null);
        when(resources.getUserPreferences()).thenReturn(userPreferences);

        return resources;
    }

    public static UserPreferences createUserPreferencesMock(){

        UserPreferences userPreferences = mock(UserPreferences.class);
        Preferences preferences = mock(Preferences.class);

        doReturn(preferences).when(userPreferences).getPreferences();

        return userPreferences;
    }

    private static ProjectileFactory createProjectileFactoryMock() {

        ProjectileFactory projectileFactoryMock = mock(ProjectileFactory.class);

        Bullet bullet = mock(Bullet.class);
        doReturn(bullet).when(projectileFactoryMock).loadProjectile(Bullet.class);

        return projectileFactoryMock;
    }

    private static EffectFactory createEffectFactoryMock() {

        EffectFactory effectFactoryMock = mock(EffectFactory.class);

        ArmorDestroyedEffect armorDestroyedEffect = mock(ArmorDestroyedEffect.class);
        doReturn(armorDestroyedEffect).when(effectFactoryMock)
            .loadEffect(eq(ArmorDestroyedEffect.class), isA(Boolean.class));

        TextureEffect deathEffect = mock(BloodSplatter.class);
        doReturn(deathEffect).when(effectFactoryMock).loadDeathEffect(isA(DeathEffectType.class),
            isA(Boolean.class));

        EnemyCoinEffect enemyCoinEffect = mock(EnemyCoinEffect.class);
        doReturn(enemyCoinEffect).when(effectFactoryMock)
            .loadEffect(eq(EnemyCoinEffect.class), isA(Boolean.class));

        return effectFactoryMock;
    }

    public static Tower createTower(String name, boolean spy) {

        Tower tower;

        switch (name) {
            case "Rifle":
                tower = new TowerRifle(null, null, new Group(), null, null, projectileFactoryMock,
                    audioMock, towerAttributes);
                break;
            case "MachineGun":
                tower = new TowerMachineGun(null, null, new Group(), null, null,
                    projectileFactoryMock, audioMock, towerAttributes);
                break;
            case "Sniper":
                tower = new TowerSniper(null, null, new Group(), null, null, projectileFactoryMock,
                    audioMock, towerAttributes);
                break;
            case "RocketLauncher":
                tower = new TowerRocketLauncher(null, null, new Group(), null, null,
                    projectileFactoryMock, audioMock, towerAttributes);
                break;
            case "FlameThrower":
                tower = new TowerFlameThrower(null, null, new Group(), null, null,
                    projectileFactoryMock, audioMock, towerAttributes);
                break;
            case "Tank":
                tower = new TowerTank(null, null, null, new Group(), null, null,
                    projectileFactoryMock, audioMock, towerAttributes);
                break;
            case "Humvee":
                tower = new TowerHumvee(null, null, null, new Group(), null, null,
                    projectileFactoryMock, audioMock, towerAttributes);
                break;
            default:
                throw new NullPointerException("Type: " + name + " doesn't exist");

        }

        if (spy) {
            tower = spy(tower);
        }

        CombatActorPool<? extends CombatActor> pool = (CombatActorPool<? extends CombatActor>) mock(
            CombatActorPool.class);
        tower.setPool(pool);

        TowerStateManager stateManager = new TowerStateManager(tower, effectFactoryMock);
        tower.setStateManager(stateManager);

        EventManager eventManager = new EventManagerImpl(tower, effectFactoryMock);
        tower.setEventManager(eventManager);

        tower.init();

        return tower;
    }


    public static Enemy createEnemy(String name, boolean spy) {

        Enemy enemy;
        Array<AtlasRegion> atlasRegion = new Array<>();
        atlasRegion.add(null);
        TextureRegion[] animatedRegions = atlasRegion.toArray(TextureRegion.class);

        switch (name) {
            case "Rifle":
                enemy = new EnemyRifle(null, animatedRegions, null, new Group(),
                    projectileFactoryMock, audioMock, enemyAttributes);
                break;
            case "MachineGun":
                enemy = new EnemyMachineGun(null, animatedRegions, null, new Group(),
                    projectileFactoryMock, audioMock, enemyAttributes);
                break;
            case "Sniper":
                enemy = new EnemySniper(null, animatedRegions, null, new Group(),
                    projectileFactoryMock, audioMock, enemyAttributes);
                break;
            case "FlameThrower":
                enemy = new EnemyFlameThrower(null, animatedRegions, null, new Group(),
                    projectileFactoryMock, audioMock, enemyAttributes);
                break;
            case "Humvee":
                enemy = new EnemyHumvee(null, null, animatedRegions, null, new Group(),
                    projectileFactoryMock, audioMock, enemyAttributes);
                break;
            case "RocketLauncher":
                enemy = new EnemyRocketLauncher(null, animatedRegions, null, new Group(),
                    projectileFactoryMock, audioMock, enemyAttributes);
                break;
            case "Tank":
                enemy = new EnemyTank(null, null, animatedRegions, null, new Group(),
                    projectileFactoryMock, audioMock, enemyAttributes);
                break;
            default:
                throw new NullPointerException("Type: " + name + " doesn't exist");
        }

        if (spy) {
            enemy = spy(enemy);
        }

        CombatActorPool<? extends CombatActor> pool = (CombatActorPool<? extends CombatActor>) mock(
            CombatActorPool.class);
        enemy.setPool(pool);

        EnemyStateManager stateManager = new EnemyStateManager(enemy, effectFactoryMock,
            playerMock);
        enemy.setStateManager(stateManager);

        EventManager eventManager = new EventManagerImpl(enemy, effectFactoryMock);
        enemy.setEventManager(eventManager);

        enemy.init();

        return enemy;
    }

    public static Viewport mockViewportUnproject(LDVector2 coords){

        Viewport viewport = mock(Viewport.class);
        doReturn(coords).when(viewport).unproject(coords);

        return viewport;
    }


    public static void mockViewportWorldWidth(float worldWidth, Actor actor){

        Stage stageMock = mock(Stage.class);
        Viewport viewportMock = mock(Viewport.class);


        doReturn(stageMock).when(actor).getStage();
        doReturn(viewportMock).when(stageMock).getViewport();
        doReturn(worldWidth).when(viewportMock).getWorldWidth();

    }

    public static void mockViewportUnproject(LDVector2 coords, Viewport viewport){

        doReturn(coords).when(viewport).unproject(any(LDVector2.class));
    }
//
//    public static void mockScreenToStageCoordinates(LDVector2 coords, Actor actor){
//
//        Stage stageMock = mock(Stage.class);
//        doReturn(stageMock).when(actor).getStage();
//        doReturn(coords).when(stageMock).screenToStageCoordinates(any(LDVector2.class));
//    }

    public static LDVector2 nonPooledLDVector2(float x, float y){

        LDVector2 vector2 = new LDVector2(x, y);
        vector2 = spy(vector2);

        doNothing().when(vector2).free();

        return vector2;
    }

    public static int getRandomNumberInRange(int min, int max) {

        if(min >= max ){
            int temp = min;
            min = max;
            max = temp;
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /*
        Determines if a line is horizontal or vertical.
        Returns 0 if the line is horizontal.
        Returns 1 if the line is vertical.
        Returns -1 if the two points are equal.
     */
    public static int lineDirection(Vector2 start, Vector2 end){

        if (start.equals(end)) {
            return -1;
        }
        if(start.y == end.y){
            // Horizontal
            return HORIZONTAL;
        } else {
            // Vertical
            return VERTICAL;
        }
    }
}
