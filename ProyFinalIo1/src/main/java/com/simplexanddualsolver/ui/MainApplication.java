package main.java.com.simplexanddualsolver.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import main.java.com.simplexanddualsolver.model.ModeloProblema;
import main.java.com.simplexanddualsolver.sensitivity.AnalizadorSensibilidad;
import main.java.com.simplexanddualsolver.sensitivity.SensibilidadFO;
import main.java.com.simplexanddualsolver.sensitivity.SensibilidadRecursos;
import main.java.com.simplexanddualsolver.sensitivity.SensibilidadTecnologica;
import main.java.com.simplexanddualsolver.solution.Solucion;

public class MainApplication {

    private static ModelInputFrame modelInputFrame;
    private static ResultsFrame resultsFrame;
    private static ProcesoResolucionFrame procesoResolucionFrame;  // NUEVO
    private static SensibilidadRecursosFrame sensibilidadRecursosFrame;
    private static SensibilidadFOFrame sensibilidadFOFrame;
    private static SensibilidadTecnologicaFrame sensibilidadTecnologicaFrame;

    // contexto actual del problema resuelto
    private static ModeloProblema modeloActual;
    private static Solucion solucionActual;
    private static AnalizadorSensibilidad analizadorSensibilidadActual;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(MainApplication::showModelInputFrame);
    }

    public static void showModelInputFrame() {
        if (modelInputFrame == null) {
            modelInputFrame = new ModelInputFrame();
            configurarVentanaPrincipal(modelInputFrame);
        }
        modelInputFrame.setVisible(true);
        cerrarVentanasSecundarias();
    }

    // Este método lo llamará ModelInputFrame al terminar de resolver
    // Guarda modelo, solución y analizador para que las ventanas de sensibilidad los usen.
    public static void notificarResultado(ModeloProblema modelo,
                                          Solucion solucion,
                                          AnalizadorSensibilidad analizador) {
        modeloActual = modelo;
        solucionActual = solucion;
        analizadorSensibilidadActual = analizador;
        showResultsFrame(solucion);
    }

    public static void showResultsFrame(Solucion solucion) {
        if (resultsFrame == null) {
            resultsFrame = new ResultsFrame();
            configurarVentanaSecundaria(resultsFrame);
        }
        solucionActual = solucion;
        resultsFrame.setSolucion(solucion);
        resultsFrame.setVisible(true);
        if (modelInputFrame != null) {
            modelInputFrame.setVisible(false);
        }
    }

    // NUEVO: Mostrar ventana de proceso de resolución
    public static void showProcesoResolucionFrame() {
        if (procesoResolucionFrame == null) {
            procesoResolucionFrame = new ProcesoResolucionFrame();
            configurarVentanaSecundaria(procesoResolucionFrame);
        }

        if (solucionActual != null) {
            procesoResolucionFrame.setSolucion(solucionActual);
        }

        procesoResolucionFrame.setVisible(true);
        if (resultsFrame != null) {
            resultsFrame.setVisible(false);
        }
    }

    public static void showSensibilidadRecursosFrame() {
        if (sensibilidadRecursosFrame == null) {
            sensibilidadRecursosFrame = new SensibilidadRecursosFrame();
            configurarVentanaSecundaria(sensibilidadRecursosFrame);
        }

        if (analizadorSensibilidadActual != null && modeloActual != null) {
            SensibilidadRecursos sRec =
                    analizadorSensibilidadActual.getSensibilidadRecursos();
            if (sRec != null) {
                sensibilidadRecursosFrame.setDatos(sRec, modeloActual);
            }
        }

        sensibilidadRecursosFrame.setVisible(true);
        if (resultsFrame != null) {
            resultsFrame.setVisible(false);
        }
    }

    public static void showSensibilidadFOFrame() {
        if (sensibilidadFOFrame == null) {
            sensibilidadFOFrame = new SensibilidadFOFrame();
            configurarVentanaSecundaria(sensibilidadFOFrame);
        }

        if (analizadorSensibilidadActual != null
                && modeloActual != null
                && solucionActual != null) {
            SensibilidadFO sFO =
                    analizadorSensibilidadActual.getSensibilidadFO();
            if (sFO != null) {
                sensibilidadFOFrame.setDatos(sFO, modeloActual, solucionActual);
            }
        }

        sensibilidadFOFrame.setVisible(true);
        if (resultsFrame != null) {
            resultsFrame.setVisible(false);
        }
    }

    public static void showSensibilidadTecnologicaFrame() {
        if (sensibilidadTecnologicaFrame == null) {
            sensibilidadTecnologicaFrame = new SensibilidadTecnologicaFrame();
            configurarVentanaSecundaria(sensibilidadTecnologicaFrame);
        }

        if (analizadorSensibilidadActual != null && solucionActual != null) {
            SensibilidadTecnologica sTec =
                    analizadorSensibilidadActual.getSensibilidadTecnologica();
            if (sTec != null) {
                sensibilidadTecnologicaFrame.setDatos(sTec, solucionActual);
            }
        }

        sensibilidadTecnologicaFrame.setVisible(true);
        if (resultsFrame != null) {
            resultsFrame.setVisible(false);
        }
    }

    private static void configurarVentanaPrincipal(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(
                        frame,
                        "¿Está seguro que desea salir de la aplicación?",
                        "Confirmar Salida",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (respuesta == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });
    }

    private static void configurarVentanaSecundaria(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                volverAVentanaAnterior(frame);
            }
        });
    }

    private static void volverAVentanaAnterior(JFrame ventanaActual) {
        // ACTUALIZADO: incluir procesoResolucionFrame
        if (ventanaActual == sensibilidadRecursosFrame
                || ventanaActual == sensibilidadFOFrame
                || ventanaActual == sensibilidadTecnologicaFrame
                || ventanaActual == procesoResolucionFrame) {

            if (resultsFrame != null) {
                resultsFrame.setVisible(true);
            }
        } else if (ventanaActual == resultsFrame) {
            if (modelInputFrame != null) {
                modelInputFrame.setVisible(true);
            }
        }
    }

    private static void cerrarVentanasSecundarias() {
        if (resultsFrame != null) resultsFrame.setVisible(false);
        if (procesoResolucionFrame != null) procesoResolucionFrame.setVisible(false);  // NUEVO
        if (sensibilidadRecursosFrame != null) sensibilidadRecursosFrame.setVisible(false);
        if (sensibilidadFOFrame != null) sensibilidadFOFrame.setVisible(false);
        if (sensibilidadTecnologicaFrame != null) sensibilidadTecnologicaFrame.setVisible(false);
    }

    public static void cerrarTodaLaAplicacion() {
        int confirm = JOptionPane.showConfirmDialog(
                null,
                "¿Está seguro que desea salir de la aplicación?",
                "Confirmar Salida",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            if (modelInputFrame != null) modelInputFrame.dispose();
            if (resultsFrame != null) resultsFrame.dispose();
            if (procesoResolucionFrame != null) procesoResolucionFrame.dispose();  // NUEVO
            if (sensibilidadRecursosFrame != null) sensibilidadRecursosFrame.dispose();
            if (sensibilidadFOFrame != null) sensibilidadFOFrame.dispose();
            if (sensibilidadTecnologicaFrame != null) sensibilidadTecnologicaFrame.dispose();
            System.exit(0);
        }
    }

    public static void volverAResultados() {
        if (resultsFrame != null) {
            resultsFrame.setVisible(true);
        }
        // Ocultar otras ventanas secundarias
        if (procesoResolucionFrame != null) procesoResolucionFrame.setVisible(false);
        if (sensibilidadRecursosFrame != null) sensibilidadRecursosFrame.setVisible(false);
        if (sensibilidadFOFrame != null) sensibilidadFOFrame.setVisible(false);
        if (sensibilidadTecnologicaFrame != null) sensibilidadTecnologicaFrame.setVisible(false);
    }

    public static void volverAIngresoPrincipal() {
        if (modelInputFrame != null) {
            modelInputFrame.setVisible(true);
        }
    }

    // getters opcionales si los necesitas en otros lados
    public static ModeloProblema getModeloActual() {
        return modeloActual;
    }

    public static Solucion getSolucionActual() {
        return solucionActual;
    }

    public static AnalizadorSensibilidad getAnalizadorSensibilidadActual() {
        return analizadorSensibilidadActual;
    }
}
