package main.java.com.simplexanddualsolver.solver;

import main.java.com.simplexanddualsolver.model.ModeloProblema;
import main.java.com.simplexanddualsolver.solution.Solucion;

public class SimplexSolver {

    private static final double EPS = 1e-9;

    public Solucion resolver(ModeloProblema modelo) {
        // Por simplicidad: asumimos que ya está en forma estándar
        // (todas las restricciones <= y b >= 0; si no, usarás DualSimplexSolver).
        double[][] A = modelo.getCoefRestricciones();
        double[] b = modelo.getLadosDerechos();
        double[] c = modelo.getCoefObjetivo();

        int m = b.length;        // restricciones
        int n = c.length;        // variables

        // Tabla: m filas de restricciones + 1 fila objetivo
        // n + m variables (originales + holguras) + 1 columna RHS
        double[][] tab = new double[m + 1][n + m + 1];

        // Copiar A y b
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tab[i][j] = A[i][j];
            }
            tab[i][n + i] = 1.0;     // holgura
            tab[i][n + m] = b[i];    // RHS
        }

        // Fila objetivo (maximizar)
        for (int j = 0; j < n; j++) {
            tab[m][j] = -c[j];
        }

        // Iteraciones simplex
        while (true) {
            int col = columnaEntrada(tab[m]);
            if (col == -1) {
                // Óptimo
                break;
            }
            int row = filaSalida(tab, col);
            if (row == -1) {
                return new Solucion(null, Double.POSITIVE_INFINITY, "NO_ACOTADA");
            }
            pivot(tab, row, col);
        }

        double[] x = new double[n];
        for (int j = 0; j < n; j++) {
            int filaBasica = filaBasica(tab, j, m);
            if (filaBasica != -1) {
                x[j] = tab[filaBasica][n + m];
            }
        }
        double z = tab[m][n + m];
        return new Solucion(x, z, "OPTIMA");
    }

    private int columnaEntrada(double[] filaObj) {
        int col = -1;
        for (int j = 0; j < filaObj.length - 1; j++) {
            if (filaObj[j] < -EPS && (col == -1 || filaObj[j] < filaObj[col])) {
                col = j;
            }
        }
        return col;
    }

    private int filaSalida(double[][] tab, int col) {
        int m = tab.length - 1;
        int rhs = tab[0].length - 1;
        int fila = -1;
        double mejor = Double.POSITIVE_INFINITY;
        for (int i = 0; i < m; i++) {
            if (tab[i][col] > EPS) {
                double ratio = tab[i][rhs] / tab[i][col];
                if (ratio < mejor - EPS) {
                    mejor = ratio;
                    fila = i;
                }
            }
        }
        return fila;
    }

    private void pivot(double[][] tab, int row, int col) {
        int m = tab.length;
        int n = tab[0].length;
        double p = tab[row][col];
        for (int j = 0; j < n; j++) tab[row][j] /= p;
        for (int i = 0; i < m; i++) {
            if (i == row) continue;
            double factor = tab[i][col];
            for (int j = 0; j < n; j++) {
                tab[i][j] -= factor * tab[row][j];
            }
        }
    }

    private int filaBasica(double[][] tab, int col, int m) {
        int fila = -1;
        for (int i = 0; i < m; i++) {
            if (Math.abs(tab[i][col] - 1.0) < EPS) {
                if (fila == -1) fila = i;
                else return -1; // no es básica
            } else if (Math.abs(tab[i][col]) > EPS) {
                return -1;
            }
        }
        return fila;
    }
}
