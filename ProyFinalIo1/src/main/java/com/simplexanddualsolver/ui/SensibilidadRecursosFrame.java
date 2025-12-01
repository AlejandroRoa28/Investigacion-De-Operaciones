package main.java.com.simplexanddualsolver.ui;

import main.java.com.simplexanddualsolver.sensitivity.SensibilidadRecursos;
import main.java.com.simplexanddualsolver.model.ModeloProblema;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SensibilidadRecursosFrame extends JFrame {

    private JTable tablaSensibilidad;
    private JButton btnCalcularImpacto;
    private JButton btnVolver;
    private JLabel lblImpactoZ;

    // referencias del back
    private SensibilidadRecursos sensibilidadRecursos;
    private ModeloProblema modelo;

    public SensibilidadRecursosFrame() {
        setTitle("Análisis de Sensibilidad - Recursos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = crearPanelSensibilidad();
        mainPanel.add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentral = crearPanelCambiosRecursos();
        mainPanel.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = crearPanelInferior();
        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Setter que llamará MainApplication
    public void setDatos(SensibilidadRecursos sensibilidadRecursos, ModeloProblema modelo) {
        this.sensibilidadRecursos = sensibilidadRecursos;
        this.modelo = modelo;
        cargarDesdeBackend();
    }

    private JPanel crearPanelSensibilidad() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 100, 0)),
                "Sensibilidad de Recursos - Precios Sombra y Rangos",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 13),
                new Color(0, 100, 0)
        ));

        String[] columnas = {
                "Restricción",
                "Recurso Actual (b)",
                "Precio Sombra",
                "Límite Inferior",
                "Límite Superior",
                "Rango Permitido",
                "Δb (cambio propuesto)"
        };

        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                // solo permitir editar la última columna (Δb)
                return column == 6;
            }
        };

        tablaSensibilidad = new JTable(model);
        tablaSensibilidad.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(tablaSensibilidad);
        scrollPane.setPreferredSize(new Dimension(900, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelCambiosRecursos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 140, 0)),
                "Simulación de Cambios en Recursos",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 13),
                new Color(255, 140, 0)
        ));

        JTextArea instrucciones = new JTextArea(
                "• Ingrese los cambios Δb en la última columna de la tabla superior\n" +
                "• Valores positivos aumentan el recurso, negativos lo reducen\n" +
                "• Luego presione 'Calcular Impacto en Z' para ver el efecto aproximado"
        );
        instrucciones.setEditable(false);
        instrucciones.setBackground(new Color(240, 240, 240));
        instrucciones.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(instrucciones, BorderLayout.NORTH);
        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelResultados = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultado del Impacto"));

        JLabel lblTexto = new JLabel("Impacto en Z: ");
        lblImpactoZ = new JLabel("0.0");
        lblImpactoZ.setFont(new Font("Arial", Font.BOLD, 14));
        lblImpactoZ.setForeground(new Color(0, 100, 0));

        panelResultados.add(lblTexto);
        panelResultados.add(lblImpactoZ);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

        btnCalcularImpacto = new JButton("Calcular Impacto en Z");
        setOptionalIcon(btnCalcularImpacto, "calculator.png", 20, 20);

        btnVolver = new JButton("Volver a Resultados");
        setOptionalIcon(btnVolver, "volver.png", 20, 20);

        panelBotones.add(btnCalcularImpacto);
        panelBotones.add(btnVolver);

        panel.add(panelResultados, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        btnCalcularImpacto.addActionListener(e -> calcularImpactoZ());
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

    // llena la tabla con datos reales de SensibilidadRecursos
    private void cargarDesdeBackend() {
        if (sensibilidadRecursos == null || modelo == null) {
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tablaSensibilidad.getModel();
        model.setRowCount(0);

        double[] b = modelo.obtenerVectorB();
        int m = b.length;

        for (int i = 0; i < m; i++) {
            double recurso = b[i];
            double precioSombra = sensibilidadRecursos.obtenerPrecioSombra(i);
            double[] rango = sensibilidadRecursos.calcularRangoB(i);
            double bMin = rango[0];
            double bMax = rango[1];
            String rangoStr;
            if (Double.isInfinite(bMin) && Double.isInfinite(bMax)) {
                rangoStr = "(-∞, +∞)";
            } else if (Double.isInfinite(bMin)) {
                rangoStr = "(-∞, " + bMax + "]";
            } else if (Double.isInfinite(bMax)) {
                rangoStr = "[" + bMin + ", +∞)";
            } else {
                rangoStr = "[" + bMin + ", " + bMax + "]";
            }
            // Δb inicial en 0
            model.addRow(new Object[]{
                    "R" + (i + 1),
                    recurso,
                    precioSombra,
                    bMin,
                    bMax,
                    rangoStr,
                    0.0
            });
        }

        lblImpactoZ.setText("0.0");
    }

    private void calcularImpactoZ() {
        if (sensibilidadRecursos == null || modelo == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay datos de sensibilidad cargados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tablaSensibilidad.getModel();
        int m = model.getRowCount();
        double[] deltaB = new double[m];

        try {
            for (int i = 0; i < m; i++) {
                Object val = model.getValueAt(i, 6); // columna Δb
                double d = 0.0;
                if (val != null && !val.toString().trim().isEmpty()) {
                    d = Double.parseDouble(val.toString());
                }
                deltaB[i] = d;
            }

            double impacto = sensibilidadRecursos.calcularImpactoEnZ(deltaB);
            lblImpactoZ.setText(String.valueOf(impacto));

            JOptionPane.showMessageDialog(this,
                    "Impacto aproximado en Z: " + impacto,
                    "Cálculo Completado",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese valores numéricos válidos en la columna Δb.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAResultados() {
        MainApplication.volverAResultados();
        this.setVisible(false);
    }
}
