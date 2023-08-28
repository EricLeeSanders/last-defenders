package com.lastdefenders.game.model.actor.projectile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.SnapshotArray;
import com.lastdefenders.game.helper.Damage;
import com.lastdefenders.game.model.actor.combat.enemy.Enemy;
import com.lastdefenders.game.model.actor.combat.enemy.EnemyFlameThrower;
import com.lastdefenders.game.model.actor.combat.tower.Tower;
import com.lastdefenders.game.model.actor.combat.tower.TowerFlameThrower;
import com.lastdefenders.game.service.factory.ProjectileFactory.ProjectilePool;
import com.lastdefenders.util.Resources;
import com.lastdefenders.util.datastructures.Dimension;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import testutil.ResourcesMock;
import testutil.TestUtil;

/**
 * Created by Eric on 5/21/2017.
 */
public class FlameTest {

    @SuppressWarnings("unchecked")
    private ProjectilePool<Flame> poolMock = mock(ProjectilePool.class);

    public Flame createFlame() {

        Resources resources = ResourcesMock.create();

        return new Flame(poolMock, resources.getAtlasRegion(""));

    }

    @Test
    public void flameTest1() {

        Tower tower = TestUtil.createTower(TowerFlameThrower.class, true, true);
        tower.setRotation(90);
        Vector2 gunPos = new Vector2(5, 5);
        doReturn(gunPos).when(tower).getGunPos();

        Flame flame = createFlame();
        Dimension flameSize = new Dimension(56, 26);
        try(MockedStatic<Damage> mockedDamage = mockStatic(Damage.class)) {
            flame.initialize(tower, tower.getEnemyGroup(), flameSize);

            //Check origin
            assertEquals(0, flame.getOriginX(), TestUtil.DELTA);
            assertEquals(flameSize.getWidth() / 2, flame.getOriginY(), TestUtil.DELTA);
            //Check position
            assertEquals(gunPos.x, flame.getX(), TestUtil.DELTA);
            assertEquals(gunPos.y - flameSize.getWidth() / 2, flame.getY(), TestUtil.DELTA);
            //Check rotation
            assertEquals(90.0f, flame.getRotation(), TestUtil.DELTA);

            //Check flameBody
            assertEquals(gunPos.x, flame.getFlameBody().getX(), TestUtil.DELTA);
            assertEquals(gunPos.y - (flameSize.getHeight() / 2), flame.getFlameBody().getY(),
                TestUtil.DELTA);
            assertEquals(90.0f, flame.getRotation(), TestUtil.DELTA);

            // rotate tower
            tower.setRotation(70);

            flame.act(0.3f);

            //Check position
            assertEquals(gunPos.x, flame.getX(), TestUtil.DELTA);
            assertEquals(gunPos.y - flameSize.getWidth() / 2, flame.getY(), TestUtil.DELTA);
            //Check rotation
            assertEquals(70.0f, flame.getRotation(), TestUtil.DELTA);

            SnapshotArray<Actor> targets = tower.getEnemyGroup().getChildren();
            Polygon flameBody = flame.getFlameBody();

            flame.act(Flame.TICK_ATTACK_SPEED);

            // Verify damage is called only once
            mockedDamage.verify(() -> Damage.dealFlameGroupDamage(eq(tower), eq(targets), eq(flameBody)), times(1));
        }
    }

    /**
     * Flame is freed after running for its duration
     */
    @Test
    public void flameTest2() {

        Tower tower = TestUtil.createTower(TowerFlameThrower.class, true, true);
        tower.setRotation(90);
        Vector2 gunPos = new Vector2(5, 5);
        doReturn(gunPos).when(tower).getGunPos();

        Flame flame = createFlame();
        Dimension flameSize = new Dimension(56, 26);
        flame.initialize(tower, tower.getEnemyGroup(), flameSize);

        flame.act(Flame.DURATION);
        verify(poolMock, times(1)).free(flame);

    }

    /**
     * Flame is freed when FlameThrower is killed
     */
    @Test
    public void flameTest3() {

        Enemy enemy = TestUtil.createEnemy(EnemyFlameThrower.class, true);
        enemy.setRotation(90);
        Vector2 gunPos = new Vector2(5, 5);
        doReturn(gunPos).when(enemy).getGunPos();

        Flame flame = createFlame();
        Dimension flameSize = new Dimension(56, 26);
        flame.initialize(enemy, enemy.getEnemyGroup(), flameSize);

        flame.act(0.0001f);

        verify(poolMock, never()).free(flame);

        enemy.setDead(true);

        flame.act(0.0001f);

        verify(poolMock, times(1)).free(flame);


    }
}
