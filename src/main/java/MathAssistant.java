import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MathAssistant {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static void main(String[] args) {
        saveAndSearchEquations();
    }

    private static void saveAndSearchEquations() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            clearEquationsTable(connection);
            createEquationTable(connection);

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("\nEnter a mathematical equation (or 'exit' to exit): ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }

                userInput = userInput.replace(",", ".");

                if (MathAssistant.isValidEquation(userInput)) {
                    saveEquationToDatabase(userInput, connection);
                    checkAndSaveRoot(userInput, connection);

                    System.out.println("\nChoose search option:");
                    System.out.println("1. Search by specific root");
                    System.out.println("2. Search by number of roots");
                    System.out.print("Enter your choice (1 or 2): ");

                    int choice;
                    try {
                        choice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                        continue;
                    }

                    switch (choice) {
                        case 1:
                            System.out.print("\nEnter a root to search for equations: ");
                            double searchRoot;
                            try {
                                searchRoot = Double.parseDouble(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid root. Please enter a valid number.");
                                continue;
                            }
                            searchEquationsByRoot(searchRoot, connection);
                            break;
                        case 2:
                            System.out.print("Enter the number of roots to search for equations: ");
                            int numberOfRoots;
                            try {
                                numberOfRoots = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number of roots. Please enter a valid number.");
                                continue;
                            }
                            searchEquationsByNumberOfRoots(numberOfRoots, connection);
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter 1 or 2.");
                            break;
                    }
                } else {
                    System.out.println("Invalid equation. Please check your input.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void clearEquationsTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM equations";
            statement.executeUpdate(sql);
            System.out.println("Equations table has been cleared.");
        }
    }

    private static void createEquationTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS equations (equation TEXT, solution DOUBLE PRECISION[])";
            statement.execute(sql);
        }
    }

    public static boolean isValidEquation(String equation) {
        if (equation.replace(" ", "").contains("/0")) {
            System.out.println("Error: Division by zero.");
        }

        String[] parts = equation.split("=");
        if (parts.length != 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            return false;
        }

        char prevChar = '\0';
        boolean hasVariable = false;
        boolean hasEqualSign = false;

        for (char c : equation.toCharArray()) {
            if (c == '=') {
                if (hasEqualSign) {
                    return false;
                }
                hasEqualSign = true;
            } else if (isOperator(c)) {
                if (isOperator(prevChar) && c != '-') {
                    return false;
                }
            } else if (c == 'x') {
                hasVariable = true;
            }

            prevChar = c;
        }

        return hasVariable && hasEqualSign && isBracketPlacementCorrect(equation);
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '=';
    }

    private static boolean isBracketPlacementCorrect(String equation) {
        int bracketCount = 0;
        for (char c : equation.toCharArray()) {
            if (c == '(') {
                bracketCount++;
            } else if (c == ')') {
                bracketCount--;
                if (bracketCount < 0) {
                    return false;
                }
            }
        }

        return bracketCount == 0;
    }

    private static void saveEquationToDatabase(String equation, Connection connection) throws SQLException {
        if (!equationExists(equation, connection)) {
            try (PreparedStatement preparedStatement = connection.
                    prepareStatement("INSERT INTO equations (equation) VALUES (?)")) {
                preparedStatement.setString(1, equation);
                preparedStatement.executeUpdate();
            }
            System.out.println("Equation saved to the database.");
        } else {
            System.out.println("Equation already exists in the database.");
        }
    }

    private static boolean equationExists(String equation, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT COUNT(*) FROM equations WHERE equation = ?")) {
            preparedStatement.setString(1, equation);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        }
    }

    private static void checkAndSaveRoot(String equation, Connection connection) throws SQLException {
        System.out.print("\nEnter a root for the equation: ");
        double userInputRoot;

        String userInputString = new Scanner(System.in).nextLine();
        userInputString = userInputString.replace(",", ".");
        userInputRoot = Double.parseDouble(userInputString);

        if (isRootOfEquation(equation, userInputRoot)) {
            saveRootToDatabase(equation, userInputRoot, connection);
            System.out.println(userInputRoot + " is a root of the equation. Root saved to the database.");
        } else {
            System.out.println(userInputRoot + " is not a root of the equation.");
        }
    }

    public static boolean isRootOfEquation(String equation, double root) {
        String substitutedEquation = equation.replace("x", Double.toString(root));
        return solveEquation(substitutedEquation);
    }

    private static boolean solveEquation(String equation) {
        String[] sides = equation.split("=");

        if (sides.length != 2) {
            throw new IllegalArgumentException("Invalid equation format: " + equation);
        }

        String leftSide = sides[0].trim();
        String rightSide = sides[1].trim();

        double leftResult = evaluateExpression(leftSide);
        double rightResult = evaluateExpression(rightSide);

        return Math.abs(leftResult - rightResult) <= 1E-9;
    }

    private static double evaluateExpression(String expression) {
        Expression exp = new ExpressionBuilder(expression).build();
        return exp.evaluate();
    }

    private static void saveRootToDatabase(String equation, double root, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("UPDATE equations SET solution = array_append(solution, ?) WHERE equation = ?")) {
            preparedStatement.setDouble(1, root);
            preparedStatement.setString(2, equation);
            preparedStatement.executeUpdate();
        }
    }

    private static void searchEquationsByRoot(double searchRoot, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.
                prepareStatement("SELECT equation FROM equations WHERE ? = ANY(solution)")) {
            preparedStatement.setDouble(1, searchRoot);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("\nEquations with root " + searchRoot + ":");
            while (resultSet.next()) {
                String equation = resultSet.getString("equation");
                System.out.println(equation);
            }
        }
    }

    private static void searchEquationsByNumberOfRoots(int numberOfRoots, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM equations WHERE array_length(solution, 1) = ?")) {
            preparedStatement.setInt(1, numberOfRoots);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("\nEquation(s) with " + numberOfRoots + " root(s):");
            while (resultSet.next()) {
                String equation = resultSet.getString("equation");
                Array rootArray = resultSet.getArray("solution");
                Double[] rootValues = (Double[]) rootArray.getArray();

                if (rootValues.length == numberOfRoots) {
                    List<Double> roots = Arrays.asList(rootValues);
                    System.out.println("Equation: " + equation + ", Roots: " + roots);
                }
            }
        }
    }
}