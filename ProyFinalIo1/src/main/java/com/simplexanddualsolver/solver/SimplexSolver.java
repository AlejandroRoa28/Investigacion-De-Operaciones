package main.java.com.simplexanddualsolver.solver;

import main.java.com.simplexanddualsolver.model.FormaEstandar;
import main.java.com.simplexanddualsolver.solution.Solucion;
import main.java.com.simplexanddualsolver.solution.Solucion.IteracionSimplex;

public class SimplexSolver implements MotorSolver {

    private double[][] tabla;
    private int[] base;
    private int numVariablesOriginales;
    private int numRestricciones;
    private int iteraciones;
    private Solucion solucionActual;

    private static final double EPSILON = 1e-10;
    private static final int MAX_ITERACIONES = 1000;

    @Override
    public void resolver(FormaEstandar forma) {
        // Inicializar
        this.tabla = copiarTabla(forma.obtenerTablaInicial());
        this.base = forma.obtenerBaseInicial().clone();
        this.numVariablesOriginales = forma.getNumVariablesOriginales();
        this.numRestricciones = forma.getNumRestricciones();
        this.iteraciones = 0;

        // Crear solución y establecer método usado
        solucionActual = new Solucion();
        solucionActual.setMetodoUsado("Simplex");

        System.out.println("\n=== INICIANDO SIMPLEX ===");

        // Guardar iteración 0 (tabla inicial)
        guardarIteracion(0, -1, -1, "Tabla inicial del Simplex");

        imprimirTabla(0);

        // Iterar hasta encontrar solución óptima
        while (!esOptima() && iteraciones < MAX_ITERACIONES) {
            iteraciones++;

            System.out.println("\n--- Iteración " + iteraciones + " ---");

            // Paso 1: Seleccionar columna entrante (coeficiente más negativo en Z)
            int colEntrante = seleccionarColumnaEntrante();

            if (colEntrante == -1) {
                // Ya es óptimo
                finalizarSolucion("OPTIMA");
                return;
            }

            String variableEntra = "x" + (colEntrante + 1);
            System.out.println("Columna entrante: " + colEntrante + " (variable " + variableEntra + ")");

            // Paso 2: Seleccionar fila saliente (prueba de la razón mínima)
            int filaSaliente = seleccionarFilaSaliente(colEntrante);

            if (filaSaliente == -1) {
                // Problema no acotado
                System.out.println("\n¡Problema NO ACOTADO! No hay fila saliente válida.");
                guardarIteracion(iteraciones, -1, colEntrante,
                    "NO ACOTADO: No existe fila saliente válida para la columna " + colEntrante);
                solucionActual.setEstado("NO_ACOTADA");
                solucionActual.setNumIteraciones(iteraciones);
                return;
            }

            String variableSale = "x" + (base[filaSaliente] + 1);
            double elementoPivote = tabla[filaSaliente][colEntrante];

            System.out.println("Fila saliente: " + filaSaliente + " (variable " + variableSale + ")");
            System.out.println("Pivote: tabla[" + filaSaliente + "][" + colEntrante + "] = " + elementoPivote);

            // Descripción del paso
            String descripcion = String.format(
                "Entra: %s (col %d) | Sale: %s (fila %d) | Pivote: %.4f | Razón mínima",
                variableEntra, colEntrante,
                variableSale, filaSaliente, elementoPivote
            );

            // Paso 3: Realizar pivoteo
            realizarPivoteo(filaSaliente, colEntrante);

            // Paso 4: Actualizar base
            base[filaSaliente] = colEntrante;

            // Guardar iteración DESPUÉS del pivoteo
            guardarIteracionCompleta(iteraciones, filaSaliente, colEntrante,
                                     variableSale, variableEntra, elementoPivote, descripcion);

            imprimirTabla(iteraciones);
        }

        // Verificar límite de iteraciones
        if (iteraciones >= MAX_ITERACIONES) {
            System.out.println("\n¡ADVERTENCIA! Se alcanzó el límite de iteraciones.");
            solucionActual.setEstado("NO_CONVERGENTE");
            solucionActual.setNumIteraciones(iteraciones);
            return;
        }

        // Solución óptima encontrada
        finalizarSolucion("OPTIMA");

        System.out.println("\n=== SIMPLEX FINALIZADO ===");
        System.out.println("Iteraciones totales: " + iteraciones);
    }

