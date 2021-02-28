package simulate.state;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import simulate.state.summary.aggregate.AggregateRoundStats;

public class StateUtil {

    public static void sortRoundStatisticsByAverage(List<AggregateRoundStats> stats){
        Collections.sort(stats, new Comparator<AggregateRoundStats>() {


            @Override
            public int compare(AggregateRoundStats o1, AggregateRoundStats o2) {

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
