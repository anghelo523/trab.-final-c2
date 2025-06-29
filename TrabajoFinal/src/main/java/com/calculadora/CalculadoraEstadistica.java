package com.calculadora;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class CalculadoraEstadistica extends JFrame {
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JTabbedPane tabbedPane;
    private DecimalFormat df = new DecimalFormat("#.##");
    private JButton exportarImportarBtn;

    public CalculadoraEstadistica() {
        setTitle("Calculadora estadística en línea");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // Barra de herramientas superior
        JPanel toolBar = crearBarraHerramientas();
        mainPanel.add(toolBar, BorderLayout.NORTH);

        // Crear tabla de datos
        crearTabla();
        JScrollPane scrollTabla = new JScrollPane(tabla);
        mainPanel.add(scrollTabla, BorderLayout.CENTER);

        // Panel inferior con pestañas de análisis
        tabbedPane = crearPanelAnalisis();
        mainPanel.add(tabbedPane, BorderLayout.SOUTH);

        add(mainPanel);
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    private JPanel crearBarraHerramientas() {
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JButton vaciarTabla = new JButton("Vaciar tabla");
        exportarImportarBtn = new JButton("Exportar / Importar");
        JButton transformarDatos = new JButton("Transformar datos");
        JButton configuracion = new JButton("Configuración");

        // Estilo de los botones
        Color colorTurquesa = new Color(0, 128, 128);
        Font fuenteBoton = new Font("Arial", Font.PLAIN, 12);
        
        Component[] botones = {vaciarTabla, exportarImportarBtn, transformarDatos, configuracion};
        for (Component boton : botones) {
            ((JButton)boton).setForeground(colorTurquesa);
            ((JButton)boton).setFont(fuenteBoton);
            ((JButton)boton).setFocusPainted(false);
        }

        // Eventos de los botones
        vaciarTabla.addActionListener(e -> vaciarTabla());
        exportarImportarBtn.addActionListener(e -> mostrarMenuExportarImportar());
        transformarDatos.addActionListener(e -> JOptionPane.showMessageDialog(this, "Función en desarrollo"));
        configuracion.addActionListener(e -> JOptionPane.showMessageDialog(this, "Configuración en desarrollo"));

        toolBar.add(vaciarTabla);
        toolBar.add(exportarImportarBtn);
        toolBar.add(transformarDatos);
        toolBar.add(configuracion);

        return toolBar;
    }

    private void crearTabla() {
        // Definir las columnas
        String[] columnas = {"Casos", "Sexo", "Salario", "Edad", "Lugar", "Peso", "Empresa", "Grado académico"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0; // La columna Casos no es editable
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setRowHeight(25);
        
        // Agregar los datos de ejemplo
        Object[][] datosMuestra = {
            {"1", "mujer", "1500", "33", "Barcelona", "80", "Mercadona", "Grado"},
            {"2", "mujer", "1200", "33", "Barcelona", "82.5", "Repsol", "No"},
            {"3", "hombre", "2200", "34", "Madrid", "100.8", "Mercadona", "Grado"},
            {"4", "hombre", "2100", "42", "Madrid", "90", "Mercadona", "Máster"},
            {"5", "mujer", "1500", "29", "Barcelona", "67", "Repsol", "Máster"},
            {"6", "mujer", "1700", "19", "Valencia", "60", "Repsol", "Máster"},
            {"7", "hombre", "3000", "50", "Valencia", "77", "Repsol", "No"},
            {"8", "hombre", "3000", "55", "Valencia", "77", "Repsol", "Grado"},
            {"9", "mujer", "2800", "31", "Madrid", "87", "Repsol", "Grado"},
            {"10", "hombre", "2900", "46", "Madrid", "70", "Iberdrola", "Máster"},
            {"11", "mujer", "2780", "36", "Valencia", "57", "Mercadona", "No"},
            {"12", "hombre", "2550", "48", "Madrid", "64", "Iberdrola", "Máster"}
        };

        for (Object[] fila : datosMuestra) {
            modeloTabla.addRow(fila);
        }

        // Añadir filas vacías adicionales
        for (int i = 13; i <= 15; i++) {
            modeloTabla.addRow(new Object[]{String.valueOf(i), "", "", "", "", "", "", ""});
        }

        // Agregar tooltips a las columnas para mostrar el tipo de variable
        tabla.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                switch (column) {
                    case 1: case 4: case 6:
                        label.setToolTipText("Variable nominal");
                        break;
                    case 2: case 3: case 5:
                        label.setToolTipText("Variable métrica");
                        break;
                    case 7:
                        label.setToolTipText("Variable ordinal");
                        break;
                }
                return label;
            }
        });
    }

    private JTabbedPane crearPanelAnalisis() {
        JTabbedPane tabbedPane = new JTabbedPane();
        String[] pestanas = {
            "Descriptivo", "Pruebas de hipótesis", "Correlación", "Regresión",
            "Mediación/Moderación", "PCA", "Fiabilidad", "Clasificación"
        };

        for (String pestana : pestanas) {
            JPanel panel = crearPanelPestana(pestana);
            tabbedPane.addTab(pestana, panel);
            tabbedPane.setForegroundAt(tabbedPane.getTabCount() - 1, new Color(0, 128, 128));
        }

        return tabbedPane;
    }

    private JPanel crearPanelPestana(String nombrePestana) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para selección de variables
        JPanel variablesPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        variablesPanel.add(new JLabel("Variables métricas:"));
        variablesPanel.add(new JLabel("Variables ordinales:"));
        variablesPanel.add(new JLabel("Variables nominales:"));

        // Panel para resultados
        JTextArea resultados = new JTextArea(10, 40);
        resultados.setEditable(false);
        JScrollPane scrollResultados = new JScrollPane(resultados);

        panel.add(variablesPanel, BorderLayout.NORTH);
        panel.add(scrollResultados, BorderLayout.CENTER);

        return panel;
    }

    private void vaciarTabla() {
        while (modeloTabla.getRowCount() > 0) {
            modeloTabla.removeRow(0);
        }
        for (int i = 1; i <= 15; i++) {
            modeloTabla.addRow(new Object[]{String.valueOf(i), "", "", "", "", "", "", ""});
        }
    }

    private void mostrarMenuExportarImportar() {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem exportar = new JMenuItem("Exportar a CSV");
        JMenuItem importar = new JMenuItem("Importar desde CSV");

        exportar.addActionListener(e -> exportarACSV());
        importar.addActionListener(e -> importarDesdeCSV());

        menu.add(exportar);
        menu.add(importar);
        
        menu.show(exportarImportarBtn, 0, exportarImportarBtn.getHeight());
    }

    private void exportarACSV() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
            
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new File(file.getAbsolutePath() + ".csv");
                }

                try (PrintWriter writer = new PrintWriter(file)) {
                    // Escribir encabezados
                    for (int i = 0; i < modeloTabla.getColumnCount(); i++) {
                        writer.print(modeloTabla.getColumnName(i));
                        writer.print(i < modeloTabla.getColumnCount() - 1 ? "," : "\n");
                    }

                    // Escribir datos
                    for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                        for (int j = 0; j < modeloTabla.getColumnCount(); j++) {
                            writer.print(modeloTabla.getValueAt(i, j));
                            writer.print(j < modeloTabla.getColumnCount() - 1 ? "," : "\n");
                        }
                    }
                }

                JOptionPane.showMessageDialog(this, "Datos exportados exitosamente");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importarDesdeCSV() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos CSV (*.csv)", "csv"));
            
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                vaciarTabla();
                
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    // Saltar la línea de encabezados
                    reader.readLine();
                    
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] datos = line.split(",");
                        if (datos.length == modeloTabla.getColumnCount()) {
                            modeloTabla.addRow(datos);
                        }
                    }
                }

                JOptionPane.showMessageDialog(this, "Datos importados exitosamente");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al importar: " + ex.getMessage(),
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new CalculadoraEstadistica().setVisible(true);
        });
    }
} 