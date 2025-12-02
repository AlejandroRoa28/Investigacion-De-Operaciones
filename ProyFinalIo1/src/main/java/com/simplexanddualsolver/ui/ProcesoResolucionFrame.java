package main.java.com.simplexanddualsolver.ui;

import main.java.com.simplexanddualsolver.solution.Solucion;
import main.java.com.simplexanddualsolver.solution.Solucion.IteracionSimplex;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class ProcesoResolucionFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    // Componentes de navegación
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private JButton btnInicio;
    private JButton btnFinal;
    private JLabel lblIteracionActual;
    private JSlider sliderIteracion;

    // Componentes de información
    private JLabel lblMetodo;
    private JLabel lblEstadoGeneral;
    private JLabel lblTotalIteraciones;

    // Componentes de la iteración actual
    private JLabel lblDescripcion;
    private JLabel lblVariableEntra;
    private JLabel lblVariableSale;
    private JLabel lblPivote;
    private JTable tablaTableau;
    private JTable tablaBase;

    // Botón volver
    private JButton btnVolver;

    // Datos
    private Solucion solucion;
    private List<IteracionSimplex> historial;
    private int iteracionActual;

    public ProcesoResolucionFrame() {
        setTitle("Proceso de Resolución - Paso a Paso");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel superior: info general y navegación
        JPanel panelSuperior = crearPanelSuperior();
        mainPanel.add(panelSuperior, BorderLayout.NORTH);

        // Panel central: tabla y detalles de la iteración
        JPanel panelCentral = crearPanelCentral();
        mainPanel.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior: botón volver
        JPanel panelInferior = crearPanelInferior();
        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Recibe la solución y carga el historial
     */
    public void setSolucion(Solucion solucion) {
        this.solucion = solucion;
        this.historial = solucion.getHistorialIteraciones();
        this.iteracionActual = 0;
        cargarInformacionGeneral();
        configurarSlider();
        mostrarIteracion(0);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Info general del método
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        panelInfo.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0, 100, 0)),
                "Información General",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(0, 100, 0)
        ));

        lblMetodo = new JLabel("Método: ");
        lblMetodo.setFont(new Font("Arial", Font.BOLD, 12));

        lblEstadoGeneral = new JLabel("Estado: ");
        lblEstadoGeneral.setFont(new Font("Arial", Font.BOLD, 12));

        lblTotalIteraciones = new JLabel("Total iteraciones: ");
        lblTotalIteraciones.setFont(new Font("Arial", Font.BOLD, 12));

        panelInfo.add(lblMetodo);
        panelInfo.add(Box.createHorizontalStrut(30));
        panelInfo.add(lblEstadoGeneral);
        panelInfo.add(Box.createHorizontalStrut(30));
        panelInfo.add(lblTotalIteraciones);

        // Panel de navegación
        JPanel panelNavegacion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelNavegacion.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLUE),
                "Navegación entre Iteraciones",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                Color.BLUE
        ));

        btnInicio = new JButton("⏮ Inicio");
        btnAnterior = new JButton("◀ Anterior");
        btnSiguiente = new JButton("Siguiente ▶");
        btnFinal = new JButton("Final ⏭");

        lblIteracionActual = new JLabel("Iteración 0 de 0");
        lblIteracionActual.setFont(new Font("Arial", Font.BOLD, 14));
        lblIteracionActual.setForeground(Color.BLUE);

        sliderIteracion = new JSlider(0, 0, 0);
        sliderIteracion.setPreferredSize(new Dimension(200, 40));
        sliderIteracion.setPaintTicks(true);
        sliderIteracion.setPaintLabels(true);
        sliderIteracion.setSnapToTicks(true);

        panelNavegacion.add(btnInicio);
        panelNavegacion.add(btnAnterior);
        panelNavegacion.add(Box.createHorizontalStrut(10));
        panelNavegacion.add(lblIteracionActual);
        panelNavegacion.add(Box.createHorizontalStrut(10));
        panelNavegacion.add(btnSiguiente);
        panelNavegacion.add(btnFinal);
        panelNavegacion.add(Box.createHorizontalStrut(20));
        panelNavegacion.add(new JLabel("Ir a:"));
        panelNavegacion.add(sliderIteracion);

        // Listeners de navegación
        btnInicio.addActionListener(e -> irAIteracion(0));
        btnAnterior.addActionListener(e -> irAIteracion(iteracionActual - 1));
        btnSiguiente.addActionListener(e -> irAIteracion(iteracionActual + 1));
        btnFinal.addActionListener(e -> irAIteracion(historial.size() - 1));
        sliderIteracion.addChangeListener(e -> {
            if (!sliderIteracion.getValueIsAdjusting()) {
                irAIteracion(sliderIteracion.getValue());
            }
        });

        panel.add(panelInfo, BorderLayout.NORTH);
        panel.add(panelNavegacion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Panel de detalles de la iteración
        JPanel panelDetalles = new JPanel(new GridLayout(2, 2, 10, 5));
        panelDetalles.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 140, 0)),
                "Detalles del Paso",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(255, 140, 0)
        ));

        lblVariableEntra = new JLabel("Variable que entra: -");
        lblVariableEntra.setFont(new Font("Arial", Font.PLAIN, 12));
        lblVariableEntra.setForeground(new Color(0, 128, 0));

        lblVariableSale = new JLabel("Variable que sale: -");
        lblVariableSale.setFont(new Font("Arial", Font.PLAIN, 12));
        lblVariableSale.setForeground(new Color(180, 0, 0));

        lblPivote = new JLabel("Elemento pivote: -");
        lblPivote.setFont(new Font("Arial", Font.PLAIN, 12));

        lblDescripcion = new JLabel("Descripción: -");
        lblDescripcion.setFont(new Font("Arial", Font.ITALIC, 12));

        panelDetalles.add(lblVariableEntra);
        panelDetalles.add(lblVariableSale);
        panelDetalles.add(lblPivote);
        panelDetalles.add(lblDescripcion);

        // Panel de la tabla (tableau)
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(128, 0, 128)),
                "Tableau Simplex",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                new Color(128, 0, 128)
        ));

        tablaTableau = new JTable();
        tablaTableau.setRowHeight(28);
        tablaTableau.setFont(new Font("Monospaced", Font.PLAIN, 12));
        tablaTableau.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));

        JScrollPane scrollTabla = new JScrollPane(tablaTableau);
        scrollTabla.setPreferredSize(new Dimension(900, 250));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        // Panel de variables básicas
        JPanel panelBase = new JPanel(new BorderLayout());
        panelBase.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Variables Básicas",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                Color.GRAY
        ));

        tablaBase = new JTable();
        tablaBase.setRowHeight(25);
        JScrollPane scrollBase = new JScrollPane(tablaBase);
        scrollBase.setPreferredSize(new Dimension(200, 150));
        panelBase.add(scrollBase, BorderLayout.CENTER);

        // Organizar paneles
        JPanel panelTablasContainer = new JPanel(new BorderLayout(10, 0));
        panelTablasContainer.add(panelTabla, BorderLayout.CENTER);
        panelTablasContainer.add(panelBase, BorderLayout.EAST);

        panel.add(panelDetalles, BorderLayout.NORTH);
        panel.add(panelTablasContainer, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));

        btnVolver = new JButton("Volver a Resultados");
        btnVolver.addActionListener(e -> {
            MainApplication.volverAResultados();
            this.setVisible(false);
        });

        panel.add(btnVolver);
        return panel;
    }

    private void cargarInformacionGeneral() {
        if (solucion == null) return;

        String metodo = solucion.getMetodoUsado();
        lblMetodo.setText("Método: " + (metodo != null ? metodo : "No especificado"));

        String estado = solucion.getEstado();
        lblEstadoGeneral.setText("Estado: " + (estado != null ? estado : "-"));
        if ("OPTIMA".equalsIgnoreCase(estado)) {
            lblEstadoGeneral.setForeground(new Color(0, 128, 0));
        } else {
            lblEstadoGeneral.setForeground(Color.RED);
        }

        lblTotalIteraciones.setText("Total iteraciones: " + solucion.getNumIteraciones());
    }

    private void configurarSlider() {
        if (historial == null || historial.isEmpty()) {
            sliderIteracion.setMaximum(0);
            sliderIteracion.setEnabled(false);
            return;
        }

        int max = historial.size() - 1;
        sliderIteracion.setMinimum(0);
        sliderIteracion.setMaximum(max);
        sliderIteracion.setValue(0);
        sliderIteracion.setMajorTickSpacing(Math.max(1, max / 5));
        sliderIteracion.setMinorTickSpacing(1);
        sliderIteracion.setEnabled(max > 0);
    }

    private void irAIteracion(int index) {
        if (historial == null || historial.isEmpty()) return;

        // Validar límites
        if (index < 0) index = 0;
        if (index >= historial.size()) index = historial.size() - 1;

        iteracionActual = index;
        sliderIteracion.setValue(index);
        mostrarIteracion(index);
        actualizarBotonesNavegacion();
    }

    private void actualizarBotonesNavegacion() {
        btnInicio.setEnabled(iteracionActual > 0);
        btnAnterior.setEnabled(iteracionActual > 0);
        btnSiguiente.setEnabled(iteracionActual < historial.size() - 1);
        btnFinal.setEnabled(iteracionActual < historial.size() - 1);
    }

    private void mostrarIteracion(int index) {
        if (historial == null || index < 0 || index >= historial.size()) return;

        IteracionSimplex iter = historial.get(index);

        // Actualizar label de iteración
        lblIteracionActual.setText("Iteración " + iter.getNumeroIteracion() + " de " + solucion.getNumIteraciones());

        // Actualizar detalles
        String varEntra = iter.getVariableEntra();
        String varSale = iter.getVariableSale();

        lblVariableEntra.setText("Variable que entra: " + (varEntra != null ? varEntra : "-"));
        lblVariableSale.setText("Variable que sale: " + (varSale != null ? varSale : "-"));

        if (iter.getElementoPivote() != 0) {
            lblPivote.setText(String.format("Elemento pivote: %.4f [fila %d, col %d]",
                    iter.getElementoPivote(),
                    iter.getFilaSaliente(),
                    iter.getColumnaEntrante()));
        } else {
            lblPivote.setText("Elemento pivote: -");
        }

        String desc = iter.getDescripcion();
        lblDescripcion.setText("Descripción: " + (desc != null ? desc : "-"));

        // Mostrar tabla
        mostrarTablaTableau(iter);

        // Mostrar base
        mostrarTablaBase(iter);
    }

    private void mostrarTablaTableau(IteracionSimplex iter) {
        double[][] tabla = iter.getTabla();
        int[] base = iter.getBase();

        if (tabla == null) return;

        int numFilas = tabla.length;
        int numCols = tabla[0].length;

        // Crear columnas: VB, x1, x2, ..., s1, s2, ..., b
        String[] columnas = new String[numCols + 1];
        columnas[0] = "VB";
        for (int j = 0; j < numCols - 1; j++) {
            columnas[j + 1] = "x" + (j + 1);
        }
        columnas[numCols] = "b";

        // Crear datos
        Object[][] datos = new Object[numFilas][numCols + 1];
        for (int i = 0; i < numFilas; i++) {
            // Columna VB
            if (i < numFilas - 1 && base != null && i < base.length) {
                datos[i][0] = "x" + (base[i] + 1);
            } else {
                datos[i][0] = "Z";
            }

            // Resto de columnas
            for (int j = 0; j < numCols; j++) {
                datos[i][j + 1] = String.format("%.4f", tabla[i][j]);
            }
        }

        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTableau.setModel(model);

        // Resaltar fila y columna del pivote
        int filaPivote = iter.getFilaSaliente();
        int colPivote = iter.getColumnaEntrante();

        tablaTableau.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Color por defecto
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);

                // Última fila (Z) en gris claro
                if (row == table.getRowCount() - 1) {
                    c.setBackground(new Color(240, 240, 240));
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                }

                // Resaltar fila pivote
                if (filaPivote >= 0 && row == filaPivote) {
                    c.setBackground(new Color(255, 255, 200));
                }

                // Resaltar columna pivote (columna + 1 porque la primera es VB)
                if (colPivote >= 0 && column == colPivote + 1) {
                    c.setBackground(new Color(200, 255, 200));
                }

                // Resaltar celda pivote
                if (filaPivote >= 0 && colPivote >= 0 &&
                    row == filaPivote && column == colPivote + 1) {
                    c.setBackground(new Color(255, 150, 150));
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                }

                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
    }

    private void mostrarTablaBase(IteracionSimplex iter) {
        int[] base = iter.getBase();
        double[][] tabla = iter.getTabla();

        if (base == null || tabla == null) return;

        String[] columnas = {"Variable", "Valor"};
        Object[][] datos = new Object[base.length][2];

        int colB = tabla[0].length - 1;

        for (int i = 0; i < base.length; i++) {
            datos[i][0] = "x" + (base[i] + 1);
            datos[i][1] = String.format("%.4f", tabla[i][colB]);
        }

        DefaultTableModel model = new DefaultTableModel(datos, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaBase.setModel(model);
    }
}
