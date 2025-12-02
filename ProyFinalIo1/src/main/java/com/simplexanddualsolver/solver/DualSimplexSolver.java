package main.java.com.simplexanddualsolver.solver;

import main.java.com.simplexanddualsolver.model.FormaEstandar;
import main.java.com.simplexanddualsolver.solution.Solucion;
import main.java.com.simplexanddualsolver.solution.Solucion.IteracionSimplex;

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
    
    private double[][] tabla;
    private int[] base;
    private int numFilas;
    private int numColumnas;
    private Solucion solucionActual;
    private int iteraciones;
    
    private static final double EPSILON = 1e-10;
    private static final int MAX_ITERACIONES = 1000;
    
    public DualSimplexSolver() {
        this.iteraciones = 0;
    }
    
    @Override
    public void resolver(FormaEstandar forma) {
        // Inicializar
        this.tabla = copiarTabla(forma.obtenerTablaInicial());
        this.base = forma.obtenerBaseInicial().clone();
        this.numFilas = tabla.length;
        this.numColumnas = tabla[0].length;
        this.iteraciones = 0;
        
        // Crear solución y establecer método usado
        solucionActual = new Solucion();
        solucionActual.setMetodoUsado("Dual Simplex");
        
        System.out.println("\n=== INICIANDO DUAL SIMPLEX ===");
        
        // Guardar iteración 0 (tabla inicial)
        guardarIteracion(0, -1, -1, "Tabla inicial del Dual Simplex");
        
        imprimirTabla(0);
        
        // Iterar hasta encontrar solución factible
        while (!esFactible() && iteraciones < MAX_ITERACIONES) {
            iteraciones++;
            
            System.out.println("\n--- Iteración " + iteraciones + " ---");
            
            // Paso 1: Seleccionar fila saliente (variable básica más negativa)
            int filaSaliente = seleccionarFilaSaliente();
            
            if (filaSaliente == -1) {
                // Ya es factible
                finalizarSolucion("OPTIMA");
                return;
            }
            
            String variableSale = "x" + (base[filaSaliente] + 1);
            System.out.println("Fila saliente: " + filaSaliente + 
                             " (variable " + variableSale + 
                             " = " + tabla[filaSaliente][numColumnas-1] + ")");
            
            // Paso 2: Seleccionar columna entrante (prueba del cociente dual)
            int columnaEntrante = seleccionarColumnaEntrante(filaSaliente);
            
            if (columnaEntrante == -1) {
                // Problema infactible
                System.out.println("\n¡Problema INFACTIBLE! No hay columna entrante válida.");
                guardarIteracion(iteraciones, filaSaliente, -1, 
                    "INFACTIBLE: No existe columna entrante válida para la fila " + filaSaliente);
                solucionActual.setEstado("INFACTIBLE");
                solucionActual.setNumIteraciones(iteraciones);
                return;
            }
            
            String variableEntra = "x" + (columnaEntrante + 1);
            double elementoPivote = tabla[filaSaliente][columnaEntrante];
            
            System.out.println("Columna entrante: " + columnaEntrante + " (variable " + variableEntra + ")");
            System.out.println("Pivote: tabla[" + filaSaliente + "][" + columnaEntrante + "] = " + elementoPivote);
            
            // Guardar estado ANTES del pivoteo (para mostrar qué se va a hacer)
            String descripcion = String.format(
                "Sale: %s (fila %d, valor=%.4f) | Entra: %s (col %d) | Pivote: %.4f",
                variableSale, filaSaliente, tabla[filaSaliente][numColumnas-1],
                variableEntra, columnaEntrante, elementoPivote
            );
            
            // Paso 3: Realizar pivoteo
            realizarPivoteo(filaSaliente, columnaEntrante);
            
            // Paso 4: Actualizar base
            base[filaSaliente] = columnaEntrante;
            
            // Guardar iteración DESPUÉS del pivoteo
            guardarIteracionCompleta(iteraciones, filaSaliente, columnaEntrante, 
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
        
        System.out.println("\n=== DUAL SIMPLEX FINALIZADO ===");
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
        // Valores de variables
        double[] valores = new double[numColumnas - 1];
        for (int i = 0; i < numFilas - 1; i++) {
            int varBasica = base[i];
            if (varBasica < valores.length) {
                valores[varBasica] = tabla[i][numColumnas - 1];
            }
        }
        
        double valorZ = tabla[numFilas - 1][numColumnas - 1];
        
        solucionActual.setValoresVariables(valores);
        solucionActual.setValorZ(valorZ);
        solucionActual.setTablaFinal(copiarTabla(tabla));
        solucionActual.setVariablesBasicas(base.clone());
        solucionActual.setNumIteraciones(iteraciones);
        solucionActual.setEstado(estado);
        
        // Guardar iteración final
        guardarIteracion(iteraciones, -1, -1, "Solución " + estado + " encontrada. Z = " + valorZ);
    }
    
    private int seleccionarFilaSaliente() {
        int filaSaliente = -1;
        double valorMasNegativo = 0.0;
        
        for (int i = 0; i < numFilas - 1; i++) {
            double valorB = tabla[i][numColumnas - 1];
            if (valorB < -EPSILON && valorB < valorMasNegativo) {
                valorMasNegativo = valorB;
                filaSaliente = i;
            }
        }
        
        return filaSaliente;
    }
    
    private int seleccionarColumnaEntrante(int filaSaliente) {
        int columnaEntrante = -1;
        double cocienteMinimo = Double.POSITIVE_INFINITY;
        int filaZ = numFilas - 1;
        
        for (int j = 0; j < numColumnas - 1; j++) {
            double valorEnFila = tabla[filaSaliente][j];
            
            if (valorEnFila < -EPSILON) {
                double valorZ = tabla[filaZ][j];
                double cociente = valorZ / valorEnFila;
                
                if (cociente < cocienteMinimo) {
                    cocienteMinimo = cociente;
                    columnaEntrante = j;
                }
            }
        }
        
        return columnaEntrante;
    }
    
    private void realizarPivoteo(int filaPivote, int columnaPivote) {
        double elementoPivote = tabla[filaPivote][columnaPivote];
        
        if (Math.abs(elementoPivote) < EPSILON) {
            System.err.println("¡ERROR! Elemento pivote muy pequeño: " + elementoPivote);
            return;
        }
        
        for (int j = 0; j < numColumnas; j++) {
            tabla[filaPivote][j] /= elementoPivote;
        }
        
        for (int i = 0; i < numFilas; i++) {
            if (i != filaPivote) {
                double factor = tabla[i][columnaPivote];
                for (int j = 0; j < numColumnas; j++) {
                    tabla[i][j] -= factor * tabla[filaPivote][j];
                }
            }
        }
    }
    
    private boolean esFactible() {
        for (int i = 0; i < numFilas - 1; i++) {
            if (tabla[i][numColumnas - 1] < -EPSILON) {
                return false;
            }
        }
        return true;
    }
    
    private double[][] copiarTabla(double[][] original) {
        int filas = original.length;
        int columnas = original[0].length;
        double[][] copia = new double[filas][columnas];
        
        for (int i = 0; i < filas; i++) {
            System.arraycopy(original[i], 0, copia[i], 0, columnas);
        }
        
        return copia;
    }
    
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
