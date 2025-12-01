package main.java.com.simplexanddualsolver.ui;

import main.java.com.simplexanddualsolver.sensitivity.SensibilidadFO;
import main.java.com.simplexanddualsolver.model.ModeloProblema;
import main.java.com.simplexanddualsolver.solution.Solucion;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class SensibilidadFOFrame extends JFrame {

    private JTable tablaCostosReducidos;
    private JTable tablaIntervalos;
    private JButton btnAnalizarCambios;
    private JButton btnVolver;
    private JLabel lblEstadoOptimalidad;

    // referencias del back
    private SensibilidadFO sensibilidadFO;
    private ModeloProblema modelo;
    private Solucion solucion;

    public SensibilidadFOFrame() {
        setTitle("Análisis de Sensibilidad - Función Objetivo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = crearPanelCostosReducidos();
        mainPanel.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = crearPanelIntervalos();
        mainPanel.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = crearPanelSimulacion();
        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Este setter lo llamará MainApplication cuando cree la ventana
    public void setDatos(SensibilidadFO sensibilidadFO, ModeloProblema modelo, Solucion solucion) {
        this.sensibilidadFO = sensibilidadFO;
        this.modelo = modelo;
        this.solucion = solucion;
        cargarDesdeBackend();
    }

    private JPanel crearPanelCostosReducidos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180)),
                "Costos Reducidos y Estado de las Variables",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 13),
                new Color(70, 130, 180)
        ));

        String[] columnas = {
                "Variable",
                "Tipo",
                "Valor Actual",
                "Coeficiente Original (c)",
                "Costo Reducido",
                "Estado en Solución"
        };

        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCostosReducidos = new JTable(model);
        tablaCostosReducidos.setRowHeight(30);
        tablaCostosReducidos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        TableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                try {
                    if (column == 4 && value != null) {
                        double costoReducido = Double.parseDouble(value.toString());
                        if (costoReducido < 0) {
                            label.setForeground(Color.RED);
                            label.setFont(label.getFont().deriveFont(Font.BOLD));
                        } else if (costoReducido > 0) {
                            label.setForeground(new Color(0, 128, 0));
                            label.setFont(label.getFont().deriveFont(Font.BOLD));
                        }
                    } else if (column == 5) {
                        if ("No Básica".equals(value)) {
                            label.setForeground(Color.BLUE);
                        } else if ("Básica".equals(value)) {
                            label.setForeground(new Color(0, 100, 0));
                        }
                    }
                } catch (NumberFormatException e) {
                    // dejar formato por defecto
                }

                return label;
            }
        };

        for (int i = 0; i < tablaCostosReducidos.getColumnCount(); i++) {
            tablaCostosReducidos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(tablaCostosReducidos);
        scrollPane.setPreferredSize(new Dimension(1000, 180));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelIntervalos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165, 42, 42)),
                "Intervalos de Variación para Coeficientes de la Función Objetivo",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 13),
                new Color(165, 42, 42)
        ));

        String[] columnas = {
                "Variable",
                "Tipo",
                "Coeficiente Actual (c)",
                "Límite Inferior (cMin)",
                "Límite Superior (cMax)",
                "Rango Permitido",
                "Base Mantenida"
        };

        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaIntervalos = new JTable(model);
        tablaIntervalos.setRowHeight(30);

        TableCellRenderer rendererIntervalos = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 6) {
                    if ("SÍ".equals(value)) {
                        label.setForeground(new Color(0, 128, 0));
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                    } else if ("NO".equals(value)) {
                        label.setForeground(Color.RED);
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                    }
                }

                return label;
            }
        };

        for (int i = 0; i < tablaIntervalos.getColumnCount(); i++) {
            tablaIntervalos.getColumnModel().getColumn(i).setCellRenderer(rendererIntervalos);
        }

        JScrollPane scrollPane = new JScrollPane(tablaIntervalos);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelSimulacion() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelSimulacion = new JPanel(new BorderLayout());
        panelSimulacion.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 140, 0)),
                "Simulación de Cambios en Coeficientes de la FO",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(255, 140, 0)
        ));

        JTextArea instrucciones = new JTextArea(
                "• Use los datos de la tabla para analizar cambios en los coeficientes\n" +
                "• Para variables básicas: cambios afectan toda la solución\n" +
                "• Para variables no básicas: cambios pueden hacerlas rentables"
        );
        instrucciones.setEditable(false);
        instrucciones.setBackground(new Color(240, 240, 240));
        instrucciones.setFont(new Font("Arial", Font.PLAIN, 12));
        instrucciones.setPreferredSize(new Dimension(900, 60));

        panelSimulacion.add(instrucciones, BorderLayout.NORTH);

        JPanel panelControl = new JPanel(new BorderLayout());

        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstado.setBorder(BorderFactory.createTitledBorder("Estado de Optimalidad"));

        JLabel lblTexto = new JLabel("Solución actual: ");
        lblEstadoOptimalidad = new JLabel("DESCONOCIDA");
        lblEstadoOptimalidad.setFont(new Font("Arial", Font.BOLD, 14));
        lblEstadoOptimalidad.setForeground(Color.GRAY);

        panelEstado.add(lblTexto);
        panelEstado.add(lblEstadoOptimalidad);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

        btnAnalizarCambios = new JButton("Analizar Cambios");
        setOptionalIcon(btnAnalizarCambios, "analizar.png", 20, 20);
        btnAnalizarCambios.setToolTipText("Analizar el impacto de los cambios en la optimalidad");

        btnVolver = new JButton("Volver a Resultados");
        setOptionalIcon(btnVolver, "volver.png", 20, 20);

        panelBotones.add(btnAnalizarCambios);
        panelBotones.add(btnVolver);

        panelControl.add(panelEstado, BorderLayout.CENTER);
        panelControl.add(panelBotones, BorderLayout.SOUTH);

        panel.add(panelSimulacion, BorderLayout.CENTER);
        panel.add(panelControl, BorderLayout.SOUTH);

        btnAnalizarCambios.addActionListener(e -> analizarCambios());
        btnVolver.addActionListener(e -> volverAResultados());

        return panel;
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

    // Cargar datos reales desde SensibilidadFO + ModeloProblema + Solucion
    private void cargarDesdeBackend() {
        if (sensibilidadFO == null || modelo == null || solucion == null) {
            return;
        }

        double[] cOriginal = modelo.obtenerVectorC();
        double[] costosReducidos = sensibilidadFO.getCostosReducidos();
        double[] valores = solucion.getValoresVariables();
        int[] base = solucion.getVariablesBasicas();

        DefaultTableModel modelCostos = (DefaultTableModel) tablaCostosReducidos.getModel();
        modelCostos.setRowCount(0);

        int n = cOriginal.length;
        for (int j = 0; j < n; j++) {
            String varName = "x" + (j + 1);
            double valorActual = (valores != null && j < valores.length) ? valores[j] : 0.0;
            double cj = cOriginal[j];
            double cr = (costosReducidos != null && j < costosReducidos.length) ? costosReducidos[j] : 0.0;

            String tipo = (valorActual != 0.0) ? "Básica" : "No Básica";
            String estado = tipo;

            modelCostos.addRow(new Object[]{varName, tipo, valorActual, cj, cr, estado});
        }

        DefaultTableModel modelIntervalos = (DefaultTableModel) tablaIntervalos.getModel();
        modelIntervalos.setRowCount(0);

        for (int j = 0; j < n; j++) {
            double cj = cOriginal[j];
            double[] intervalo = sensibilidadFO.calcularIntervaloObjetivoNoBasica(j, cj);
            double cMin = intervalo[0];
            double cMax = intervalo[1];

            String varName = "x" + (j + 1);
            String tipo = (valores != null && j < valores.length && valores[j] != 0.0) ? "Básica" : "No Básica";

            String rango;
            String baseMantenida = "SÍ";
            if (Double.isInfinite(cMin) && Double.isInfinite(cMax)) {
                rango = "(-∞, +∞)";
            } else if (Double.isInfinite(cMin)) {
                rango = "(-∞, " + cMax + "]";
            } else if (Double.isInfinite(cMax)) {
                rango = "[" + cMin + ", +∞)";
            } else {
                rango = "[" + cMin + ", " + cMax + "]";
            }

            modelIntervalos.addRow(new Object[]{varName, tipo, cj, cMin, cMax, rango, baseMantenida});
        }

        lblEstadoOptimalidad.setText(solucion.esOptima() ? "ÓPTIMA" : solucion.getEstado());
        lblEstadoOptimalidad.setForeground(solucion.esOptima() ? new Color(0, 128, 0) : Color.RED);
    }

    private void analizarCambios() {
        JOptionPane.showMessageDialog(this,
                "Use los intervalos mostrados para decidir si los cambios en c_j\n" +
                "mantienen la base óptima actual.",
                "Análisis de Cambios",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void volverAResultados() {
        MainApplication.volverAResultados();
        this.setVisible(false);
    }
}
