package simulate.state;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Eric on 12/17/2019.
 */

public class StateWriter {

    public static void save(List<GameState> states){
        StringBuilder sb = new StringBuilder();
        for(GameState state : states){
            GameBeginState beginState = state.getBeginState();
            GameEndState endState = state.getEndState();

            sb.append(writeBeginState(beginState));
            sb.append("\n\n");
            sb.append(writeEndState(endState));
            sb.append("\n\n");
        }

        try {
            writeFile(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void writeFile(String s) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("state.csv"));
        writer.write(s);

        writer.close();
    }

    private static String writeEndState(GameEndState endState){
        StringBuilder sb = new StringBuilder();

        PlayerState player = endState.getPlayerState();
        sb.append("END STATE,Lives\n");
        sb.append( appendArgs(
            "",
            player.getLives())
        );

        sb.append("\n\nTowers\n\n");
        sb.append("Name,ID,Kills,Position,Armor,Attack Increased,Speed Increased,Range Increased\n");
        for(TowerState tower : endState.getTowers()){
            sb.append( appendArgs(
                tower.getName(),
                tower.getID(),
                tower.getKills(),
                tower.getPosition(),
                tower.getHasArmor(),
                tower.getAttackIncreased(),
                tower.getSpeedIncreased(),
                tower.getRangeIncreased()

            ));
            sb.append("\n");

        }

        return sb.toString();
    }

    private static String writeBeginState(GameBeginState beginState){

        StringBuilder sb = new StringBuilder();

        PlayerState player = beginState.getPlayerState();
        sb.append("BEGIN STATE, Wave,Money,Lives\n");
        sb.append( appendArgs(
            "",
            player.getWaves(),
            player.getMoney(),
            player.getLives())
        );

        sb.append("\n\nTowers\n\n");
        sb.append("Name,ID, Kills,Position,Armor,Attack Increased,Speed Increased,Range Increased\n");
        for(TowerState tower : beginState.getTowers()){
            sb.append( appendArgs(
                tower.getName(),
                tower.getID(),
                tower.getKills(),
                tower.getPosition(),
                tower.getHasArmor(),
                tower.getAttackIncreased(),
                tower.getSpeedIncreased(),
                tower.getRangeIncreased()

            ));
            sb.append("\n");

        }

        sb.append("\n\nEnemies\n\n");
        sb.append("Name,ID,Armor,Speed,Spawn Delay\n");
        for(EnemyState enemy : beginState.getEnemies()){
            sb.append( appendArgs(
                enemy.getName(),
                enemy.getID(),
                enemy.getHasArmor(),
                enemy.getSpeed(),
                enemy.getSpawnDelay()

            ));
            sb.append("\n");

        }

        return sb.toString();
    }

    private static String appendArgs(Object...args){
        String s = "";
        Character c = '"';
        for(int i = 0; i < args.length; i++){
            if(args[i] != null){
                s += c.toString() + args[i] + c.toString();
            }

            if(i < args.length - 1){
                s +=",";
            }
        }

        return s;
    }

}
