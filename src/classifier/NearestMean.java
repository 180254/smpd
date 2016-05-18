package classifier;

import classifier.enums.ClassifType;
import classifier.enums.DistanceType;
import classifier.sets.Dataset;
import exception.InverseException;
import utils.Math2;
import utils.Matrix2;
import utils.Matrix3;
import utils.Utils2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearestMean implements Classifier {

    private Dataset ds;
    private DistanceType distanceType;
    private ClassifType classifType;

    List<List<double[][]>> Class_Mod_TrainingSetsMeans_N; // [class_id][mod_id][feature_mean][1]
    List<List<double[][]>> Class_Mod_TrainingSetsCovarianceInv; // [class_id][mod_id][cov_inv,cov_inv]

    final int K_MIN = 1;
    final int K_MAX = 10;

    public NearestMean(Dataset ds, DistanceType distanceType, ClassifType classifType) {
        this.ds = ds;
        this.distanceType = distanceType;
        this.classifType = classifType;
    }

    @Override
    public void trainClassifier() {
        Class_Mod_TrainingSetsMeans_N = new ArrayList<>();
        Class_Mod_TrainingSetsCovarianceInv = new ArrayList<>();

        // dla każdej klasy
        // each_class:
        for (int classId = 0; classId < ds.ClassLength; classId++) {

            // w przypadku NN (bez k) zakladamy, ze klasa ma jeden mod
            // srednia i kowariancja jest liczona dla calej klasy
            if (classifType == ClassifType.One) {

                double[][] means_n = Math2.means_n(ds.TrainingSets_N[classId]);
                Class_Mod_TrainingSetsMeans_N.add(new ArrayList<>());
                Class_Mod_TrainingSetsMeans_N.get(classId);
                Class_Mod_TrainingSetsMeans_N.get(classId).add(means_n);

                double[][] covariance = Math2.covariance(ds.TrainingSets_N[classId]);
                double[][] covarianceInv = Matrix2.inverse(covariance);
                Class_Mod_TrainingSetsCovarianceInv.add(new ArrayList<>());
                Class_Mod_TrainingSetsCovarianceInv.get(classId).add(covarianceInv);

                continue; // each_class; pomijamy kolejne kroki kierowane do k-NN
            }

            // classifType = ClassifType.K; pelna procedura
            // zmienne przechowujace wyliczone wartosci dla kazdego z k
            List<List<double[][]>> K_Mod_Means_N = new ArrayList<>(); // [k_id][mod_id][feature_mean,1]
            List<List<double[][]>> K_Mod_CovariancesInv = new ArrayList<>(); // [k_id][mod_id][cov_inv,cov_inv]
            List<List<List<Integer>>> K_Mod_TrainingIndexes_T = new ArrayList<>(); // [k_id][mod_id] list<indexes>
            List<Double> K_Mean_Errors = new ArrayList<>(); // [k_id][mean_error]

            // mozliwe powtorzenia obliczen srodkow modow
            // ponowne proby sa podejmowane w przypadku, gdy macierz kowariancji okaze sie nieodwracalna
            int retries = 0;
            final int MAX_RETRY = 5;

            // dla kazdego sprawdzanego k
            all_k_loop:
            for (int cur_k = K_MIN; cur_k <= K_MAX; cur_k++) {
                // srednie; macierze kowariancji dla kazdego skupistka; popelnione bledy
                List<double[][]> Mod_Means_N = new ArrayList<>(); // [mod_id][feature_mean,1]
                List<double[][]> Mod_CovariancesInv = new ArrayList<>(); // [mod_id][cov_inv,cov_inv]
                List<Double> Mod_Errors = new ArrayList<>(); // list <errors>

                // lista indeksow dla kazdego skupistka;
                List<List<Integer>> Mod_TrainingIndexes_T = new ArrayList<>(); // [mod_id] list<indexes>
                Map<Integer, Integer> TrainingIndexToModId = new HashMap<>(); // TrainingIndex -> mod_id

                // wstepnie wylosowana srednia dla kazdego skupiska;
                random_mean_loop:
                for (int mod_i = 0; mod_i < cur_k; mod_i++) {
                    double[][] random_mean = Utils2.random_mean_n2(ds.TrainingSets_N[classId]);

                    // nalezy sie uprawnic, czy taki srodek juz nie zostal wczesniej wylosowany
                    for (double[][] modMeanN : Mod_Means_N) {
                        if (Matrix3.equals(random_mean, modMeanN, 1e-20)) {
                            mod_i--;
                            continue random_mean_loop;
                        }
                    }

                    Mod_Means_N.add(random_mean);
                }

                // iteracyjne poprawianie sredniej; policzenie wartosci dla modu
                boolean doNextIteration;
                do {
                    Mod_TrainingIndexes_T.clear();
                    TrainingIndexToModId.clear();
                    for (int ki = 0; ki < cur_k; ki++) {
                        Mod_TrainingIndexes_T.add(new ArrayList<>());
                    }

                    // klasyfikowanie kazdej probki
                    for (int ti = 0; ti < ds.TrainingSets_T[classId].length; ti++) {
                        double[] sample_v = ds.TrainingSets_T[classId][ti];

                        double[] distances_v = new double[cur_k];
                        for (int mod_i = 0; mod_i < cur_k; mod_i++) {
                            double[] mean_v = Matrix2.to_vector_n(Mod_Means_N.get(mod_i));
                            distances_v[mod_i] = Math2.distance_euclidean(sample_v, mean_v);
                        }

                        int bestMod = Math2.arg_min(distances_v);
                        Mod_TrainingIndexes_T.get(bestMod).add(ti);
                        TrainingIndexToModId.put(ti, bestMod);
                    }

                    // pobliczenie nowej sredniej i jej korekta
                    List<double[][]> prev_Mod_Means_N = new ArrayList<>(Mod_Means_N);
                    for (int mod_i = 0; mod_i < cur_k; mod_i++) {
                        if (Mod_TrainingIndexes_T.get(mod_i).size() == 0)
                            continue;

                        int[] trainingIndexes = Utils2.to_int_array(Mod_TrainingIndexes_T.get(mod_i));
                        double[][] modi_dataset_t = Utils2.extract_rows(ds.TrainingSets_T[classId], trainingIndexes);
                        double[][] modi_dataset_n = Matrix2.transpose(modi_dataset_t);
                        Mod_Means_N.set(mod_i, Math2.means_n(modi_dataset_n));
                    }

                    // sprawdzenie, czy zaszla jakas korekta srednich
                    doNextIteration = false;
                    for (int mod_i = 0; mod_i < cur_k; mod_i++) {
                        final double epsilon = 1e-20;
                        if (!Matrix3.equals(prev_Mod_Means_N.get(mod_i), Mod_Means_N.get(mod_i), epsilon)) {
                            doNextIteration = true;
                            break;
                        }
                    }
                } while (doNextIteration); // end: iteracje; srodki modow juz sa znane

                // policzenie kowariancji
                if (distanceType == DistanceType.Mahalanobis) {
                    for (int mod_i = 0; mod_i < cur_k; mod_i++) {
                        if (Mod_TrainingIndexes_T.get(mod_i).size() == 0) {
                            Mod_CovariancesInv.add(mod_i, null);
                            continue;
                        }

                        int[] trainingIndexes = Utils2.to_int_array(Mod_TrainingIndexes_T.get(mod_i));
                        double[][] modi_dataset_t = Utils2.extract_rows(ds.TrainingSets_T[classId], trainingIndexes);
                        double[][] modi_dataset_n = Matrix2.transpose(modi_dataset_t);

                        try {
                            Mod_CovariancesInv.add(mod_i, Matrix2.inverse(Math2.covariance(modi_dataset_n)));
                        } catch (InverseException ex) {
                            if (++retries <= MAX_RETRY) {
                                cur_k--;
                                continue all_k_loop;
                            } else {
                                break all_k_loop;
                            }
                        }
                    }
                }

                // policzenie bledow - odleglosci od modu, do ktorego probka nalezy
                for (int ti = 0; ti < ds.TrainingSets_T[classId].length; ti++) {
                    int bestMod = TrainingIndexToModId.get(ti);
                    double[] sample_v = ds.TrainingSets_T[classId][ti];
                    double[] mean_v = Matrix2.to_vector_n(Mod_Means_N.get(bestMod));
                    Mod_Errors.add(Math2.distance_euclidean(sample_v, mean_v));
                }

                // sredni popelniony blad
                double meanError = Mod_Errors.stream().mapToDouble(d -> d).average().orElse(0);
                // System.out.println(String.format("%.15f", meanError).replace(".", ","));
                System.out.println(String.format("c=%d // k=%2d // %.15f // %s",
                        classId, cur_k, meanError,
                        Mod_TrainingIndexes_T.stream()
                                .map(List::size).map(String::valueOf)
                                .reduce((s, s2) -> String.format("%3s, %3s", s, s2))
                                .orElse("")
                ));

                // zapisanie wyniku dla danego k
                K_Mod_Means_N.add(Mod_Means_N);
                K_Mod_CovariancesInv.add(Mod_CovariancesInv);
                K_Mod_TrainingIndexes_T.add(Mod_TrainingIndexes_T);
                K_Mean_Errors.add(meanError);

                retries = 0;
            } // end: kazde k

            // policzenie najlepszego k
            int bestKi = Math2.inflection_point(Utils2.to_dbl_array(K_Mean_Errors));
            int bestK = bestKi + K_MIN;

            // policzenie efektywnego K, byc moze jakies mody sa puste?
            int[] emptyModsId = Utils2.empty_lists_ids(K_Mod_TrainingIndexes_T.get(bestKi));
            for (int emptyModId : emptyModsId) {
                K_Mod_Means_N.get(bestKi).remove(emptyModId);

                if (distanceType == DistanceType.Mahalanobis) {
                    K_Mod_CovariancesInv.get(bestKi).remove(emptyModId);
                }
            }

            int efectiveK = K_Mod_Means_N.get(bestKi).size();
            System.out.printf("c=%d, min(k) = %d, max(k) = %d, k=%d, efektywneK = %d%n",
                    classId, K_MIN, K_Mod_Means_N.size(), bestK, efectiveK);

            // zapisanie srednich i kowariancji dla klasy - najlepsze ustalone K
            Class_Mod_TrainingSetsMeans_N.add(K_Mod_Means_N.get(bestKi));
            Class_Mod_TrainingSetsCovarianceInv.add(K_Mod_CovariancesInv.get(bestKi));

//            System.out.println("-----------------------------------------------");
        } // end: kazda klasa
    }

    @Override
    public double testClassifier() {
        // klasyfikator jeszcze nie wytrenowany
        if (Class_Mod_TrainingSetsMeans_N == null) return -1;

        int ok = 0;

        for (int i = 0; i < ds.TestSet_T_Length; i++) {
            int classLabel = nearestMeanLabel(ds.TestSet_T[i]);
            int properLabel = ds.TestLabels_T[i];
            if (properLabel == classLabel) ok++;
        }

        return ok / (double) ds.TestSet_T_Length;
    }

    /**
     * wynik = indeks wskazujacy do ktorej klasy (label) jest najblizej
     */
    private int nearestMeanLabel(double[] features_v) {
        double[] distances = new double[ds.ClassLength];

        for (int cur_class = 0; cur_class < ds.ClassLength; cur_class++) {
            distances[cur_class] = distanceToClass(cur_class, features_v);
        }

        return Math2.arg_min(distances);
    }

    /**
     * wynik = ogległość wektora features_v do klasy o nr  classId
     * liczona jest odległośc do każdego modu i wybierana najmniejsza
     */
    private double distanceToClass(int classId, double[] features_v) {
        int modLength = Class_Mod_TrainingSetsMeans_N.get(classId).size();
        double[] distances = new double[modLength];

        for (int cur_mod = 0; cur_mod < modLength; cur_mod++) {

            if (distanceType == DistanceType.Euclidean) {
                double[][] means_n = Class_Mod_TrainingSetsMeans_N.get(classId).get(cur_mod);
                double[] mean_v = Matrix2.to_vector_n(means_n);
                distances[cur_mod] = Math2.distance_euclidean(features_v, mean_v);

            } else if (distanceType == DistanceType.Mahalanobis) {
                double[][] point_n = Matrix2.to_matrix_n(features_v);
                double[][] means_n = Class_Mod_TrainingSetsMeans_N.get(classId).get(cur_mod);
                double[][] covarianceInv = Class_Mod_TrainingSetsCovarianceInv.get(classId).get(cur_mod);
                distances[cur_mod] = Math2.distance_mahalanobis(point_n, means_n, covarianceInv);
            }
        }

        return Math2.min(distances);
    }
}
