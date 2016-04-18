package classifier;

import utils.Math2;
import utils.Matrix2;
import utils.Matrix3;
import utils.Utils2;

import java.util.ArrayList;
import java.util.List;

public class NearestMean extends Classifier {

    private DistanceType distanceType;
    private ClassType classType;

    double[][][] TrainingSets_N; // [class_id][feature][sample]
    double[][][] TrainingSets_T; // [class_id][sample][feature]
    List<List<double[][]>> TrainingSetsMeans_N; // [class_id][mod_id][feature_mean][1]
    List<List<double[][]>> TrainingSetsCovarianceInv; // [class_id][mod_id][cov_inv,cov_inv]

    final int MAX_RETRY = 10;
    final int K_MIN = 1;
    final int K_MAX = 15;

    public NearestMean(DistanceType distanceType, ClassType classType) {
        this.distanceType = distanceType;
        this.classType = classType;
    }

    @Override
    public void trainClassifier() {
        // extract training set for each class
        TrainingSets_T = Utils2.extract_classes_t(TrainingSet_T, TrainingLabels_T, ClassNames);
        TrainingSets_N = Utils2.extract_classes_n(TrainingSets_T);
        TrainingSetsMeans_N = new ArrayList<>();
        TrainingSetsCovarianceInv = new ArrayList<>();

        // dla każdej klasy
        // each_class:
        for (int classId = 0; classId < TrainingSets_N.length; classId++) {

            // w przypadku NN (bez k) zakladamy, ze klasa ma jeden mod
            // srednia i kowariancja jest liczona dla calej klasy
            if (classType == ClassType.ONE) {

                double[][] means_n = Math2.means_n(TrainingSets_N[classId]);
                TrainingSetsMeans_N.add(new ArrayList<>());
                TrainingSetsMeans_N.get(classId).add(means_n);

                double[][] covariance = Math2.covariance(TrainingSets_N[classId]);
                double[][] covarianceInv = Matrix2.inverse(covariance);
                TrainingSetsCovarianceInv.add(new ArrayList<>());
                TrainingSetsCovarianceInv.get(classId).add(covarianceInv);

                continue; // each_class; pomijamy kolejne kroki kierowane do k-NN
            }

            // classType = ClassType = K; pelna procedura
            // zmienne przechowujace wyliczone wartosci dla kazdego z k
            List<List<double[][]>> K_Mod_Means_N = new ArrayList<>(); // [k_id][mod_id][feature_mean,1]
            List<List<double[][]>> K_Mod_CovariancesInv = new ArrayList<>(); // [k_id][mod_id][cov_inv,cov_inv]
            List<List<List<Integer>>> K_Mod_TrainingIndexes_T = new ArrayList<>(); // [k_id][mod_id] list<indexes>
            List<Double> K_Mean_Errors = new ArrayList<>(); // [k_id][mean_error]

            int retries = 0;
            // dla kazdego sprawdzanego k
            for (int cur_k = K_MIN; cur_k < K_MAX; cur_k++) {

                try {
                    // srednie; macierze kowariancji dla kazdego skupistka
                    // lista indeksow dla kazdego skupistka; popelnione bledy
                    List<double[][]> Mod_Means_N = new ArrayList<>();
                    List<double[][]> Mod_CovariancesInv = new ArrayList<>();
                    List<List<Integer>> Mod_TrainingIndexes_T = new ArrayList<>();
                    List<Double> Mod_Errors = new ArrayList<>();

                    // wstepnie wylosowana srednia dla kazdego skupiska;
                    for (int ki = 0; ki < cur_k; ki++) {
                        Mod_Means_N.add(Utils2.random_mean_n(TrainingSets_N[classId]));
                    }

                    // iteracyjne poprawianie sredniej
                    boolean doNextIteration;
                    do {
                        Mod_TrainingIndexes_T.clear();
                        for (int ki = 0; ki < cur_k; ki++) {
                            Mod_TrainingIndexes_T.add(new ArrayList<>());
                        }
                        Mod_Errors.clear();

                        // klasyfikowanie kazdej probki i policzenie bledu
                        for (int ti = 0; ti < TrainingSets_T[classId].length; ti++) {
                            double[] sample_v = TrainingSets_T[classId][ti];
                            double[] distances_v = new double[cur_k];

                            for (int ki = 0; ki < cur_k; ki++) {
                                double[] mean_v = Matrix2.to_vector_n(Mod_Means_N.get(ki));
                                distances_v[ki] = Math2.distance_euclidean(sample_v, mean_v);
                            }

                            int bestMod = Math2.arg_min(distances_v);
                            Mod_TrainingIndexes_T.get(bestMod).add(ti);
                            Mod_Errors.add(distances_v[bestMod]);
                        }

                        // pobliczenie nowej sredniej i jej korekta
                        List<double[][]> prev_Mod_Means_N = new ArrayList<>(Mod_Means_N);
                        for (int ki = 0; ki < cur_k; ki++) {
                            if (Mod_TrainingIndexes_T.get(ki).size() == 0)
                                continue;

                            int[] trainingIndexes = Utils2.to_int_array(Mod_TrainingIndexes_T.get(ki));
                            double[][] ki_dataset_t = Utils2.extract_rows(TrainingSets_T[classId], trainingIndexes);
                            double[][] ki_dataset_n = Matrix2.transpose(ki_dataset_t);
                            Mod_Means_N.set(ki, Math2.means_n(ki_dataset_n));
                        }

                        // sprawdzenie, czy zaszla jakas korekta srednich
                        doNextIteration = false;
                        for (int ki = 0; ki < cur_k; ki++) {
                            final double epsilon = 1e-10;
                            if (!Matrix3.equals(prev_Mod_Means_N.get(ki), Mod_Means_N.get(ki), epsilon)) {
                                doNextIteration = true;
                                break;
                            }
                        }
                    } while (doNextIteration);

                    // policzenie kowariancji, tu moze sie okazac, ze macierz jest nieodwracalna
                    if (distanceType == DistanceType.Mahalanobis) {
                        for (int ki = 0; ki < cur_k; ki++) {
                            if (Mod_TrainingIndexes_T.get(ki).size() == 0) {
                                Mod_CovariancesInv.add(ki, null);
                                continue;
                            }
                            int[] trainingIndexes = Utils2.to_int_array(Mod_TrainingIndexes_T.get(ki));
                            double[][] ki_dataset_t = Utils2.extract_rows(TrainingSets_T[classId], trainingIndexes);
                            double[][] ki_dataset_n = Matrix2.transpose(ki_dataset_t);

                            Mod_CovariancesInv.add(ki, Matrix2.inverse(Math2.covariance(ki_dataset_n)));
                        }
                    }

                    // sredni popelniony blad
                    double meanError = Mod_Errors.stream().mapToDouble(d -> d).average().orElse(0);
                    // System.out.println(String.format("%.15f", meanError).replace(".", ","));
                    /* System.out.println(String.format("c=%d // k=%2d // %.15f // %s",
                            classId,
                            cur_k,
                            meanError,
                            Mod_TrainingIndexes_T.stream()
                                    .map(List::size).map(String::valueOf)
                                    .reduce((s, s2) -> String.format("%3s, %3s", s, s2))
                                    .orElse("")
                    )); */

                    // zapisanie wyniku dla danego k
                    K_Mod_Means_N.add(Mod_Means_N);
                    K_Mod_CovariancesInv.add(Mod_CovariancesInv);
                    K_Mod_TrainingIndexes_T.add(Mod_TrainingIndexes_T);
                    K_Mean_Errors.add(meanError);

                    retries = 0;
                } catch (RuntimeException e) {
                    if (retries++ > MAX_RETRY)
                        break;
                    cur_k--;
                }
            } // end: kazde k

            // policzenie najlepszego k
            int bestK = Math2.inflection_point(Utils2.to_dbl_array(K_Mean_Errors));
            int bestKi = bestK - K_MIN;

            // policzenie efektywnego K
            int[] empty_mod = Utils2.empty_lists_ids(K_Mod_TrainingIndexes_T.get(bestKi));
            for (int empty : empty_mod) {
                K_Mod_Means_N.get(bestKi).remove(empty);
                if (distanceType == DistanceType.Mahalanobis) {
                    K_Mod_CovariancesInv.get(bestKi).remove(empty);
                }
            }

            int efectiveK = K_Mod_Means_N.get(bestKi).size();
            System.out.printf("c=%d, k=%d, efektywneK = %d%n", classId, bestK, efectiveK);

            // zapisanie srednich i kowariancji dla klasy - najlepsze ustalone K
            TrainingSetsMeans_N.add(K_Mod_Means_N.get(bestKi));
            TrainingSetsCovarianceInv.add(K_Mod_CovariancesInv.get(bestKi));

//            System.out.println("-----------------------------------------------");
        } // end: kazda klasa
    }


