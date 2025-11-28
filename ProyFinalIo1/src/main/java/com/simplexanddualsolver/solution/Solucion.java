package main.java.com.simplexanddualsolver.solution;

public class Solucion {

    private double[] valoresVariables;
    private double valorZ;
    private String estado;           // "OPTIMA", "INFACTIBLE", "NO_ACOTADA", "NO_CONVERGENTE"
    private double[][] tablaFinal;
    private int[] variablesBasicas;
    private int numIteraciones;

    // Constructor vacío (lo usa DualSimplexSolver)
    public Solucion() {
    }

    // Constructor completo opcional (por si quieres crearla de una vez)
    public Solucion(double[] valoresVariables,
                    double valorZ,
                    String estado,
                    double[][] tablaFinal) {
        this.valoresVariables = valoresVariables;
        this.valorZ = valorZ;
        this.estado = estado;
        this.tablaFinal = tablaFinal;
    }

    // Getters
    public double[] getValoresVariables() { return valoresVariables; }
    public double getValorZ() { return valorZ; }
    public String getEstado() { return estado; }
    public double[][] getTablaFinal() { return tablaFinal; }
    public int[] getVariablesBasicas() { return variablesBasicas; }
    public int getNumIteraciones() { return numIteraciones; }

    public boolean esOptima()      { return "OPTIMA".equalsIgnoreCase(estado); }
    public boolean esNoAcotada()   { return "NO_ACOTADA".equalsIgnoreCase(estado); }
    public boolean esInfactible()  { return "INFACTIBLE".equalsIgnoreCase(estado); }

    // Setters (necesarios para DualSimplexSolver)
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
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
}
