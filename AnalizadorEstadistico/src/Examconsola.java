import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

public class Examconsola {

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
                    performMediationAnalysis();
                    break;
                case 8:
                    performModerationAnalysis();
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
                    row.add(null);
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

        System.out.print("Index\t");
        for (String varName : variableNames) {
            System.out.print(varName + "\t");
        }
        System.out.println();

        for (int i = 0; i < data.size(); i++) {
            System.out.print((i + 1) + "\t");
            List<Object> row = data.get(i);
            for (int j = 0; j < row.size(); j++) {
                Object value = row.get(j);
                if (value == null) {
                    System.out.print("NULL\t");
                } else if (value instanceof Double) {
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
                newRow.add(Double.parseDouble(input.trim().replaceAll(",", ".")));
            } catch (NumberFormatException e) {
                newRow.add(input.trim());
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
            newValue = Double.parseDouble(newValueInput.trim().replaceAll(",", "."));
        } catch (NumberFormatException e) {
            newValue = newValueInput.trim();
        }

        while (data.get(rowIndex).size() <= colIndex) {
            data.get(rowIndex).add(null);
        }
        data.get(rowIndex).set(colIndex, newValue);
        System.out.println("Valor actualizado.");
    }

    private static int getVariableIndex(String varName) {
        return variableNames.indexOf(varName);
    }

    private static boolean isNumericVariable(String varName) {
        int colIndex = getVariableIndex(varName);
        if (colIndex == -1) return false;

        boolean foundNumeric = false;
        for (List<Object> row : data) {
            if (colIndex < row.size() && row.get(colIndex) != null) {
                if (row.get(colIndex) instanceof Double) {
                    foundNumeric = true;
                    break;
                } else {
                    try {
                        Double.parseDouble(row.get(colIndex).toString().trim().replaceAll(",", "."));
                        foundNumeric = true;
                        break;
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }
        return foundNumeric;
    }

    private static boolean hasVariance(String varName) {
        int colIndex = getVariableIndex(varName);
        if (colIndex == -1) return false;

        Set<Double> distinctValues = new HashSet<>();
        for (List<Object> row : data) {
            if (colIndex < row.size() && row.get(colIndex) != null) {
                try {
                    double val;
                    if (row.get(colIndex) instanceof Double) {
                        val = (Double) row.get(colIndex);
                    } else {
                        val = Double.parseDouble(row.get(colIndex).toString().trim().replaceAll(",", "."));
                    }
                    distinctValues.add(val);
                } catch (NumberFormatException e) {
                }
            }
        }
        return distinctValues.size() >= 2;
    }

    private static double[] calculateRegression(String dependentVarName, String[] independentVarNames) {
        int depColIndex = getVariableIndex(dependentVarName);
        if (depColIndex == -1) {
            System.err.println("Error en cálculo de regresión: Variable dependiente '" + dependentVarName + "' no encontrada.");
            return null;
        }

        if (!hasVariance(dependentVarName)) {
            System.err.println("Error en cálculo de regresión: La variable dependiente '" + dependentVarName + "' ( sus valores numéricos son iguales).");
            return null;
        }

        int[] indepColIndices = new int[independentVarNames.length];
        for (int i = 0; i < independentVarNames.length; i++) {
            indepColIndices[i] = getVariableIndex(independentVarNames[i]);
            if (indepColIndices[i] == -1) {
                System.err.println("Error en cálculo de regresión: Variable independiente '" + independentVarNames[i] + "' no encontrada.");
                return null;
            }
            if (!hasVariance(independentVarNames[i])) {
                System.err.println("Error en cálculo de regresión: La variable independiente '" + independentVarNames[i] + "' no tiene varianza (todos sus valores numéricos son iguales).");
                return null;
            }
        }

        List<double[]> designMatrixRows = new ArrayList<>();
        List<Double> yVector = new ArrayList<>();

        for (List<Object> row : data) {
            double[] xRow = new double[1 + independentVarNames.length];
            double yVal = 0;
            boolean skipRow = false;

            try {
                Object yObj = (depColIndex < row.size()) ? row.get(depColIndex) : null;
                if (yObj == null) {
                    skipRow = true;
                } else if (yObj instanceof Double) {
                    yVal = (Double) yObj;
                } else {
                    yVal = Double.parseDouble(yObj.toString().trim().replaceAll(",", "."));
                }
                if (!skipRow) {
                    yVector.add(yVal);
                }
            } catch (ClassCastException | NullPointerException | NumberFormatException e) {
                skipRow = true;
            }

            if (skipRow) {
                if (!yVector.isEmpty() && yVector.size() > designMatrixRows.size()) {
                    yVector.remove(yVector.size() - 1);
                }
                continue;
            }

            xRow[0] = 1.0;
            for (int i = 0; i < independentVarNames.length; i++) {
                try {
                    Object xObj = (indepColIndices[i] < row.size()) ? row.get(indepColIndices[i]) : null;
                    if (xObj == null) {
                        skipRow = true;
                        break;
                    } else if (xObj instanceof Double) {
                        xRow[1 + i] = (Double) xObj;
                    } else {
                        xRow[1 + i] = Double.parseDouble(xObj.toString().trim().replaceAll(",", "."));
                    }
                } catch (ClassCastException | NullPointerException | NumberFormatException e) {
                    skipRow = true;
                    break;
                }
            }

            if (!skipRow) {
                designMatrixRows.add(xRow);
            } else {
                if (!yVector.isEmpty() && yVector.size() > designMatrixRows.size()) {
                    yVector.remove(yVector.size() - 1);
                }
            }
        }

        int N = designMatrixRows.size();
        int P = 1 + independentVarNames.length;

        if (N < P) {
            System.err.println("Error: Observaciones válidas insuficientes (" + N + "). Se necesitan al menos " + P + " observaciones válidas para estimar " + P + " coeficientes.");
            return null;
        }
        if (N == 0) {
            System.err.println("Error: No se encontraron observaciones válidas para las variables seleccionadas.");
            return null;
        }

        double[][] X = new double[N][P];
        double[] Y = new double[N];
        for (int i = 0; i < N; i++) {
            X[i] = designMatrixRows.get(i);
            Y[i] = yVector.get(i);
        }

        double[][] X_T = transpose(X);

        double[][] X_T_X = multiplyMatrices(X_T, X);

        double[] X_T_Y = multiplyMatrixVector(X_T, Y);

        double[] B = solveLinearSystem(X_T_X, X_T_Y);

        if (B == null) {
            System.err.println("Error: No se pudo resolver el sistema de ecuaciones para los coeficientes.");
            return null;
        }

        double meanY = 0;
        for (double yVal : Y) {
            meanY += yVal;
        }
        meanY /= N;

        double totalSumSquares = 0;
        double residualSumSquares = 0;

        for (int i = 0; i < N; i++) {
            double actualY = Y[i];
            double predictedY = B[0];
            for (int j = 0; j < independentVarNames.length; j++) {
                predictedY += B[1 + j] * X[i][1 + j];
            }
            totalSumSquares += (actualY - meanY) * (actualY - meanY);
            residualSumSquares += (actualY - predictedY) * (actualY - predictedY);
        }

        double rSquared = (totalSumSquares == 0) ? 0 : (1 - (residualSumSquares / totalSumSquares));

        double[] results = new double[1 + P];
        results[0] = rSquared;
        System.arraycopy(B, 0, results, 1, P);

        return results;
    }

    private static double[][] transpose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] result = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    private static double[][] multiplyMatrices(double[][] matrixA, double[][] matrixB) {
        int aRows = matrixA.length;
        int aCols = matrixA[0].length;
        int bRows = matrixB.length;
        int bCols = matrixB[0].length;

        if (aCols != bRows) {
            throw new IllegalArgumentException("multiplied: A columns != B rows");
        }

        double[][] result = new double[aRows][bCols];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                for (int k = 0; k < aCols; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    private static double[] multiplyMatrixVector(double[][] matrix, double[] vector) {
        int mRows = matrix.length;
        int mCols = matrix[0].length;
        int vLen = vector.length;

        if (mCols != vLen) {
            throw new IllegalArgumentException("columns != Vector length");
        }

        double[] result = new double[mRows];
        for (int i = 0; i < mRows; i++) {
            for (int j = 0; j < mCols; j++) {
                result[i] += matrix[i][j] * vector[j];
            }
        }
        return result;
    }
    private static double[] solveLinearSystem(double[][] A, double[] b) {
        int n = A.length;
        if (n == 0 || A[0].length != n || b.length != n) {
            System.err.println("Error: Dimensiones de matriz o vector inválidas para el solucionador del sistema lineal.");
            return null;
        }

        double[][] M = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, M[i], 0, n);
            M[i][n] = b[i];
        }

        for (int k = 0; k < n; k++) {
            int pivotRow = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(M[i][k]) > Math.abs(M[pivotRow][k])) {
                    pivotRow = i;
                }
            }

            double[] temp = M[k];
            M[k] = M[pivotRow];
            M[pivotRow] = temp;

            if (Math.abs(M[k][k]) < 1e-9) {
                return null;
            }

            for (int i = k + 1; i < n; i++) {
                double factor = M[i][k] / M[k][k];
                for (int j = k; j <= n; j++) {
                    M[i][j] -= factor * M[k][j];
                }
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += M[i][j] * x[j];
            }
            if (Math.abs(M[i][i]) < 1e-9) {
                return null;
            }
            x[i] = (M[i][n] - sum) / M[i][i];
        }
        return x;
    }

    private static void performSimpleRegression() {
        if (variableNames.size() < 2 || data.size() < 2) {
            System.out.println("Para la regresión lineal simple: se necesita al menos 2 variables definidas y al menos 2 observaciones en el dataset.");
            return;
        }

        System.out.println("\n--- Regresión Lineal Simple ---");
        System.out.println("Variables disponibles:");
        for (String var : variableNames) {
            System.out.println("- " + var);
        }

        System.out.print("Ingresa el nombre de la variable DEPENDIENTE (Y): ");
        String dependentVar = scanner.nextLine();
        System.out.print("Ingresa el nombre de la variable INDEPENDIENTE (X): ");
        String independentVar = scanner.nextLine();

        if (dependentVar.equalsIgnoreCase(independentVar)) {
            System.out.println("La variable dependiente e independiente no pueden ser la misma.");
            return;
        }

        double[] results = calculateRegression(dependentVar, new String[]{independentVar});
        if (results != null && results.length == 3) {
            System.out.println("\n--- Resultados de la Regresión Lineal Simple ---");
            System.out.println("Ecuación: " + dependentVar + " = " + String.format("%.4f", results[1]) + " + " + String.format("%.4f", results[2]) + " * " + independentVar);
            System.out.println(String.format("Intercepto (b0): %.4f", results[1]));
            System.out.println(String.format("Pendiente (b1) para %s: %.4f", independentVar, results[2]));
            System.out.println(String.format("R-cuadrado (R^2): %.4f", results[0]));
            System.out.println("------------------------------------");
        } else {
            System.err.println("No se pudo realizar la regresión simple. Por favor, revisa lo siguiente:");
            System.err.println("- Asegurate de que las variables seleccionadas existan y sean numéricas.");
            System.err.println("- Verifica que haya al menos 2 observaciones validas (filas sin datos faltantes/no numericos para ambas variables).");
            System.err.println("- Confirma que la variable dependiente y la independiente tengan varianza (no todos sus valores sean idénticos).");
        }
    }

    private static void performMultipleRegression() {
        if (variableNames.size() < 3 || data.size() < 3) {
            System.out.println("Para la regresión lineal múltiple, necesitas al menos 3 variables definidas y al menos 3 observaciones en el dataset.");
            return;
        }

        System.out.println("\n--- Regresión Lineal Múltiple ---");
        System.out.println("Variables disponibles:");
        for (String var : variableNames) {
            System.out.println("- " + var);
        }

        System.out.print("Ingresa el nombre de la variable DEPENDIENTE (Y): ");
        String dependentVar = scanner.nextLine();

        List<String> independentVarsList = new ArrayList<>();
        System.out.println("Ingresa los nombres de las variables INDEPENDIENTES (X1, X2, ...), una por una. Escribe 'fin' para terminar.");
        String indepVarName;
        while (true) {
            System.out.print("Nombre de variable independiente (o 'fin'): ");
            indepVarName = scanner.nextLine();
            if (indepVarName.equalsIgnoreCase("fin")) {
                break;
            }
            if (independentVarsList.contains(indepVarName)) {
                System.out.println("Esa variable ya fue añadida. Elige otra.");
            } else if (indepVarName.equalsIgnoreCase(dependentVar)) {
                System.out.println("Una variable independiente no puede ser igual a la dependiente.");
            }
            else {
                independentVarsList.add(indepVarName);
            }
        }

        if (independentVarsList.isEmpty()) {
            System.out.println("No se especificaron variables independientes.");
            return;
        }

        String[] independentVarsArray = independentVarsList.toArray(new String[0]);

        double[] results = calculateRegression(dependentVar, independentVarsArray);

        if (results != null) {
            System.out.println("\n--- Resultados de la Regresión Lineal Múltiple ---");
            StringBuilder equation = new StringBuilder();
            equation.append(dependentVar).append(" = ");
            equation.append(String.format("%.4f", results[1]));

            System.out.println(String.format("Intercepto (b0): %.4f", results[1]));
            for (int i = 0; i < independentVarsArray.length; i++) {
                double coef = results[2 + i];
                System.out.println(String.format("Coeficiente para %s: %.4f", independentVarsArray[i], coef));
                equation.append((coef >= 0 ? " + " : " - ")).append(String.format("%.4f", Math.abs(coef))).append(" * ").append(independentVarsArray[i]);
            }
            System.out.println("Ecuación: " + equation.toString());
            System.out.println(String.format("R-cuadrado (R^2): %.4f", results[0]));
            System.out.println("------------------------------------");
        } else {
            System.err.println("No se pudo realizar la regresión múltiple. Por favor, revisa lo siguiente:");
            System.err.println("- Asegúrate de que todas las variables seleccionadas existan y sean numéricas.");
            System.err.println("- Verifica que haya suficientes observaciones válidas para el número de predictores (al menos [número de predictores + 1]).");
            System.err.println("- Confirma que ninguna variable seleccionada tenga varianza cero o que no haya multicolinealidad severa (predictores altamente correlacionados).");
        }
    }


    private static void performMediationAnalysis() {
        if (variableNames.size() < 3 || data.size() < 3) {
            System.out.println("Para el análisis de mediación, necesitas al menos 3 variables definidas (X, M, Y) y al menos 3 observaciones en el dataset.");
            return;
        }

        System.out.println("\n--- Análisis de Mediación (Cálculos de Caminos) ---");
        System.out.println("Variables disponibles:");
        for (String var : variableNames) {
            System.out.println("- " + var);
        }

        System.out.print("Ingresa la variable INDEPENDIENTE (X): ");
        String xVar = scanner.nextLine();
        System.out.print("Ingresa la variable MEDIADORA (M): ");
        String mVar = scanner.nextLine();
        System.out.print("Ingresa la variable DEPENDIENTE (Y): ");
        String yVar = scanner.nextLine();

        if (getVariableIndex(xVar) == -1 || getVariableIndex(mVar) == -1 || getVariableIndex(yVar) == -1) {
            System.err.println("Error: Asegúrate de que las variables X, M, e Y existan en tu dataset.");
            return;
        }
        if (xVar.equalsIgnoreCase(mVar) || xVar.equalsIgnoreCase(yVar) || mVar.equalsIgnoreCase(yVar)) {
            System.err.println("Las variables X, M e Y deben ser distintas.");
            return;
        }

        System.out.println("\nCalculando caminos de mediación...");

        System.out.println("\n--- Camino 'a': " + mVar + " = b0a + a * " + xVar + " ---");
        double[] aPathResults = calculateRegression(mVar, new String[]{xVar});
        double coef_a = Double.NaN;
        if (aPathResults != null && aPathResults.length > 2) {
            coef_a = aPathResults[2];
            System.out.println(String.format("Coeficiente 'a' (X -> M): %.4f", coef_a));
            System.out.println(String.format("R^2 del camino 'a': %.4f", aPathResults[0]));
        } else {
            System.err.println("No se pudo calcular el Camino 'a'. Revisa tus datos. (Necesita al menos 2 observaciones válidas y varianza para " + xVar + " y " + mVar + ")");
        }

        System.out.println("\n--- Camino 'c' (Efecto Total): " + yVar + " = b0c + c * " + xVar + " ---");
        double[] cPathResults = calculateRegression(yVar, new String[]{xVar});
        double coef_c = Double.NaN;
        if (cPathResults != null && cPathResults.length > 2) {
            coef_c = cPathResults[2];
            System.out.println(String.format("Coeficiente 'c' (X -> Y - Efecto Total): %.4f", coef_c));
            System.out.println(String.format("R^2 del camino 'c': %.4f", cPathResults[0]));
        } else {
            System.err.println("No se pudo calcular el Camino 'c'. Revisa tus datos. (Necesita al menos 2 observaciones válidas y varianza para " + xVar + " y " + yVar + ")");
        }

        System.out.println("\n--- Camino 'b' y 'c'' (Efecto Directo): " + yVar + " = b0 + b * " + mVar + " + c' * " + xVar + " ---");
        double[] bcPrimePathResults = calculateRegression(yVar, new String[]{mVar, xVar});

        double coef_b = Double.NaN;
        double coef_c_prime = Double.NaN;

        if (bcPrimePathResults != null && bcPrimePathResults.length >= 4) {
            coef_b = bcPrimePathResults[2];
            coef_c_prime = bcPrimePathResults[3];
            System.out.println(String.format("Coeficiente 'b' (M -> Y, controlando por X): %.4f", coef_b));
            System.out.println(String.format("Coeficiente 'c'' (X -> Y, controlando por M - Efecto Directo): %.4f", coef_c_prime));
            System.out.println(String.format("R^2 del camino 'b' y 'c'': %.4f", bcPrimePathResults[0]));
        } else {
            System.err.println("No se pudo calcular el Camino 'b' y 'c'' (regresión múltiple). Revisa tus datos. (Necesita al menos 3 observaciones válidas y varianza para " + xVar + ", " + mVar + " y " + yVar + ")");
        }

        System.out.println("\n--- Resumen del Análisis de Mediación ---");
        double indirectEffect = coef_a * coef_b;
        System.out.println(String.format("Efecto Indirecto (a * b): %.4f", indirectEffect));

        System.out.println(String.format("Suma de Efecto Directo + Indirecto: %.4f", coef_c_prime + indirectEffect));
        System.out.println(String.format("Efecto Total Original (c): %.4f", coef_c));

        System.out.println("\nNota: Este análisis de mediación solo muestra los coeficientes básicos.");
    }

    private static void performModerationAnalysis() {
        if (variableNames.size() < 3 || data.size() < 4) {
            System.out.println("Para el análisis de moderación, necesitas al menos 3 variables definidas (X, W, Y) y al menos 4 observaciones en el dataset.");
            return;
        }

        System.out.println("\n--- Análisis de Moderación ---");
        System.out.println("Variables disponibles:");
        for (String var : variableNames) {
            System.out.println("- " + var);
        }

        System.out.print("Ingresa la variable PREDICTOR (X): ");
        String xVar = scanner.nextLine();
        System.out.print("Ingresa la variable MODERADORA (W): ");
        String wVar = scanner.nextLine();
        System.out.print("Ingresa la variable DEPENDIENTE (Y): ");
        String yVar = scanner.nextLine();

        if (getVariableIndex(xVar) == -1 || getVariableIndex(wVar) == -1 || getVariableIndex(yVar) == -1) {
            System.err.println("Error: Asegúrate de que las variables X, W, e Y existan en tu dataset.");
            return;
        }
        if (xVar.equalsIgnoreCase(wVar) || xVar.equalsIgnoreCase(yVar) || wVar.equalsIgnoreCase(yVar)) {
            System.err.println("Las variables X, W e Y deben ser distintas.");
            return;
        }

        String interactionVarName = xVar + "_x_" + wVar;
        int interactionColIndex = getVariableIndex(interactionVarName);
        boolean interactionVarAdded = false;

        if (interactionColIndex == -1) {
            variableNames.add(interactionVarName);
            interactionColIndex = variableNames.size() - 1;
            interactionVarAdded = true;
        } else {
            System.out.println("La variable de interacción '" + interactionVarName + "' ya existe en el dataset. Recalculando sus valores.");
        }

        int xColIndex = getVariableIndex(xVar);
        int wColIndex = getVariableIndex(wVar);

        for (List<Object> row : data) {
            while (row.size() <= interactionColIndex) {
                row.add(null);
            }
            try {
                Object xObj = (xColIndex < row.size()) ? row.get(xColIndex) : null;
                Object wObj = (wColIndex < row.size()) ? row.get(wColIndex) : null;

                if (xObj == null || wObj == null) {
                    row.set(interactionColIndex, null);
                } else {
                    double xVal = (xObj instanceof Double) ? (Double) xObj : Double.parseDouble(xObj.toString().trim().replaceAll(",", "."));
                    double wVal = (wObj instanceof Double) ? (Double) wObj : Double.parseDouble(wObj.toString().trim().replaceAll(",", "."));
                    row.set(interactionColIndex, xVal * wVal);
                }
            } catch (IndexOutOfBoundsException | ClassCastException | NullPointerException | NumberFormatException e) {
                row.set(interactionColIndex, null);
            }
        }

        if (!hasVariance(interactionVarName)) {
            System.err.println("Error: La variable de interacción '" + interactionVarName + "' no tiene varianza. Esto puede ocurrir si 'X' o 'W' no tienen varianza, o si uno de ellos es cero en todas las observaciones válidas.");
            if (interactionVarAdded) {
                variableNames.remove(interactionColIndex);
                for (List<Object> row : data) {
                    if (interactionColIndex < row.size()) {
                        row.remove(interactionColIndex);
                    }
                }
            }
            return;
        }


        System.out.println("\n--- Regresión: " + yVar + " = b0 + b1*" + xVar + " + b2*" + wVar + " + b3*(" + interactionVarName + ") ---");

        String[] moderationPredictors = {xVar, wVar, interactionVarName};
        double[] moderationResults = calculateRegression(yVar, moderationPredictors);

        if (interactionVarAdded) {
            variableNames.remove(interactionColIndex);
            for (List<Object> row : data) {
                if (interactionColIndex < row.size()) {
                    row.remove(interactionColIndex);
                }
            }
        }

        if (moderationResults != null && moderationResults.length >= 5) {
            System.out.println("\n--- Resultados del Análisis de Moderación ---");
            System.out.println(String.format("Intercepto (b0): %.4f", moderationResults[1]));
            System.out.println(String.format("Coeficiente de " + xVar + " (b1): %.4f", moderationResults[2]));
            System.out.println(String.format("Coeficiente de " + wVar + " (b2): %.4f", moderationResults[3]));
            System.out.println(String.format("Coeficiente de Interacción (" + interactionVarName + ") (b3): %.4f", moderationResults[4]));
            System.out.println(String.format("R^2 del Modelo de Moderación: %.4f", moderationResults[0]));

            if (Math.abs(moderationResults[4]) > 1e-4) {
                System.out.println("\nInterpretación: El coeficiente del término de interacción (b3) es distinto de cero, lo que sugiere un efecto moderador.");
                System.out.println("El efecto de " + xVar + " sobre " + yVar + " depende del nivel de " + wVar + ".");
            } else {
                System.out.println("\nInterpretación: El coeficiente del término de interacción (b3) es cercano a cero, lo que sugiere que no hay un efecto moderador significativo.");
                System.out.println("El efecto de " + xVar + " sobre " + yVar + " no parece depender del nivel de " + wVar + ".");
            }

            System.out.println("\nNota: Este análisis de moderación solo muestra los coeficientes básicos.");

        } else {
            System.err.println("No se pudo realizar el análisis de moderación. Por favor, revisa lo siguiente:");
            System.err.println("- Asegúrate de que las variables X, W, Y existan y sean numéricas en las observaciones válidas.");
            System.err.println("- Verifica que haya al menos 4 observaciones válidas para todos los predictores (X, W, y la variable de interacción X*W).");
            System.err.println("- Confirma que ninguna de las variables seleccionadas o la variable de interacción tenga varianza cero.");
            System.err.println("- Comprueba que no haya multicolinealidad severa (ej., X y W son linealmente dependientes).");
        }
        System.out.println("------------------------------------");
    }
}