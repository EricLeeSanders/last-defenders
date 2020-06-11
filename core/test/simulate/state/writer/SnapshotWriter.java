package simulate.state.writer;

import static com.lastdefenders.levelselect.LevelName.SERPENTINE_RIVER;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Resources;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import simulate.state.combatactor.EnemyState;
import simulate.state.combatactor.TowerState;
import simulate.state.support.AirStrikeState;
import simulate.state.support.ApacheState;
import simulate.state.support.LandMineState;
import simulate.state.support.SupplyDropState;
import simulate.state.support.SupportState;

/**
 * Created by Eric on 12/17/2019.
 */

public class SnapshotWriter {

    private static final Map<LevelName, String> LEVEL_IMAGE_PATHS = new HashMap<>();
    private static final String LEVEL_IMAGE_PATH_BASE = "../../files/simulation/images/level-images/";
    private static final String DEAD_IMAGE = "../../files/simulation/images/tower-dead.png";
    private static final String LANDMINE_IMAGE = "../../files/simulation/images/landmine.png";
    private static final String APACHE_IMAGE = "../../files/simulation/images/apache.png";
    private static final String SUPPLY_DROP_IMAGE = "../../files/simulation/images/supplydrop.png";
    private static final String AIRSTRIKE_IMAGE = "../../files/simulation/images/airstrike-loc.png";

    static
    {
        LEVEL_IMAGE_PATHS.put(SERPENTINE_RIVER, LEVEL_IMAGE_PATH_BASE + "serpentine-river.png");
    }

    private static final Map<String, String> TOWER_IMAGE_PATHS = new HashMap<>();
    private static final String TOWER_IMAGE_PATH_BASE = "../../files/assets/game/actor/lo/";

    static
    {
        TOWER_IMAGE_PATHS.put("TowerRifle", TOWER_IMAGE_PATH_BASE + "tower-rifle.png");
        TOWER_IMAGE_PATHS.put("TowerFlameThrower", TOWER_IMAGE_PATH_BASE + "tower-flame-thrower.png");
        TOWER_IMAGE_PATHS.put("TowerHumvee", TOWER_IMAGE_PATH_BASE + "tower-humvee.png");
        TOWER_IMAGE_PATHS.put("TowerMachineGun", TOWER_IMAGE_PATH_BASE + "tower-machine-gun.png");
        TOWER_IMAGE_PATHS.put("TowerRocketLauncher", TOWER_IMAGE_PATH_BASE + "tower-rocket-launcher.png");
        TOWER_IMAGE_PATHS.put("TowerSniper", TOWER_IMAGE_PATH_BASE + "tower-sniper.png");
        TOWER_IMAGE_PATHS.put("TowerTank", TOWER_IMAGE_PATH_BASE + "tower-tank-body.png");
    }

    byte[] createSnapshot(Array<TowerState> towers, Array<EnemyState> enemies, Array<SupportState> supportStates, LevelName levelName)
        throws IOException {

        BufferedImage levelImage = getLevelImage(levelName);
        Graphics2D background = levelImage.createGraphics();

        for(TowerState tower : towers){
            drawTower(background, tower);
        }

        for(EnemyState enemy : enemies){
            if(enemy.isDead()) {
                drawDeadEnemy(background, enemy);
            }
        }

        for(SupportState supportState : supportStates){
            supportState.writeSnapshotState(background, this);
        }

        ByteArrayOutputStream baps = new ByteArrayOutputStream();
        ImageIO.write(levelImage, "png", baps);

        return baps.toByteArray();
    }

    private void drawTower(Graphics2D background, TowerState tower) throws IOException {

        BufferedImage towerImg = getImage(TOWER_IMAGE_PATHS.get(tower.getName()));

        float x = tower.getPosition().x;
        float y = tower.getPosition().y;

        x -= towerImg.getWidth() / 2.0f;
        y += towerImg.getHeight() / 2.0f;

        y = Resources.VIRTUAL_HEIGHT - y;

        background.drawImage(towerImg, (int)x, (int)y, null);


        if(tower.isDead()){
            BufferedImage deadImg = getImage(DEAD_IMAGE);
            float deadX = tower.getPosition().x - (deadImg.getWidth() / 2.0f);
            float deadY = tower.getPosition().y + (deadImg.getHeight() / 2.0f);
            deadY = Resources.VIRTUAL_HEIGHT - deadY;

            background.drawImage(deadImg, (int)deadX, (int)deadY, null);
        }
    }

    private void drawDeadEnemy(Graphics2D background, EnemyState enemy) throws IOException {

        BufferedImage deadImg = getImage(DEAD_IMAGE);

        float x = enemy.getDeadPosition().x - (deadImg.getWidth() / 2.0f);
        float y = enemy.getDeadPosition().y + (deadImg.getHeight() / 2.0f);

        y = Resources.VIRTUAL_HEIGHT - y;

        background.drawImage(deadImg, (int)x, (int)y, null);
    }

    public void drawLandMine(Graphics2D background, LandMineState state)
        throws IOException {

        BufferedImage landmineImg = getImage(LANDMINE_IMAGE);

        float x = state.getLocation().x - (landmineImg.getWidth() / 2.0f);
        float y = state.getLocation().y + (landmineImg.getHeight() / 2.0f);

        y = Resources.VIRTUAL_HEIGHT - y;

        background.drawImage(landmineImg, (int)x, (int)y, null);
    }

    public void drawApacheState(Graphics2D background, ApacheState state)
        throws IOException {

        BufferedImage apacheImg = getImage(APACHE_IMAGE);

        float x = state.getLocation().x - (apacheImg.getWidth() / 2.0f);
        float y = state.getLocation().y + (apacheImg.getHeight() / 2.0f);

        y = Resources.VIRTUAL_HEIGHT - y;

        System.out.println("drawing apache at: " + x + " , " + y);

        background.drawImage(apacheImg, (int)x, (int)y, null);
    }

    public void drawSupplyDropState(Graphics2D background, SupplyDropState state)
        throws IOException {

        BufferedImage supplyDropImg = getImage(SUPPLY_DROP_IMAGE);

        float x = state.getLocation().x - (supplyDropImg.getWidth() / 2.0f);
        float y = state.getLocation().y + (supplyDropImg.getHeight() / 2.0f);

        y = Resources.VIRTUAL_HEIGHT - y;

        background.drawImage(supplyDropImg, (int)x, (int)y, null);
    }

    public void drawAirStrikeState(Graphics2D background, AirStrikeState state)
        throws IOException {

        BufferedImage airStrikeImage = getImage(AIRSTRIKE_IMAGE);

        for(Vector2 location : state.getLocations()){

            float x = location.x - (airStrikeImage.getWidth() / 2.0f);
            float y = location.y + (airStrikeImage.getHeight() / 2.0f);

            y = Resources.VIRTUAL_HEIGHT - y;

            background.drawImage(airStrikeImage, (int)x, (int)y, null);
        }

    }

    private BufferedImage getLevelImage(LevelName levelName) throws IOException {
        return getImage(LEVEL_IMAGE_PATHS.get(levelName));
    }

    private BufferedImage getImage(String path) throws IOException {
        BufferedImage img = ImageIO.read(Gdx.files.internal(path).file());

        return img;
    }
}
