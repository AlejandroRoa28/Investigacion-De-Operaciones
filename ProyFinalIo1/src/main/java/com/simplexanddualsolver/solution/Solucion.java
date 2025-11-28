package main.java.com.simplexanddualsolver.solution;
public class Solucion {

    private double[] valoresVariables;
    private double valorZ;
    private String estado; // "OPTIMA", "INFACTIBLE", "NO_ACOTADA"
    private double[][] tablaFinal;

    public Solucion(double[] valoresVariables,
                    double valorZ,
                    String estado,
                    double[][] tablaFinal) {
        this.valoresVariables = valoresVariables;
        this.valorZ = valorZ;
        this.estado = estado;
        this.tablaFinal = tablaFinal;
    }

    public double[] getValoresVariables() { return valoresVariables; }
    public double getValorZ() { return valorZ; }
    public String getEstado() { return estado; }
    public double[][] getTablaFinal() { return tablaFinal; }

    public boolean esOptima()     { return "OPTIMA".equalsIgnoreCase(estado); }
    public boolean esNoAcotada()  { return "NO_ACOTADA".equalsIgnoreCase(estado); }
    public boolean esInfactible() { return "INFACTIBLE".equalsIgnoreCase(estado); }

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

    public int[] getVariablesBasicas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVariablesBasicas'");
    }
}

