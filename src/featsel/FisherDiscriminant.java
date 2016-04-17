package featsel;

import org.apache.commons.math3.util.Combinations;
import utils.Math2;
import utils.Matrix2;
import utils.Utils2;

import java.util.HashMap;
import java.util.Map;

public class FisherDiscriminant {

    public int[] get_features(double[][] DataSet_N, int[] DataSetLabels_T, String[] ClassNames, int select_k) {
        if (ClassNames.length != 2) {
            throw new RuntimeException();
        }

        double[][] DataSet_T = Matrix2.transpose(DataSet_N);
        double[][][] DataSets_T = Utils2.extract_classes_t(DataSet_T, DataSetLabels_T, ClassNames);
        double[][][] DataSets_N = Utils2.extract_classes_n(DataSets_T);

        Map<int[], Double> fishers = new HashMap<>();

        for (int[] features : new Combinations(DataSet_N.length, select_k)) {
            double fisher = fisher2(DataSets_N[0], DataSets_N[1], features);
            fishers.put(features, fisher);
        }

        return Math2.max(fishers).getKey();
    }

    /**
     * Współczynnik fishera. Wersja, która która liczy go dla dwóch klas, i dowolnej liczby cech.
     */
    public static double fisher2(double[][] dataset_c1, double[][] dataset_c2, int[] features) {

        double[][] dataset_n1 = Utils2.extract_rows(dataset_c1, features);
        double[][] dataset_n2 = Utils2.extract_rows(dataset_c2, features);

        double[][] means_n1 = Math2.means_n(dataset_n1);
        double[] means_v1 = Matrix2.to_vector_n(means_n1);
        double[][] covariance_1 = Math2.covariance(dataset_n1, 2);

        double[][] means_n2 = Math2.means_n(dataset_n2);
        double[] means_v2 = Matrix2.to_vector_n(means_n2);
        double[][] covariance_2 = Math2.covariance(dataset_n2, 2);

        return Math2.distance_euclidean(means_v1, means_v2)
                / (Matrix2.det(covariance_1) + Matrix2.det(covariance_2));
    }
}
