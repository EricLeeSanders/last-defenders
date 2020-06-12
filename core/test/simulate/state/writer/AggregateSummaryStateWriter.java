package simulate.state.writer;

import com.lastdefenders.levelselect.LevelName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import simulate.SimulationRunType;
import simulate.state.StateUtil;
import simulate.state.WaveState;
import simulate.state.summary.EnemyStateSummary;
import simulate.state.summary.SupportStateSummary;
import simulate.state.summary.aggregate.SupportStateSummaryAggregate;
import simulate.state.summary.helper.EnemyStateSummaryHelper;
import simulate.state.summary.TowerStateSummary;
import simulate.state.summary.helper.SupportStateSummaryHelper;
import simulate.state.summary.helper.TowerStateSummaryHelper;
import simulate.state.summary.aggregate.EnemyStateSummaryAggregate;
import simulate.state.summary.aggregate.RoundStatsAggregate;
import simulate.state.summary.aggregate.TowerStateSummaryAggregate;
import simulate.state.writer.StateWriterUtil.RowCounter;

public class AggregateSummaryStateWriter {

    private TowerStateSummaryHelper towerStateSummaryHelper = new TowerStateSummaryHelper();
    private EnemyStateSummaryHelper enemyStateSummaryHelper = new EnemyStateSummaryHelper();
    private SupportStateSummaryHelper supportStateSummaryHelper = new SupportStateSummaryHelper();

    public void writeState(XSSFWorkbook workbook, Map<SimulationRunType, Map<Integer, List<WaveState>>> waveStatesByRunType,
        LevelName levelName){

        XSSFSheet sheet = workbook.createSheet(levelName.getName());
        RowCounter rowCounter = new RowCounter();

        List<TowerStateSummaryAggregate> towerStateSummaryAggregates = new ArrayList<>();
        List<EnemyStateSummaryAggregate> enemyStateSummaryAggregates = new ArrayList<>();
        List<SupportStateSummaryAggregate> supportStateSummaryAggregates = new ArrayList<>();
        List<RoundStatsAggregate> roundStatsAggregates = new ArrayList<>();

        for (Map.Entry<SimulationRunType, Map<Integer, List<WaveState>>> runTypeMapEntry : waveStatesByRunType.entrySet()) {

            SimulationRunType simulationRunType = runTypeMapEntry.getKey();

            List<TowerStateSummary> towerStateSummaries = new ArrayList<>();
            List<EnemyStateSummary> enemyStateSummaries = new ArrayList<>();
            List<SupportStateSummary> supportStateSummaries = new ArrayList<>();
            IntSummaryStatistics stats = new IntSummaryStatistics();

            for (Map.Entry<Integer, List<WaveState>> waveStatesEntry : runTypeMapEntry.getValue().entrySet()) {

                List<WaveState> waveStates = waveStatesEntry.getValue();

                towerStateSummaries.addAll(towerStateSummaryHelper.calculateTowerSummaries(waveStates).values());
                enemyStateSummaries.addAll(enemyStateSummaryHelper.calculateEnemyStateSummaries(waveStates).values());
                supportStateSummaries.addAll(supportStateSummaryHelper.calculateSupportStateSummaries(waveStates).values());
                stats.accept(waveStatesEntry.getValue().size());
            }

            towerStateSummaryAggregates.add( new TowerStateSummaryAggregate(simulationRunType, towerStateSummaries));
            enemyStateSummaryAggregates.add( new EnemyStateSummaryAggregate(simulationRunType, enemyStateSummaries));
            supportStateSummaryAggregates.add( new SupportStateSummaryAggregate(simulationRunType, supportStateSummaries));
            roundStatsAggregates.add( new RoundStatsAggregate(simulationRunType, stats));
        }

        writeSummariesByRunType(sheet, rowCounter, towerStateSummaryAggregates, enemyStateSummaryAggregates, supportStateSummaryAggregates, roundStatsAggregates );


    }

    private void writeSummariesByRunType(XSSFSheet sheet,
        RowCounter rowCounter,
        List<TowerStateSummaryAggregate> towerStateSummaryAggregates,
        List<EnemyStateSummaryAggregate> enemyStateSummaryAggregates,
        List<SupportStateSummaryAggregate> supportStateSummaryAggregates,
        List<RoundStatsAggregate> roundStatsAggregates){

        StateUtil.sortRoundStatisticsByAverage(roundStatsAggregates);

        writeRoundStatsAggregate(sheet, rowCounter, roundStatsAggregates);

        rowCounter.skip(1);// Skip for buffer

        writeTowerSummariesAggregate(sheet, towerStateSummaryAggregates, rowCounter);

        rowCounter.skip(1);

        writeEnemySummariesAggregate(sheet, enemyStateSummaryAggregates, rowCounter);

        rowCounter.skip(1);

        writeSupportSummariesAggregate(sheet, supportStateSummaryAggregates, rowCounter);


    }

