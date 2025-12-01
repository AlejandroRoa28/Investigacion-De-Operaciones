package main.java.com.simplexanddualsolver.ui;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import main.java.com.simplexanddualsolver.model.FormaEstandar;
import main.java.com.simplexanddualsolver.model.ModeloProblema;
import main.java.com.simplexanddualsolver.sensitivity.AnalizadorSensibilidad;
import main.java.com.simplexanddualsolver.solution.Solucion;
import main.java.com.simplexanddualsolver.solver.DualSimplexSolver;
import main.java.com.simplexanddualsolver.solver.MotorSolver;
import main.java.com.simplexanddualsolver.solver.SimplexSolver;

public class ModelInputFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField txtVariables;
    private JTextField txtRestricciones;
    private JButton btnGenerar;
    private JPanel panelFO;
    private JPanel panelRestricciones;
    private JButton btnValidar;
    private JComboBox<String> cmbMetodo;
    private JButton btnResolver;

    // Guardar referencias al backend para reusar en ResultsFrame
    private ModeloProblema modelo;
    private FormaEstandar forma;
    private Solucion solucion;
    private AnalizadorSensibilidad analizador;

    public ModelInputFrame() {
        setTitle("Ingreso del Modelo - Programa Lineal");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel configPanel = crearPanelConfiguracion();
        mainPanel.add(configPanel);

        panelFO = crearPanelFuncionObjetivo();
        mainPanel.add(panelFO);

        panelRestricciones = crearPanelRestricciones();
        mainPanel.add(panelRestricciones);

        JPanel controlPanel = crearPanelControl();
        mainPanel.add(controlPanel);

        add(mainPanel);
    }

    private JPanel crearPanelConfiguracion() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Configuración Inicial",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                Color.BLUE
        ));

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel lblVariables = new JLabel("Número de Variables:");
        txtVariables = new JTextField(5);

        JLabel lblRestricciones = new JLabel("Número de Restricciones:");
        txtRestricciones = new JTextField(5);

        btnGenerar = new JButton("Generar Campos");
        setOptionalIcon(btnGenerar, "generar.png", 20, 20);
        btnGenerar.setToolTipText("Generar campos para ingresar los coeficientes");

        panel.add(lblVariables);
        panel.add(txtVariables);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(lblRestricciones);
        panel.add(txtRestricciones);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(btnGenerar);

        btnGenerar.addActionListener(e -> generarCamposDinamicos());

        return panel;
    }

    private JPanel crearPanelFuncionObjetivo() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Función Objetivo (Coeficientes c)",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                Color.BLUE
        ));
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private JPanel crearPanelRestricciones() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Restricciones (Matriz A, Vector b y Tipos)",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                Color.BLUE
        ));
        panel.setLayout(new BorderLayout());
        return panel;
    }

    private JPanel crearPanelControl() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        btnValidar = new JButton("Validar Modelo");
        setOptionalIcon(btnValidar, "validar.png", 20, 20);
        btnValidar.setToolTipText("Validar que el modelo esté correctamente ingresado");

        JLabel lblMetodo = new JLabel("Método:");
        cmbMetodo = new JComboBox<>(new String[]{"Simplex", "Dual Simplex"});

        btnResolver = new JButton("Resolver");
        setOptionalIcon(btnResolver, "resolver.png", 20, 20);
        btnResolver.setToolTipText("Resolver el modelo con el método seleccionado");

        panel.add(btnValidar);
        panel.add(lblMetodo);
        panel.add(cmbMetodo);
        panel.add(btnResolver);

        btnValidar.addActionListener(e -> validarModelo());
        btnResolver.addActionListener(e -> resolverModelo());

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

    private void generarCamposDinamicos() {
        try {
            int numVars = Integer.parseInt(txtVariables.getText());
            int numRest = Integer.parseInt(txtRestricciones.getText());

            panelFO.removeAll();
            JPanel subPanelFO = new JPanel(new FlowLayout(FlowLayout.LEFT));
            subPanelFO.add(new JLabel("Max Z = ")); // usas MAX

            for (int i = 0; i < numVars; i++) {
                JTextField coef = new JTextField(5);
                coef.setText("0");
                coef.setName("FO_coef_" + i); // para poder encontrarlos luego
                subPanelFO.add(coef);
                subPanelFO.add(new JLabel("x" + (i + 1)));
                if (i < numVars - 1) subPanelFO.add(new JLabel(" + "));
            }
            panelFO.add(subPanelFO, BorderLayout.NORTH);

            panelRestricciones.removeAll();
            JPanel matrixPanel = new JPanel();
            matrixPanel.setLayout(new GridLayout(numRest, numVars + 2, 5, 5));

            for (int i = 0; i < numRest; i++) {
                for (int j = 0; j < numVars; j++) {
                    JTextField cell = new JTextField();
                    cell.setText("0");
                    cell.setName("A_" + i + "_" + j);
                    matrixPanel.add(cell);
                }

                JComboBox<String> tipo = new JComboBox<>(new String[]{"<=", ">=", "="});
                tipo.setName("tipo_" + i);
                matrixPanel.add(tipo);

                JTextField bVal = new JTextField();
                bVal.setText("0");
                bVal.setName("b_" + i);
                matrixPanel.add(bVal);
            }

            JScrollPane scrollPane = new JScrollPane(matrixPanel);
            panelRestricciones.add(scrollPane, BorderLayout.CENTER);

            panelFO.revalidate();
            panelFO.repaint();
            panelRestricciones.revalidate();
            panelRestricciones.repaint();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Por favor ingrese valores numéricos válidos",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void validarModelo() {
        try {
            construirModeloDesdeUI();
            if (modelo.validarModelo()) {
                JOptionPane.showMessageDialog(this,
                        "Modelo validado correctamente",
                        "Validación",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "El modelo no es válido (dimensiones incoherentes).",
                        "Validación",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al validar el modelo: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resolverModelo() {
        try {
            if (txtVariables.getText().isEmpty() || txtRestricciones.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor, ingrese el número de variables y restricciones primero",
                        "Datos Incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            construirModeloDesdeUI();
            if (!modelo.validarModelo()) {
                JOptionPane.showMessageDialog(this,
                        "El modelo no es válido. Revise los datos.",
                        "Error de modelo",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            forma = new FormaEstandar();
            forma.convertirAEstandar(modelo);

            String metodo = (String) cmbMetodo.getSelectedItem();
            MotorSolver solver;
            if ("Dual Simplex".equalsIgnoreCase(metodo)) {
                solver = new DualSimplexSolver();
            } else {
                solver = new SimplexSolver();
            }

            solver.resolver(forma);
            solucion = solver.obtenerSolucion();

            if (solucion.esOptima()) {
                analizador = new AnalizadorSensibilidad(solucion, modelo, forma);
            }

            // Notificar a MainApplication para que guarde modelo, solución y analizador
            MainApplication.notificarResultado(modelo, solucion, analizador);
            this.setVisible(false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al resolver el modelo: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void construirModeloDesdeUI() {
        int numVars = Integer.parseInt(txtVariables.getText());
        int numRest = Integer.parseInt(txtRestricciones.getText());

        double[][] A = new double[numRest][numVars];
        double[] b = new double[numRest];
        double[] c = new double[numVars];
        String[] tipos = new String[numRest];

        // Leer FO
        Component[] foComponents = ((JPanel) panelFO.getComponent(0)).getComponents();
        int idxC = 0;
        for (Component comp : foComponents) {
            if (comp instanceof JTextField && comp.getName() != null && comp.getName().startsWith("FO_coef_")) {
                c[idxC++] = Double.parseDouble(((JTextField) comp).getText());
            }
        }

        // Leer restricciones
        JScrollPane scroll = (JScrollPane) panelRestricciones.getComponent(0);
        JPanel matrixPanel = (JPanel) scroll.getViewport().getView();
        Component[] comps = matrixPanel.getComponents();
        for (Component comp : comps) {
            if (comp instanceof JTextField) {
                String name = comp.getName();
                if (name == null) continue;
                if (name.startsWith("A_")) {
                    String[] parts = name.split("_");
                    int i = Integer.parseInt(parts[1]);
                    int j = Integer.parseInt(parts[2]);
                    A[i][j] = Double.parseDouble(((JTextField) comp).getText());
                } else if (name.startsWith("b_")) {
                    int i = Integer.parseInt(name.split("_")[1]);
                    b[i] = Double.parseDouble(((JTextField) comp).getText());
                }
            } else if (comp instanceof JComboBox) {
                String name = comp.getName();
                if (name != null && name.startsWith("tipo_")) {
                    int i = Integer.parseInt(name.split("_")[1]);
                    tipos[i] = (String) ((JComboBox<?>) comp).getSelectedItem();
                }
            }
        }

        modelo = new ModeloProblema(A, b, c, tipos);
        System.out.println("FUNCION OBJETIVO: " + Arrays.toString(c));
        System.out.println("A:");
        for (int i = 0; i < numRest; i++) System.out.println(Arrays.toString(A[i]));
        System.out.println("b: " + Arrays.toString(b));
    }
}
