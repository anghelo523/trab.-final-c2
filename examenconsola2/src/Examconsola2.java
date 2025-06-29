import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class Examconsola2 {

    private static List<String> variableNames;
    private static List<List<Object>> data;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("¡Bienvenido al Analizador Estadístico de Consola (Examconsola)!");
        variableNames = new ArrayList<>();
        data = new ArrayList<>();

        showMainMenu();
    }

    private static void showMainMenu() {
        int choice;
        do {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("Dataset Actual: " + variableNames.size() + " variables, " + data.size() + " observaciones");
            System.out.println("1. Definir/Añadir Variables (Columnas)");
            System.out.println("2. Mostrar Dataset");
            System.out.println("3. Añadir Observación (Fila)");
            System.out.println("4. Editar Valor (Celda)");
            System.out.println("5. Realizar Regresión Lineal Simple");
            System.out.println("6. Realizar Regresión Lineal Múltiple");
            System.out.println("7. Realizar Análisis de Mediación");
            System.out.println("8. Realizar Análisis de Moderación");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingresa un número.");
                choice = -1;
            }

            switch (choice) {
                case 1:
                    defineVariables();
                    break;
                case 2:
                    printDataset();
                    break;
                case 3:
                    addObservation();
                    break;
                case 4:
                    editCellValue();
                    break;
                case 5:
                    performSimpleRegression();
                    break;
                case 6:
                    performMultipleRegression();
                    break;
                case 7:
                    performMediationAnalysis(); // Aquí se llama al análisis de mediación
                    break;
                case 8:
                    performModerationAnalysis(); // Aquí se llama al análisis de moderación
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (choice != 0);
        scanner.close();
    }

    private static void defineVariables() {
        System.out.println("--- Definir/Añadir Variables al Dataset ---");
        System.out.println("Introduce los nombres de las variables, uno por uno. Escribe 'fin' para terminar.");
        String varName;
        while (true) {
            System.out.print("Nombre de variable (o 'fin'): ");
            varName = scanner.nextLine();
            if (varName.equalsIgnoreCase("fin")) {
                break;
            }
            if (variableNames.contains(varName)) {
                System.out.println("Esa variable ya existe. Elige otro nombre.");
            } else {
                variableNames.add(varName);
                for (List<Object> row : data) {
                    // Asegúrate de que todas las filas existentes tengan el mismo número de columnas
                    // Si ya hay más columnas de las que se necesitan, no añadir null
                    while (row.size() < variableNames.size()) {
                        row.add(null); // Añadir null para la nueva columna en filas existentes
                    }
                }
                System.out.println("Variable '" + varName + "' añadida.");
            }
        }
        if (variableNames.isEmpty()) {
            System.out.println("No se definieron variables.");
        } else {
            System.out.println("Variables actuales: " + variableNames);
        }
    }

    private static void printDataset() {
        if (variableNames.isEmpty()) {
            System.out.println("El dataset no tiene variables definidas.");
            return;
        }
        if (data.isEmpty()) {
            System.out.println("El dataset está vacío (no hay observaciones).");
            return;
        }

        // Imprimir cabecera
        System.out.print("Index\t");
        for (String varName : variableNames) {
            System.out.print(varName + "\t");
        }
        System.out.println();

        // Imprimir datos
        for (int i = 0; i < data.size(); i++) {
            System.out.print((i + 1) + "\t");
            List<Object> row = data.get(i);
            for (int j = 0; j < variableNames.size(); j++) { // Iterar hasta variableNames.size() para evitar IndexOutOfBounds
                Object value = (j < row.size()) ? row.get(j) : null; // Obtener valor o null si la columna no existe en la fila
                if (value == null) {
                    System.out.print("NULL\t");
                } else if (value instanceof Double) {
                    // Imprimir como entero si es un número entero
                    if ((Double) value == ((Double) value).longValue()) {
                        System.out.print(((Double) value).longValue() + "\t");
                    } else {
                        System.out.print(String.format("%.2f", (Double) value) + "\t");
                    }
                } else {
                    System.out.print(value.toString() + "\t");
                }
            }
            System.out.println();
        }
        System.out.println("------------------------------------");
    }

    private static void addObservation() {
        if (variableNames.isEmpty()) {
            System.out.println("Primero debes definir las variables del dataset (Opción 1).");
            return;
        }

        List<Object> newRow = new ArrayList<>();
        System.out.println("--- Añadiendo Nueva Observación (Fila " + (data.size() + 1) + ") ---");
        System.out.println("Ingresa los valores para cada variable. Se intentará convertir a número si es posible.");
        for (String varName : variableNames) {
            System.out.print("Valor para '" + varName + "': ");
            String input = scanner.nextLine();
            try {
                // Usa .replaceAll(",", ".") para sistemas que usan coma como separador decimal
                newRow.add(Double.parseDouble(input.trim().replaceAll(",", ".")));
            } catch (NumberFormatException e) {
                newRow.add(input.trim()); // Mantener como String si no es numérico
            }
        }
        data.add(newRow);
        System.out.println("Observación añadida.");
    }

    private static void editCellValue() {
        if (data.isEmpty() || variableNames.isEmpty()) {
            System.out.println("El dataset está vacío. No hay celdas para editar.");
            return;
        }

        printDataset();

        System.out.print("Ingresa el índice de la fila (observación) a editar (1-" + data.size() + "): ");
        int rowIndex;
        try {
            rowIndex = Integer.parseInt(scanner.nextLine()) - 1;
            if (rowIndex < 0 || rowIndex >= data.size()) {
                System.out.println("Índice de fila inválido.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida para el índice de fila.");
            return;
        }

        System.out.print("Ingresa el nombre de la variable (columna) a editar: ");
        String varName = scanner.nextLine();
        int colIndex = variableNames.indexOf(varName);

        if (colIndex == -1) {
            System.out.println("Nombre de variable inválido.");
            return;
        }

        System.out.print("Ingresa el nuevo valor para '" + varName + "' en la fila " + (rowIndex + 1) + ": ");
        String newValueInput = scanner.nextLine();
        Object newValue;
        try {
            // Usa .replaceAll(",", ".") para sistemas que usan coma como separador decimal
            newValue = Double.parseDouble(newValueInput.trim().replaceAll(",", "."));
        } catch (NumberFormatException e) {
            newValue = newValueInput.trim(); // Mantener como String si no es numérico
        }

        data.get(rowIndex).set(colIndex, newValue);
        System.out.println("Valor actualizado correctamente.");
    }

    // --- Métodos de Análisis Estadístico (Stubs) ---
    // Estas funciones aún no tienen la lógica de cálculo, solo muestran un mensaje.
    // Deberás implementar la lógica matemática para cada una.

    private static void performSimpleRegression() {
        System.out.println("\n--- Regresión Lineal Simple ---");
        System.out.println("Esta función aún no está implementada.");
        System.out.println("Se necesita seleccionar una variable dependiente (Y) y una independiente (X).");
    }

    private static void performMultipleRegression() {
        System.out.println("\n--- Regresión Lineal Múltiple ---");
        System.out.println("Esta función aún no está implementada.");
        System.out.println("Se necesita seleccionar una variable dependiente (Y) y múltiples independientes (X1, X2, ...).");
    }

    private static void performMediationAnalysis() {
        System.out.println("\n--- Análisis de Mediación ---");
        System.out.println("Esta función aún no está implementada.");
        System.out.println("Se necesita definir la variable independiente, el mediador y la variable dependiente.");
    }

    private static void performModerationAnalysis() {
        System.out.println("\n--- Análisis de Moderación ---");
        System.out.println("Esta función aún no está implementada.");
        System.out.println("Se necesita definir la variable independiente, el moderador y la variable dependiente.");
    }
}