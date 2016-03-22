package pr;

import smpd.Utils;

public abstract class Classifier {

    protected double[][] TrainingSet_N, TestSet_N;
    protected double[][] TrainingSet_T, TestSet_T;
    protected int[] TrainingLabels_T, TestLabels_T;

    void generateTrainingAndTestSets(double[][] Dataset_N, int[] DataSetLabels_T, double TrainSetSize) {

        double[][] Dataset_T = Utils.transpose(Dataset_N);
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
                TrainingSet_T[TrainCount] = new double[Dataset_N.length];
                System.arraycopy(Dataset_T[i], 0, TrainingSet_T[TrainCount], 0, Dataset_N.length);
                TrainCount++;

            } else {
                TestLabels_T[TestCount] = DataSetLabels_T[i];
                TestSet_T[TestCount] = new double[Dataset_N.length];
                System.arraycopy(Dataset_T[i], 0, TestSet_T[TestCount], 0, Dataset_N.length);
                TestCount++;
            }
        }

        TrainingSet_N = Utils.transpose(TrainingSet_T);
        TestSet_N = Utils.transpose(TestSet_T);
    }

    protected abstract void trainClassifier();

    protected abstract double testClassifier();
}