    private void writeSimulationRunTypeSubHeader(XSSFSheet sheet, RowCounter counter, SimulationRunType simulationRunType){

        Row subTitleRow = sheet.createRow(counter.next());

        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setFontHeightInPoints((short)12);
        titleFont.setBold(true);
        titleFont.setItalic(false);
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        titleStyle.setFont(titleFont);

        Cell titleCell = subTitleRow.createCell(0);
        titleCell.setCellValue(simulationRunType.name());
        titleCell.setCellStyle(titleStyle);
    }

    private void writeSupportSummariesAggregate(XSSFSheet sheet, List<SupportStateSummaryAggregate> supportStateSummaryAggregates, RowCounter counter){

        Row titleRow = sheet.createRow(counter.next());

        Font titleFont = sheet.getWorkbook().createFont();
        titleFont.setFontHeightInPoints((short)14);
        CellStyle titleStyle = sheet.getWorkbook().createCellStyle();
        titleStyle.setFont(titleFont);

        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Support");
        titleCell.setCellStyle(titleStyle);

        Row headerRow = sheet.createRow(counter.next());
        createSupportSummariesHeader(headerRow);

        for(SupportStateSummaryAggregate supportStateSummaryAggregate : supportStateSummaryAggregates) {
            writeSimulationRunTypeSubHeader(sheet, counter, supportStateSummaryAggregate.getSimulationRunType());
            writeSupportSummaries(supportStateSummaryAggregate.getSupportStateSummaries(), sheet, counter);
            counter.skip(1);
        }


    }

    private void writeSupportSummaries(Collection<SupportStateSummary> supportSummaries, XSSFSheet sheet, RowCounter counter){
        for(SupportStateSummary supportStateSummary : supportSummaries){
            Row row = sheet.createRow(counter.next());
            writeSupportSummary(row, supportStateSummary);
        }
    }

    private void writeSupportSummary(Row row, SupportStateSummary supportStateSummary){
        int counter = 0;
        row.createCell(counter++).setCellValue(supportStateSummary.getSupportName());
        row.createCell(counter++).setCellValue(supportStateSummary.getTotalCount());
    }


    private void createSupportSummariesHeader(Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue("Name");
        row.createCell(counter++).setCellValue("Total");

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

    private void writeEnemySummariesAggregate(XSSFSheet sheet, List<EnemyStateSummaryAggregate> enemyStateSummaryAggregates, RowCounter counter){

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

        for(EnemyStateSummaryAggregate enemyStateSummaryAggregate : enemyStateSummaryAggregates) {
            writeSimulationRunTypeSubHeader(sheet, counter, enemyStateSummaryAggregate.getSimulationRunType());
            writeEnemySummaries(enemyStateSummaryAggregate.getEnemyStateSummaries(), sheet, counter);
            counter.skip(1);
        }


    }

    private void writeEnemySummaries(Collection<EnemyStateSummary> enemySummaries, XSSFSheet sheet, RowCounter counter){
        for(EnemyStateSummary enemyStateSummary : enemySummaries){
            Row row = sheet.createRow(counter.next());
            writeEnemySummary(row, enemyStateSummary);
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


    private void writeTowerSummariesAggregate(XSSFSheet sheet, List<TowerStateSummaryAggregate> towerStateSummaryAggregates, RowCounter counter){

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

        for(TowerStateSummaryAggregate towerStateSummaryAggregate : towerStateSummaryAggregates) {
            writeSimulationRunTypeSubHeader(sheet, counter, towerStateSummaryAggregate.getSimulationRunType());
            writeTowerSummaries(towerStateSummaryAggregate.getTowerStateSummaries(), sheet, counter);
            counter.skip(1);
        }


    }

    private void writeTowerSummaries(Collection<TowerStateSummary> towerStateSummaries, XSSFSheet sheet, RowCounter counter){
        for(TowerStateSummary towerSummary : towerStateSummaries){
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

    private void writeRoundStatsAggregate(XSSFSheet sheet, RowCounter rowCounter,  List<RoundStatsAggregate> roundStatsAggregates){

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

        for(RoundStatsAggregate roundStats : roundStatsAggregates){
            writeRoundStats(sheet.createRow(rowCounter.next()), roundStats);
        }

    }


    private void writeRoundStats(Row row, RoundStatsAggregate roundStats){
        int counter = 0;
        row.createCell(counter++).setCellValue(roundStats.getSimulationRunType().name());
        row.createCell(counter++).setCellValue(roundStats.getStats().getMin());
        row.createCell(counter++).setCellValue(roundStats.getStats().getMax());
        row.createCell(counter++).setCellValue(roundStats.getStats().getAverage());
    }


    private void createStatsHeader(Row row){
        int counter = 0;
        row.createCell(counter++).setCellValue("Simulation Run Type");
        row.createCell(counter++).setCellValue("Min");
        row.createCell(counter++).setCellValue("Max");
        row.createCell(counter++).setCellValue("Average");


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
