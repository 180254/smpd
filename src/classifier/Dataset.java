package classifier;

import utils.Matrix2;
import utils.Utils2;

public class Dataset {

    public String[] ClassNames;

    public double[][] TrainingSet_N, TestSet_N;
    public double[][] TrainingSet_T, TestSet_T;
    public int[] TrainingLabels_T, TestLabels_T;

    public double[][][] TrainingSets_N; // [class_id][feature][sample]
    double[][][] TrainingSets_T; // [class_id][sample][feature]

    public Dataset() {
    }

    public boolean splitted() {
        return ClassNames != null;
    }

    public void generate(double[][] Dataset_N, int[] DataSetLabels_T, double TrainSetSize, String[] classNames) {
        generateTrainingAndTestSets(Dataset_N, DataSetLabels_T, TrainSetSize, classNames);
        generatePerClassTrainingSets();
    }

    private void generateTrainingAndTestSets(
            double[][] Dataset_N, int[] DataSetLabels_T, double TrainSetSize, String[] classNames) {
        this.ClassNames = classNames;

        double[][] Dataset_T = Matrix2.transpose(Dataset_N);
        // Dataset_N.length = liczba cech
        // Dataset_T.length = liczba probek

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

        System.out.printf("TrainingSet_T.length = %d (%.0f%%)%n",
                TrainingSet_T.length,
                TrainingSet_T.length / (double) Dataset_T.length * 100);
        System.out.println("TestSet_T.length = " + TestSet_T.length);
        System.out.println("Features_V.length = " + TrainingSet_N.length);
    }

    private void generatePerClassTrainingSets() {
        TrainingSets_T = Utils2.extract_classes_t(TrainingSet_T, TrainingLabels_T, ClassNames);
        TrainingSets_N = Utils2.extract_classes_n(TrainingSets_T);
    }
}