package simulate.state.writer;

import com.badlogic.gdx.utils.Array;
import com.lastdefenders.util.Resources;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import simulate.state.WaveState;
import simulate.state.combatactor.EnemyState;
import simulate.state.combatactor.TowerState;
import simulate.state.helper.EnemyStateSummary;
import simulate.state.helper.EnemyStateSummaryHelper;
import simulate.state.helper.TowerStateSummary;
import simulate.state.helper.TowerStateSummaryHelper;
import simulate.state.writer.StateWriterUtil.RowCounter;


public class SummaryStateWriter {

    private TowerStateSummaryHelper towerStateSummaryHelper = new TowerStateSummaryHelper();
    private EnemyStateSummaryHelper enemyStateSummaryHelper = new EnemyStateSummaryHelper();


    public void writeSummary(XSSFWorkbook workbook, List<WaveState> waveStates){

        XSSFSheet sheet = workbook.createSheet("Summary");
        RowCounter rowCounter = new RowCounter();

        writeStats(sheet, rowCounter, waveStates);

        rowCounter.skip(1);// Skip for buffer

        writeEnemies(sheet, waveStates, rowCounter);

        rowCounter.skip(1);// Skip for buffer

        writeTowers(sheet, waveStates, rowCounter);

    }

    private void writeEnemies(XSSFSheet sheet, List<WaveState> waveStates, RowCounter counter){

        Row titleRow = sheet.createRow(counter.next());

        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setFontHeightInPoints((short)14);
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        titleStyle.setFont(titleFont);

        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Enemies");
        titleCell.setCellStyle(titleStyle);

        Row enemyHeaderRow = sheet.createRow(counter.next());
        createEnemySummariesHeader(enemyHeaderRow);

        Map<String, EnemyStateSummary> enemySummaries = enemyStateSummaryHelper.calculateEnemyStateSummaries(waveStates);

        writeEnemySummmaries(enemySummaries.values(), sheet, counter);


    }

    private void writeEnemySummmaries(Collection<EnemyStateSummary> enemySummaries, XSSFSheet sheet, RowCounter counter){
        for(EnemyStateSummary enemySummary : enemySummaries){
            Row enemySummaryRow = sheet.createRow(counter.next());
            writeEnemySummary(enemySummaryRow, enemySummary);
        }
    }

    private void writeEnemySummary(Row row, EnemyStateSummary enemySummary){
        int counter = 0;
        row.createCell(counter++).setCellValue(enemySummary.getEnemyName());
        row.createCell(counter++).setCellValue(enemySummary.getTotalCount());
        row.createCell(counter++).setCellValue(enemySummary.getTotalKills());
        row.createCell(counter++).setCellValue(enemySummary.getAverageKills());
        row.createCell(counter++).setCellValue(enemySummary.getAverageSpawnDelay());
        row.createCell(counter++).setCellValue(enemySummary.getTotalArmor());
        row.createCell(counter++).setCellValue(enemySummary.getTotalReachedEnd());
        row.createCell(counter++).setCellValue(enemySummary.getTotalDead());
        row.createCell(counter++).setCellValue(enemySummary.getTotalReward());
    }


    private void createEnemySummariesHeader(Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue("Name");
        row.createCell(counter++).setCellValue("Total");
        row.createCell(counter++).setCellValue("Total Kills");
        row.createCell(counter++).setCellValue("Avg Kills");
        row.createCell(counter++).setCellValue("Avg Spawn Delay");
        row.createCell(counter++).setCellValue("Total Armor");
        row.createCell(counter++).setCellValue("Total Reached End");
        row.createCell(counter++).setCellValue("Total Dead");
        row.createCell(counter++).setCellValue("Total Reward");

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

    private void writeTowers(XSSFSheet sheet, List<WaveState> waveStates, RowCounter counter){

        Row titleRow = sheet.createRow(counter.next());

        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setFontHeightInPoints((short)14);
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        titleStyle.setFont(titleFont);

        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Towers");
        titleCell.setCellStyle(titleStyle);

        Row towerHeaderRow = sheet.createRow(counter.next());
        createTowerSummariesHeader(towerHeaderRow);

        Map<String, TowerStateSummary> towerSummaries = towerStateSummaryHelper.calculateTowerSummaries(waveStates);
        writeTowerSummmaries(towerSummaries.values(), sheet, counter);


    }

    private void writeTowerSummmaries(Collection<TowerStateSummary> enemySummaries, XSSFSheet sheet, RowCounter counter){
        for(TowerStateSummary towerSummary : enemySummaries){
            Row row = sheet.createRow(counter.next());
            writeTowerSummary(row, towerSummary);
        }
    }

    private void writeTowerSummary(Row row, TowerStateSummary towerSummary){
        int counter = 0;
        row.createCell(counter++).setCellValue(towerSummary.getTowerName());
        row.createCell(counter++).setCellValue(towerSummary.getTotalCount());
        row.createCell(counter++).setCellValue(towerSummary.getTotalKills());
        row.createCell(counter++).setCellValue(towerSummary.getMaxKill());
        row.createCell(counter++).setCellValue(towerSummary.getAverageKills());
        row.createCell(counter++).setCellValue(towerSummary.getTotalArmor());
        row.createCell(counter++).setCellValue(towerSummary.getTotalAttackIncrease());
        row.createCell(counter++).setCellValue(towerSummary.getTotalSpeedIncrease());
        row.createCell(counter++).setCellValue(towerSummary.getTotalRangeIncrease());
        row.createCell(counter++).setCellValue(towerSummary.getTotalDead());
    }


    private void createTowerSummariesHeader(Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue("Name");
        row.createCell(counter++).setCellValue("Total");
        row.createCell(counter++).setCellValue("Total Kills");
        row.createCell(counter++).setCellValue("Max Kill");
        row.createCell(counter++).setCellValue("Avg Kills");
        row.createCell(counter++).setCellValue("Total Armor");
        row.createCell(counter++).setCellValue("Total Attack Increase");
        row.createCell(counter++).setCellValue("Total Speed Increase");
        row.createCell(counter++).setCellValue("Total Range Increase");
        row.createCell(counter++).setCellValue("Total Dead");

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


    private void writeStats(XSSFSheet sheet, RowCounter rowCounter, List<WaveState> waveStates){

        Row titleRow = sheet.createRow(rowCounter.next());

        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setFontHeightInPoints((short)14);
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        titleStyle.setFont(titleFont);

        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Stats");
        titleCell.setCellStyle(titleStyle);

        Row headerRow = sheet.createRow(rowCounter.next());
        createStatsHeader(headerRow);

        writeStats(sheet.createRow(rowCounter.next()), waveStates);
    }


    private void writeStats(Row row, List<WaveState> waveStates){
        int counter = 0;
        row.createCell(counter++).setCellValue(waveStates.size());
        row.createCell(counter++).setCellValue(waveStates.get(waveStates.size() - 1).getLivesEnd());

    }


    private void createStatsHeader(Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue("End Wave");
        row.createCell(counter++).setCellValue("End Lives");


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

}
