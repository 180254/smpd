package classifier;

import utils.Math2;
import utils.Matrix2;
import utils.Utils2;


public class NearestNeighbour extends Classifier {

    private boolean useKdt;
    private KdtNode kdt; // drzewo k-dim-tree

    private DistanceType distanceType;
    private ClassType classType;
    private Integer kParam;

    private double[][][] CovarianceInv; // [class_id][mod_id][cov_inv,cov_inv]

    public NearestNeighbour(DistanceType distanceType, ClassType classType, boolean useKdt) {
        this.distanceType = distanceType;
        this.classType = classType;
        this.useKdt = useKdt;
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

        if (useKdt) {
            int expectedLeafs = 10; // podzial nastapi na 10 sektorow
            int kdtDepth = (int) Math2.log(expectedLeafs, 2); // jest to drzewo binarne, liczba lisci = 2^poziom
            kdt = KdtNode.KdtTree(TrainingSet_T, kdtDepth);
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

        int[] AllTrainingSetIndexes = Utils2.range_ex(0, TrainingSet_T.length);

        for (int i = 0; i < TestSet_T.length; i++) {
            try {
                // jezeli uzywamy kdt wskazujemy odpowiedni lisc z drzewam,
                // w przeciwnym wypadku przeszukujemy caly TrainingSet_T
                int[] neighbourIndexes = useKdt ? kdt.FindNeighbours(TestSet_T[i]) : AllTrainingSetIndexes;
                if (i == 0) System.out.printf("Użyto próbek treningowych = %d (%.0f%%)%n",
                        neighbourIndexes.length,
                        neighbourIndexes.length / (double) AllTrainingSetIndexes.length * 100);

                int[] trainingIndexes = nearestNeighbour(TestSet_T[i], neighbourIndexes);
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

        System.out.printf("Straconych próbek testowych: %d/%d%n", TestSet_T.length - maxOk, TestSet_T.length);
        return ok / (double) maxOk;
    }

    /**
     * Najblizszy sasiad dla TrainingSet_T.
     * Sasiedzi sa szukani tylko wsrod tych ze zbioru testowego, ktorzy byli wskazani w Training_Indexes.
     * wynik = indeksy z TrainingSet_T z k-najblizszych sasiadow
     */
    private int[] nearestNeighbour(double[] features_v, int[] Training_Indexes) {
        double[] distances = new double[TrainingSet_T.length];

        for (int i : Training_Indexes) {

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
