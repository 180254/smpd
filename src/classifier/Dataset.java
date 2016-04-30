package classifier;

import utils.Matrix2;
import utils.Utils2;

public class Dataset {

    public String[] ClassNames;
    public int ClassLength;

    public double[][] TrainingSet_N, TestSet_N;
    public double[][] TrainingSet_T, TestSet_T;
    public int[] TrainingLabels_T, TestLabels_T;

    public double[][][] TrainingSets_N; // [class_id][feature][sample]
    double[][][] TrainingSets_T; // [class_id][sample][feature]

    int DataSet_T_Length;
    int TrainingSet_T_Length;
    int TestSet_T_Length;
    int Features_V_Length;

    public Dataset() {
    }

    public boolean hasData() {
        return ClassNames != null;
    }

    /**
     * 1. Dzieli zbiory na treningowy i testowy.
     * 2. Wydziela te zbiory dla konkretnych klas.
     *
     * @param Dataset_N       kompletny zbiór próbek
     * @param DataSetLabels_T indeksy klas do których należą kolejne próbki
     * @param TrainSetSize    procentowy udział zbioru treningowego
     * @param ClassNames      nazwy klas, których przekazany został zbiór próbek
     */
    public void generate(double[][] Dataset_N, int[] DataSetLabels_T, double TrainSetSize, String[] ClassNames) {
        generateTrainingAndTestSets(Dataset_N, DataSetLabels_T, TrainSetSize, ClassNames);
        generatePerClassTrainingSets();
    }

    private void generateTrainingAndTestSets(
            double[][] Dataset_N, int[] DataSetLabels_T, double TrainSetSize, String[] ClassNames) {
        this.ClassNames = ClassNames;
        this.ClassLength = ClassNames.length;

        double[][] Dataset_T = Matrix2.transpose(Dataset_N);
        int[] Index = new int[Dataset_T.length];
        double Th = TrainSetSize / 100.0;

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

        TrainingSet_T = new double[TrainCount][Dataset_N.length];
        TestSet_T = new double[TestCount][Dataset_N.length];

        TrainingLabels_T = new int[TrainCount];
        TestLabels_T = new int[TestCount];

        TrainCount = 0;
        TestCount = 0;

        // label vectors for training/test sets
        for (int i = 0; i < Index.length; i++) {
            if (Index[i] == TRAIN_SET) {
                TrainingLabels_T[TrainCount] = DataSetLabels_T[i];
                System.arraycopy(Dataset_T[i], 0, TrainingSet_T[TrainCount], 0, Dataset_N.length);
                TrainCount++;

            } else {
                TestLabels_T[TestCount] = DataSetLabels_T[i];
                System.arraycopy(Dataset_T[i], 0, TestSet_T[TestCount], 0, Dataset_N.length);
                TestCount++;
            }
        }

        TrainingSet_N = Matrix2.transpose(TrainingSet_T);
        TestSet_N = Matrix2.transpose(TestSet_T);

        DataSet_T_Length = Dataset_T.length;
        TrainingSet_T_Length = TrainingSet_T.length;
        TestSet_T_Length = TestSet_T.length;
        Features_V_Length = TrainingSet_N.length;

        double trainingSetPercent = TrainingSet_T_Length / (double) DataSet_T_Length * 100;
        System.out.printf("TrainingSet_T.length = %d (%.0f%%)%n", TrainingSet_T_Length, trainingSetPercent);
        System.out.println("TestSet_T.length = " + TestSet_T_Length);
        System.out.println("Features_V.length = " + Features_V_Length);
    }

    private void generatePerClassTrainingSets() {
        TrainingSets_T = Utils2.extract_classes_t(TrainingSet_T, TrainingLabels_T, ClassLength);
        TrainingSets_N = Utils2.extract_classes_n(TrainingSets_T);
    }
}