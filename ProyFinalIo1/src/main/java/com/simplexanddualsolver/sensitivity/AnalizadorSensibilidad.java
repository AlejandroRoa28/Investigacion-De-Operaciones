package main.java.com.simplexanddualsolver.sensitivity;

import main.java.com.simplexanddualsolver.model.FormaEstandar;
import main.java.com.simplexanddualsolver.model.ModeloProblema;
import main.java.com.simplexanddualsolver.solution.Solucion;

public class AnalizadorSensibilidad {

    private final SensibilidadRecursos sensRecursos;
    private final SensibilidadTecnologica sensTecnologica;
    private final SensibilidadFO sensFO;

    public AnalizadorSensibilidad(Solucion solucion,
                                  ModeloProblema modelo,
                                  FormaEstandar forma) {

        double[][] tablaFinal = solucion.getTablaFinal();
        int[] columnasHolgura = forma.getColumnasHolgura();
        double[] bOriginal = modelo.obtenerVectorB();
        int[] baseFinal = solucion.getVariablesBasicas();

        this.sensRecursos = new SensibilidadRecursos(tablaFinal, columnasHolgura, bOriginal);
        this.sensTecnologica = new SensibilidadTecnologica(tablaFinal, baseFinal);
        this.sensFO = new SensibilidadFO(tablaFinal);
    }

    public SensibilidadRecursos getSensibilidadRecursos() {
        return sensRecursos;
    }

    public SensibilidadTecnologica getSensibilidadTecnologica() {
        return sensTecnologica;
    }

    public SensibilidadFO getSensibilidadFO() {
        return sensFO;
    }
}

