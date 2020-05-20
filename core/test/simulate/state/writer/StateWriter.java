package simulate.state.writer;

import com.badlogic.gdx.utils.Array;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Resources;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import simulate.SimulationRunType;
import simulate.state.combat.EnemyState;
import simulate.state.GameBeginState;
import simulate.state.GameEndState;
import simulate.state.GameState;
import simulate.state.PlayerState;
import simulate.state.combat.TowerState;

/**
 * Created by Eric on 12/17/2019.
 */

public class StateWriter {

    private static final String SIMULATION_SAVE_PATH = "../../files/simulation/simulations/";

    public static void save(List<GameState> states, LevelName levelName, SimulationRunType simulationType) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        for(GameState state : states){
            saveState(workbook, state, levelName);
        }

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("MMddYYYYhhmmss");
        String dateAsString = simpleDateFormat.format(new Date());

        FileOutputStream fos = new FileOutputStream(SIMULATION_SAVE_PATH+ dateAsString + "-" + simulationType + ".xlsx");
        workbook.write(fos);
        fos.close();

    }

    private static void saveState(XSSFWorkbook workbook, GameState state, LevelName levelName)
        throws IOException {
        RowCounter counter = new RowCounter();
        Integer wave = state.getBeginState().getPlayerState().getWaves();
        XSSFSheet sheet = workbook.createSheet("wave " + wave);

        writeBeginState(sheet, state.getBeginState(), levelName, counter);
        writeEndState(sheet, state.getEndState(), levelName, counter);

        addSnapshot(sheet, state.getBeginState().getTowers(), levelName, counter);
        counter.skip(20);
        addSnapshot(sheet, state.getEndState().getTowers(), levelName, counter);
//        for(int i = 0; i < 12; i++){
//            sheet.autoSizeColumn(i);
//        }
    }

    private static void writeEndState(XSSFSheet sheet, GameEndState endState, LevelName levelName, RowCounter counter)
        throws IOException {
        counter.skip(3);// Skip 3 for buffer to begin the end state title
        createStateHeader(sheet, endState.getPlayerState(), counter, "End State");
        writeTowers(sheet, endState.getTowers(),counter);

    }

    private static void writeBeginState(XSSFSheet sheet, GameBeginState beginState, LevelName levelName, RowCounter counter)
        throws IOException {

        createStateHeader(sheet, beginState.getPlayerState(), counter, "Begin State");
        writeTowers(sheet, beginState.getTowers(),counter);
        writeEnemies(sheet, beginState.getEnemies(), counter);

    }

    private static void addSnapshot(XSSFSheet sheet, Array<TowerState> towers, LevelName levelName, RowCounter counter)
        throws IOException {
        SnapshotWriter.createSnapshot2(towers, levelName);
        byte [] snapshot = SnapshotWriter.createSnapshot(towers, levelName);
        int pictureIdx = sheet.getWorkbook().addPicture(snapshot, Workbook.PICTURE_TYPE_PNG);
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFCreationHelper helper = sheet.getWorkbook().getCreationHelper();
        XSSFClientAnchor anchor = helper.createClientAnchor();
        anchor.setAnchorType(AnchorType.DONT_MOVE_AND_RESIZE);
        anchor.setCol1(1);
        anchor.setRow1(counter.skip(3));

        Picture picture = drawing.createPicture(anchor, pictureIdx);
        picture.resize();
    }

    private static void writeEnemies(XSSFSheet sheet, Array<EnemyState> enemies, RowCounter counter){

        Row titleRow = sheet.createRow(counter.skip(3));

        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setFontHeightInPoints((short)14);
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        titleStyle.setFont(titleFont);

        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Enemies");
        titleCell.setCellStyle(titleStyle);

        Row enemyHeaderRow = sheet.createRow(counter.next());
        createEnemyHeader(enemyHeaderRow);

        for(EnemyState enemy : enemies){
            Row row = sheet.createRow(counter.next());
            writeEnemy(enemy, row);
        }
    }

    private static void writeEnemy(EnemyState enemy, Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue(enemy.getName());
        row.createCell(counter++).setCellValue(enemy.getID());
        row.createCell(counter++).setCellValue(enemy.getHasArmor());
        row.createCell(counter++).setCellValue(enemy.getSpeed());
        row.createCell(counter++).setCellValue(enemy.getSpawnDelay());
    }


    private static void createEnemyHeader(Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue("Name");
        row.createCell(counter++).setCellValue("ID");
        row.createCell(counter++).setCellValue("Armor");
        row.createCell(counter++).setCellValue("Speed");
        row.createCell(counter++).setCellValue("Spawn Delay");

        Font font= row.getSheet().getWorkbook().createFont();
        font.setFontHeightInPoints((short)12);
        font.setBold(true);
        font.setItalic(false);

        CellStyle rowStyle = row.getSheet().getWorkbook().createCellStyle();
        rowStyle.setFont(font);

        for(int i = 0; i<counter; i++) {
            row.getCell(i).setCellStyle(rowStyle);
        }
    }

    private static void writeTowers(XSSFSheet sheet, Array<TowerState> towers, RowCounter counter){

        Row towerTitleRow = sheet.createRow(counter.skip(3));

        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setFontHeightInPoints((short)14);
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        titleStyle.setFont(titleFont);

        Cell titleCell = towerTitleRow.createCell(0);
        titleCell.setCellValue("Towers");
        titleCell.setCellStyle(titleStyle);

        Row towerHeaderRow = sheet.createRow(counter.next());
        createTowerHeader(towerHeaderRow);

        for(TowerState tower : towers){
            Row row = sheet.createRow(counter.next());
            writeTower(tower, row);
        }
    }

    private static void writeTower(TowerState tower, Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue(tower.getName());
        row.createCell(counter++).setCellValue(tower.getID());
        row.createCell(counter++).setCellValue(tower.getKills());
        row.createCell(counter++).setCellValue(tower.getPosition().toString());
        row.createCell(counter++).setCellValue(Resources.VIRTUAL_HEIGHT - tower.getPosition().y);
        row.createCell(counter++).setCellValue(tower.getHasArmor());
        row.createCell(counter++).setCellValue(tower.getAttackIncreased());
        row.createCell(counter++).setCellValue(tower.getSpeedIncreased());
        row.createCell(counter++).setCellValue(tower.getRangeIncreased());
    }

    private static void createTowerHeader(Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue("Name");
        row.createCell(counter++).setCellValue("ID");
        row.createCell(counter++).setCellValue("Kills");
        row.createCell(counter++).setCellValue("Position");
        row.createCell(counter++).setCellValue("Y Flipped");
        row.createCell(counter++).setCellValue("Armor");
        row.createCell(counter++).setCellValue("Attack Increased");
        row.createCell(counter++).setCellValue("Speed Increased");
        row.createCell(counter++).setCellValue("Range Increased");

        Font font= row.getSheet().getWorkbook().createFont();
        font.setFontHeightInPoints((short)12);
        font.setBold(true);
        font.setItalic(false);

        CellStyle rowStyle = row.getSheet().getWorkbook().createCellStyle();
        rowStyle.setFont(font);

        for(int i = 0; i<counter; i++) {
            row.getCell(i).setCellStyle(rowStyle);
        }
    }

    private static void createEndStateHeader(XSSFSheet sheet, GameEndState endState, RowCounter counter){

        Row titleRow = sheet.createRow(counter.next());

        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Begin State");
        titleCell.setCellStyle(headerStyle);

        // Create stats

        Row statsHeaderRow = sheet.createRow(counter.next());
        Cell waveHeaderCell = statsHeaderRow.createCell(0);
        waveHeaderCell.setCellValue("Wave");
        Cell moneyHeaderCell = statsHeaderRow.createCell(1);
        moneyHeaderCell.setCellValue("Money");
        Cell livesHeaderCell = statsHeaderRow.createCell(2);
        livesHeaderCell.setCellValue("Lives");

        PlayerState playerState = endState.getPlayerState();

        Row statsRow = sheet.createRow(counter.next());
        Cell waveCell = statsRow.createCell(0);
        waveCell.setCellValue(playerState.getWaves());
        Cell moneyCell = statsRow.createCell(0);
        moneyCell.setCellValue(playerState.getMoney());
        Cell livesCell = statsRow.createCell(0);
        livesCell.setCellValue(playerState.getLives());
    }

    private static void createStateHeader(XSSFSheet sheet, PlayerState playerState, RowCounter counter, String titleStr){

        Row titleRow = sheet.createRow(counter.next());

        CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = createHeaderFont(sheet.getWorkbook());
        headerStyle.setFont(headerFont);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(titleStr);
        titleCell.setCellStyle(headerStyle);

        // Create stats

        Row statsHeaderRow = sheet.createRow(counter.next());
        Cell waveHeaderCell = statsHeaderRow.createCell(0);
        waveHeaderCell.setCellValue("Wave");
        Cell moneyHeaderCell = statsHeaderRow.createCell(1);
        moneyHeaderCell.setCellValue("Money");
        Cell livesHeaderCell = statsHeaderRow.createCell(2);
        livesHeaderCell.setCellValue("Lives");

        Row statsRow = sheet.createRow(counter.next());
        Cell waveCell = statsRow.createCell(0);
        waveCell.setCellValue(playerState.getWaves());
        Cell moneyCell = statsRow.createCell(1);
        moneyCell.setCellValue(playerState.getMoney());
        Cell livesCell = statsRow.createCell(2);
        livesCell.setCellValue(playerState.getLives());
    }

    private static Font createHeaderFont(XSSFWorkbook wb){
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short)16);

        return headerFont;
    }

    private static class RowCounter {
        private Integer value = 0;

        public Integer next(){
            return value++;
        }

        public Integer skip(Integer amount){
            value += amount;

            return next();
        }
    }

}
