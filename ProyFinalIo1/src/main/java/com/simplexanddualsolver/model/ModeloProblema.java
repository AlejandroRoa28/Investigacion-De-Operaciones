package main.java.com.simplexanddualsolver.model;

/**
 * Clase que representa un modelo de Programación Lineal
 * basado en el diagrama UML suministrado.
 */
public class ModeloProblema {

    // ==================== ATRIBUTOS SEGÚN UML ====================
    private double[][] A;        // Matriz de restricciones
    private double[] b;          // Vector lado derecho
    private double[] c;          // Vector función objetivo
    private int numVariables;    // Número de variables
    private int numRestricciones; // Número de restricciones

    // ==================== CONSTRUCTORES ====================

    /**
     * Constructor vacío
     */
    public ModeloProblema() {
    }

    /**
     * Constructor con matrices A, b y c
     */
    public ModeloProblema(double[][] A, double[] b, double[] c) {
        this.A = A;
        this.b = b;
        this.c = c;

        this.numRestricciones = A.length;
        this.numVariables = A[0].length;
    }

    // ==================== MÉTODOS DEL UML ====================

    /**
     * Valida que el modelo sea consistente:
     * - Las dimensiones coinciden
     * - No hay valores nulos
     * - Las matrices y vectores tienen tamaño correcto
     */
    public boolean validarModelo() {

        if (A == null || b == null || c == null) {
            System.err.println("Error: Matrices A, b o c son nulas.");
            return false;
        }

        if (A.length == 0 || A[0].length == 0) {
            System.err.println("Error: La matriz A no puede estar vacía.");
            return false;
        }

        if (b.length != A.length) {
            System.err.println("Error: El vector b debe tener " + A.length + " elementos.");
            return false;
        }

        if (c.length != A[0].length) {
            System.err.println("Error: El vector c debe tener " + A[0].length + " elementos.");
            return false;
        }

        // Verificar que todas las filas de A tengan el mismo número de columnas
        for (int i = 0; i < A.length; i++) {
            if (A[i].length != c.length) {
                System.err.println("Error: La fila " + i + " de A no tiene " + c.length + " columnas.");
                return false;
            }
        }

        return true;
    }

    /**
     * Retorna la matriz A
     */
    public double[][] obtenerMatrizA() {
        return A;
    }

    /**
     * Retorna el vector b
     */
    public double[] obtenerVectorB() {
        return b;
    }

    /**
     * Retorna el vector c
     */
    public double[] obtenerVectorC() {
        return c;
    }

    // ==================== GETTERS ADICIONALES ====================

    public int getNumVariables() {
        return numVariables;
    }

    public int getNumRestricciones() {
        return numRestricciones;
    }
}
