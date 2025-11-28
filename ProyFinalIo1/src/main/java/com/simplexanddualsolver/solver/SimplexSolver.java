package main.java.com.simplexanddualsolver.solver;
import main.java.com.simplexanddualsolver.model.FormaEstandar;
import main.java.com.simplexanddualsolver.solution.Solucion;

public class SimplexSolver implements MotorSolver {

    private double[][] tabla;
    private int[] base;
    private int numVariablesOriginales;
    private int numRestricciones;

    @Override
    public void resolver(FormaEstandar forma) {
        // 1. Copiar la tabla y base inicial
        this.tabla = copiarTabla(forma.obtenerTablaInicial());
        this.base = forma.obtenerBaseInicial().clone();
        this.numVariablesOriginales = forma.getNumVariablesOriginales();
        this.numRestricciones = forma.getNumRestricciones();

        // 2. Iterar hasta óptimo
        while (!esOptima()) {
            int colEntrante = seleccionarColumnaEntrante();
            
            // Verificar si es no acotado
            if (colEntrante == -1) {
                break; // problema no acotado o ya óptimo
            }
            
            int filaSaliente = seleccionarFilaSaliente(colEntrante);
            
            if (filaSaliente == -1) {
                // Problema no acotado
                break;
            }
            
            realizarPivoteo(filaSaliente, colEntrante);
            
            // Actualizar base
            base[filaSaliente] = colEntrante;
        }
    }

    @Override
    public Solucion obtenerSolucion() {
        double[] valoresVariables = new double[numVariablesOriginales];
        int colB = tabla[0].length - 1;
            for (int i = 0; i < numRestricciones; i++) {
            int varBasica = base[i];
                if (varBasica < numVariablesOriginales) {
                valoresVariables[varBasica] = tabla[ i][colB];
                }   
            }   

            int filaZ = tabla.length - 1;
            double valorZ = tabla[filaZ][colB];

            Solucion sol = new Solucion();
            sol.setValoresVariables(valoresVariables);
            sol.setValorZ(valorZ);
            sol.setEstado("OPTIMA");
            sol.setTablaFinal(copiarTabla(tabla));
            sol.setVariablesBasicas(base.clone());
            sol.setNumIteraciones(0); // si luego cuentas iteraciones, ajustas

            return sol;
    }

    private boolean esOptima() {
        int filaZ = tabla.length - 1; // última fila es Z
        int numCols = tabla[0].length - 1; // sin contar columna b
        
        // Para MAX: óptimo si todos los coeficientes en fila Z son >= 0
        for (int j = 0; j < numCols; j++) {
            if (tabla[filaZ][j] < -1e-10) { // tolerancia numérica
                return false;
            }
        }
        return true;
    }

    private int seleccionarColumnaEntrante() {
        int filaZ = tabla.length - 1;
        int numCols = tabla[0].length - 1; // sin columna b
        
        int colEntrante = -1;
        double valorMin = 0;
        
        // Buscar el coeficiente más negativo en fila Z (para MAX)
        for (int j = 0; j < numCols; j++) {
            if (tabla[filaZ][j] < valorMin) {
                valorMin = tabla[filaZ][j];
                colEntrante = j;
            }
        }
        
        return colEntrante; // -1 si no hay negativo (ya es óptimo)
    }

    private int seleccionarFilaSaliente(int colEntrante) {
        int colB = tabla[0].length - 1; // columna de b
        int filaSaliente = -1;
        double minRatio = Double.MAX_VALUE;
        
        // Prueba de la razón mínima
        for (int i = 0; i < numRestricciones; i++) {
            double coef = tabla[i][colEntrante];
            
            if (coef > 1e-10) { // solo consideramos coeficientes positivos
                double ratio = tabla[i][colB] / coef;
                
                if (ratio < minRatio) {
                    minRatio = ratio;
                    filaSaliente = i;
                }
            }
        }
        
        return filaSaliente; // -1 si no hay coeficiente positivo (no acotado)
    }

    private void realizarPivoteo(int filaPivote, int colPivote) {
        double pivote = tabla[filaPivote][colPivote];
        
        // 1. Dividir fila pivote por el elemento pivote
        for (int j = 0; j < tabla[0].length; j++) {
            tabla[filaPivote][j] /= pivote;
        }
        
        // 2. Hacer ceros en el resto de la columna pivote
        for (int i = 0; i < tabla.length; i++) {
            if (i != filaPivote) {
                double factor = tabla[i][colPivote];
                for (int j = 0; j < tabla[0].length; j++) {
                    tabla[i][j] -= factor * tabla[filaPivote][j];
                }

            }
        }
    }

    private double[][] copiarTabla(double[][] original) {
        double[][] copia = new double[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copia[i], 0, original[i].length);
        }
        return copia;
    }
}
