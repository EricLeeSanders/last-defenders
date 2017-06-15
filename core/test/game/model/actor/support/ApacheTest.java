package game.model.actor.support;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.projectile.Bullet;
import com.foxholedefense.game.model.actor.support.Apache;
import com.foxholedefense.game.service.factory.ProjectileFactory;
import com.foxholedefense.game.service.factory.SupportActorFactory.SupportActorPool;
import com.foxholedefense.util.ActorUtil;
import com.foxholedefense.util.FHDAudio;
import com.foxholedefense.util.Resources;
import com.foxholedefense.util.datastructures.Dimension;

import org.junit.Before;
import org.junit.Test;


import testutil.TestUtil;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Eric on 5/22/2017.
 */

public class ApacheTest {

    private SupportActorPool poolMock = mock(SupportActorPool.class);
    private Bullet bulletMock = mock(Bullet.class);
    @Before
    public void initApacheTest() {
        Gdx.app = mock(Application.class);
    }

    public Apache createApache() {
        Resources resourcesMock = TestUtil.createResourcesMock();
        FHDAudio audioMock = mock(FHDAudio.class);
        ProjectileFactory projectileFactoryMock = mock(ProjectileFactory.class);
        doReturn(bulletMock).when(projectileFactoryMock).loadBullet();

        Apache apache = new Apache(poolMock, new Group(),  projectileFactoryMock, resourcesMock.getTexture(""), new TextureRegion[] {resourcesMock.getTexture("")}, resourcesMock.getTexture(""),  audioMock);

        return apache;

    }

    public Enemy createEnemy(String type, float lengthToEnd, Vector2 posCenter){
        Enemy enemy = TestUtil.createEnemy(type, true);
        enemy.setPositionCenter(posCenter);
        doReturn(lengthToEnd).when(enemy).getLengthToEnd();
        enemy.setDead(false);
        enemy.setActive(true);

        return enemy;
    }

    /**
     * Initialize an apahce
     */
    @Test
    public void apacheTest1(){
        Vector2 destination = new Vector2(250,250);

        Apache apache = createApache();

        Enemy enemy1 = createEnemy("Rifle", 200, new Vector2(250, 240));
        Enemy enemy2 = createEnemy("FlameThrower", 100, new Vector2(250, 260));
        Enemy enemy3 = createEnemy("RocketLauncher", 300, new Vector2(250, 150));

        Group targetGroup = apache.getTargetGroup();
        targetGroup.addActor(enemy1);
        targetGroup.addActor(enemy2);
        targetGroup.addActor(enemy3);

        assertFalse(apache.isActive());
        assertFalse(apache.isReadyToAttack());
        assertFalse(apache.isExitingStage());

        apache.initialize(destination);

        assertEquals(1, apache.getActions().size);
        assertFalse(apache.isReadyToAttack());
        assertFalse(apache.isExitingStage());
        apache.setActive(true);
        assertTrue(apache.isActive());

        // get to position
        apache.act(Apache.TIME_ACTIVE_LIMIT / 2);

        assertTrue(apache.isReadyToAttack());
        verify(bulletMock, times(1)).initialize(eq(apache), eq(enemy2), isA(Dimension.class) );
        assertEquals(ActorUtil.calculateRotation(enemy2.getPositionCenter(), apache.getPositionCenter()), apache.getRotation(), TestUtil.DELTA);

        apache.act(Apache.TIME_ACTIVE_LIMIT / 2);

        assertTrue(apache.isExitingStage());
        assertFalse(apache.isReadyToAttack());
        assertFalse(apache.isActive());

        apache.act(100f);

        verify(poolMock, times(1)).free(apache);

    }

}
