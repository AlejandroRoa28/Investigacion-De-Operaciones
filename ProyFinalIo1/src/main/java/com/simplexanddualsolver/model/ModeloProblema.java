package main.java.com.simplexanddualsolver.model;

public class ModeloProblema {

    private double[] coefObjetivo;        // c
    private double[][] coefRestricciones; // A
    private double[] ladosDerechos;       // b
    private String[] tiposRestriccion;    // "<=", ">=", "="
    private boolean esMaximizacion;

    public ModeloProblema(double[] coefObjetivo,
                          double[][] coefRestricciones,
                          double[] ladosDerechos,
                          String[] tiposRestriccion,
                          boolean esMaximizacion) {
        this.coefObjetivo = coefObjetivo;
        this.coefRestricciones = coefRestricciones;
        this.ladosDerechos = ladosDerechos;
        this.tiposRestriccion = tiposRestriccion;
        this.esMaximizacion = esMaximizacion;
    }

    public int getNumVariables() {
        return coefObjetivo.length;
    }

    public int getNumRestricciones() {
        return ladosDerechos.length;
    }

    // Getters y setters
    public double[] getCoefObjetivo() { return coefObjetivo; }
    public double[][] getCoefRestricciones() { return coefRestricciones; }
    public double[] getLadosDerechos() { return ladosDerechos; }
    public String[] getTiposRestriccion() { return tiposRestriccion; }
    public boolean isEsMaximizacion() { return esMaximizacion; }
}
