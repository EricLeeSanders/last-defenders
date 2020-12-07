package simulate.state;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import simulate.state.summary.aggregate.RoundStatsAggregate;

public class StateUtil {

    public static void sortRoundStatisticsByAverage(List<RoundStatsAggregate> stats){
        Collections.sort(stats, new Comparator<RoundStatsAggregate>() {


            @Override
            public int compare(RoundStatsAggregate o1, RoundStatsAggregate o2) {

                if(o1.getStats().getAverage() > o2.getStats().getAverage()){
                    return -1;
                } else if(o2.getStats().getAverage() > o1.getStats().getAverage()){
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }


}
