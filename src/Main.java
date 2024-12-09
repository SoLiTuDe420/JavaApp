import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("File Conversion Tool");
            System.out.println("=====================");
            System.out.println("1. Convert CSV to SQL");
            System.out.println("2. Convert YAML to SQL");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    convertCsvToSql();
                    break;
                case 2:
                    convertYamlToSql();
                    break;
                case 3:
                    System.out.println("Exiting the program. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to handle CSV to SQL conversion
    private static void convertCsvToSql() {
        File file = promptForFile("csv");
        if (file == null) return;

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            StringBuilder sql = new StringBuilder();

            if (!lines.isEmpty()) {
                String[] headers = lines.get(0).split(",");
                sql.append("CREATE TABLE your_table_name (");
                for (String header : headers) {
                    sql.append(header.trim()).append(" TEXT, ");
                }
                sql.setLength(sql.length() - 2); // Remove the last comma and space
                sql.append(");\n");

                for (int i = 1; i < lines.size(); i++) {
                    String[] values = lines.get(i).split(",");
                    sql.append("INSERT INTO your_table_name VALUES (");
                    for (String value : values) {
                        sql.append("'").append(value.trim().replace("'", "''")).append("', ");
                    }
                    sql.setLength(sql.length() - 2); // Remove the last comma and space
                    sql.append(");\n");
                }
            }

            System.out.println("SQL Output:\n" + sql);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle YAML to SQL conversion
    private static void convertYamlToSql() {
        File file = promptForFile("yaml");
        if (file == null) return;

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            StringBuilder sql = new StringBuilder("CREATE TABLE your_table_name (key TEXT, value TEXT);\n");

            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    sql.append("INSERT INTO your_table_name VALUES ('")
                            .append(key.replace("'", "''"))
                            .append("', '")
                            .append(value.replace("'", "''"))
                            .append("');\n");
                }
            }

            System.out.println("SQL Output:\n" + sql);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Utility method to prompt the user for a file
    private static File promptForFile(String expectedExtension) {
        Scanner scanner = new Scanner(System.in);
        File file;
        while (true) {
            System.out.print("Enter the path to the " + expectedExtension.toUpperCase() + " file: ");
            String filePath = scanner.nextLine();
            file = new File(filePath);

            if (!file.exists()) {
                System.out.println("File not found. Please try again.");
            } else if (expectedExtension.equals("yaml") &&
                    (filePath.toLowerCase().endsWith(".yaml") || filePath.toLowerCase().endsWith(".yml"))) {
                return file; // Valid YAML file
            } else if (filePath.toLowerCase().endsWith("." + expectedExtension)) {
                return file; // Valid CSV or other file
            } else {
                System.out.println("Invalid file type. Please provide a valid " + expectedExtension.toUpperCase() + " file.");
            }
        }
    }
}
