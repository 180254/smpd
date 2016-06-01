package classifier.sets;


import utils.Matrix2;
import utils.Utils2;

import java.security.SecureRandom;
import java.util.*;

public class DatasetBootstrap extends Dataset {


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

    private int iterationIndex = -1;
    private int numberOfInterations = 50;

    // próbki dla każdej iteracji bootstrap w zbiorze treningowym
    List<List<Integer>> samplesNo = new ArrayList<>();

    public DatasetBootstrap(double[][] o_Dataset_N, int[] o_DataSetLabels_T, double o_TrainSetSize, String[] o_ClassNames) {
        super(o_Dataset_N, o_DataSetLabels_T, o_TrainSetSize, o_ClassNames);
        numberOfInterations = (int) o_TrainSetSize;
    }

    @Override
    public void split() {
        Random rnd = new SecureRandom();

        samplesNo.clear();
        for (int k = 0; k < numberOfInterations; k++) {
            samplesNo.add(new ArrayList<>());

            for (int i = 0; i < DataSet_T_Length; i++) {
                samplesNo.get(k).add(rnd.nextInt(DataSet_T_Length));
            }

            samplesNo.get(k).sort(Integer::compare);
        }
    }

    @Override
    public boolean nextData() {
        iterationIndex++;
        if (iterationIndex >= numberOfInterations) return false;

        // wypełnienie TrainingSet_T danymi, metoda bootstrap
        TrainingSet_T_Length = DataSet_T_Length;
        TrainingSet_T = new double[TrainingSet_T_Length][Features_V_Length];
        TrainingLabels_T = new int[TrainingSet_T_Length];

        for (int i = 0; i < TrainingSet_T_Length; i++) {
            int sampleNo = samplesNo.get(iterationIndex).get(i);
            TrainingSet_T[i] = o_Dataset_T[sampleNo];
            TrainingLabels_T[i] = o_DataSetLabels_T[sampleNo];
        }

        Set<Object> uniqueSamplesNo = new HashSet<>();
        uniqueSamplesNo.addAll(samplesNo.get(iterationIndex));

        // wypełnienie TestSet_T danymi, metoda bootstrap
        TestSet_T_Length = DataSet_T_Length - uniqueSamplesNo.size();
        TestSet_T = new double[TestSet_T_Length][Features_V_Length];
        TestLabels_T = new int[TestSet_T_Length];

        int testIndex = 0;
        //noinspection Duplicates
        for (int i = 0; i < DataSet_T_Length; i++) {
            if (!samplesNo.get(iterationIndex).contains(i)) {
                TestSet_T[testIndex] = o_Dataset_T[i];
                TestLabels_T[testIndex] = o_DataSetLabels_T[i];
                testIndex++;
            }
        }

        // pozostale zmienne
        TrainingSet_N = Matrix2.transpose(TrainingSet_T);
        TestSet_N = Matrix2.transpose(TestSet_T);

        TrainingSets_T = Utils2.extract_classes_t(TrainingSet_T, TrainingLabels_T, ClassLength);
        TrainingSets_N = Utils2.extract_classes_n(TrainingSets_T);

        printInfo();
        return true;
    }

    @Override
    public void reset() {
        iterationIndex = -1;
    }
}


