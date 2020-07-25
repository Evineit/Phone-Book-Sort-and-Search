import java.util.*;

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
                return put(key, value);
            }
            table[idx] = new TableEntry(key, value);
            table[idx].removed = false;
            return true;
        }

        public T get(int key) {
            int idx = findKey(key);

            if (idx == -1 || table[idx] == null) {
                return null;
            }

            return (T) table[idx];
        }

        public void remove(int key) {
            Arrays.stream(table).filter(Objects::nonNull).filter(tableEntry -> tableEntry.key == key).forEach(TableEntry::remove);
        }

        private int findKey(int key) {
            int hash = key % size;

            while (!(table[hash] == null || table[hash].getKey() == key)) {
                hash = rehash(hash);

                if (hash == key % size) {
                    resize();
                    return -1;
                }
            }

            return hash;
        }

        private void resize() {
            int tableSize = 2 * size;
            this.size = tableSize;
            TableEntry[] oldTable = table;
            table = new TableEntry[tableSize];
            for (int i = 0; i < oldTable.length; i++)
                if (oldTable[i] != null)
                    put(oldTable[i].getKey(), (T) oldTable[i].getValue());

        }

        private int rehash(int hash) {
            return (hash + 1) % size;
        }

        @Override
        public String toString() {
            StringBuilder tableStringBuilder = new StringBuilder();

            for (int i = 0; i < table.length; i++) {
                if (table[i] == null) {
                    tableStringBuilder.append(i + ": null");
                } else {
                    tableStringBuilder.append(i + ": key=" + table[i].getKey()
                            + ", value=" + table[i].getValue()
                            + ", removed=" + table[i].isRemoved());
                }

                if (i < table.length - 1) {
                    tableStringBuilder.append("\n");
                }
            }

            return tableStringBuilder.toString();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        final int n = scanner.nextInt();
        final int m = Integer.parseInt(scanner.nextLine().strip());
        HashTable<String> table = new HashTable<>(5);

        for (int i = 0; i < n; i++) {
            table.put(scanner.nextInt(),scanner.next());
        }
        for (int i = 0; i < m; i++) {
            table.remove(scanner.nextInt());
        }
        System.out.println(table);
    }
}