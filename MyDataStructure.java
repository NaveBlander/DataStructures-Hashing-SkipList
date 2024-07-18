import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class MyDataStructure {
    IndexableSkipList skiplist;
    ChainedHashTable table;
    /*
     * You may add any members that you wish to add.
     * Remember that all the data-structures you use must be YOUR implementations,
     * except for the List and its implementation for the operation Range(low, high).
     */

    /***
     * This function is the Init function described in Part 4.
     *
     * @param N The maximal number of items expected in the DS.
     */
    public MyDataStructure(int N) {
        HashFactory hash = new ModularHash();
        table = new ChainedHashTable<>(hash);
        skiplist = new IndexableSkipList(0.5);
    }
    /*
     * In the following functions,
     * you should REMOVE the place-holder return statements.
     */
    public boolean insert(int value) { // O(logn)
        boolean output = false;

        if (!contains(value)){
            skiplist.insert(value);         // O(logn)
            table.insert(value, value);     // O(1)
            output = true;
        }

        return output;
    }

    public boolean delete(int value) {
        boolean output = false;

        if (contains(value)){
            skiplist.delete(new AbstractSkipList.Node(value));
            table.delete(value);
            output = true;
        }

        return output;
    }

    public boolean contains(int value) {
        boolean output = false;

        if (table != null && table.search((Object)value) != null)
            output = true;

        return output;
    }

    public int rank(int value) {
        return skiplist.rank(value);
    }

    public int select(int index) {
        return skiplist.select(index);
    }

    public List<Integer> range(int low, int high) {
        List output = new LinkedList();

        if (!(contains(low)))
            return null;
        else {
            for (int i = low; i <= high; i++){
                if (contains(i)){
                    output.add(i);
                }
            }
        }
        return output;
    }
}
