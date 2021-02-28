package simulate.state.writer;

import com.lastdefenders.levelselect.LevelName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import simulate.SimulationRunType;
import simulate.state.AggregateSimulationState;
import simulate.state.SingleSimulationState;
import simulate.state.StateUtil;
import simulate.state.WaveState;
import simulate.state.summary.EnemyStateSummary;
import simulate.state.summary.SupportStateSummary;
import simulate.state.summary.aggregate.AggregateSupportStateSummary;
import simulate.state.summary.helper.EnemyStateSummaryHelper;
import simulate.state.summary.TowerStateSummary;
import simulate.state.summary.helper.SupportStateSummaryHelper;
import simulate.state.summary.helper.TowerStateSummaryHelper;
import simulate.state.summary.aggregate.AggregateEnemyStateSummary;
import simulate.state.summary.aggregate.AggregateRoundStats;
import simulate.state.summary.aggregate.AggregateTowerStateSummary;
import simulate.state.writer.StateWriterUtil.RowCounter;

public class AggregateSummaryStateWriter {

    private TowerStateSummaryHelper towerStateSummaryHelper = new TowerStateSummaryHelper();
    private EnemyStateSummaryHelper enemyStateSummaryHelper = new EnemyStateSummaryHelper();
    private SupportStateSummaryHelper supportStateSummaryHelper = new SupportStateSummaryHelper();

    public void writeState(XSSFWorkbook workbook, Set<AggregateSimulationState> aggregateSimulationStates,
        LevelName levelName){

        XSSFSheet sheet = workbook.createSheet(levelName.getName());
        RowCounter rowCounter = new RowCounter();

        List<AggregateTowerStateSummary> aggregateTowerStateSummaries = new ArrayList<>();
        List<AggregateEnemyStateSummary> aggregateEnemyStateSummaries = new ArrayList<>();
        List<AggregateSupportStateSummary> aggregateSupportStateSummaries = new ArrayList<>();
        List<AggregateRoundStats> aggregateRoundStats = new ArrayList<>();

        for (AggregateSimulationState aggregateSimulationState : aggregateSimulationStates) {

            SimulationRunType simulationRunType = aggregateSimulationState.getSimulationRunType();

            Map<String, TowerStateSummary> towerSummaries = new HashMap<>();
            Map<String, EnemyStateSummary> enemySummaries = new HashMap<>();
            Map<String, SupportStateSummary> supportSummaries = new HashMap<>();
            IntSummaryStatistics stats = new IntSummaryStatistics();

            Map<Integer, Integer> numOfWavesByWaveCount = new HashMap<>();

            for (SingleSimulationState singleSimulationState : aggregateSimulationState.getSingleSimulationStates()) {

                List<WaveState> waveStates = singleSimulationState.getWaveStates();

                towerStateSummaryHelper.calculateTowerSummaries(waveStates, towerSummaries);
                enemyStateSummaryHelper.calculateEnemyStateSummaries(waveStates, enemySummaries);
                supportStateSummaryHelper.calculateSupportStateSummaries(waveStates, supportSummaries);
                int waveCount = singleSimulationState.getWaveStates().size();
                stats.accept(waveCount);

                Integer numOfWaves = numOfWavesByWaveCount.get(waveCount);
                if(numOfWaves == null){
                    numOfWaves = 0;
                }
                numOfWaves++;
                numOfWavesByWaveCount.put(waveCount, numOfWaves);
            }

            aggregateTowerStateSummaries.add( new AggregateTowerStateSummary(simulationRunType, towerSummaries.values()));
            aggregateEnemyStateSummaries.add( new AggregateEnemyStateSummary(simulationRunType, enemySummaries.values()));
            aggregateSupportStateSummaries.add( new AggregateSupportStateSummary(simulationRunType, supportSummaries.values()));
            aggregateRoundStats.add( new AggregateRoundStats(simulationRunType, stats, numOfWavesByWaveCount));
        }

        writeSummariesByRunType(sheet, rowCounter, aggregateTowerStateSummaries, aggregateEnemyStateSummaries, aggregateSupportStateSummaries, aggregateRoundStats);


    }

    private void writeSummariesByRunType(XSSFSheet sheet,
        RowCounter rowCounter,
        List<AggregateTowerStateSummary> towerStateSummaryAggregates,
        List<AggregateEnemyStateSummary> enemyStateSummaryAggregates,
        List<AggregateSupportStateSummary> supportStateSummaryAggregates,
        List<AggregateRoundStats> roundStatsAggregates){

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

    private void writeSupportSummariesAggregate(XSSFSheet sheet, List<AggregateSupportStateSummary> supportStateSummaryAggregates, RowCounter counter){

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

        for(AggregateSupportStateSummary supportStateSummaryAggregate : supportStateSummaryAggregates) {
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

    private void writeEnemySummariesAggregate(XSSFSheet sheet, List<AggregateEnemyStateSummary> enemyStateSummaryAggregates, RowCounter counter){

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

        for(AggregateEnemyStateSummary enemyStateSummaryAggregate : enemyStateSummaryAggregates) {
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


    private void writeTowerSummariesAggregate(XSSFSheet sheet, List<AggregateTowerStateSummary> towerStateSummaryAggregates, RowCounter counter){

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

        for(AggregateTowerStateSummary towerStateSummaryAggregate : towerStateSummaryAggregates) {
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

    private void writeRoundStatsAggregate(XSSFSheet sheet, RowCounter rowCounter,  List<AggregateRoundStats> roundStatsAggregates){

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

        for(AggregateRoundStats roundStats : roundStatsAggregates){
            writeRoundStats(sheet.createRow(rowCounter.next()), roundStats);
        }

    }


    private void writeRoundStats(Row row, AggregateRoundStats roundStats){
        int counter = 0;
        row.createCell(counter++).setCellValue(roundStats.getSimulationRunType().name());
        row.createCell(counter++).setCellValue(roundStats.getStats().getMin());
        row.createCell(counter++).setCellValue(roundStats.getStats().getMax());
        row.createCell(counter++).setCellValue(roundStats.getStats().getAverage());

        Map<Integer, Integer> numOfWavesByWaveCount = roundStats.getNumOfWavesByWaveCount();

        SortedSet<Integer> waveCountSortedKeys = new TreeSet<>(numOfWavesByWaveCount.keySet());
        for(Integer waveCount : waveCountSortedKeys){
            Integer numOfWaves = numOfWavesByWaveCount.get(waveCount);
            row.createCell(counter++).setCellValue(waveCount + " - " + numOfWaves);
        }

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
