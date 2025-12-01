package main.java.com.simplexanddualsolver.sensitivity;

public class SensibilidadTecnologica {

    private final double[][] tablaFinal;
    private final int filaZ;
    private final int[] baseFinal; // columnas de las variables básicas

    public SensibilidadTecnologica(double[][] tablaFinal, int[] baseFinal) {
        this.tablaFinal = tablaFinal;
        this.filaZ = tablaFinal.length - 1;
        this.baseFinal = baseFinal;
    }

    // Costo reducido de la variable j
    public double calcularCostoReducido(int j) {
        return tablaFinal[filaZ][j];
    }

    // Vector completo de costos reducidos
    public double[] getCostosReducidos() {
        int n = tablaFinal[0].length - 1; // sin la columna b
        double[] cr = new double[n];
        for (int j = 0; j < n; j++) {
            cr[j] = tablaFinal[filaZ][j];
        }
        return cr;
    }

    // ¿La variable j es básica en la solución óptima?
    public boolean esVariableBasica(int j) {
        if (baseFinal == null) return false;
        for (int colBasica : baseFinal) {
            if (colBasica == j) {
                return true;
            }
        }
        return false;
    }

    // Para MAX: conviene introducir j si su costo reducido es < 0 y no es básica
    public boolean esRentableIntroducir(int j) {
        if (esVariableBasica(j)) {
            return false;
        }
        double cr = calcularCostoReducido(j);
        return cr < -1e-10;
    }
}
