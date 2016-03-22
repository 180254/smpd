package smpd;

import pr.Classifier;

public class NearestNeighbour extends Classifier {

    @Override
    protected void trainClassifier() {

    }

    @Override
    protected double testClassifier() {
        int ok = 0;

        for (int i = 0; i < TestSet_T.length; i++) {
            int nn = nearestNeighbour(TestSet_T[i]);
            int nnLabel = TrainingLabels_T[nn];
            int properLabel = TestLabels_T[i];

            if (properLabel == nnLabel) ok++;
        }

        return ok / (double) TestSet_T.length;

    }

    /**
     * Najblizszy sasiad dla TrainingSet_T.
     * wynik = indeks z TrainingSet_T z najblizszym sasiadem
     *
     * @param features features
     * @return int
     */
    private int nearestNeighbour(double[] features) {
        double[] distances = new double[TrainingSet_T.length];

        for (int i = 0; i < TrainingSet_T.length; i++) {
            distances[i] = Utils.distance(TrainingSet_T[i], features);
        }

        return Utils.argMax(distances);
    }
}
