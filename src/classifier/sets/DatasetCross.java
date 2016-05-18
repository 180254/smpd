package classifier.sets;

public class DatasetCross extends Dataset {

    public DatasetCross(double[][] o_Dataset_N, int[] o_DataSetLabels_T, double o_TrainSetSize, String[] o_ClassNames) {
        super(o_Dataset_N, o_DataSetLabels_T, o_TrainSetSize, o_ClassNames);
    }

    @Override
    public void split() {

    }

    @Override
    public boolean nextData() {
        return false;
    }

    @Override
    public void reset() {

    }
}
