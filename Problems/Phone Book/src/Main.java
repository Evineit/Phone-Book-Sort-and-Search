import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    private static class TableEntry<T> {
        private final int key;
        private final T value;
        private boolean removed;

        public TableEntry(int key, T value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }

        public void remove() {
             removed = true;   
        }

        public boolean isRemoved() {
             return removed;   
        }
    }

    private static class HashTable<T> {
        private int size;
        private TableEntry[] table;

        public HashTable(int size) {
            this.size = size;
            table = new TableEntry[size];
        }

        public boolean put(int key, T value) {
            int idx = findKey(key);
            if (idx == -1) {
                return false;
            }
            table[idx] = new TableEntry(key, value);
            table[idx].removed = false;
            return true;
        }

        public void get(int key) {
            int idx = findKey(key);
            if (table[idx] == null || table[idx].isRemoved()) {
                System.out.println(-1);
            }else {
                System.out.println(table[idx].getValue());
            }
        }

        public void remove(int key) {
            table[findKey(key)] = null;

        }

        private int findKey(int key) {
            int hash = key % size;

            while (!(table[hash] == null || table[hash].getKey() == key)) {
                hash = (hash + 1) % size;

                if (hash == key % size) {
                    return -1;
                }
            }

            return hash;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final int m = Integer.parseInt(scanner.nextLine().strip());
        HashTable<String> table = new HashTable<>(m);

        for (int i = 0; i < m; i++) {
            final String s = scanner.nextLine();
            final String[] s1 = s.split(" ");
            final char c = s1[0].charAt(0);
            if (c =='p'){
                table.put(Integer.parseInt(s1[1]),s1[2]);
            }else if (c=='g'){
                table.get(Integer.parseInt(s1[1]));
            }else{
                table.remove(Integer.parseInt(s1[1]));
            }
        }
    }
}