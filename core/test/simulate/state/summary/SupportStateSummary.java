package simulate.state.summary;

public class SupportStateSummary {

    private String supportName;
    private int totalCount;


    public SupportStateSummary(String supportName) {

        this.supportName = supportName;
    }

    public void addCount(){
        totalCount++;
    }

    public String getSupportName(){
        return supportName;
    }

    public int getTotalCount(){
        return totalCount;
    }
}
