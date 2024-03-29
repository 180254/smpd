package featurespace;

import org.apache.commons.math3.util.Combinations;
import utils.Math2;
import utils.Matrix2;
import utils.Utils2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class FisherDiscriminant {

    /**
     * Wybiera najlepsze cechy na podstawie wspolczynnikow fishera.
     * Sprawdzane sa wszystkie kombinacje k-cech.
     *
     * @param DataSet_N       Zbior danych treningowych.
     * @param DataSetLabels_T Etykiety, do ktorej klasy naleza kolejne probki.
     * @param ClassLength     Ile jest klas (możliwych etykiet)
     * @param select_n        Ile cech nalezy wybrac.
     * @return Tablica cech, ktore sa najlepsze do liniowej dyskryminacji.
     */
    public static int[] get_features(double[][] DataSet_N, int[] DataSetLabels_T, int ClassLength, int select_n) {
        if (ClassLength != 2) {
            throw new RuntimeException();
        }
        ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 5
        );

        // podzial na klasy
        double[][] DataSet_T = Matrix2.transpose(DataSet_N);
        double[][][] DataSets_T = Utils2.extract_classes_t(DataSet_T, DataSetLabels_T, ClassLength);
        double[][][] DataSets_N = Utils2.extract_classes_n(DataSets_T);

        // policzenie srednich dla kazdej z klas
        double[][][] DataSetMeans_N = new double[DataSets_N.length][][];
        for (int i = 0; i < ClassLength; i++) {
            DataSetMeans_N[i] = Math2.means_n(DataSets_N[i]);
        }

        Combinations featuersIt = new Combinations(DataSet_N.length, select_n); // iterator kombinacji
        Map<int[], Double> fishers = new ConcurrentHashMap<>(); // wyniki policzonych fisherow

        // zebranie wszystkich obliczen do wykonania
        List<Callable<Object>> fisherTasks = new ArrayList<>();
        for (int[] features : featuersIt) {
            fisherTasks.add(Executors.callable(() -> {
                double fisher = select_n == 1
                        ? fisher1(DataSets_N[0], DataSets_N[1], DataSetMeans_N[0], DataSetMeans_N[1], features)
                        : fisher2(DataSets_N[0], DataSets_N[1], DataSetMeans_N[0], DataSetMeans_N[1], features);
                fishers.put(features, fisher);
            }));
        }

        // wykonanie obliczen
        try {
            executor.invokeAll(fisherTasks, 5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Math2.max(fishers).getKey();
    }


    /**
     * Współczynnik fishera. Wersja, która która liczy go dla dwóch klas, i dowolnej liczby cech.
     *
     * @param dataset_n_c1      dataset_n dla klasy 1
     * @param dataset_n_c2      dataset_n dla klasy 2
     * @param datasetmeans_n_c1 policzone srednie cech dla klasy 1
     * @param datasetmeans_n_c2 policzone srednie cech dla klasy 2
     * @param features          wybrane cechy np. {2,3,5}
     * @return wspolczynik fishera
     */
    public static double fisher2(double[][] dataset_n_c1, double[][] dataset_n_c2,
                                 double[][] datasetmeans_n_c1, double[][] datasetmeans_n_c2,
                                 int[] features) {

        double[][] dataset_n1 = Utils2.extract_rows(dataset_n_c1, features);
        double[][] means_n1 = Utils2.extract_rows(datasetmeans_n_c1, features);
        double[] means_v1 = Matrix2.to_vector_n(means_n1);
        double[][] covariance_1 = Math2.covariance(dataset_n1, means_n1, 2);

        double[][] dataset_n2 = Utils2.extract_rows(dataset_n_c2, features);
        double[][] means_n2 = Utils2.extract_rows(datasetmeans_n_c2, features);
        double[] means_v2 = Matrix2.to_vector_n(means_n2);
        double[][] covariance_2 = Math2.covariance(dataset_n2, means_n2, 2);

        return Math2.distance_euclidean(means_v1, means_v2)
                / (Matrix2.det(covariance_1) + Matrix2.det(covariance_2));
    }

    /**
     * Współczynnik fishera. Wersja, która która liczy go dla dwóch klas, i jednej cechy.
     *
     * @param dataset_n_c1 dataset_n dla klasy 1
     * @param dataset_n_c2 dataset_n dla klasy 2
     * @param features     wybrane cechy np. {2,3,5}
     * @return wspolczynik fishera
     */
    public static double fisher1(double[][] dataset_n_c1, double[][] dataset_n_c2,
                                 double[][] datasetmeans_n_c1, double[][] datasetmeans_n_c2,
                                 int[] features) {

        double[][] dataset_n1 = Utils2.extract_rows(dataset_n_c1, features);
        double[][] means_n1 = Utils2.extract_rows(datasetmeans_n_c1, features);
        double[] means_v1 = Matrix2.to_vector_n(means_n1);
        double stddev_1 = Math2.stddev(dataset_n1[0]);

        double[][] dataset_n2 = Utils2.extract_rows(dataset_n_c2, features);
        double[][] means_n2 = Utils2.extract_rows(datasetmeans_n_c2, features);
        double[] means_v2 = Matrix2.to_vector_n(means_n2);
        double stddev_2 = Math2.stddev(dataset_n2[0]);

        return Math2.distance_euclidean(means_v1, means_v2) / (stddev_1 + stddev_2);
    }
}
