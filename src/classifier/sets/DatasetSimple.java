package classifier.sets;

import utils.Matrix2;
import utils.Utils2;

public class DatasetSimple extends Dataset {

    int iterationIndex = -1;


    /*
    public String[] ClassNames;
    public int ClassLength;

    public double[][] TrainingSet_N, TestSet_N;
    public double[][] TrainingSet_T, TestSet_T;
    public int[] TrainingLabels_T, TestLabels_T;

    public double[][][] TrainingSets_N; // [class_id][feature][sample]
    public double[][][] TrainingSets_T; // [class_id][sample][feature]

    public int DataSet_T_Length;
    public int TrainingSet_T_Length;
    public int TestSet_T_Length;
    public int Features_V_Length;

    protected double[][] o_Dataset_N;
    protected double[][] o_Dataset_T;
    protected int[] o_DataSetLabels_T;
    protected double o_TrainSetSize;
    protected String[] o_ClassNames;
    */


    public DatasetSimple(double[][] o_Dataset_N, int[] o_DataSetLabels_T, double o_TrainSetSize, String[] o_ClassNames) {
        super(o_Dataset_N, o_DataSetLabels_T, o_TrainSetSize, o_ClassNames);
    }

    @Override
    public void split() {
        /**
         * Prosty podział, gdzie o_TrainSetSize procent próbek będzie w zbiorze treningowym.
         * Z uwagi na zastosowany algorytm o_TrainSetSize jest przybliżeniem.
         * Uzyskany procent będzie bardzo zbliżony do oczekiwanego, ale nie idealny.
         */

        double[][] Dataset_T = Matrix2.transpose(o_Dataset_N);
        int[] Index = new int[Dataset_T.length];
        double Th = o_TrainSetSize / 100.0;

        int TrainCount = 0, TestCount = 0;
        int TRAIN_SET = 0;
        int TEST_SET = 1;

        for (int i = 0; i < Dataset_T.length; i++) {
            if (Math.random() <= Th) {
                Index[i] = TRAIN_SET;
                TrainCount++;
            } else {
                Index[i] = TEST_SET;
                TestCount++;
            }
        }

        TrainingSet_T = new double[TrainCount][o_Dataset_N.length];
        TestSet_T = new double[TestCount][o_Dataset_N.length];

        TrainingLabels_T = new int[TrainCount];
        TestLabels_T = new int[TestCount];

        TrainCount = 0;
        TestCount = 0;

        // label vectors for training/test sets
        for (int i = 0; i < Index.length; i++) {
            if (Index[i] == TRAIN_SET) {
                TrainingLabels_T[TrainCount] = o_DataSetLabels_T[i];
                System.arraycopy(Dataset_T[i], 0, TrainingSet_T[TrainCount], 0, o_Dataset_N.length);
                TrainCount++;

            } else {
                TestLabels_T[TestCount] = o_DataSetLabels_T[i];
                System.arraycopy(Dataset_T[i], 0, TestSet_T[TestCount], 0, o_Dataset_N.length);
                TestCount++;
            }
        }

        TrainingSet_N = Matrix2.transpose(TrainingSet_T);
        TestSet_N = Matrix2.transpose(TestSet_T);

        DataSet_T_Length = Dataset_T.length;
        TrainingSet_T_Length = TrainingSet_T.length;
        TestSet_T_Length = TestSet_T.length;

        TrainingSets_T = Utils2.extract_classes_t(TrainingSet_T, TrainingLabels_T, ClassLength);
        TrainingSets_N = Utils2.extract_classes_n(TrainingSets_T);
    }

    @Override
    public boolean nextData() {

        /**
         * Tylko jedna iteracja. Implementacja nie wspiera n iteracji, tak, by iteracje były powtarzalne.
         */
        iterationIndex++;
        boolean ret = iterationIndex < 1;
        if (ret) printInfo();
        return ret;
    }

    @Override
    public void reset() {
        iterationIndex = -1;
    }
}
