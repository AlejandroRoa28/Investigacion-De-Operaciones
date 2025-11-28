package main.java.com.simplexanddualsolver.solver;

import main.java.com.simplexanddualsolver.model.FormaEstandar;
import main.java.com.simplexanddualsolver.solution.Solucion;

/**
 * Implementación del método Dual Simplex para resolver problemas de Programación Lineal
 * 
 * El Dual Simplex es útil cuando:
 * - La solución inicial NO es factible (tiene valores negativos en b)
 * - Pero SÍ es óptima en el dual (todos los coeficientes de Z son correctos)
 * 
 * Diferencia con Simplex regular:
 * - Simplex: Factible pero no óptimo → busca mejorar Z
 * - Dual Simplex: Óptimo pero no factible → busca hacer factible sin perder optimalidad
 */
public class DualSimplexSolver implements MotorSolver {
    
    private double[][] tabla;           // Tabla del Simplex
    private int[] base;                 // Variables básicas (índices)
    private int numFilas;               // Número de filas (restricciones + fila Z)
    private int numColumnas;            // Número de columnas (variables + b)
    private Solucion solucionActual;    // Solución encontrada
    private int iteraciones;            // Contador de iteraciones
    
    private static final double EPSILON = 1e-10;  // Tolerancia para comparaciones
    private static final int MAX_ITERACIONES = 1000;  // Límite de iteraciones
    
    /**
     * Constructor
     */
    public DualSimplexSolver() {
        this.iteraciones = 0;
    }
    
    /**
     * Resuelve el problema usando el método Dual Simplex
     * @param forma FormaEstandar que contiene la tabla inicial y la base
     */
    @Override
    public void resolver(FormaEstandar forma) {
        // Obtener tabla inicial y base desde FormaEstandar
        this.tabla = copiarTabla(forma.obtenerTablaInicial());
        this.base = forma.obtenerBaseInicial().clone();
        this.numFilas = tabla.length;
        this.numColumnas = tabla[0].length;
        this.iteraciones = 0;
        
        System.out.println("\n=== INICIANDO DUAL SIMPLEX ===");
        imprimirTabla(0);
        
        // Iterar hasta encontrar solución factible
        while (!esFactible() && iteraciones < MAX_ITERACIONES) {
            iteraciones++;
            
            System.out.println("\n--- Iteración " + iteraciones + " ---");
            
            // Paso 1: Seleccionar fila saliente (variable básica más negativa)
            int filaSaliente = seleccionarFilaSaliente();
            
            if (filaSaliente == -1) {
                // No hay valores negativos, ya es factible
                solucionActual = extraerSolucion();
                solucionActual.setEstado("OPTIMA");
                return;
            }
            
            System.out.println("Fila saliente: " + filaSaliente + 
                             " (variable x" + (base[filaSaliente]+1) + 
                             " = " + tabla[filaSaliente][numColumnas-1] + ")");
            
            // Paso 2: Seleccionar columna entrante (prueba del cociente dual)
            int columnaEntrante = seleccionarColumnaEntrante(filaSaliente);
            
            if (columnaEntrante == -1) {
                // Problema infactible
                System.out.println("\n¡Problema INFACTIBLE! No hay columna entrante válida.");
                solucionActual = new Solucion();
                solucionActual.setEstado("INFACTIBLE");
                return;
            }
            
            System.out.println("Columna entrante: " + columnaEntrante + " (variable x" + (columnaEntrante+1) + ")");
            System.out.println("Pivote: tabla[" + filaSaliente + "][" + columnaEntrante + "] = " + 
                             tabla[filaSaliente][columnaEntrante]);
            
            // Paso 3: Realizar pivoteo
            realizarPivoteo(filaSaliente, columnaEntrante);
            
            // Paso 4: Actualizar base
            base[filaSaliente] = columnaEntrante;
            
            imprimirTabla(iteraciones);
        }
        
        // Verificar si se alcanzó el límite de iteraciones
        if (iteraciones >= MAX_ITERACIONES) {
            System.out.println("\n¡ADVERTENCIA! Se alcanzó el límite de iteraciones.");
            solucionActual = new Solucion();
            solucionActual.setEstado("NO_CONVERGENTE");
            return;
        }
        
        // Extraer solución final
        solucionActual = extraerSolucion();
        solucionActual.setEstado("OPTIMA");
        
        System.out.println("\n=== DUAL SIMPLEX FINALIZADO ===");
        System.out.println("Iteraciones totales: " + iteraciones);
    }
    
    /**
     * PASO 1: Seleccionar fila saliente
     * Buscar la variable básica con el valor más negativo (más infactible)
     * @return Índice de la fila saliente, o -1 si ya es factible
     */
    private int seleccionarFilaSaliente() {
        int filaSaliente = -1;
        double valorMasNegativo = 0.0;
        
        // Recorrer todas las filas excepto la fila Z (última)
        for (int i = 0; i < numFilas - 1; i++) {
            double valorB = tabla[i][numColumnas - 1];  // Última columna es b
            
            // Si b[i] es negativo y es el más negativo hasta ahora
            if (valorB < -EPSILON && valorB < valorMasNegativo) {
                valorMasNegativo = valorB;
                filaSaliente = i;
            }
        }
        
        return filaSaliente;
    }
    
