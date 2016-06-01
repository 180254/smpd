package classifier.sets;

import utils.Matrix2;
import utils.Utils2;

import java.util.ArrayList;
import java.util.List;

public class DatasetCross extends Dataset {

    public static int NUMBER_OF_PARTS = 5;
    private int iterationIndex = -1;

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

    protected int samplesPerPart = -1; // liczba próbek przypadająca na każdą część
    protected int numberOfParts = -1;
    protected List<List<Integer>> samplesForPart = new ArrayList<>(); // próbki przypisane do każdej części

    public DatasetCross(double[][] o_Dataset_N, int[] o_DataSetLabels_T, double o_TrainSetSize, String[] o_ClassNames) {
        super(o_Dataset_N, o_DataSetLabels_T, o_TrainSetSize, o_ClassNames);
    }

    @Override
    public void split() {
        samplesPerPart = (int) Math.ceil(DataSet_T_Length * 1.0 / NUMBER_OF_PARTS);
        int[] samples = Utils2.range_ex(0, DataSet_T_Length);
        int[] shuffledSamples = Utils2.shuffleArray(samples);

        numberOfParts = (int) Math.ceil(DataSet_T_Length * 1.0 / samplesPerPart);

        samplesForPart.clear();
        for (int i = 0; i < numberOfParts; i++) {
            samplesForPart.add(new ArrayList<>());
        }

        int sampleIndex = 0;
        for (int i = 0; i < DataSet_T_Length; i++) {
            samplesForPart.get(sampleIndex).add(shuffledSamples[i]);
            if (samplesForPart.get(sampleIndex).size() == samplesPerPart) {
                sampleIndex++;
            }
        }
    }

    @Override
    public boolean nextData() {
        iterationIndex++;
        if (iterationIndex >= numberOfParts) return false;

        // do zbioru treningowego wpadaja wszystkie probki poza tymi z części dla aktualnej iteracji
        TrainingSet_T_Length = o_Dataset_T.length - samplesForPart.get(iterationIndex).size();
        TrainingSet_T = new double[TrainingSet_T_Length][Features_V_Length];
        TrainingLabels_T = new int[TrainingSet_T_Length];

        int trainingIndex = 0;
        //noinspection Duplicates
        for (int i = 0; i < DataSet_T_Length; i++) {
            if (!samplesForPart.get(iterationIndex).contains(i)) {
                TrainingSet_T[trainingIndex] = o_Dataset_T[i];
                TrainingLabels_T[trainingIndex] = o_DataSetLabels_T[i];
                trainingIndex++;
            }
        }

        // do zbioru testowego wpadają wszystkie próbki z części przeznaczonej dla aktualnej iteracji
        TestSet_T_Length = DataSet_T_Length - TrainingSet_T_Length;
        TestSet_T = new double[TestSet_T_Length][Features_V_Length];
        TestLabels_T = new int[TestSet_T_Length];

        for (int i = 0; i < TestSet_T_Length; i++) {
            int datasetIndex = samplesForPart.get(iterationIndex).get(i);
            TestSet_T[i] = o_Dataset_T[datasetIndex];
            TestLabels_T[i] = o_DataSetLabels_T[datasetIndex];
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
