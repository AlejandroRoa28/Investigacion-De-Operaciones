package main.java.com.simplexanddualsolver.solution;

import java.util.ArrayList;
import java.util.List;

public class Solucion {

    private double[] valoresVariables;
    private double valorZ;
    private String estado;           // "OPTIMA", "INFACTIBLE", "NO_ACOTADA", "NO_CONVERGENTE"
    private double[][] tablaFinal;
    private int[] variablesBasicas;
    private int numIteraciones;
    private String metodoUsado;      // "Simplex" o "Dual Simplex"

    // Historial de iteraciones para visualización
    private List<IteracionSimplex> historialIteraciones;

    // Constructor vacío
    public Solucion() {
        this.historialIteraciones = new ArrayList<>();
    }

    // Constructor completo opcional
    public Solucion(double[] valoresVariables,
                    double valorZ,
                    String estado,
                    double[][] tablaFinal) {
        this.valoresVariables = valoresVariables;
        this.valorZ = valorZ;
        this.estado = estado;
        this.tablaFinal = tablaFinal;
        this.historialIteraciones = new ArrayList<>();
    }

    // Getters existentes
    public double[] getValoresVariables() { return valoresVariables; }
    public double getValorZ() { return valorZ; }
    public String getEstado() { return estado; }
    public double[][] getTablaFinal() { return tablaFinal; }
    public int[] getVariablesBasicas() { return variablesBasicas; }
    public int getNumIteraciones() { return numIteraciones; }
    public String getMetodoUsado() { return metodoUsado; }

    public boolean esOptima()      { return "OPTIMA".equalsIgnoreCase(estado); }
    public boolean esNoAcotada()   { return "NO_ACOTADA".equalsIgnoreCase(estado); }
    public boolean esInfactible()  { return "INFACTIBLE".equalsIgnoreCase(estado); }

    // Setters existentes
    public void setValoresVariables(double[] valoresVariables) {
        this.valoresVariables = valoresVariables;
    }

    public void setValorZ(double valorZ) {
        this.valorZ = valorZ;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setTablaFinal(double[][] tablaFinal) {
        this.tablaFinal = tablaFinal;
    }

    public void setVariablesBasicas(int[] variablesBasicas) {
        this.variablesBasicas = variablesBasicas;
    }

    public void setNumIteraciones(int numIteraciones) {
        this.numIteraciones = numIteraciones;
    }

    public void setMetodoUsado(String metodoUsado) {
        this.metodoUsado = metodoUsado;
    }

    // Métodos para el historial de iteraciones
    public void agregarIteracion(IteracionSimplex iteracion) {
        this.historialIteraciones.add(iteracion);
    }

    public List<IteracionSimplex> getHistorialIteraciones() {
        return historialIteraciones;
    }

    public IteracionSimplex getIteracion(int index) {
        if (index >= 0 && index < historialIteraciones.size()) {
            return historialIteraciones.get(index);
        }
        return null;
    }

    public int getTotalIteracionesGuardadas() {
        return historialIteraciones.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Método: ").append(metodoUsado != null ? metodoUsado : "No especificado").append("\n");
        sb.append("Estado: ").append(estado).append("\n");
        sb.append("Valor óptimo Z: ").append(valorZ).append("\n");
        if (valoresVariables != null) {
            for (int i = 0; i < valoresVariables.length; i++) {
                sb.append("x").append(i + 1).append(" = ")
                  .append(valoresVariables[i]).append("\n");
            }
        }
        return sb.toString();
    }

    // Clase interna para representar una iteración
    public static class IteracionSimplex {
        private int numeroIteracion;
        private double[][] tabla;
        private int[] base;
        private int filaSaliente;      // -1 si no aplica
        private int columnaEntrante;   // -1 si no aplica
        private double elementoPivote;
        private String variableEntra;
        private String variableSale;
        private String descripcion;    // Descripción adicional del paso

        public IteracionSimplex(int numeroIteracion) {
            this.numeroIteracion = numeroIteracion;
            this.filaSaliente = -1;
            this.columnaEntrante = -1;
        }

        // Getters
        public int getNumeroIteracion() { return numeroIteracion; }
        public double[][] getTabla() { return tabla; }
        public int[] getBase() { return base; }
        public int getFilaSaliente() { return filaSaliente; }
        public int getColumnaEntrante() { return columnaEntrante; }
        public double getElementoPivote() { return elementoPivote; }
        public String getVariableEntra() { return variableEntra; }
        public String getVariableSale() { return variableSale; }
        public String getDescripcion() { return descripcion; }

        // Setters
        public void setTabla(double[][] tabla) {
            // Copiar la tabla para evitar referencias
            int filas = tabla.length;
            int cols = tabla[0].length;
            this.tabla = new double[filas][cols];
            for (int i = 0; i < filas; i++) {
                System.arraycopy(tabla[i], 0, this.tabla[i], 0, cols);
            }
        }

        public void setBase(int[] base) {
            this.base = base.clone();
        }

        public void setFilaSaliente(int filaSaliente) {
            this.filaSaliente = filaSaliente;
        }

        public void setColumnaEntrante(int columnaEntrante) {
            this.columnaEntrante = columnaEntrante;
        }

        public void setElementoPivote(double elementoPivote) {
            this.elementoPivote = elementoPivote;
        }

        public void setVariableEntra(String variableEntra) {
            this.variableEntra = variableEntra;
        }

        public void setVariableSale(String variableSale) {
            this.variableSale = variableSale;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }
}
