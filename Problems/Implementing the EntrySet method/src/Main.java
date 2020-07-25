import java.util.*;

public class Main {
    private static class TableEntry<T> {
        private final int key;
        private final T value;

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

            T t = get(key);
            if (t == null) {
                table[idx] = new TableEntry<>(key, value);
            } else {
                table[idx] = new TableEntry<>(key, t + " " + value);
            }
            return true;
        }

        public T get(int key) {
            int idx = findKey(key);

            if (idx == -1 || table[idx] == null) {
                return null;
            }

            return (T) table[idx].getValue();
        }

        public String entrySet() {
            StringBuilder tableStringBuilder = new StringBuilder();
            Arrays.stream(table).filter(Objects::nonNull).forEach(tableEntry -> {
                tableStringBuilder.append(tableEntry.getKey())
                        .append(": ")
                        .append(tableEntry.getValue())
                        .append("\n");
            });
            return tableStringBuilder.toString();
        }

        private int findKey(int key) {
            int hash = key % size;

            while (!(table[hash] == null || table[hash].getKey() == key)) {
                hash = rehash(hash);

                if (hash == key % size) {
                    return -1;
                }
            }

            return hash;
        }

        private int rehash(int hash) {
            return (hash + 1) % size;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> list = new ArrayList<>();
        final int s1 = Integer.parseInt(scanner.nextLine().strip());
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }
        HashTable<String> table = new HashTable<>(s1);
        for (String value : list) {
            final String[] s = value.split(" ", 2);
            table.put(Integer.parseInt(s[0]), s[1]);
        }
        System.out.println(table.entrySet());
    }
}