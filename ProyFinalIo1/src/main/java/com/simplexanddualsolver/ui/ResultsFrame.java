package main.java.com.simplexanddualsolver.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import main.java.com.simplexanddualsolver.solution.Solucion;

public class ResultsFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel lblEstado;
    private JLabel lblValorZ;
    private JLabel lblIteraciones;
    private JLabel lblMetodo;  // NUEVO: mostrar qué método se usó
    private JTable tablaVariables;
    private JTable tablaVariablesBasicas;
    private JButton btnVerProceso;  // NUEVO: botón para ver proceso
    private JButton btnSensibilidadRecursos;
    private JButton btnSensibilidadFO;
    private JButton btnSensibilidadTecnologica;
    private JButton btnVolver;

    // referencia a la solución que viene del back
    private Solucion solucion;

    public ResultsFrame() {
        setTitle("Resultados de la Solución - Programa Lineal");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 750);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = crearPanelSuperior();
        mainPanel.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = crearPanelCentral();
        mainPanel.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = crearPanelInferior();
        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // este método lo llamará MainApplication o ModelInputFrame
    public void setSolucion(Solucion solucion) {
        this.solucion = solucion;
        cargarDesdeSolucion();
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 100, 0)),
                "Información General de la Solución",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 13),
                new Color(0, 100, 0)
        ));
        panel.setLayout(new GridLayout(2, 2, 10, 10));

        // Panel Estado
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstado.add(new JLabel("Estado:"));
        lblEstado = new JLabel();
        lblEstado.setFont(new Font("Arial", Font.BOLD, 12));
        lblEstado.setForeground(Color.BLUE);
        panelEstado.add(lblEstado);

        // Panel Valor Z
        JPanel panelZ = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelZ.add(new JLabel("Valor Óptimo (Z):"));
        lblValorZ = new JLabel();
        lblValorZ.setFont(new Font("Arial", Font.BOLD, 12));
        lblValorZ.setForeground(new Color(0, 100, 0));
        panelZ.add(lblValorZ);

        // Panel Iteraciones
        JPanel panelIteraciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIteraciones.add(new JLabel("Iteraciones:"));
        lblIteraciones = new JLabel();
        lblIteraciones.setFont(new Font("Arial", Font.BOLD, 12));
        panelIteraciones.add(lblIteraciones);

        // NUEVO: Panel Método usado
        JPanel panelMetodo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMetodo.add(new JLabel("Método:"));
        lblMetodo = new JLabel();
        lblMetodo.setFont(new Font("Arial", Font.BOLD, 12));
        lblMetodo.setForeground(new Color(128, 0, 128));
        panelMetodo.add(lblMetodo);

        panel.add(panelEstado);
        panel.add(panelZ);
        panel.add(panelIteraciones);
        panel.add(panelMetodo);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2, 15, 0));

        JPanel panelVariables = new JPanel(new BorderLayout());
        panelVariables.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLUE),
                "Valores de las Variables de Decisión",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                Color.BLUE
        ));

        String[] columnasVariables = {"Variable", "Valor"};
        DefaultTableModel modelVariables = new DefaultTableModel(columnasVariables, 0);
        tablaVariables = new JTable(modelVariables);
        tablaVariables.setRowHeight(25);
        JScrollPane scrollVariables = new JScrollPane(tablaVariables);
        panelVariables.add(scrollVariables, BorderLayout.CENTER);

        JPanel panelBasicas = new JPanel(new BorderLayout());
        panelBasicas.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(128, 0, 128)),
                "Variables Básicas en la Solución Óptima",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(128, 0, 128)
        ));

        String[] columnasBasicas = {"Variable Básica", "Valor"};
        DefaultTableModel modelBasicas = new DefaultTableModel(columnasBasicas, 0);
        tablaVariablesBasicas = new JTable(modelBasicas);
        tablaVariablesBasicas.setRowHeight(25);
        JScrollPane scrollBasicas = new JScrollPane(tablaVariablesBasicas);
        panelBasicas.add(scrollBasicas, BorderLayout.CENTER);

        panel.add(panelVariables);
        panel.add(panelBasicas);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));

        // NUEVO: Panel para el botón "Ver Proceso"
        JPanel panelProceso = new JPanel();
        panelProceso.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 100, 180)),
                "Proceso de Resolución",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(0, 100, 180)
        ));
        panelProceso.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnVerProceso = new JButton("📊 Ver Proceso Paso a Paso");
        btnVerProceso.setFont(new Font("Arial", Font.BOLD, 12));
        btnVerProceso.setBackground(new Color(0, 100, 180));
        btnVerProceso.setForeground(Color.WHITE);
        btnVerProceso.setFocusPainted(false);
        btnVerProceso.setToolTipText("Ver cómo se resolvió el problema iteración por iteración");
        btnVerProceso.addActionListener(e -> abrirProcesoResolucion());

        JLabel lblInfoProceso = new JLabel("Visualiza cada iteración del método Simplex o Dual Simplex");
        lblInfoProceso.setFont(new Font("Arial", Font.ITALIC, 11));
        lblInfoProceso.setForeground(Color.GRAY);

        panelProceso.add(btnVerProceso);
        panelProceso.add(lblInfoProceso);

        // Panel de Sensibilidad (existente)
        JPanel panelSensibilidad = new JPanel();
        panelSensibilidad.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 140, 0)),
                "Análisis de Sensibilidad",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(255, 140, 0)
        ));
        panelSensibilidad.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnSensibilidadRecursos = new JButton("Sensibilidad de Recursos");
        setOptionalIcon(btnSensibilidadRecursos, "recursos.png", 24, 24);
        btnSensibilidadRecursos.setToolTipText("Analizar sensibilidad de los recursos (vector b)");

        btnSensibilidadFO = new JButton("Sensibilidad FO");
        setOptionalIcon(btnSensibilidadFO, "funcion_objetivo.png", 24, 24);
        btnSensibilidadFO.setToolTipText("Analizar sensibilidad de los coeficientes de la función objetivo");

        btnSensibilidadTecnologica = new JButton("Sensibilidad Tecnológica");
        setOptionalIcon(btnSensibilidadTecnologica, "tecnologica.png", 24, 24);
        btnSensibilidadTecnologica.setToolTipText("Analizar sensibilidad tecnológica de las variables");

        btnVolver = new JButton("Volver al Ingreso");
        setOptionalIcon(btnVolver, "volver.png", 24, 24);

        panelSensibilidad.add(btnSensibilidadRecursos);
        panelSensibilidad.add(btnSensibilidadFO);
        panelSensibilidad.add(btnSensibilidadTecnologica);
        panelSensibilidad.add(Box.createHorizontalStrut(50));
        panelSensibilidad.add(btnVolver);

        btnSensibilidadRecursos.addActionListener(e -> abrirSensibilidadRecursos());
        btnSensibilidadFO.addActionListener(e -> abrirSensibilidadFO());
        btnSensibilidadTecnologica.addActionListener(e -> abrirSensibilidadTecnologica());
        btnVolver.addActionListener(e -> volverAIngreso());

        // Agregar ambos paneles al contenedor
        panelContenedor.add(panelProceso);
        panelContenedor.add(panelSensibilidad);

        return panelContenedor;
    }

    private void setOptionalIcon(JButton button, String iconName, int width, int height) {
        try {
            ImageIcon icon = loadIcon(iconName, width, height);
            if (icon != null && icon.getImageLoadStatus() == java.awt.MediaTracker.COMPLETE) {
                button.setIcon(icon);
            }
        } catch (Exception e) {
            System.out.println("Icono no disponible: " + iconName);
        }
    }

    private ImageIcon loadIcon(String iconName, int width, int height) {
        try {
            String[] possiblePaths = {
                    "icons/" + iconName,
                    "../icons/" + iconName,
                    System.getProperty("user.dir") + "/icons/" + iconName
            };

            for (String path : possiblePaths) {
                java.io.File file = new java.io.File(path);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(path);
                    Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImage);
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // llena la interfaz con la solución real
    private void cargarDesdeSolucion() {
        if (solucion == null) {
            return;
        }

        lblEstado.setText(solucion.getEstado());
        lblValorZ.setText(String.valueOf(solucion.getValorZ()));
        lblIteraciones.setText(String.valueOf(solucion.getNumIteraciones()));

        // NUEVO: Mostrar método usado
        String metodo = solucion.getMetodoUsado();
        lblMetodo.setText(metodo != null ? metodo : "No especificado");

        DefaultTableModel modelVariables = (DefaultTableModel) tablaVariables.getModel();
        modelVariables.setRowCount(0);

        double[] valores = solucion.getValoresVariables();
        if (valores != null) {
            for (int i = 0; i < valores.length; i++) {
                modelVariables.addRow(new Object[]{"x" + (i + 1), valores[i]});
            }
        }

        DefaultTableModel modelBasicas = (DefaultTableModel) tablaVariablesBasicas.getModel();
        modelBasicas.setRowCount(0);

        int[] base = solucion.getVariablesBasicas();
        if (base != null && valores != null) {
            for (int idx : base) {
                if (idx >= 0 && idx < valores.length) {
                    modelBasicas.addRow(new Object[]{"x" + (idx + 1), valores[idx]});
                }
            }
        }

        // Desactivar botones de sensibilidad si la solución no es óptima
        boolean optima = solucion.esOptima();
        btnSensibilidadRecursos.setEnabled(optima);
        btnSensibilidadFO.setEnabled(optima);
        btnSensibilidadTecnologica.setEnabled(optima);

        // NUEVO: Habilitar botón de proceso solo si hay historial
        boolean tieneHistorial = solucion.getTotalIteracionesGuardadas() > 0;
        btnVerProceso.setEnabled(tieneHistorial);
    }

    // NUEVO: Abrir ventana de proceso de resolución
    private void abrirProcesoResolucion() {
        MainApplication.showProcesoResolucionFrame();
        this.setVisible(false);
    }

    private void abrirSensibilidadRecursos() {
        MainApplication.showSensibilidadRecursosFrame();
        this.setVisible(false);
    }

    private void abrirSensibilidadFO() {
        MainApplication.showSensibilidadFOFrame();
        this.setVisible(false);
    }

    private void abrirSensibilidadTecnologica() {
        MainApplication.showSensibilidadTecnologicaFrame();
        this.setVisible(false);
    }

    private void volverAIngreso() {
        MainApplication.volverAIngresoPrincipal();
        this.setVisible(false);
    }
}
