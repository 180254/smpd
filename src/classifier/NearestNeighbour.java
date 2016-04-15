package classifier;

import utils.Math2;

public class NearestNeighbour extends Classifier {

    @Override
    public void trainClassifier() {

    }

    @Override
    public double testClassifier() {
        int ok = 0;

        for (int i = 0; i < TestSet_T.length; i++) {
            int trainingIndex = nearestNeighbour(TestSet_T[i]);
            int trainingLabel = TrainingLabels_T[trainingIndex];
            int properLabel = TestLabels_T[i];

            if (properLabel == trainingLabel) ok++;
        }

        return ok / (double) TestSet_T.length;

    }

    /**
     * Najblizszy sasiad dla TrainingSet_T.
     * wynik = indeks z TrainingSet_T z najblizszym sasiadem
     */
    private int nearestNeighbour(double[] features) {
        double[] distances = new double[TrainingSet_T.length];

        for (int i = 0; i < TrainingSet_T.length; i++) {
            distances[i] = Math2.distance_euclidean(TrainingSet_T[i], features);
        }

        return Math2.arg_min(distances);
    }
}
