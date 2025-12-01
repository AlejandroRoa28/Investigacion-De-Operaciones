package main.java.com.simplexanddualsolver.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import main.java.com.simplexanddualsolver.sensitivity.SensibilidadTecnologica;
import main.java.com.simplexanddualsolver.solution.Solucion;

public class SensibilidadTecnologicaFrame extends JFrame {

    private JTable tablaSensibilidadTec;
    private JTable tablaRentabilidad;
    private JButton btnAnalizarRentabilidad;
    private JButton btnVolver;
    private JLabel lblResumenRentabilidad;
    private JTextArea areaAnalisis;

    // Referencias backend
    private SensibilidadTecnologica sensibilidadTecnologica;
    private Solucion solucion;

    public SensibilidadTecnologicaFrame() {
        setTitle("Análisis de Sensibilidad Tecnológica");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = crearPanelSensibilidadTecnologica();
        mainPanel.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = crearPanelRentabilidad();
        mainPanel.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = crearPanelInferior();
        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Este método lo llamará MainApplication para llenar los datos
    public void setDatos(SensibilidadTecnologica sensibilidadTecnologica, Solucion solucion) {
        this.sensibilidadTecnologica = sensibilidadTecnologica;
        this.solucion = solucion;
        cargarDesdeBackend();
    }

    private JPanel crearPanelSensibilidadTecnologica() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(75, 0, 130)),
                "Sensibilidad Tecnológica - Costos Reducidos y Variables",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 13),
                new Color(75, 0, 130)
        ));

        String[] columnas = {
                "Variable j",
                "Descripción",
                "Tipo",
                "Valor Actual",
                "Costo Reducido",
                "Estado",
                "Sensibilidad"
        };

        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaSensibilidadTec = new JTable(model);
        tablaSensibilidadTec.setRowHeight(30);
        tablaSensibilidadTec.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

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
                    } else if (column == 6) {
                        String sensibilidad = value.toString();
                        if ("ALTA".equals(sensibilidad)) {
                            label.setForeground(Color.RED);
                            label.setFont(label.getFont().deriveFont(Font.BOLD));
                        } else if ("MEDIA".equals(sensibilidad)) {
                            label.setForeground(Color.ORANGE);
                            label.setFont(label.getFont().deriveFont(Font.BOLD));
                        } else if ("BAJA".equals(sensibilidad)) {
                            label.setForeground(new Color(0, 128, 0));
                        }
                    } else if (column == 2) {
                        if ("No Básica".equals(value)) {
                            label.setForeground(Color.BLUE);
                            label.setFont(label.getFont().deriveFont(Font.BOLD));
                        }
                    }
                } catch (NumberFormatException e) {
                    // por defecto
                }

                return label;
            }
        };

        for (int i = 0; i < tablaSensibilidadTec.getColumnCount(); i++) {
            tablaSensibilidadTec.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(tablaSensibilidadTec);
        scrollPane.setPreferredSize(new Dimension(1100, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelRentabilidad() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(139, 0, 139)),
                "Análisis de Rentabilidad - Variables No Básicas",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 13),
                new Color(139, 0, 139)
        ));

        JPanel panelTabla = new JPanel(new BorderLayout());

        String[] columnasRentabilidad = {
                "Variable No Básica",
                "Costo Reducido",
                "Umbral de Rentabilidad",
                "Recomendación",
                "Potencial de Mejora",
                "¿Rentable Introducir?"
        };

        DefaultTableModel modelRentabilidad = new DefaultTableModel(columnasRentabilidad, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRentabilidad = new JTable(modelRentabilidad);
        tablaRentabilidad.setRowHeight(30);

        TableCellRenderer rendererRentabilidad = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (column == 5) {
                    if ("SÍ".equals(value)) {
                        label.setForeground(new Color(0, 128, 0));
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                        label.setBackground(new Color(220, 255, 220));
                    } else if ("NO".equals(value)) {
                        label.setForeground(Color.RED);
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                        label.setBackground(new Color(255, 220, 220));
                    }
                } else if (column == 3) {
                    if ("INTRODUCIR".equals(value)) {
                        label.setForeground(new Color(0, 100, 0));
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                    } else if ("MANTENER".equals(value)) {
                        label.setForeground(Color.BLUE);
                    } else if ("EVITAR".equals(value)) {
                        label.setForeground(Color.RED);
                    }
                } else if (column == 4) {
                    if ("ALTO".equals(value)) {
                        label.setForeground(new Color(0, 128, 0));
                        label.setFont(label.getFont().deriveFont(Font.BOLD));
                    } else if ("MEDIO".equals(value)) {
                        label.setForeground(Color.ORANGE);
                    } else if ("BAJO".equals(value)) {
                        label.setForeground(Color.GRAY);
                    }
                }

                label.setOpaque(true);
                return label;
            }
        };

        for (int i = 0; i < tablaRentabilidad.getColumnCount(); i++) {
            tablaRentabilidad.getColumnModel().getColumn(i).setCellRenderer(rendererRentabilidad);
        }

        JScrollPane scrollRentabilidad = new JScrollPane(tablaRentabilidad);
        scrollRentabilidad.setPreferredSize(new Dimension(1000, 150));
        panelTabla.add(scrollRentabilidad, BorderLayout.CENTER);

        JPanel panelAnalisis = new JPanel(new BorderLayout());
        panelAnalisis.setBorder(BorderFactory.createTitledBorder("Análisis Detallado"));

        areaAnalisis = new JTextArea();
        areaAnalisis.setEditable(false);
        areaAnalisis.setLineWrap(true);
        areaAnalisis.setWrapStyleWord(true);
        areaAnalisis.setFont(new Font("Arial", Font.PLAIN, 12));
        areaAnalisis.setBackground(new Color(240, 240, 240));
        areaAnalisis.setText("Seleccione una variable de la tabla para ver un análisis detallado...");

        JScrollPane scrollAnalisis = new JScrollPane(areaAnalisis);
        scrollAnalisis.setPreferredSize(new Dimension(1000, 100));
        panelAnalisis.add(scrollAnalisis, BorderLayout.CENTER);

        tablaRentabilidad.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarAnalisisDetallado();
            }
        });

        panel.add(panelTabla, BorderLayout.NORTH);
        panel.add(panelAnalisis, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelResumen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelResumen.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(47, 79, 79)),
                "Resumen de Rentabilidad",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(47, 79, 79)
        ));

        lblResumenRentabilidad = new JLabel();
        lblResumenRentabilidad.setFont(new Font("Arial", Font.BOLD, 12));
        actualizarResumen();

        panelResumen.add(lblResumenRentabilidad);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

        btnAnalizarRentabilidad = new JButton("Analizar Rentabilidad Completa");
        setOptionalIcon(btnAnalizarRentabilidad, "rentabilidad.png", 20, 20);
        btnAnalizarRentabilidad.setToolTipText("Realizar análisis completo de rentabilidad de todas las variables");

        btnVolver = new JButton("Volver a Resultados");
        setOptionalIcon(btnVolver, "volver.png", 20, 20);

        panelBotones.add(btnAnalizarRentabilidad);
        panelBotones.add(btnVolver);

        panel.add(panelResumen, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        btnAnalizarRentabilidad.addActionListener(e -> analizarRentabilidadCompleta());
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
            // ignorar
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

    // Método central para cargar desde el backend
    private void cargarDesdeBackend() {
        if (sensibilidadTecnologica == null || solucion == null) return;

        DefaultTableModel modelTec = (DefaultTableModel) tablaSensibilidadTec.getModel();
        modelTec.setRowCount(0);

        double[] valores = solucion.getValoresVariables();
        int[] base = solucion.getVariablesBasicas();
        double[] costosReducidos = sensibilidadTecnologica.getCostosReducidos();

        int n = valores.length;
        for (int j = 0; j < n; j++) {
            String varName = "x" + (j + 1);
            String tipo = esVariableBasica(base, j) ? "Básica" : "No Básica";
            double valorActual = valores[j];
            double cr = costosReducidos[j];
            String estado = tipo;
            String sensibilidad = calcularNivelSensibilidad(cr);

            String descripcion = varName; // Si tienes descripciones en tu modelo, puedes mapearlas aquí

            modelTec.addRow(new Object[]{varName, descripcion, tipo, valorActual, cr, estado, sensibilidad});
        }

        // En la tabla de rentabilidad, muestra solo no básicas
        DefaultTableModel modelRent = (DefaultTableModel) tablaRentabilidad.getModel();
        modelRent.setRowCount(0);
        for (int j = 0; j < n; j++) {
            if (!esVariableBasica(base, j)) {
                double cr = costosReducidos[j];
                boolean rentable = cr < 0;
                String recomendacion = rentable ? "INTRODUCIR" : "EVITAR";
                String potencial = rentable ? "ALTO" : "BAJO";
                String umbral = rentable ? "cr < 0" : "cr >= 0";
                String rentableStr = rentable ? "SÍ" : "NO";
                modelRent.addRow(new Object[]{
                        "x" + (j + 1), cr, umbral, recomendacion, potencial, rentableStr
                });
            }
        }
        actualizarResumen();
    }

    private boolean esVariableBasica(int[] base, int col) {
        if (base == null) return false;
        for (int idx : base) if (col == idx) return true;
        return false;
    }

    private String calcularNivelSensibilidad(double cr) {
        if (Math.abs(cr) > 2.0) return "ALTA";
        if (Math.abs(cr) > 1.0) return "MEDIA";
        return "BAJA";
    }

    private void actualizarAnalisisDetallado() {
        int selectedRow = tablaRentabilidad.getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) tablaRentabilidad.getModel();
            String variable = model.getValueAt(selectedRow, 0).toString();
            String costoReducido = model.getValueAt(selectedRow, 1).toString();
            String recomendacion = model.getValueAt(selectedRow, 3).toString();
            String rentable = model.getValueAt(selectedRow, 5).toString();
            String analisis = generarAnalisisDetallado(variable, costoReducido, recomendacion, rentable);
            areaAnalisis.setText(analisis);
        }
    }

    private String generarAnalisisDetallado(String variable, String costoReducido, String recomendacion, String rentable) {
        StringBuilder analisis = new StringBuilder();
        analisis.append("ANÁLISIS DETALLADO - Variable ").append(variable).append("\n\n");
        analisis.append("• Costo Reducido: ").append(costoReducido).append("\n");
        analisis.append("• Recomendación: ").append(recomendacion).append("\n");
        analisis.append("• Rentable Introducir: ").append(rentable).append("\n\n");

        if ("SÍ".equals(rentable)) {
            analisis.append("RECOMENDACIÓN: CONSIDERAR INTRODUCIR\n");
            analisis.append("Esta variable no básica tiene potencial para mejorar la solución actual.\n");
            analisis.append("Al introducirla en la base, el valor de Z podría mejorar en ").append(costoReducido).append(" unidades por cada unidad de esta variable.\n");
        } else {
            analisis.append("RECOMENDACIÓN: MANTENER FUERA DE LA BASE\n");
            analisis.append("Esta variable no es rentable introducirla en la solución actual.\n");
            analisis.append("Su inclusión en la base empeoraría el valor objetivo en ").append(costoReducido).append(" unidades por cada unidad.\n");
        }

        return analisis.toString();
    }

    private void actualizarResumen() {
        DefaultTableModel model = (DefaultTableModel) tablaRentabilidad.getModel();
        int total = model.getRowCount();
        int rentables = 0;
        for (int i = 0; i < total; i++) {
            if ("SÍ".equals(model.getValueAt(i, 5).toString())) rentables++;
        }
        String resumen = String.format("Variables analizadas: %d | Rentables: %d | No rentables: %d",
                total, rentables, total - rentables);
        lblResumenRentabilidad.setText(resumen);
    }

    private void analizarRentabilidadCompleta() {
        DefaultTableModel model = (DefaultTableModel) tablaRentabilidad.getModel();
        int rentables = 0;
        StringBuilder recomend = new StringBuilder();
        for (int i = 0; i < model.getRowCount(); i++) {
            if ("SÍ".equals(model.getValueAt(i, 5).toString())) {
                rentables++;
                recomend.append(model.getValueAt(i, 0)).append(" ");
            }
        }
        JOptionPane.showMessageDialog(this,
                String.format("Análisis completado.\nSe encontraron %d variables rentables para introducir.\n%s",
                        rentables, rentables > 0 ? "Se recomienda considerar: " + recomend : "Ninguna.")
                ,
                "Análisis de Rentabilidad Completo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void volverAResultados() {
        MainApplication.volverAResultados();
        this.setVisible(false);
    }
}
