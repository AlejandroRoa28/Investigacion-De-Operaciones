package main.java.com.simplexanddualsolver.sensitivity;

import main.java.com.simplexanddualsolver.solution.Solucion;
import main.java.com.simplexanddualsolver.model.ModeloProblema;
import main.java.com.simplexanddualsolver.model.FormaEstandar;

public class AnalizadorSensibilidad {

    private SensibilidadRecursos sensRecursos;
    private SensibilidadTecnologica sensTecnologica;

    public AnalizadorSensibilidad(Solucion solucion, 
                                   ModeloProblema modelo,
                                   FormaEstandar forma) {
        
        double[][] tablaFinal = solucion.getTablaFinal();
        int[] columnasHolgura = forma.getColumnasHolgura();
        double[] bOriginal = modelo.obtenerVectorB();
        int[] baseFinal = null; // tu compañero debe agregar esto a Solucion
        
        this.sensRecursos = new SensibilidadRecursos(tablaFinal, columnasHolgura, bOriginal);
        this.sensTecnologica = new SensibilidadTecnologica(tablaFinal, baseFinal);
    }

    public SensibilidadRecursos getSensibilidadRecursos() {
        return sensRecursos;
    }

    public SensibilidadTecnologica getSensibilidadTecnologica() {
        return sensTecnologica;
    }
}

