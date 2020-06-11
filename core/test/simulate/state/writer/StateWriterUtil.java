package simulate.state.writer;

public class StateWriterUtil {


    static class RowCounter {
        private Integer value = 0;

        public Integer next(){
            return value++;
        }

        public Integer skip(Integer amount){
            value += amount;

            return next();
        }
    }
}
