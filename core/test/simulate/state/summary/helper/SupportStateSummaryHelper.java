package simulate.state.summary.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import simulate.state.WaveState;
import simulate.state.summary.SupportStateSummary;
import simulate.state.support.SupportState;

public class SupportStateSummaryHelper {

    public Map<String, SupportStateSummary> calculateSupportStateSummaries(List<WaveState> waveStates){
        Map<String, SupportStateSummary> supportSummaries = new HashMap<>();

        return calculateSupportStateSummaries(waveStates, supportSummaries);
    }

    public Map<String, SupportStateSummary> calculateSupportStateSummaries(List<WaveState> waveStates, Map<String, SupportStateSummary> supportSummaries){


        for(WaveState waveState : waveStates){
            for(SupportState supportState : waveState.getSupportStates()){
                SupportStateSummary summary = supportSummaries.get(supportState.getSupportName());

                if(summary == null){
                    summary = new SupportStateSummary(supportState.getSupportName());
                    supportSummaries.put(supportState.getSupportName(), summary);
                }

                summary.addCount();
            }

        }

        return supportSummaries;
    }

}
