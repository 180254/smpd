package classifier;

import utils.Math2;
import utils.Matrix2;
import utils.Utils2;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

// https://ashokharnal.wordpress.com/2015/01/20/a-working-example-of-k-d-tree-formation-and-k-nearest-neighbor-algorithms/
// https://en.wikipedia.org/wiki/K-d_tree

/**
 * k-d-tree; "points only in leaves" modification
 */
public class KdtNode {

    // docelowa liczba indeksów/liść
    public final static int LEAF_IND_LENGTH = 10;

    // standardowe zmienne
    private final double[] median_v; // wektor kolejnych cech dla mediany
    private final Integer axis; // cecha(oś) na podstawie której nastąpił podział
    private final KdtNode left; // punkty mniejsze lub rowne (w tym mediana)
    private final KdtNode right; // punkty wieksze
    private final int indLength; // liczba indeksow, ktore zostaly zgromadzone w lisciach

    // zmienne dla liscia; lista indeksow sie w nim znajdujących;
    // sa to indeksy DataSet_T ktory byl przekazany przy budowaie drzerwa
    private int[] indexes;

    private KdtNode(double[] median_v, Integer axis, KdtNode left, KdtNode right,
                    int indLength, int[] indexes) {
        this.median_v = median_v;
        this.axis = axis;
        this.left = left;
        this.right = right;
        this.indLength = indLength;
        this.indexes = indexes;
    }

    private static KdtNode node(double[] median_v, Integer axis, KdtNode left, KdtNode right, int indLength) {
        return new KdtNode(median_v, axis, left, right, indLength, null);
    }

    private static KdtNode leaf(int[] indexes) {
        return new KdtNode(null, null, null, null, indexes.length, indexes);
    }

    private boolean isLeaf() {
        return indexes != null;
    }

    public int Depth() {
        int i = 1;
        KdtNode kdt = this;
        while (kdt.left != null) {
            kdt = kdt.left;
            i++;
        }
        return i;
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * utworzenie k-dim-tree dla danych wejściowych
     */
    public static KdtNode KdtTree(double[][] DataSet_T) {
        // nalezy wzbogacic dane o dodatkowa kolumne sluzaca do sortowania
        // ostatnia kolumna bedzie takze oryginalnym indeksem
        double[][] DataSet_To = Utils2.add_order_column(DataSet_T);
        return KdtTree(DataSet_To, 0);
    }

    private static KdtNode KdtTree(double[][] DataSet_T, int depth) {

        if (DataSet_T.length <= LEAF_IND_LENGTH) {
            // ostatnia kolumna to kolumna porządkowa (do sortowania)
            // kolumna sortująca jest jednocześnie indeksem próbki
            // do liście nalezy zapisać wszystkie indeksy próbek
            double[] indexesAsDbls = Utils2.extract_column(DataSet_T, DataSet_T[0].length - 1);
            return KdtNode.leaf(Utils2.dbl_to_int(indexesAsDbls));
        }

        // kopia otrzymanej tablicy (immutable, otrzymane dane nie są modyfikowane)
        double[][] DataSet_Tc = Matrix2.copy(DataSet_T);

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
        int bestAxis = Math2.arg_max(stddevs);
        Arrays.sort(DataSet_Tc, Utils2.array_by_col_comparator(bestAxis, true));

        // lewe dziecko to będzie pierwsza połowa próbek; prawe dziecko druga połowa próbek
        int splitPoint = (sampleLen + 1) / 2;
        double[][] left_t = Utils2.extract_rows(DataSet_Tc, Utils2.range_ex(0, splitPoint));
        double[][] right_t = Utils2.extract_rows(DataSet_Tc, Utils2.range_ex(splitPoint, sampleLen));
        double[] median = left_t[left_t.length - 1];

        // rekurencyjnie tworzymy nowy node
        return KdtNode.node(
                median,
                bestAxis,
                KdtTree(left_t, depth + 1),
                KdtTree(right_t, depth + 1),
                sampleLen
        );
    }

    /**
     * Zwraca listę K-próbek będących sasiadami dla danej próbki testowej.
     * Próbki są wracane jako indeksy z DataSet_T podanego przy tworzeniu k-dim-tree
     */
    public int[] FindNeighbours(double[] sample_v, int k) {
        KdtNode kdt = this;

        // dopóki liczba próbek jest zbyt duża (wystarczy jedno dziecko)
        while (kdt.indLength / 2 >= k && !kdt.isLeaf()) {
            // porównujemy cechy, na podstawie której był podział i wskazujemy, czy będziemy szukać po lewej, czy prawej
            kdt = sample_v[kdt.axis] <= kdt.median_v[kdt.axis]
                    ? kdt.left
                    : kdt.right;
        }

        // zebranie indeksow ze wszystkich dzieci
        return kdt.CollectIndexes();
    }

    /**
     * Zbiera indeksy zgromadzone w lisciach, do ktorych prowadza wszystkie dzieci.
     * Aktualny node staje sie root, i to drzewo jest cale przejrzane.
     */
    private int[] CollectIndexes() {
        int[] indexes = new int[indLength];
        int copyIndPtr = 0;

        Deque<KdtNode> stack = new ArrayDeque<>();
        stack.push(this);

        while (!stack.isEmpty()) {
            KdtNode kdt = stack.pop();

            if (kdt.indexes != null) {
                System.arraycopy(kdt.indexes, 0, indexes, copyIndPtr, kdt.indexes.length);
                copyIndPtr += kdt.indexes.length;
            }

            if (kdt.left != null)
                stack.push(kdt.left);
            if (kdt.right != null)
                stack.push(kdt.right);
        }

        return indexes;
    }
}
