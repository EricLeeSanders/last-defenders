package util;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.foxholedefense.game.model.actor.ActorGroups;
import com.foxholedefense.game.model.actor.combat.CombatActor;
import com.foxholedefense.game.model.actor.combat.enemy.Enemy;
import com.foxholedefense.game.model.actor.combat.enemy.EnemyRifle;
import com.foxholedefense.game.model.actor.combat.tower.Tower;
import com.foxholedefense.game.model.actor.combat.tower.TowerRifle;
import com.foxholedefense.game.service.factory.CombatActorFactory;
import com.foxholedefense.game.service.factory.CombatActorFactory.CombatActorPool;
import com.foxholedefense.util.Resources;

import org.junit.Before;

import static org.mockito.Mockito.*;

/**
 * Created by Eric on 4/23/2017.
 */

public class TestUtil {
    public static final double DELTA = 1e-15;
    private static ActorGroups actorGroups = new ActorGroups();
    private static Resources resources = createResourcesMock();
    private static CombatActorFactory combatActorFactory = new CombatActorFactory(actorGroups, null, resources, null, null, null);

    private static Resources createResourcesMock(){
        Array<AtlasRegion> atlasRegion = new Array<AtlasRegion>();
        atlasRegion.add(null);

        Resources resources = mock(Resources.class);
        when(resources.getAtlasRegion(anyString())).thenReturn(atlasRegion);
        when(resources.getTexture(anyString())).thenReturn(null);

        return resources;
    }

    public static Tower createTower(String name){

        Tower tower = combatActorFactory.loadTower(name);
        CombatActorPool<? extends CombatActor> pool = mock(CombatActorPool.class);
        tower.setPool(pool);

        Tower towerSpy = spy(tower);
        doReturn(new Group()).when(towerSpy).getTargetGroup();

        return towerSpy;
    }


    public static Enemy createEnemy(String name){

        Enemy enemy = combatActorFactory.loadEnemy(name);
        CombatActorPool<? extends CombatActor> pool = mock(CombatActorPool.class);


        Enemy enemySpy = spy(enemy);
        doReturn(new Group()).when(enemySpy).getTargetGroup();
        doNothing().when(enemySpy).setTextureRegion(any(TextureRegion.class));
        enemySpy.setPool(pool);

        return enemySpy;
    }



}
