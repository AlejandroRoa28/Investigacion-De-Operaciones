package main.java.com.simplexanddualsolver.solution;

public class Solucion {

    private double[] valoresVariables;
    private double valorObjetivo;
    private String estado; // "OPTIMA", "INFACTIBLE", "NO_ACOTADA"

    public Solucion(double[] valoresVariables,
                    double valorObjetivo,
                    String estado) {
        this.valoresVariables = valoresVariables;
        this.valorObjetivo = valorObjetivo;
        this.estado = estado;
    }

    public double[] getValoresVariables() { return valoresVariables; }
    public double getValorObjetivo() { return valorObjetivo; }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Estado: ").append(estado).append("\n");
        sb.append("Valor óptimo: ").append(valorObjetivo).append("\n");
        if (valoresVariables != null) {
            for (int i = 0; i < valoresVariables.length; i++) {
                sb.append("x").append(i + 1).append(" = ")
                  .append(valoresVariables[i]).append("\n");
            }
        }
        return sb.toString();
    }
}
