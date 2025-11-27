package main.java.com.simplexanddualsolver.model;
public class FormaEstandar {

    private double[][] tablaSimplexInicial;
    private String[] tiposRestricciones;

    private int numVariablesOriginales;
    private int numRestricciones;

    private int numHolgura;
    private int numExceso;
    private int numArtificial;

    private int[] baseInicial;

    private int[] columnasHolgura;

    public void convertirAEstandar(ModeloProblema modelo) {
    double[][] A = modelo.obtenerMatrizA();
    double[] b = modelo.obtenerVectorB();
    double[] c = modelo.obtenerVectorC();
    this.tiposRestricciones = modelo.getTiposRestricciones();

    this.numRestricciones = A.length;
    this.numVariablesOriginales = A[0].length;

    // 1) Contar holguras, excesos, artificiales
    numHolgura = 0;
    numExceso = 0;
    numArtificial = 0;

    for (int i = 0; i < numRestricciones; i++) {
        String tipo = tiposRestricciones[i];
        if ("<=".equals(tipo)) {
            numHolgura++;
        } else if (">=".equals(tipo)) {
            numExceso++;
            numArtificial++;
        } else if ("=".equals(tipo)) {
            numArtificial++;
        } else {
            throw new IllegalArgumentException("Tipo de restricción no soportado: " + tipo);
        }
    }
    columnasHolgura = new int[numHolgura];
    
    // 2) Tamaños
    int columnasTotales = numVariablesOriginales
                        + numHolgura
                        + numExceso
                        + numArtificial
                        + 1; // b
    int filasTotales = numRestricciones + 1; // +Z

    tablaSimplexInicial = new double[filasTotales][columnasTotales];
    baseInicial = new int[numRestricciones];

    int inicioHolguras   = numVariablesOriginales;
    int inicioExcesos    = inicioHolguras + numHolgura;
    int inicioArtificial = inicioExcesos + numExceso;
    int colB             = columnasTotales - 1;

    int contadorHolgura    = 0;
    int contadorExceso     = 0;
    int contadorArtificial = 0;

    // 3) Restricciones
    for (int i = 0; i < numRestricciones; i++) {
        for (int j = 0; j < numVariablesOriginales; j++) {
            tablaSimplexInicial[i][j] = A[i][j];
        }

        String tipo = tiposRestricciones[i];

        if ("<=".equals(tipo)) {
            int colS = inicioHolguras + contadorHolgura;
            tablaSimplexInicial[i][colS] = 1.0;
            baseInicial[i] = colS;
            columnasHolgura[contadorHolgura] = colS;
            contadorHolgura++;
        } else if (">=".equals(tipo)) {
            int colE = inicioExcesos + contadorExceso;
            tablaSimplexInicial[i][colE] = -1.0;
            contadorExceso++;

            int colA = inicioArtificial + contadorArtificial;
            tablaSimplexInicial[i][colA] = 1.0;
            baseInicial[i] = colA;
            contadorArtificial++;

        } else if ("=".equals(tipo)) {
            int colA = inicioArtificial + contadorArtificial;
            tablaSimplexInicial[i][colA] = 1.0;
            baseInicial[i] = colA;
            contadorArtificial++;
        }

        tablaSimplexInicial[i][colB] = b[i];
    }

    // 4) Fila Z (sin esMaximizacion, asumimos MAX y c tal como viene)
    int filaZ = filasTotales - 1;
    for (int j = 0; j < numVariablesOriginales; j++) {
        // Z - sum(cj * xj) = 0 => coef = -cj
        tablaSimplexInicial[filaZ][j] = -c[j];
    }
    // El resto de columnas de Z se queda en 0
}

    public double[][] obtenerTablaInicial() {
        return tablaSimplexInicial;
    }

    public int[] obtenerBaseInicial() {
        return baseInicial;
    }

    public int getNumVariablesOriginales() {
        return numVariablesOriginales;
    }

    public int getNumRestricciones() {
        return numRestricciones;
    }

    public int getNumHolgura() {
        return numHolgura;
    }

    public int getNumExceso() {
        return numExceso;
    }

    public int getNumArtificial() {
        return numArtificial;
    }
    public int[] getColumnasHolgura() {
    return columnasHolgura;
}

}

