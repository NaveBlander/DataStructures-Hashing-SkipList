import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;

abstract public class AbstractSkipList {
    final protected Node head;
    final protected Node tail;
    private int size;                                                                                                   // I added

    public AbstractSkipList() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        increaseHeight();
    }

    public void increaseHeight() {
        head.addLevel(tail, null);
        tail.addLevel(null, head);
    }

    public void  decreaseHeight() {                                                                                     // I added
        head.removeLevel();
        tail.removeLevel();
    }

    abstract Node find(int key);

    abstract public int generateHeight();

    public Node search(int key) {
        Node curr = find(key);

        return curr.key() == key ? curr : null;
    }

    public Node insert(int key) {

        int oldHeight = head.height;
        int nodeHeight = generateHeight();
        while (nodeHeight > head.height()) {
            increaseHeight();
        }

        Node prevNode = find(key);
        if (prevNode.key() == key) {
            return null;
        }

        size += 1;
        updateHeadAndTailInsertion(oldHeight);

        Node newNode = new Node(key);

        for (int level = 0; level <= nodeHeight && prevNode != null; ++level) {
            Node nextNode = prevNode.getNext(level);

            newNode.addLevel(nextNode, prevNode);
            prevNode.setNext(level, newNode);
            nextNode.setPrev(level, newNode);

            while (prevNode != null && prevNode.height() == level) {
                prevNode = prevNode.getPrev(level);
            }
        }
        updateWidthes(newNode);

        return newNode;
    }

    private void updateHeadAndTailInsertion(int oldHeight) {
        int[] headWidth = resizeArray(head.width, head.height+1);                                               //I added
        headWidth[0] = 1;
        for (int i = oldHeight+1; i < headWidth.length; i++) {                                                          //I added
            headWidth[i] = size;                                                                                        //I added
        }                                                                                                               //I added

        int[] tailWidth = new int[tail.height+1];                                                                       //I added
        for (int i = 0; i < headWidth.length; i++) {                                                                    //I added
            tailWidth[i] = 0;                                                                                           //I added
        }

        head.width = headWidth;                                                                                         //I added
        tail.width = tailWidth;                                                                                         //I added
    }

    public int[] resizeArray(int[] originalArray, int newSize) {                                                        // I added
        int[] newArray = new int[newSize];
        for (int i = 0; i < originalArray.length && i < newSize; i++) {
            newArray[i] = originalArray[i];
        }
        return newArray;
    }

    private void updateWidthes(Node newNode) {                                                                          //I added:
        //In this point, the new node already inserted to skiplist, now we just need to set the width field of the new node, and the width field of the previous node in each level
        newNode.width = new int[newNode.height+1];
        //Set current node width in each level
        newNode.getWidth()[0] = 1;
        for (int level = 1 ; level <= newNode.height ; level += 1){
            newNode.getWidth()[level] = computeLevelWidth(newNode, level);
        }

        //Set previous node's width in each level
        for (int level = 1 ; level <= newNode.height ; level += 1){
            newNode.getPrev(level).getWidth()[level] = newNode.getPrev(level).getWidth()[level] - newNode.getWidth()[level] + 1;
        }
        updateHigherLevelWidths(newNode);
    }



    private void updateHigherLevelWidths(Node newNode) {                                                                // I added
        Node curr = head;
        for (int level = head.height ; level > newNode.height ; level = level-1){
            while (curr.getNext(level) != null && curr.getNext(level).key() < newNode.key()){
                curr = curr.getNext(level);
            }
            curr.getWidth()[level] += 1;
        }
    }


    private int computeLevelWidth(Node newNode, int level) { /// Compute the width of this node in level 'level'        //I added
        int widthInLevel = newNode.getWidth()[level-1];
        Node currNode = (Node) newNode.getNext(level-1);
        while(currNode != newNode.getNext(level)){
            widthInLevel += currNode.getWidth()[level-1];
            currNode = currNode.getNext(level-1);
        }
        return widthInLevel;
    }

    public boolean delete(Node node) {

        node = find(node.key());
        int[] widthOfDeletedNode = node.getWidth();

        for (int level = 0; level <= node.height(); ++level) {
            Node prev = node.getPrev(level);
            Node next = node.getNext(level);
            prev.setNext(level, next);
            next.setPrev(level, prev);
        }

        size = size-1;
        updateHeadAndTailDeletion();
        updatePreviousNodeDeletion(node, widthOfDeletedNode);

        return true;
    }

    private void updatePreviousNodeDeletion(Node node, int[] widthOfDeletedNode) {
        //UPDATE WIDTH OF THE PRVIOUS NODE                                                                              // I added
        Node curr = head;                                                                                               // I added
        for (int level = head.height ; level >= 0 ; level = level-1){                                                   // I added
            while (curr.getNext(level) != null && curr.getNext(level).key() < node.key){
                curr = curr.getNext(level);                                                                             // I added
            }
            if (curr.getNext(level).key() > node.key){                                                                  // I added
                int toAdd = 0;                                                                                          // I added
                if (level < widthOfDeletedNode.length)                                                                  // I added
                    toAdd = widthOfDeletedNode[level];                                                                  // I added
                curr.getWidth()[level] = curr.getWidth()[level] + toAdd -1;                                             // I added
            }
        }
    }

    private void updateHeadAndTailDeletion() {
        for (int level = head.height ; level >= 0; level = level-1){                                                    // I added
            if(head.getNext(level).key() == tail.key())                                                                 // I added
                decreaseHeight();                                                                                       // I added
        }                                                                                                               // I added
        head.width = resizeArray(head.getWidth(), head.height+1);                                               // I added
    }

    public int predecessor(Node node) {
        return node.getPrev(0).key();
    }

    public int successor(Node node) {
        return node.getNext(0).key();
    }

    public int minimum() {
        if (head.getNext(0) == tail) {
            throw new NoSuchElementException("Empty Linked-List");
        }

        return head.getNext(0).key();
    }

    public int maximum() {
        if (tail.getPrev(0) == head) {
            throw new NoSuchElementException("Empty Linked-List");
        }

        return tail.getPrev(0).key();
    }

    private void levelToString(StringBuilder s, int level) {
        s.append("H    ");
        // I change from this: Node curr = head.getNext(level); to this:    Node curr = head;
        Node curr = head.getNext(level);
        while (curr != tail) {
            s.append(curr.key + ",("+curr.width[level]+")");
            s.append("    ");

            curr = curr.getNext(level);
        }

        s.append("T\n");
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int level = head.height(); level >= 0; --level) {
            levelToString(str, level);
        }

        return str.toString();
    }

    public static class Node {
        final private List<Node> next;
        final private List<Node> prev;
        private int height;
        final private int key;
        private int[] width;

        public Node(int key) {
            next = new ArrayList<>();
            prev = new ArrayList<>();
            this.height = -1;
            this.key = key;
            this.width = new int[0];
        }

        public Node getPrev(int level) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            return prev.get(level);
        }

        public Node getNext(int level) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            return next.get(level);
        }

        public void setNext(int level, Node next) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            this.next.set(level, next);
        }

        public void setPrev(int level, Node prev) {
            if (level > height) {
                throw new IllegalStateException("Fetching height higher than current node height");
            }

            this.prev.set(level, prev);
        }

        public void addLevel(Node next, Node prev) {
            ++height;
            this.next.add(next);
            this.prev.add(prev);
        }

        public void removeLevel(){
            height = height-1;
            this.next.remove(this.next.size()-1);
            this.prev.remove(this.prev.size()-1);
        }

        public int height() { return height; }
        public int key() { return key; }

        public int[] getWidth() {
            return width;
        }
    }
}
