package simulate.state.writer;

import static com.lastdefenders.levelselect.LevelName.SERPENTINE_RIVER;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Resources;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import simulate.state.combat.tower.TowerState;

/**
 * Created by Eric on 12/17/2019.
 */

public class SnapshotWriter {

    private static final Map<LevelName, String> LEVEL_IMAGE_PATHS = new HashMap<>();
    private static final String LEVEL_IMAGE_PATH_BASE = "../../files/simulation/level-images/";

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
    static byte[] createSnapshot(Array<TowerState> towers, LevelName levelName)
        throws IOException {

        BufferedImage levelImage = getLevelImage(levelName);
        Graphics2D background = levelImage.createGraphics();

        for(TowerState tower : towers){
            drawTower(tower, background);
        }
        ByteArrayOutputStream baps = new ByteArrayOutputStream();
        ImageIO.write(levelImage, "png", baps);

        return baps.toByteArray();
    }

    static void createSnapshot2(Array<TowerState> towers, LevelName levelName)
        throws IOException {

        BufferedImage levelImage = getLevelImage(levelName);
        Graphics2D background = levelImage.createGraphics();

        for(TowerState tower : towers){
            drawTower(tower, background);
        }

        ImageIO.write(levelImage, "png", new File("output_image.png"));
    }

    private static void drawTower(TowerState tower, Graphics2D background) throws IOException {

        BufferedImage towerImg = getImage(TOWER_IMAGE_PATHS.get(tower.getName()));

        float x = tower.getPosition().x;
        float y = tower.getPosition().y;

        x -= towerImg.getWidth() / 2;
        y += towerImg.getHeight() / 2;

        y = Resources.VIRTUAL_HEIGHT - y;

        background.drawImage(towerImg, (int)x, (int)y, null);
    }

    private static BufferedImage getLevelImage(LevelName levelName) throws IOException {
        return getImage(LEVEL_IMAGE_PATHS.get(levelName));
    }

    private static BufferedImage getImage(String path) throws IOException {
        BufferedImage img = ImageIO.read(Gdx.files.internal(path).file());

        return img;
    }
}
