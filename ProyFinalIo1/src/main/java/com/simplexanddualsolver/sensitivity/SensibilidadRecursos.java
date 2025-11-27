package main.java.com.simplexanddualsolver.sensitivity;
public class SensibilidadRecursos {

    private final double[][] tablaFinal;
    private final int filaZ;
    private final int[] columnasHolgura;
    private final double[] bOriginal;

    public SensibilidadRecursos(double[][] tablaFinal, 
                                 int[] columnasHolgura,
                                 double[] bOriginal) {
        this.tablaFinal = tablaFinal;
        this.filaZ = tablaFinal.length - 1;
        this.columnasHolgura = columnasHolgura;
        this.bOriginal = bOriginal;
    }

    public double obtenerPrecioSombra(int i) {
        int colS = columnasHolgura[i];
        return tablaFinal[filaZ][colS];
    }

    public double calcularImpactoEnZ(double[] deltaB) {
        double deltaZ = 0.0;
        
        for (int i = 0; i < deltaB.length; i++) {
            double precioSombra = obtenerPrecioSombra(i);
            deltaZ += precioSombra * deltaB[i];
        }
        
        return deltaZ;
    }

    public double[] calcularRangoB(int i) {
        int colS = columnasHolgura[i];
        int colRHS = tablaFinal[0].length - 1;
        
        double deltaMin = Double.NEGATIVE_INFINITY;
        double deltaMax = Double.POSITIVE_INFINITY;
        
        for (int fila = 0; fila < filaZ; fila++) {
            double coefS = tablaFinal[fila][colS];
            double valorActual = tablaFinal[fila][colRHS];
            
            if (Math.abs(coefS) < 1e-10) {
                continue;
            }
            
            double limite = -valorActual / coefS;
            
            if (coefS > 0) {
                deltaMin = Math.max(deltaMin, limite);
            } else {
                deltaMax = Math.min(deltaMax, limite);
            }
        }
        
        double bMin = bOriginal[i] + deltaMin;
        double bMax = bOriginal[i] + deltaMax;
        
        return new double[]{bMin, bMax};
    }
}