    @Override
    public double testClassifier() {
        int ok = 0;
        int maxOk = TestSet_T.length;

        for (int i = 0; i < TestSet_T.length; i++) {

            try {
                int classLabel = nearestMean(TestSet_T[i]);
                int properLabel = TestLabels_T[i];
                if (properLabel == classLabel) ok++;

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
     * wynik = indeks wskazujacy do ktorej klasy (label) jest najblizej
     */
    private int nearestMean(double[] features_v) {
        int classLength = ClassNames.length;
        double[] distances = new double[classLength];

        for (int cur_class = 0; cur_class < classLength; cur_class++) {
            distances[cur_class] = distanceToClass(cur_class, features_v);
        }

        return Math2.arg_min(distances);
    }

    /**
     * wynik = ogległość wektora features_v do klasy o nr  classId
     * liczona jest odległośc do każdego modu i wybierana najmniejsza
     */
    private double distanceToClass(int classId, double[] features_v) {
        int modLength = TrainingSetsMeans_N.get(classId).size();
        double[] distances = new double[modLength];

        for (int cur_mod = 0; cur_mod < modLength; cur_mod++) {

            if (distanceType == DistanceType.Euclidean) {
                double[][] means_n = TrainingSetsMeans_N.get(classId).get(cur_mod);
                double[] mean_v = Matrix2.to_vector_n(means_n);
                distances[cur_mod] = Math2.distance_euclidean(features_v, mean_v);

            } else if (distanceType == DistanceType.Mahalanobis) {
                double[][] point_n = Matrix2.to_matrix_n(features_v);
                double[][] means_n = TrainingSetsMeans_N.get(classId).get(cur_mod);
                double[][] covarianceInv = TrainingSetsCovarianceInv.get(classId).get(cur_mod);
                distances[cur_mod] = Math2.distance_mahalanobis(point_n, means_n, covarianceInv);
            }
        }

        return Math2.min(distances);
    }
}
