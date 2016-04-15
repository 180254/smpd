package classifier;

import utils.Math2;

public class KNearestNeighbour extends Classifier {

    private Integer kParam;

    @Override
    public void trainClassifier() {
        kParam = 5;
        System.out.println("k = " + kParam);
    }

    @Override
    public double testClassifier() {
        if (kParam == null) return -1;
        int ok = 0;

        for (int i = 0; i < TestSet_T.length; i++) {
            int[] trainingIndexes = nearestNeighbours(TestSet_T[i]);
            int[] trainingLabels = trainingIndexesToLabels(trainingIndexes);
            int trainingMostPopularLabel = Math2.most_popular(trainingLabels);
            int properLabel = TestLabels_T[i];

            if (properLabel == trainingMostPopularLabel) ok++;
        }

        return ok / (double) TestSet_T.length;

    }

    /**
     * Najblizszy sasiad dla TrainingSet_T.
     * wynik = indeksy z TrainingSet_T z najblizszymi sasiadami
     */
    private int[] nearestNeighbours(double[] features) {
        double[] distances = new double[TrainingSet_T.length];

        for (int i = 0; i < TrainingSet_T.length; i++) {
            distances[i] = Math2.distance_euclidean(TrainingSet_T[i], features);
        }

        return Math2.arg_min(distances, kParam);
    }

    /**
     * Zamienia tablicę indeksów zbioru treningowego na tablicę klas.
     */
    private int[] trainingIndexesToLabels(int[] trainingIndexes) {
        int[] labels = new int[trainingIndexes.length];

        for (int i = 0; i < trainingIndexes.length; i++) {
            labels[i] = TrainingLabels_T[trainingIndexes[i]];
        }

        return labels;
    }
}
