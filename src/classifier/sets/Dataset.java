package classifier.sets;

import utils.Matrix2;

public abstract class Dataset {

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

    public Dataset(double[][] o_Dataset_N, int[] o_DataSetLabels_T, double o_TrainSetSize, String[] o_ClassNames) {
        this.o_Dataset_N = o_Dataset_N;
        this.o_DataSetLabels_T = o_DataSetLabels_T;
        this.o_TrainSetSize = o_TrainSetSize;
        this.o_ClassNames = o_ClassNames;

        ClassNames = o_ClassNames;
        ClassLength = ClassNames.length;

        o_Dataset_T = Matrix2.transpose(o_Dataset_N);
        DataSet_T_Length = o_Dataset_T.length;
        Features_V_Length = o_Dataset_N.length;
    }

    /**
     * Generuje zbiory danych, na których będzie dokonywana klasyfikacja.
     */
    public abstract void split();

    /**
     * while(nextData()) {...}
     * Dataset może zawierać więcej niż jeden zbiór danych na którym należy dokonać obliczeń.
     * nextData() ustawia zmienne na kolejny zbiór danych.
     * Zwraca informację, czy przestawienie zostało dokonane.
     * jeżeli return = false koniec iteracji.
     */
    public abstract boolean nextData();

    /**
     * Resetuje "iterator", tak by znów było można wykonać:
     * while(nextData()) {...}
     */
    public abstract void reset();


    /**
     * Wypisuje informacje o aktualnym stanie danych.
     */
    public void printInfo() {
        double trainingSetPercent = TrainingSet_T_Length / (double) DataSet_T_Length * 100;
        System.out.printf("TrainingSet_T.length = %d (%.0f%%)%n", TrainingSet_T_Length, trainingSetPercent);

        double testSetPercent = TestSet_T_Length / (double) DataSet_T_Length * 100;
        System.out.printf("TestSet_T.length = %d (%.0f%%)%n", TestSet_T_Length, testSetPercent);

        System.out.println("Features_V.length = " + Features_V_Length);
    }
}
