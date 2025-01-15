import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        HogwartsApp app = new HogwartsApp();
        Scanner input = new Scanner(System.in);

        System.out.println("Running HogwartsApp...");

        // Hardcoded file path and type
        String basePath = System.getProperty("user.dir") + "/src/";
        String filePath = basePath + "punkte.csv"; // Change this to process a different file type
        String outputPath = basePath + "ergebnis.txt";

        // Load data based on the hardcoded file type
        List<Entry> entries = app.leseCSV(filePath); // Change `leseCSV` to the corresponding method for the file type
        System.out.println("Processing file: " + filePath);

        // Perform operations
        System.out.println("Enter a character to filter student names (e.g., 'H'):");
        char g = input.next().charAt(0);
        System.out.println("Students with names starting with '" + g + "':");
        app.studierendeGrossbuchstaben(g, entries);

        System.out.println("\nGryffindor students:");
        app.griffindorStudierende(entries);

        // Write results to file
        app.schreibeDatei(outputPath, entries);
        System.out.println("\nResults written to: " + outputPath);
    }
}