    /**
     * PASO 2: Seleccionar columna entrante (Prueba del cociente dual)
     * 
     * Para la fila saliente k, calcular:
     * cociente[j] = Z[j] / tabla[k][j]   para todo j donde tabla[k][j] < 0
     * 
     * Seleccionar j con el cociente MÍNIMO
     * 
     * @param filaSaliente Fila que sale de la base
     * @return Índice de la columna entrante, o -1 si no hay columna válida
     */
    private int seleccionarColumnaEntrante(int filaSaliente) {
        int columnaEntrante = -1;
        double cocienteMinimo = Double.POSITIVE_INFINITY;
        int filaZ = numFilas - 1;  // Última fila es Z
        
        // Recorrer todas las columnas excepto la última (b)
        for (int j = 0; j < numColumnas - 1; j++) {
            double valorEnFila = tabla[filaSaliente][j];
            
            // Solo considerar si el valor es NEGATIVO
            if (valorEnFila < -EPSILON) {
                double valorZ = tabla[filaZ][j];
                double cociente = valorZ / valorEnFila;
                
                // Buscar el cociente MÍNIMO (más negativo o menos positivo)
                if (cociente < cocienteMinimo) {
                    cocienteMinimo = cociente;
                    columnaEntrante = j;
                }
            }
        }
        
        return columnaEntrante;
    }
    
    /**
     * PASO 3: Realizar pivoteo
     * 
     * 1. Dividir fila pivote por elemento pivote
     * 2. Hacer ceros en el resto de la columna pivote
     * 
     * @param filaPivote Fila del elemento pivote
     * @param columnaPivote Columna del elemento pivote
     */
    private void realizarPivoteo(int filaPivote, int columnaPivote) {
        double elementoPivote = tabla[filaPivote][columnaPivote];
        
        if (Math.abs(elementoPivote) < EPSILON) {
            System.err.println("¡ERROR! Elemento pivote muy pequeño: " + elementoPivote);
            return;
        }
        
        // 1. Dividir toda la fila pivote por el elemento pivote
        for (int j = 0; j < numColumnas; j++) {
            tabla[filaPivote][j] /= elementoPivote;
        }
        
        // 2. Hacer ceros en el resto de la columna pivote
        for (int i = 0; i < numFilas; i++) {
            if (i != filaPivote) {
                double factor = tabla[i][columnaPivote];
                for (int j = 0; j < numColumnas; j++) {
                    tabla[i][j] -= factor * tabla[filaPivote][j];
                }
            }
        }
    }
    
    /**
     * Verifica si la solución actual es factible
     * Una solución es factible si todos los valores de b son ≥ 0
     * @return true si es factible, false en caso contrario
     */
    private boolean esFactible() {
        for (int i = 0; i < numFilas - 1; i++) {
            if (tabla[i][numColumnas - 1] < -EPSILON) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Extrae la solución de la tabla final
     * @return Objeto Solucion con los resultados
     */
    private Solucion extraerSolucion() {
        Solucion solucion = new Solucion();
        
        // Inicializar valores de todas las variables en 0
        double[] valores = new double[numColumnas - 1];
        
        // Asignar valores a variables básicas
        for (int i = 0; i < numFilas - 1; i++) {
            int varBasica = base[i];
            if (varBasica < valores.length) {
                valores[varBasica] = tabla[i][numColumnas - 1];
            }
        }
        
        // Obtener valor de Z (última fila, última columna)
        double valorZ = tabla[numFilas - 1][numColumnas - 1];
        
        solucion.setValoresVariables(valores);
        solucion.setValorZ(valorZ);
        solucion.setTablaFinal(copiarTabla(tabla));
        solucion.setVariablesBasicas(base.clone());
        solucion.setNumIteraciones(iteraciones);
        
        return solucion;
    }
    
    /**
     * Copia una tabla (matriz)
     */
    private double[][] copiarTabla(double[][] original) {
        int filas = original.length;
        int columnas = original[0].length;
        double[][] copia = new double[filas][columnas];
        
        for (int i = 0; i < filas; i++) {
            System.arraycopy(original[i], 0, copia[i], 0, columnas);
        }
        
        return copia;
    }
    
    /**
     * Imprime la tabla actual (para depuración)
     */
    private void imprimirTabla(int iteracion) {
        System.out.println("\nTabla en iteración " + iteracion + ":");
        System.out.println("Base: " + java.util.Arrays.toString(base));
        
        for (int i = 0; i < numFilas; i++) {
            if (i < numFilas - 1) {
                System.out.print("x" + (base[i]+1) + " | ");
            } else {
                System.out.print("Z  | ");
            }
            
            for (int j = 0; j < numColumnas; j++) {
                System.out.printf("%8.3f ", tabla[i][j]);
            }
            System.out.println();
        }
    }
    
    @Override
    public Solucion obtenerSolucion() {
        return solucionActual;
    }
}