package classifier;

import utils.Math2;
import utils.Matrix2;
import utils.Utils2;

import java.util.Arrays;

// https://ashokharnal.wordpress.com/2015/01/20/a-working-example-of-k-d-tree-formation-and-k-nearest-neighbor-algorithms/
// https://en.wikipedia.org/wiki/K-d_tree
public class KdtNode {

    private final double[] median_v; // wektor kolejnych cech dla mediany
    private final Integer featureCmp; // cecha na podstawie której nastąpił podział
    private final KdtNode less; // punkty mniejsze
    private final KdtNode greater; // punkty większe

    private int[] leafIndexes; // dla liscia lista indeksow sie w nim znajdujących

    private KdtNode(double[] median_v, Integer featureCmp, KdtNode less, KdtNode greater, int[] leafIndexes) {
        this.median_v = median_v;
        this.featureCmp = featureCmp;
        this.less = less;
        this.greater = greater;
        this.leafIndexes = leafIndexes;
    }

    // utworzenie k-dim-tree dla danych wejściowych
    public static KdtNode KdtTree(double[][] DataSet_T, int depth) {
        return KdtTree(DataSet_T, 0, depth);
    }

    /**
     * Zwraca listę próbek będących sasiadami dla danej próbki testowej.
     * Próbki są wracane jako indeksy z DataSet_T podanego przy tworzeniu k-dim-tree
     */
    public int[] FindNeighbours(double[] sample_v) {
        KdtNode kdt = this;

        while (kdt.leafIndexes == null) { // dopóki nie osiągnięto liścia
            // porównujemy cechy, na podstawie której był podział i wskazujemy, czy będziemy szukać po lewej, czy prawej
            kdt = sample_v[kdt.featureCmp] <= kdt.median_v[kdt.featureCmp]
                    ? kdt.less
                    : kdt.greater;
        }

        return kdt.leafIndexes;
    }

    private static KdtNode leaf(int[] leafIndexes) {
        return new KdtNode(null, null, null, null, leafIndexes);
    }

    private static KdtNode node(double[] median_v, Integer featureCmp, KdtNode less, KdtNode greater) {
        return new KdtNode(median_v, featureCmp, less, greater, null);
    }

    private static KdtNode KdtTree(
            double[][] DataSet_T,
            int depth, int maxDepth) {

        if (depth == maxDepth) {
            // ostatnia kolumna to kolumna porządkowa (do sortowania)
            // kolumna sortująca jest jednocześnie indeksem próbki
            // do liście nalezy zapisać wszystkie indeksy próbek
            double[] doubles = Utils2.extract_column(DataSet_T, DataSet_T[0].length - 1);
            return KdtNode.leaf(Utils2.dbl_to_int(doubles));
        }

        // kopia otrzymanej tablicy (immutable, otrzymane dane nie są modyfikowane)
        // jezeli to jest pierwszy krok nalezy takze wzbogacic dane o dodatkowa kolumne sluzaca do srtowania
        // ostatnia kolumna bedzie takze oryginalnym indeksem
        double[][] DataSet_Tc = depth == 0
                ? Utils2.add_order_column(DataSet_T)
                : Matrix2.copy(DataSet_T);

        // liczba posiadanych próbek cech
        int sampleLen = DataSet_Tc.length;
        int featuresLen = DataSet_Tc[0].length - 1;

        // określenie na podstawie której cechy będzie dokonywany podział
        // najlepsza będzieta cecha o największym odchyleniu standardowym
        double[] stddevs = new double[featuresLen];
        for (int i = 0; i < featuresLen; i++) {
            stddevs[i] = Math2.stddev(Utils2.extract_column(DataSet_Tc, i));
        }

        // posortowanie danych wejściowych na podstawie najlepszej cechy
        int bestFeature = Math2.arg_max(stddevs);
        // int bestFeature = depth%sampleLen; // alternatywa, kazda cecha jest uzyta po kolei
        Arrays.sort(DataSet_Tc, Utils2.array_comparator(bestFeature, true));

        // lewe dziecko to będzie pierwsza połowa próbek; prawe dziecko druga połowa próbek
        double[][] left_t = Utils2.extract_rows(DataSet_Tc, Utils2.range_ex(0, sampleLen / 2));
        double[][] right_t = Utils2.extract_rows(DataSet_Tc, Utils2.range_ex(sampleLen / 2, sampleLen));
        double[] median = left_t[left_t.length - 1];

        // rekurencyjnie tworzymy nowy node
        return KdtNode.node(
                median,
                bestFeature,
                KdtTree(left_t, depth + 1, maxDepth),
                KdtTree(right_t, depth + 1, maxDepth)
        );
    }
}
