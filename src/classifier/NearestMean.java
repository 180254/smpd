package classifier;

import utils.Math2;
import utils.Matrix2;
import utils.Utils2;

public class NearestMean extends Classifier {

    private double[][][] TrainingSets_N; // [class_id][feature][sample]
    private double[][][] TrainingSetsMeans_N; // [class_id][feature_mean][1]
    private double[][][] TrainingSetsCovarianceInv; // [class_id][cov_inv,cov_inv]

    @Override
    public void trainClassifier() {
        // extract training set for each class
        TrainingSets_N = new double[ClassNames.length][][];
        for (int i = 0; i < ClassNames.length; i++) {
            int[] indexes = Utils2.args_for_value(TrainingLabels_T, i);
            TrainingSets_N[i] = Matrix2.transpose(Utils2.extract_rows(TrainingSet_T, indexes));
        }

        // dla kazdej klasy srednie i macierz kowariancji
        TrainingSetsMeans_N = new double[ClassNames.length][][];
        TrainingSetsCovarianceInv = new double[ClassNames.length][][];

        for (int i = 0; i < ClassNames.length; i++) {
            TrainingSetsMeans_N[i] = Math2.means_n(TrainingSets_N[i]);
            TrainingSetsCovarianceInv[i] = Math2.covariance(TrainingSets_N[i]);
            TrainingSetsCovarianceInv[i] = Matrix2.inverse(TrainingSetsCovarianceInv[i]);
        }
    }

    @Override
    @SuppressWarnings("Duplicates")
    public double testClassifier() {
        int ok = 0;
        int maxOk = TestSet_T.length;

        for (int i = 0; i < TestSet_T.length; i++) {

            try {
                int classLabel = nearestMean(TestSet_T[i]);
                int properLabel = TestLabels_T[i];
                if (properLabel == classLabel) ok++;

            } catch (RuntimeException ex) {
                maxOk--;
            }
        }

        System.out.printf("Straconych próbek: %d/%d%n", TestSet_T.length - maxOk, TestSet_T.length);
        return ok / (double) maxOk;
    }

    /**
     * wynik = indeks z TrainingSets_N, wskazujacy do ktorej klasy (label) jest najblizej
     */
    private int nearestMean(double[] features_v) {
        int classLength = TrainingSets_N.length;

        double[][] point_n = Matrix2.to_matrix_n(features_v);
        double[] distances = new double[classLength];

        for (int i = 0; i < classLength; i++) {
            distances[i] = Math2.distance_mahalanobis(point_n, TrainingSetsMeans_N[i], TrainingSetsCovarianceInv[i]);
        }
        return Math2.arg_min(distances);
    }
}