    /**
     * Guarda una iteración en el historial (versión simple)
     */
    private void guardarIteracion(int numIter, int filaSaliente, int colEntrante, String descripcion) {
        IteracionSimplex iter = new IteracionSimplex(numIter);
        iter.setTabla(tabla);
        iter.setBase(base);
        iter.setFilaSaliente(filaSaliente);
        iter.setColumnaEntrante(colEntrante);
        iter.setDescripcion(descripcion);
        solucionActual.agregarIteracion(iter);
    }

    /**
     * Guarda una iteración con información completa del pivoteo
     */
    private void guardarIteracionCompleta(int numIter, int filaSaliente, int colEntrante,
                                          String varSale, String varEntra,
                                          double pivote, String descripcion) {
        IteracionSimplex iter = new IteracionSimplex(numIter);
        iter.setTabla(tabla);
        iter.setBase(base);
        iter.setFilaSaliente(filaSaliente);
        iter.setColumnaEntrante(colEntrante);
        iter.setVariableSale(varSale);
        iter.setVariableEntra(varEntra);
        iter.setElementoPivote(pivote);
        iter.setDescripcion(descripcion);
        solucionActual.agregarIteracion(iter);
    }

    /**
     * Finaliza la solución extrayendo los valores finales
     */
    private void finalizarSolucion(String estado) {
        int colB = tabla[0].length - 1;

        // Valores de variables
        double[] valoresVariables = new double[numVariablesOriginales];
        for (int i = 0; i < numRestricciones; i++) {
            int varBasica = base[i];
            if (varBasica < numVariablesOriginales) {
                valoresVariables[varBasica] = tabla[i][colB];
            }
        }

        int filaZ = tabla.length - 1;
        double valorZ = tabla[filaZ][colB];

        solucionActual.setValoresVariables(valoresVariables);
        solucionActual.setValorZ(valorZ);
        solucionActual.setTablaFinal(copiarTabla(tabla));
        solucionActual.setVariablesBasicas(base.clone());
        solucionActual.setNumIteraciones(iteraciones);
        solucionActual.setEstado(estado);

        // Guardar iteración final
        guardarIteracion(iteraciones, -1, -1, "Solución " + estado + " encontrada. Z = " + valorZ);
    }

    @Override
    public Solucion obtenerSolucion() {
        return solucionActual;
    }

    private boolean esOptima() {
        int filaZ = tabla.length - 1;
        int numCols = tabla[0].length - 1;

        for (int j = 0; j < numCols; j++) {
            if (tabla[filaZ][j] < -EPSILON) {
                return false;
            }
        }
        return true;
    }

    private int seleccionarColumnaEntrante() {
        int filaZ = tabla.length - 1;
        int numCols = tabla[0].length - 1;

        int colEntrante = -1;
        double valorMin = 0;

        for (int j = 0; j < numCols; j++) {
            if (tabla[filaZ][j] < valorMin) {
                valorMin = tabla[filaZ][j];
                colEntrante = j;
            }
        }

        return colEntrante;
    }

    private int seleccionarFilaSaliente(int colEntrante) {
        int colB = tabla[0].length - 1;
        int filaSaliente = -1;
        double minRatio = Double.MAX_VALUE;

        for (int i = 0; i < numRestricciones; i++) {
            double coef = tabla[i][colEntrante];

            if (coef > EPSILON) {
                double ratio = tabla[i][colB] / coef;

                if (ratio >= 0 && ratio < minRatio) {
                    minRatio = ratio;
                    filaSaliente = i;
                }
            }
        }

        return filaSaliente;
    }

    private void realizarPivoteo(int filaPivote, int colPivote) {
        double pivote = tabla[filaPivote][colPivote];

        for (int j = 0; j < tabla[0].length; j++) {
            tabla[filaPivote][j] /= pivote;
        }

        for (int i = 0; i < tabla.length; i++) {
            if (i != filaPivote) {
                double factor = tabla[i][colPivote];
                for (int j = 0; j < tabla[0].length; j++) {
                    tabla[i][j] -= factor * tabla[filaPivote][j];
                }
            }
        }
    }

    private double[][] copiarTabla(double[][] original) {
        double[][] copia = new double[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copia[i], 0, original[i].length);
        }
        return copia;
    }

    private void imprimirTabla(int iteracion) {
        System.out.println("\nTabla en iteración " + iteracion + ":");
        System.out.println("Base: " + java.util.Arrays.toString(base));

        for (int i = 0; i < tabla.length; i++) {
            if (i < numRestricciones) {
                System.out.print("x" + (base[i] + 1) + " | ");
            } else {
                System.out.print("Z  | ");
            }

            for (int j = 0; j < tabla[0].length; j++) {
                System.out.printf("%8.3f ", tabla[i][j]);
            }
            System.out.println();
        }
    }
}
