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
import simulate.state.writer.StateWriterUtil.RowCounter;


public class SummaryStateWriter {


    public static void writeSummary(XSSFWorkbook workbook, List<WaveState> waveStates){

        XSSFSheet sheet = workbook.createSheet("Summary");
        RowCounter rowCounter = new RowCounter();

        writeStats(sheet, rowCounter, waveStates);

        rowCounter.skip(1);// Skip for buffer

        writeEnemies(sheet, waveStates, rowCounter);

        rowCounter.skip(1);// Skip for buffer

        writeTowers(sheet, waveStates, rowCounter);

    }

    private static void writeEnemies(XSSFSheet sheet, List<WaveState> waveStates, RowCounter counter){

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

        Map<String, EnemySummary> enemySummaries = new HashMap<>();
        for(WaveState waveState : waveStates){
            for(EnemyState enemy : waveState.getEnemyStates()){
                EnemySummary enemySummary = enemySummaries.get(enemy.getName());

                if(enemySummary == null){
                    enemySummary = new EnemySummary(enemy.getName());
                    enemySummaries.put(enemy.getName(), enemySummary);
                }

                enemySummary.addCount();
                enemySummary.addKills(enemy.getKills());
                enemySummary.addSpawnDelay(enemy.getSpawnDelay());
                if(enemy.getHasArmor()) {
                    enemySummary.addArmorCount();
                }

                if(enemy.getReachedEnd()) {
                    enemySummary.addReachedEndCount();
                }

                if(enemy.isDead()) {
                    enemySummary.addDeadCount();
                    enemySummary.addReward(enemy.getReward());
                }

            }
        }

        writeEnemySummmaries(enemySummaries.values(), sheet, counter);


    }

    private static void writeEnemySummmaries(Collection<EnemySummary> enemySummaries, XSSFSheet sheet, RowCounter counter){
        for(EnemySummary enemySummary : enemySummaries){
            Row enemySummaryRow = sheet.createRow(counter.next());
            writeEnemySummary(enemySummaryRow, enemySummary);
        }
    }

