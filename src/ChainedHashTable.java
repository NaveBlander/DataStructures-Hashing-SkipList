import java.util.*;

public class ChainedHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 2;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;

    // us
    private LinkedList<Pair>[] table;
    private double loadFactor;
    private int numOfObj;


    /*
     * You should add additional private members as needed.
     */

    public ChainedHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ChainedHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);

        //us
        this.table = new LinkedList[(int)Math.pow(2,k)];
        this.numOfObj = 0;
        this.loadFactor = (double) numOfObj /table.length;
    }

    public V search(K key) {
        LinkedList<Pair> temp = table[hashFunc.hash(key)];
        if (temp != null) {
            for (Pair p : temp) {
                if (p.first().equals(key)) {
                    return (V) p.second();
                }
            }
        }
        return null;
    }

    public void insert(K key, V value) {

        // Increase the count of objects since we are about to add one.
        numOfObj++;

        // Calculate the load factor after the new object is inserted
        loadFactor = (double) numOfObj / table.length;

        // Check if the load factor exceeds the maximum load factor after insertion.
        if (loadFactor >= maxLoadFactor){ //need to rehash
            // Store the old capacity before updating it.
            int oldCapacity = capacity;
            capacity = 2*capacity;

            // Create a new table with the updated capacity.
            LinkedList<Pair>[] newTable = new LinkedList[capacity];

            // Select a new hash function according to the updated capacity.
            // Logarithm base 2 of the capacity gives the k value.
            hashFunc = hashFactory.pickHash((int)(Math.log(capacity) / Math.log(2)));

            // Rehash the objects in the old table to the new table.
            for (int i = 0; i < oldCapacity; i++){
                LinkedList<Pair> temp = table[i];
                if (temp != null) {
                    // Iterate through the linked list at the current index.
                    for (Pair curr : temp) {
                        // Calculate the new hash value for the current object.
                        int index = hashFunc.hash((K) curr.first());
                        // Get the linked list at the new hashed index.
                        LinkedList<Pair> currList = newTable[index];

                        // If there is no linked list at the hashed index in the new table,
                        // create a new linked list.
                        if (currList == null) {
                            currList = new LinkedList<>();
                            newTable[index] = currList;
                        }

                        // Add the current object to the linked list at the hashed index.
                        currList.add(curr);
                    }
                }
            }

            // Replace the old table with the new one.
            this.table = newTable;
        }

        // Create a new pair with the provided key and value.
        Pair p = new Pair(key, value);

        // Hash the key to find the index to insert the pair.
        int hashIndex = hashFunc.hash(key);

        // If there is no linked list at the hashed index, create a new linked list.
        if (table[hashIndex] == null) {
            LinkedList<Pair> l = new LinkedList<>();
            table[hashIndex] = l;
            l.add(p);
        } else {
            // If there is a linked list at the hashed index, add the pair to the linked list.
            table[hashIndex].add(p);
        }

        // Recalculate the load factor after the insertion.
        loadFactor = (double) numOfObj / table.length;
    }


    public boolean delete(K key) {
        boolean found = false;
        int indexShouldbe = hashFunc.hash(key);
        LinkedList<Pair> listShouldBe = table[indexShouldbe];
        if (listShouldBe != null) {
            Iterator<Pair> iter = listShouldBe.iterator();
            while (iter.hasNext() && !found) { // look for the key
                Pair p = iter.next();
                if (p.first().equals(key)) { // found the key
                    listShouldBe.remove(p);
                    found = true;
                    numOfObj--;
                    if (listShouldBe.size() == 0) {
                        table[indexShouldbe] = null;
                    }
                    loadFactor = (double) numOfObj / table.length;
                }
            }
        }

        return found;
    }

    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() { return capacity; }

}