package featurespace;

import utils.Math2;
import utils.Matrix2;
import utils.Utils2;

import java.util.ArrayList;
import java.util.List;

public class SequentialFS {

    /**
     * Wybiera najlepsze cechy na podstawie wspolczynnikow fishera.
     * Do przyspieszenia obliczen wykorzystany algorytm SFS.
     *
     * @param DataSet_N       Zbior danych treningowych.
     * @param DataSetLabels_T Etykiety, do ktorej klasy naleza kolejne probki.
     * @param ClassNames      Nazwy kolejnych klas. Indeksem tablicy sa etykiety z DataSetLabels_T.
     * @param select_k        Ile cech nalezy wybrac.
     * @return Tablica cech, ktore sa najlepsze do liniowej dyskryminacji.
     */
    public int[] get_features(double[][] DataSet_N, int[] DataSetLabels_T, String[] ClassNames, int select_k) {
        if (ClassNames.length != 2) {
            throw new RuntimeException();
        }

        int featuresLen = DataSet_N.length;

        // podzial na klasy
        double[][] DataSet_T = Matrix2.transpose(DataSet_N);
        double[][][] DataSets_T = Utils2.extract_classes_t(DataSet_T, DataSetLabels_T, ClassNames);
        double[][][] DataSets_N = Utils2.extract_classes_n(DataSets_T);

        // policzenie srednich dla kazdej z klas
        double[][][] DataSetMeans_N = new double[DataSets_N.length][][];
        for (int i = 0; i < ClassNames.length; i++) {
            DataSetMeans_N[i] = Math2.means_n(DataSets_N[i]);
        }

        // zapisane te juz wybrane cechy
        List<Integer> s_features = new ArrayList<>();

        // kazda iteracja: wybor kolejnej ceshy
        for (int ki = 0; ki < select_k; ki++) {

            // obliczenia fishera dla kazdego "towarzysza"
            double[] ki_fishers = new double[featuresLen];
            for (int fi = 0; fi < featuresLen; fi++) {

                // pomijane obliczenia dla "samego siebie" i dla cech juz wybranych
                if (fi == ki || s_features.contains(fi)) {
                    ki_fishers[fi] = Double.MIN_VALUE;
                    continue;
                }

                // do cech juz wybranych dorzucana jest aktualnie badana cecha, i obliczany jest fisher takiej selekcji
                List<Integer> fi_features = new ArrayList<>(s_features);
                fi_features.add(fi);

                ki_fishers[fi] = FisherDiscriminant.fisher2(
                        DataSets_N[0], DataSets_N[1],
                        DataSetMeans_N[0], DataSetMeans_N[1],
                        Utils2.to_int_array(fi_features));
            }

            // cecha o najlepszym fisherze zostaje dodana do wybranych
            int bestNextFeature = Math2.arg_max(ki_fishers);
            s_features.add(bestNextFeature);
        }

        return Utils2.to_int_array(s_features);
    }
}