    private static void writeEnemySummary(Row row, EnemySummary enemySummary){
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


    private static void createEnemySummariesHeader(Row row){
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

    private static void writeTowers(XSSFSheet sheet, List<WaveState> waveStates, RowCounter counter){

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

        Map<String, TowerSummary> towerSummaries = new HashMap<>();
        for(int i = 0; i < waveStates.size(); i++){
            WaveState waveState = waveStates.get(i);
            for(TowerState tower : waveState.getTowerStates()){
                TowerSummary towerSummary = towerSummaries.get(tower.getName());

                if(towerSummary == null){
                    towerSummary = new TowerSummary(tower.getName());
                    towerSummaries.put(tower.getName(), towerSummary);
                }

                towerSummary.addCount();

                if(tower.getHasArmor()) {
                    towerSummary.addArmorCount();
                }

                if(tower.getAttackIncreased()){
                    towerSummary.addAttackIncreaseCount();
                }

                if(tower.getRangeIncreased()){
                    towerSummary.addRangeIncreaseCount();
                }

                if(tower.getSpeedIncreased()){
                    towerSummary.addSpeedIncreaseCount();
                }

                if(tower.isDead()) {
                    towerSummary.addDeadCount();
                }
                /*
                If the tower is dead, or we are on the last state, we add the kills.
                */
                if(tower.isDead() || i == waveStates.size() - 1) {
                    towerSummary.addKills(tower.getKills());
                }

            }
        }

        for(TowerSummary towerSummary : towerSummaries.values()){
            towerSummary.calculateMaxKill(waveStates);
        }

        writeTowerSummmaries(towerSummaries.values(), sheet, counter);


    }

    private static void writeTowerSummmaries(Collection<TowerSummary> enemySummaries, XSSFSheet sheet, RowCounter counter){
        for(TowerSummary towerSummary : enemySummaries){
            Row row = sheet.createRow(counter.next());
            writeTowerSummary(row, towerSummary);
        }
    }

    private static void writeTowerSummary(Row row, TowerSummary towerSummary){
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


    private static void createTowerSummariesHeader(Row row){
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


    private static void writeStats(XSSFSheet sheet, RowCounter rowCounter, List<WaveState> waveStates){

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


    private static void writeStats(Row row, List<WaveState> waveStates){
        int counter = 0;
        row.createCell(counter++).setCellValue(waveStates.size());
        row.createCell(counter++).setCellValue(waveStates.get(waveStates.size() - 1).getLivesEnd());

    }


    private static void createStatsHeader(Row row){
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

    private static class TowerSummary{

        private String towerName;

        private int totalCount = 0;
        private int totalKills = 0;
        private int maxKill = 0;
        private int totalArmor = 0;
        private int totalAttackIncrease = 0;
        private int totalSpeedIncrease = 0;
        private int totalRangeIncrease = 0;
        private int totalDead = 0;


        public TowerSummary(String towerName){
            this.towerName = towerName;
        }

        public void addCount(){
            totalCount++;
        }

        public void addKills(int kills){
            this.totalKills += kills;
        }


        public void addArmorCount(){
            totalArmor++;
        }

        public void addAttackIncreaseCount(){
            totalAttackIncrease++;
        }

        public void addSpeedIncreaseCount(){
            totalSpeedIncrease++;
        }

        public void addRangeIncreaseCount(){
            totalRangeIncrease++;
        }
        public void addDeadCount(){
            totalDead++;
        }

        public void calculateMaxKill(List<WaveState> states){
            int best = 0;
            for(WaveState waveState : states){
                for(TowerState towerState : waveState.getTowerStates()){
                    if(towerState.getKills() > best){
                        best = towerState.getKills();
                    }
                }
            }

            maxKill = best;
        }

        public int getMaxKill(){
            return maxKill;
        }

        public int getTotalCount() {

            return totalCount;
        }

        public int getTotalKills() {

            return totalKills;
        }

        public float getAverageKills(){
            return (float)totalKills / (float)totalCount;
        }

        public int getTotalArmor() {

            return totalArmor;
        }

        public int getTotalAttackIncrease(){

            return totalAttackIncrease;
        }

        public int getTotalSpeedIncrease(){
            return totalSpeedIncrease;
        }

        public int getTotalRangeIncrease(){
            return totalRangeIncrease;
        }

        public int getTotalDead() {

            return totalDead;
        }

        public String getTowerName(){
            return towerName;
        }
    }

    private static class EnemySummary{

        private String enemyName;

        private int totalCount = 0;
        private int totalKills = 0;
        private float totalSpawnDelay = 0;
        private int totalArmor = 0;
        private int totalReachedEnd = 0;
        private int totalDead = 0;
        private int totalReward = 0;


        public EnemySummary(String enemyName){
            this.enemyName = enemyName;
        }

        public void addCount(){
            totalCount++;
        }

        public void addKills(int kills){
            this.totalKills += kills;
        }

        public void addSpawnDelay(float spawnDelay){
            this.totalSpawnDelay += spawnDelay;
        }

        public void addArmorCount(){
            totalArmor++;
        }

        public void addReachedEndCount(){
            totalReachedEnd++;
        }

        public void addDeadCount(){
            totalDead++;
        }

        public void addReward(int reward){
            this.totalReward += reward;
        }

        public int getTotalCount() {

            return totalCount;
        }

        public int getTotalKills() {

            return totalKills;
        }

        public float getAverageKills(){
            return (float)totalKills / (float)totalCount;
        }

        public float getTotalSpawnDelay() {

            return totalSpawnDelay;
        }

        public float getAverageSpawnDelay(){
            return totalSpawnDelay / totalCount;
        }

        public int getTotalArmor() {

            return totalArmor;
        }

        public int getTotalReachedEnd() {

            return totalReachedEnd;
        }

        public int getTotalDead() {

            return totalDead;
        }

        public int getTotalReward() {

            return totalReward;
        }

        public String getEnemyName(){
            return enemyName;
        }
    }

}
