package game.model.actor.support;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.projectile.Explosion;
import com.foxholedefense.game.model.actor.support.LandMine;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.game.service.factory.SupportActorFactory.SupportActorPool;
import com.foxholedefense.util.Logger;
import com.foxholedefense.util.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import util.TestUtil;


import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/23/2017.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Logger.class})
public class LandMineTest {

    private SupportActorPool poolMock = mock(SupportActorPool.class);
    private Explosion explosionMock = mock(Explosion.class);

    @Before
    public void initLandMinTest() {
        PowerMockito.mockStatic(Logger.class);
    }

    public LandMine createLandMine(){

        Resources resourcesMock = TestUtil.createResourcesMock();
        ProjectileFactory projectileFactoryMock = mock(ProjectileFactory.class);
        doReturn(explosionMock).when(projectileFactoryMock).loadExplosion();

        LandMine landMine = new LandMine(poolMock, new Group(), projectileFactoryMock, resourcesMock.getTexture(""), resourcesMock.getTexture(""));

        return landMine;
    }

    public Enemy createEnemy(String type, float lengthToEnd, Vector2 posCenter){
        Enemy enemy = TestUtil.createEnemy(type, true);
        enemy.setPositionCenter(posCenter);
        doReturn(lengthToEnd).when(enemy).getLengthToEnd();
        enemy.setDead(false);
        enemy.setActive(true);

        return enemy;
    }

    @Test
    public void landMinTest1(){
        LandMine landMine = createLandMine();
        landMine.setPositionCenter(250, 255);
        landMine.setActive(true);

        Enemy enemy1 = createEnemy("Rifle", 200, new Vector2(250, 240));
        Enemy enemy2 = createEnemy("FlameThrower", 100, new Vector2(250, 260));
        Enemy enemy3 = createEnemy("RocketLauncher", 300, new Vector2(250, 150));

        Group targetGroup = landMine.getTargetGroup();
        targetGroup.addActor(enemy1);
        targetGroup.addActor(enemy2);
        targetGroup.addActor(enemy3);

        landMine.act(1f);

        verify(explosionMock, times(1)).initialize(eq(landMine), isA(Float.class), eq(landMine.getPositionCenter()));
        verify(poolMock, times(1)).free(landMine);

    }

}
