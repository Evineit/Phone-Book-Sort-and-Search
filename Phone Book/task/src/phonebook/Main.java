package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
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
        System.out.println("Start searching (linear search)...");

        Duration linearSearchDuration = new Duration();
        linearSearchDuration.start();
        for (String person : toFinds) {
                if (linearSearch(directory,person)!=-1) {
                    counter++;
                }
        }
        linearSearchDuration.stop();
        System.out.printf("Found %d / 500 entries. Time taken: %s%n",counter,linearSearchDuration);
        counter=0;
        System.out.println("Start searching (bubble sort + jump search)...");

        Duration bubbleSortDuration = new Duration();
        if (bubbleSort(directory,bubbleSortDuration, linearSearchDuration.getMilliseconds()*10)){
            Duration jumpSearchDuration = new Duration();
            jumpSearchDuration.start();
            for (String person : toFinds) {
                if (jumpSearch(directory,person)!=-1) {
                    counter++;
                }
            }
            jumpSearchDuration.stop();
            System.out.printf("Found %d / 500 entries. Time taken: %s%n",counter,
                    new Duration(bubbleSortDuration.getMilliseconds()+jumpSearchDuration.getMilliseconds()));
            System.out.printf("Sorting time: %s%n",bubbleSortDuration);
            System.out.printf("Searching time: %s%n",jumpSearchDuration);
        }else {
            Duration linearSearchDuration2 = new Duration();
            linearSearchDuration2.start();
            for (String person : toFinds) {
                if (linearSearch(directory,person)!=-1) {
                    counter++;
                }
            }
            linearSearchDuration2.stop();
            System.out.printf("Found %d / 500 entries. Time taken: %s%n",counter,
                    new Duration(bubbleSortDuration.getMilliseconds()+linearSearchDuration2.getMilliseconds()));
            System.out.printf("Sorting time: %s - STOPPED, moved to linear search %n",bubbleSortDuration);
            System.out.printf("Searching time: %s%n",linearSearchDuration2);
        }
    }

    public static int jumpSearch(List<String> array, String target) {
        int currentRight = 0; // right border of the current block
        int prevRight = 0; // right border of the previous block

        /* If array is empty, the element is not found */
        if (array.size() == 0) {
            return -1;
        }

        /* Check the first element */
        if (array.get(currentRight).equals(target)) {
            return 0;
        }

        /* Calculating the jump length over array elements */
        int jumpLength = (int) Math.sqrt(array.size());

        /* Finding a block where the element may be present */
        while (currentRight < array.size() - 1) {

            /* Calculating the right border of the following block */
            currentRight = Math.min(array.size() - 1, currentRight + jumpLength);

            if (array.get(currentRight).charAt(9) >= target.charAt(0)) {
                break; // Found a block that may contain the target element
            }

            prevRight = currentRight; // update the previous right block border
        }

        /* If the last block is reached and it cannot contain the target value => not found */
        if ((currentRight == array.size() - 1) && target.charAt(0) > array.get(currentRight).charAt(9)) {
            return -1;
        }

        /* Doing linear search in the found block */
        return backwardSearch(array, target, prevRight, currentRight);
    }

    public static int backwardSearch(List<String> array, String target, int leftExcl, int rightIncl) {
        for (int i = rightIncl; i > leftExcl; i--) {
            if (array.get(i).contains(target)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean bubbleSort(List<String> list, Duration duration, long maxTime) throws RuntimeException {
        duration.start();
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i -1; j++) {
                if (list.get(j).charAt(9) > list.get(j + 1).charAt(9)){
                    String temp = list.get(j);
                    list.set(j,list.get(j+1));
                    list.set(j+1,temp);
                }
            }
            if (duration.getMillisSinceStart() > maxTime){
                duration.stop();
                return false;
            }
        }
        duration.stop();
        return true;
    }
    public static int linearSearch(List<String> arrayList,
                                   String string) {
        int index = -1;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).contains(string)) {
                index = i;
            }
        }
        return index;
    }
}
