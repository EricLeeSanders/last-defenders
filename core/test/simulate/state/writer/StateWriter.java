package simulate.state.writer;

import com.badlogic.gdx.utils.Array;
import com.lastdefenders.levelselect.LevelName;
import com.lastdefenders.util.Resources;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import simulate.state.WaveState;
import simulate.state.combatactor.EnemyState;
import simulate.state.combatactor.TowerState;
import simulate.state.support.SupportState;
import simulate.state.writer.StateWriterUtil.RowCounter;

/**
 * Created by Eric on 12/17/2019.
 */

public class StateWriter {

    private static final String SIMULATION_SAVE_PATH = "../../files/simulation/simulations/";

    private SummaryStateWriter summaryStateWriter = new SummaryStateWriter();
    private SnapshotWriter snapshotWriter = new SnapshotWriter();

    public void save(List<WaveState> states, LevelName levelName, SimulationRunType simulationType) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();

        summaryStateWriter.writeSummary(workbook, states);

        for(WaveState state : states){
            saveState(workbook, state, levelName);
        }

        SimpleDateFormat simpleDateFormat =
            new SimpleDateFormat("MMddYYYYhhmmss");
        String dateAsString = simpleDateFormat.format(new Date());

        FileOutputStream fos = new FileOutputStream(SIMULATION_SAVE_PATH+ dateAsString + "-" + simulationType + ".xlsx");
        workbook.write(fos);
        fos.close();

    }

    private void saveState(XSSFWorkbook workbook, WaveState state, LevelName levelName)
        throws IOException {
        RowCounter rowCounter = new RowCounter();
        Integer wave = state.getWaveNumber();
        XSSFSheet sheet = workbook.createSheet("wave " + wave);

        writeState(sheet, state, levelName, rowCounter);
    }

    private void writeState(XSSFSheet sheet, WaveState waveState, LevelName levelName, RowCounter rowCounter) throws IOException {
        createStats(sheet, waveState, rowCounter, "Stats");

        rowCounter.skip(1);// Skip for buffer

        writeTowers(sheet, waveState.getTowerStates(), rowCounter);

        rowCounter.skip(1);// Skip for buffer

        writeEnemies(sheet, waveState.getEnemyStates(), rowCounter);

        rowCounter.skip(1);// Skip for buffer

        writeSupports(sheet, waveState.getSupportStates(), rowCounter);

        addSnapshot(sheet, waveState.getTowerStates(), waveState.getEnemyStates(), waveState.getSupportStates(), levelName, rowCounter);
    }


    private void addSnapshot(XSSFSheet sheet, Array<TowerState> towers, Array<EnemyState> enemies, Array<SupportState> supportStates, LevelName levelName, RowCounter counter)
        throws IOException {

        byte [] snapshot = snapshotWriter.createSnapshot(towers, enemies, supportStates, levelName);
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

    private void writeEnemies(XSSFSheet sheet, Array<EnemyState> enemies, RowCounter counter){

        Row titleRow = sheet.createRow(counter.next());

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

    private void writeEnemy(EnemyState enemy, Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue(enemy.getName());
        row.createCell(counter++).setCellValue(enemy.getID());
        row.createCell(counter++).setCellValue(enemy.getKills());
        row.createCell(counter++).setCellValue(enemy.getHasArmor());
        row.createCell(counter++).setCellValue(enemy.getSpeed());
        row.createCell(counter++).setCellValue(enemy.getSpawnDelay());
        row.createCell(counter++).setCellValue(enemy.isDead());
        row.createCell(counter++).setCellValue(enemy.getReachedEnd());
        if(enemy.isDead()) {
            row.createCell(counter++).setCellValue(enemy.getDeadPosition().toString());
            row.createCell(counter++)
                .setCellValue(Resources.VIRTUAL_HEIGHT - enemy.getDeadPosition().y);
        }
    }


    private void createEnemyHeader(Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue("Name");
        row.createCell(counter++).setCellValue("ID");
        row.createCell(counter++).setCellValue("Kills");
        row.createCell(counter++).setCellValue("Armor");
        row.createCell(counter++).setCellValue("Speed");
        row.createCell(counter++).setCellValue("Spawn Delay");
        row.createCell(counter++).setCellValue("Dead");
        row.createCell(counter++).setCellValue("Reached End");
        row.createCell(counter++).setCellValue("Position");
        row.createCell(counter++).setCellValue("Y Flipped");

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

    private void writeTowers(XSSFSheet sheet, Array<TowerState> towers, RowCounter counter){

        Row towerTitleRow = sheet.createRow(counter.next());

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

    private void writeTower(TowerState tower, Row row){
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
        row.createCell(counter++).setCellValue(tower.isDead());
    }

    private void createTowerHeader(Row row){
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
        row.createCell(counter++).setCellValue("Dead");

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

    private void writeSupports(XSSFSheet sheet, Array<SupportState> supportStates, RowCounter counter){

        Row supportTitleRow = sheet.createRow(counter.next());

        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setFontHeightInPoints((short)14);
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        titleStyle.setFont(titleFont);

        Cell titleCell = supportTitleRow.createCell(0);
        titleCell.setCellValue("Support");
        titleCell.setCellStyle(titleStyle);

        Row supportHeaderRow = sheet.createRow(counter.next());
        createSupportHeader(supportHeaderRow);

        Map<String, Integer> supportCounter = new HashMap<>();
        for(SupportState s : supportStates){
            Integer count = supportCounter.get(s.getClass().getSimpleName());
            supportCounter.put(s.getClass().getSimpleName(), (count == null) ? 1 : count + 1);
        }

        for (Map.Entry<String, Integer> val : supportCounter.entrySet()) {
            Row row = sheet.createRow(counter.next());
            writeSupport(val.getKey(), val.getValue(), row);
        }
    }

    private void writeSupport(String name, Integer count, Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue(name);
        row.createCell(counter++).setCellValue(count);

    }

    private void createSupportHeader(Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue("Name");
        row.createCell(counter++).setCellValue("Count");

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

    private void createStats(XSSFSheet sheet, WaveState waveState, RowCounter counter, String titleStr){

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
        Cell startMoneyHeaderCell = statsHeaderRow.createCell(1);
        startMoneyHeaderCell.setCellValue("Start Money");
        Cell endMoneyHeaderCell = statsHeaderRow.createCell(2);
        endMoneyHeaderCell.setCellValue("End Money");
        Cell startLivesHeaderCell = statsHeaderRow.createCell(3);
        startLivesHeaderCell.setCellValue("Start Lives");
        Cell endLivesHeaderCell = statsHeaderRow.createCell(4);
        endLivesHeaderCell.setCellValue("End Lives");

        Row statsRow = sheet.createRow(counter.next());
        statsRow.createCell(0).setCellValue(waveState.getWaveNumber());
        statsRow.createCell(1).setCellValue(waveState.getMoneyStart());
        statsRow.createCell(2).setCellValue(waveState.getMoneyEnd());
        statsRow.createCell(3).setCellValue(waveState.getLivesStart());
        statsRow.createCell(4).setCellValue(waveState.getLivesEnd());
    }

    private static Font createHeaderFont(XSSFWorkbook wb){
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short)16);

        return headerFont;
    }


}
