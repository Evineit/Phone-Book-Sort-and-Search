package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String pathToDirectory = "C:\\Users\\kevin\\random\\directory.txt";
        String pathToFind = "C:\\Users\\kevin\\random\\find.txt";
        File fileDirectory = new File(pathToDirectory);
        File fileFind = new File(pathToFind);
        List<String> directory = new ArrayList<>();
        List<String> toFinds = new ArrayList<>();
        try (Scanner scanner = new Scanner(fileDirectory);
             Scanner scanner1 = new Scanner(fileFind)) {
            while (scanner.hasNext()) {
                directory.add(scanner.nextLine());
            }
            while (scanner1.hasNext()) {
                toFinds.add(scanner1.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found");
        }
        int counter = 0;
        System.out.println("Start searching...");
        long start = System.currentTimeMillis();
        for (String person : toFinds) {
            for (String s : directory) {
                String[] dir = s.split(" ", 2);
                if (dir[1].equals(person)) {
                    counter++;
                    break;
                }
            }
        }
        long finish = System.currentTimeMillis();
        System.out.println("Found " + counter + " / 500 entries. Time taken: " + miliProcess(finish - start));
    }

    /**
     * Gives a minutes, seconds and milliseconds format to the passed milliseconds
     * @param i milliseconds to be processed
     * @return String of minutes, seconds and milliseconds.
     */
    public static String miliProcess(long i) {
        long sec = i / 1000;
        long mili = sec % 1000;
        long min = sec / 60;
        sec = sec % 60;
        return String.format("%s min. %s sec. %s ms.", min, sec, mili);
    }

    public static int linearSearch(ArrayList<String> arrayList,
                                   String string) {
        int index = -1;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals(string)) {
                index = i;
            }
        }
        return index;
    }
}
