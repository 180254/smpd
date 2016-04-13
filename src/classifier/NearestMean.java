package classifier;

import utils.Math2;
import utils.Matrix;
import utils.Utils;

public class NearestMean extends Classifier {

    private double[][][] TrainingSets_N; // double[][] dla każdej klasy, pierwszy wymiar to nr klasy (label)
    private double[][][] TrainingSetsMeans_N;
    private double[][][] TrainingSetsCovarianceInv;

    @Override
    public void trainClassifier() {
        TrainingSets_N = new double[ClassNames.length][][];
        TrainingSetsMeans_N = new double[ClassNames.length][][];
        TrainingSetsCovarianceInv = new double[ClassNames.length][][];

        for (int i = 0; i < ClassNames.length; i++) {
            int[] indexes = Utils.args_for_value(TrainingLabels_T, i);
            TrainingSets_N[i] = Matrix.transpose(Utils.extract_rows(TrainingSet_T, indexes));
        }

        for (int i = 0; i < ClassNames.length; i++) {
            TrainingSetsMeans_N[i] = Math2.means_n(TrainingSets_N[i]);

            TrainingSetsCovarianceInv[i] = Math2.covariance(TrainingSets_N[i], TrainingSetsMeans_N[i]);
            TrainingSetsCovarianceInv[i] = Matrix.inverse(TrainingSetsCovarianceInv[i]);
        }
    }

    @Override
    public double testClassifier() {
        int ok = 0;

        for (int i = 0; i < TestSet_T.length; i++) {

            int classLabel = nearestMean(TestSet_T[i]);
            int properLabel = TestLabels_T[i];

            if (properLabel == classLabel) ok++;
        }

        return ok / (double) TestSet_T.length;
    }

    /**
     * wynik = indeks z TrainingSets_N, wskazujacy do ktorej klasy (label( jest najblizej
     */
    private int nearestMean(double[] features) {
        double[][] point_n = Matrix.transpose(Matrix.to_matrix(features));
        double[] distances = new double[TrainingSets_N.length];

        for (int i = 0; i < TrainingSets_N.length; i++) {
            distances[i] = Math2.distanceMahalanobis(point_n, TrainingSetsMeans_N[i], TrainingSetsCovarianceInv[i]);
        }
        return Math2.argMin(distances);
    }
}