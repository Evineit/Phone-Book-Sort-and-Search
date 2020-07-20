package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String pathToDirectory = "C:\\Users\\kevin\\random\\directory.txt";
        String pathToFind = "C:\\Users\\kevin\\random\\find.txt";
        File fileDirectory = new File(pathToDirectory);
        File fileFind = new File(pathToFind);

        try (Scanner scanner = new Scanner(fileDirectory);
        Scanner scanner1 = new Scanner(fileFind)) {
            while (scanner.hasNext()) {
                System.out.print(scanner.nextLine() + " ");
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + pathToDirectory);
        }
    }
}
