package main.java.com.simplexanddualsolver.solver;

import main.java.com.simplexanddualsolver.model.FormaEstandar;
import main.java.com.simplexanddualsolver.solution.Solucion;

public interface MotorSolver {
    void resolver(FormaEstandar forma);
    Solucion obtenerSolucion();
}
