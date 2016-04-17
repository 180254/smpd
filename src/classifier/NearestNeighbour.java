package classifier;

import utils.Math2;
import utils.Matrix2;
import utils.Utils2;

public class NearestNeighbour extends Classifier {

    private DistanceType distanceType;
    private ClassType classType;
    private Integer kParam;

    private double[][][] CovarianceInv; // [class_id][mod_id][cov_inv,cov_inv]

    public NearestNeighbour(DistanceType distanceType, ClassType classType) {
        this.distanceType = distanceType;
        this.classType = classType;
    }

    @Override
    public void trainClassifier() {
        if (distanceType == DistanceType.Mahalanobis) {
            // macierze kowariancji dla kazdej klasy

            CovarianceInv = new double[ClassNames.length][][];

            double[][][] TrainingSets_T = Utils2.extract_classes_t(TrainingSet_T, TrainingLabels_T, ClassNames);
            double[][][] TrainingSets_N = Utils2.extract_classes_n(TrainingSets_T);

            for (int i = 0; i < ClassNames.length; i++) {
                CovarianceInv[i] = Matrix2.inverse(Math2.covariance(TrainingSets_N[i]));
            }
        }

        if (classType == ClassType.ONE) {
            kParam = 1;
        } else if (classType == ClassType.K) {
            kParam = 5;
        }
        System.out.printf("k = %d%n", kParam);
    }

    @Override
    public double testClassifier() {
        if (kParam == null) return -1;
        int ok = 0;
        int maxOk = TestSet_T.length;

        for (int i = 0; i < TestSet_T.length; i++) {
            try {
                int[] trainingIndexes = nearestNeighbour(TestSet_T[i]);
                int[] trainingLabels = Utils2.map_int_arr(trainingIndexes, TrainingLabels_T);

                int popularLabel = Math2.most_popular(trainingLabels);
                int properLabel = TestLabels_T[i];

                if (properLabel == popularLabel) ok++;

            } catch (RuntimeException ex) {
                if (ex.getMessage().contains("singular") || ex.getMessage().contains("overflow")) {
                    maxOk--;
                } else {
                    throw ex;
                }
            }
        }

        System.out.printf("Straconych próbek: %d/%d%n", TestSet_T.length - maxOk, TestSet_T.length);
        return ok / (double) maxOk;

    }

    /**
     * Najblizszy sasiad dla TrainingSet_T.
     * wynik = indeksy z TrainingSet_T z k-najblizszych sasiadow
     */
    private int[] nearestNeighbour(double[] features_v) {
        double[] distances = new double[TrainingSet_T.length];

        for (int i = 0; i < TrainingSet_T.length; i++) {

            if (distanceType == DistanceType.Euclidean) {
                distances[i] = Math2.distance_euclidean(features_v, TrainingSet_T[i]);

            } else if (distanceType == DistanceType.Mahalanobis) {
                int classId = TrainingLabels_T[i];
                distances[i] = Math2.distance_mahalanobis(
                        Matrix2.to_matrix_n(features_v),
                        Matrix2.to_matrix_n(TrainingSet_T[i]),
                        CovarianceInv[classId]);
            }
        }

        return Math2.arg_min(distances, kParam);
    }

}
