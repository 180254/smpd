package utils;

public class Matrix3 {

    public static boolean almostEquals(double a, double b, double eps) {
        return Math.abs(a - b) < eps
                || (Double.isNaN(a) && Double.isNaN(b))
                || (Double.isInfinite(a) && Double.isInfinite(b));
    }

    public static boolean equals(double[][] matrix1, double[][] matrix2, double epsilon) {
        if (matrix1.length != matrix2.length) {
            return false;
        }

        for (int i = 0; i < matrix1.length; i++) {
            if (matrix1[i].length != matrix2[i].length) {
                return false;
            }

            for (int j = 0; j < matrix1[i].length; j++) {
                if (!almostEquals(matrix1[i][j], matrix2[i][j], epsilon)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static String to_string(double[][] matrix) {
        return to_string(matrix, ',');
    }

    public static String to_string(double[][] matrix, char dot) {
        StringBuilder sb = new StringBuilder();
        for (double[] row : matrix) {
            for (double value : row) {
                String formatted = String.format("%.5f", value);
                sb.append(formatted.replace(',', dot)).append(" ");
            }

            sb.setLength(Math.max(sb.length() - 1, 0));
            sb.append("\n");
        }

        sb.setLength(Math.max(sb.length() - 1, 0));
        return sb.toString();
    }

    public static double[][] from_string(String matrix) {
        String[] rows = matrix.split("\n");
        double[][] result = new double[rows.length][];

        for (int i = 0; i < rows.length; i++) {
            String[] numbers = rows[i].split(" ");
            result[i] = new double[numbers.length];

            for (int j = 0; j < numbers.length; j++) {
                result[i][j] = Double.parseDouble(numbers[j]);
            }
        }
        return result;
    }
}
