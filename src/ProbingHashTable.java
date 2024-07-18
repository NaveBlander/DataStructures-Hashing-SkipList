import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

public class ProbingHashTable<K, V> implements HashTable<K, V> {
    final static int DEFAULT_INIT_CAPACITY = 4;
    final static double DEFAULT_MAX_LOAD_FACTOR = 0.75;
    final private HashFactory<K> hashFactory;
    final private double maxLoadFactor;
    private int capacity;
    private HashFunctor<K> hashFunc;

    // us
    private Pair[] table;
    private double loadFactor;
    private int numOfObj;

    /*
     * You should add additional private members as needed.
     */

    public ProbingHashTable(HashFactory<K> hashFactory) {
        this(hashFactory, DEFAULT_INIT_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ProbingHashTable(HashFactory<K> hashFactory, int k, double maxLoadFactor) {
        this.hashFactory = hashFactory;
        this.maxLoadFactor = maxLoadFactor;
        this.capacity = 1 << k;
        this.hashFunc = hashFactory.pickHash(k);
        //us
        this.table = new Pair[(int)Math.pow(2,k)];
        this.numOfObj = 0;
        this.loadFactor = (double) numOfObj /table.length;
    }

    public V search(K key) {
        V output = null;
        int index = hashFunc.hash(key);
        while (table[index] != null & output == null){
            if (table[index].first().equals(key)){ // If the key is found
                output = (V) (table[index].second()); //
            }
            else {
                index = HashingUtils.mod((index + 1), table.length);
            }
        }
        return output;
    }

    public void insert(K key, V value) {
        // Increment numOfObj first
        numOfObj = numOfObj + 1;

        // Recalculate the load factor
        loadFactor = (double) numOfObj / table.length;

        if (loadFactor >= maxLoadFactor) { //need to rehash
            int newNumOfObj = 0;
            capacity = 2*capacity;
            Pair[] newTable = new Pair[capacity];
            hashFunc = hashFactory.pickHash((int)(Math.log(capacity) / Math.log(2)));

            for (int i = 0; i < table.length; i++){
                Pair object = table[i];
                Pair deletedElement = new Pair<>(Integer.MAX_VALUE, Integer.MAX_VALUE); // Flag for deleted objects
                if (object != deletedElement & object != null){ // If the object is not deleted and not null
                    int index = hashFunc.hash((K) object.first()); // Recalculate the index for the object in the new table
                    newTable[index] = (Pair) object; // Insert the object into the new table
                    newNumOfObj = newNumOfObj + 1;
                }
            }
            table = newTable;
            numOfObj = newNumOfObj;
            loadFactor = (double) numOfObj / table.length;
        }

        Pair p = new Pair(key, value);
        int index = hashFunc.hash(key);

        Pair deletedObject = new Pair<>(Integer.MAX_VALUE, Integer.MAX_VALUE); //a flag means there is no element there

        if (this.table[index] == null) {
            this.table[index] = p;
        }
        else{
            while (this.table[index] != null & this.table[index] != deletedObject){ //if there is an element in the index
                index = HashingUtils.mod((index+1), table.length);
            }
            this.table[index] = p;
        }
        loadFactor = (double) numOfObj / table.length;
    }

    public boolean delete(K key) {
        boolean output = false;
        int index = findIndex(key);
        if (index != -1){
            // Use a dummy pair to flag the deleted slot
            table[index] = new Pair<>(Integer.MAX_VALUE, Integer.MAX_VALUE);
            output = true;
        }
        return output;
    }


    public HashFunctor<K> getHashFunc() {
        return hashFunc;
    }

    public int capacity() { return capacity; }

    public int findIndex(K key) { //return the index of the key in the table
        int index = hashFunc.hash(key);
        while (table[index] != null) {
            if (table[index].first().equals(key)){
                return index;
            }
            else {
                index = HashingUtils.mod((index + 1), table.length);
            }
        }
        return -1; //it means there is no such key in the table
    }
}
