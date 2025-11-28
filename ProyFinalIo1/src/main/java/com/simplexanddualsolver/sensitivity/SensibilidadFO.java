package main.java.com.simplexanddualsolver.sensitivity;

public class SensibilidadFO {

    private final double[][] tablaFinal;
    private final int filaZ;
    private final int numColumnas;
    private final int numVars;

    public SensibilidadFO(double[][] tablaFinal) {
        this.tablaFinal = tablaFinal;
        this.filaZ = tablaFinal.length - 1;
        this.numColumnas = tablaFinal[0].length;
        this.numVars = numColumnas - 1;
    }

    public double getCostoReducido(int j) {
        return tablaFinal[filaZ][j];
    }

    public double[] getCostosReducidos() {
        double[] cr = new double[numVars];
        for (int j = 0; j < numVars; j++) {
            cr[j] = tablaFinal[filaZ][j];
        }
        return cr;
    }

    public double[] calcularIntervaloObjetivoNoBasica(int j, double cjOriginal) {
        double costoReducidoActual = getCostoReducido(j);
        double deltaMax = -costoReducidoActual;
        double cMin = Double.NEGATIVE_INFINITY;
        double cMax = cjOriginal + deltaMax;
        return new double[]{cMin, cMax};
    }

    public double[] calcularIntervaloObjetivoBasica(int j, double cjOriginal) {
        return new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY};
    }
}
