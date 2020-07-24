package phonebook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> directory = new ArrayList<>();
        List<String> dirCopy1 = new ArrayList<>();
        List<String> dirCopy2 = new ArrayList<>();
        List<String> toFinds = new ArrayList<>();
        Duration bubbleSortDuration = new Duration();
        Duration quickSortDuration = new Duration();
        Duration jumpSearchDuration = new Duration();
        Duration linearSearchDuration = new Duration();
        Duration binarySearchDuration = new Duration();

        int counter;

        try {
            directory = Files.readAllLines(Path.of("C:\\Users\\kevin\\random\\directory.txt"));
            toFinds = Files.readAllLines(Path.of("C:\\Users\\kevin\\random\\find.txt"));
            dirCopy1 = new ArrayList<>(directory);
            dirCopy2 = new ArrayList<>(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        counter = 0;
        System.out.println("\nStart searching (linear search)...");
        linearSearchDuration.start();
        for (String person : toFinds) {
            if (linearSearch(directory, person) != -1) {
                counter++;
            }
        }
        linearSearchDuration.stop();
        System.out.printf("Found %d / 500 entries. Time taken: %s%n", counter, linearSearchDuration);

        counter = 0;
        System.out.println("\nStart searching (bubble sort + jump search)...");

        if (bubbleSort(dirCopy1, bubbleSortDuration, linearSearchDuration.getMilliseconds() * 10)) {
            jumpSearchDuration.start();
            for (String person : toFinds) {
                if (jumpSearch(dirCopy1, person) != -1) {
                    counter++;
                }
            }
            jumpSearchDuration.stop();
            System.out.printf("Found %d / 500 entries. Time taken: %s%n", counter,
                    new Duration(bubbleSortDuration.getMilliseconds() + jumpSearchDuration.getMilliseconds()));
            System.out.printf("Sorting time: %s%n", bubbleSortDuration);
        } else {
            jumpSearchDuration.start();
            for (String person : toFinds) {
                if (linearSearch(dirCopy1, person) != -1) {
                    counter++;
                }
            }
            jumpSearchDuration.stop();
            System.out.printf("Found %d / 500 entries. Time taken: %s%n", counter,
                    new Duration(bubbleSortDuration.getMilliseconds() + jumpSearchDuration.getMilliseconds()));
            System.out.printf("Sorting time: %s - STOPPED, moved to linear search %n", bubbleSortDuration);
        }
        System.out.printf("Searching time: %s%n", jumpSearchDuration);

        counter = 0;
        System.out.println("\nStart searching (quick sort + binary search)...");
        quickSortDuration.start();
        quickSort(dirCopy2, 0, dirCopy2.size() - 1);
        quickSortDuration.stop();

        if (!isSorted(dirCopy2)) throw new AssertionError();

        binarySearchDuration.start();
        for (String person : toFinds) {
            if (binarySearch(dirCopy2, person, 0, dirCopy2.size() - 1) != -1) {
                counter++;
            }
        }
        binarySearchDuration.stop();
        System.out.printf("Found %d / 500 entries. Time taken: %s%n", counter,
                new Duration(quickSortDuration.getMilliseconds() + binarySearchDuration.getMilliseconds()));
        System.out.printf("Sorting time: %s%n", quickSortDuration);
        System.out.printf("Searching time: %s%n", binarySearchDuration);


    }

    private static int partition(List<String> a, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        String v = a.get(lo);
        while (true) {
            // find item on lo to swap
            while (less(a.get(++i), v))
                if (i == hi) break;

            // find item on hi to swap
            while (less(v, a.get(--j)))
                if (j == lo) break;      // redundant since a[lo] acts as sentinel

            // check if pointers cross
            if (i >= j) break;

            swap(a, i, j);
        }

        // put v = a[j] into position
        swap(a, lo, j);

        // with a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;
    }

    private static void swap(List<String> array, int i, int j) {
        String temp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, temp);
    }

    private static int median3(List<String> a, int i, int j, int k) {
        return (less(a.get(i), a.get(j)) ?
                (less(a.get(j), a.get(k)) ? j : less(a.get(i), a.get(k)) ? k : i) :
                (less(a.get(k), a.get(j)) ? j : less(a.get(k), a.get(i)) ? k : i));
    }

    private static boolean less(String v, String w) {
        return splitName(v).compareTo(splitName(w)) < 0;
    }

    private static String splitName(String s) {
        return s.split(" ", 2)[1];
    }

    // quicksort the subarray from a[lo] to a[hi]
    private static void quickSort(List<String> a, int lo, int hi) {
        // cutoff to insertion quickSort
        int n = hi - lo + 1;
        if (n <= 8) {
            insertionSort(a, lo, hi);
            // show(a, lo, -1, -1, hi);
            return;
        }

        int m = median3(a, lo, lo + n / 2, hi);
        swap(a, m, lo);

        int j = partition(a, lo, hi);
        quickSort(a, lo, j - 1);
        quickSort(a, j + 1, hi);
    }
    // partition the subarray a[lo .. hi] by returning an index j
    // so that a[lo .. j-1] <= a[j] <= a[j+1 .. hi]

    // quickSort from a[lo] to a[hi] using insertion quickSort
    private static void insertionSort(List<String> a, int lo, int hi) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a.get(j), a.get(j - 1)); j--)
                swap(a, j, j - 1);
    }

    static boolean isSorted(List<String> array) {
        for (int i = 0; i < array.size() - 1; ++i) {
            if (splitName(array.get(i)).compareTo(splitName(array.get(i + 1))) > 0)
                return false;
        }
        return true;
    }


    public static int binarySearch(List<String> array, String elem, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2; // the index of the middle element
            int res = elem.compareTo(splitName(array.get(mid)));
            if (res == 0) {
                return mid; // the element is found, return its index
            } else if (res > 0) {
                left = mid + 1; // go to the right subarray
            } else {
                right = mid - 1; // go to the left subarray
            }
        }
        return -1; // the element is not found
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

    /**
     * @param list     List to be sorted
     * @param duration Duration object for timestamps
     * @param maxTime  Maximum time taken
     * @return Returns whether it finishes sorting
     */
    public static boolean bubbleSort(List<String> list, Duration duration, long maxTime) {
        duration.start();
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).charAt(9) > list.get(j + 1).charAt(9)) {
                    String temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
            if (duration.getMillisSinceStart() > maxTime) {
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
