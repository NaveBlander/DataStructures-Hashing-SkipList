
public class IndexableSkipList extends AbstractSkipList {
    final protected double probability;

    public IndexableSkipList(double probability) {
        super();
        this.probability = probability;
    }

    @Override
    public Node find(int val) {
        Node p = head;
        for (int i = head.height(); i >= 0 ; i--) {
            while (p.getNext(i) != null && p.getNext(i).key() <= val) {
                p = p.getNext(i);
            }
        }
        return p;
    }

    @Override
    public int generateHeight() {
        int count = 0;
        double result = 0;

        while (result == 0)
            result = Math.random();


        while (this.probability < result) {
            count++;
            result = Math.random();
            while (result == 0)
                result = Math.random();
        }

        return count;
    }

    public int rank(int val) {
        int rank = 0;
        Node curr = head;
        for(int level = head.height(); level >= 0 ; level = level-1){
            while(curr.getNext(level) != null && curr.getNext(level).key() <= val){
                rank += curr.getWidth()[level];
                curr = curr.getNext(level);
            }
        }
        return rank;
    }

    public int select(int index) {
        Node curr = head;
        int indexAcummulator = 0;
        for (int level = head.height() ; level >= 0 ; level = level-1){
            while (curr.getNext(level) != null && (indexAcummulator + curr.getWidth()[level] <= index)){
                indexAcummulator += curr.getWidth()[level];
                curr = curr.getNext(level);
                if(indexAcummulator == index){
                    return curr.key();
                }
            }
        }
        return curr.key();
    }

}
