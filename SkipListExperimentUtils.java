import javax.print.attribute.standard.JobKOctets;
import java.util.*;

public class SkipListExperimentUtils {
    public static double measureLevels(double p, int x) {
        IndexableSkipList sl = new IndexableSkipList(p);
        double sumOfHeights = 0;

        for (int i = 0; i < x; i++) {
            sumOfHeights += sl.generateHeight();
        }

        return ((sumOfHeights / x) + 1);
    }

    /*
     * The experiment should be performed according to these steps:
     * 1. Create the empty Data-Structure.
     * 2. Generate a randomly ordered list (or array) of items to insert.
     *
     * 3. Save the start time of the experiment (notice that you should not
     *    include the previous steps in the time measurement of this experiment).
     * 4. Perform the insertions according to the list/array from item 2.
     * 5. Save the end time of the experiment.
     *
     * 6. Return the DS and the difference between the times from 3 and 5.
     */
    public static Pair<AbstractSkipList, Double> measureInsertions(double p, int size) {
        IndexableSkipList sl = new IndexableSkipList(p);
        List<Integer> evenList = new ArrayList<>();
        for (int i = 0; i <= 2 * size; i = i + 2) {
            evenList.add(i);
        }
        Collections.shuffle(evenList);
        double durAllInserts = 0;

        for (int val : evenList) {
            double startTime = System.nanoTime();
            sl.insert(val);
            double endTime = System.nanoTime();
            double duration = endTime - startTime;
            durAllInserts = durAllInserts + duration;
        }

        double aveInsertTime = (durAllInserts / (size + 1));
        Pair<AbstractSkipList, Double> pair = new Pair<>(sl, aveInsertTime);
        return pair;
    }

    public static double measureSearch(AbstractSkipList skipList, int size) {
        List<Integer> valList = new ArrayList<>();
        for (int i = 0; i <= size*2; i = i + 1) {
            valList.add(i);
        }
        Collections.shuffle(valList);
        double durAllSearches = 0;

        for (int val : valList) {
            double startTime = System.nanoTime();
            skipList.search(val);
            double endTime = System.nanoTime();
            double duration = endTime - startTime;
            durAllSearches = durAllSearches + duration;
        }

        double aveSearchTime = (durAllSearches / valList.size());
        return aveSearchTime;
    }

    public static double measureDeletions(AbstractSkipList skipList, int size) {
        List<Integer> evenList = new ArrayList<>();
        for (int i = 0; i <= 2 * size; i = i + 2) {
            evenList.add(i);
        }
        Collections.shuffle(evenList);
        double durAllDel = 0;

        for (int val : evenList) {
            AbstractSkipList.Node nodeToDelete = new AbstractSkipList.Node(val);
            double startTime = System.nanoTime();
            skipList.delete(nodeToDelete);
            double endTime = System.nanoTime();
            double duration = endTime - startTime;
            durAllDel = durAllDel + duration;
        }

        double aveInsertTime = (durAllDel / evenList.size());
        return aveInsertTime;
    }

    public static Object[] experiment(double p, int size) {
        double aveInsert;
        double aveSearch;
        double aveDelete;

        Pair<AbstractSkipList, Double> meInsertResults = measureInsertions(p, size);
        IndexableSkipList sl = (IndexableSkipList) meInsertResults.first();

        aveInsert = (double) meInsertResults.second();
        aveSearch = measureSearch(sl, size);
        aveDelete = measureDeletions(sl, size);

        Object[] InsertSearchDelete = {aveInsert, aveSearch, aveDelete};
        return InsertSearchDelete;
    }

    public static Object[] averageAfter30Exp(double p, int size) {
        double totalAveInsert = 0;
        double totalAveSearch = 0;
        double totalAveDelete = 0;

        for (int i = 0; i < 30 ; i = i+1) {
            Object[] allAverages = experiment(p, size);
            totalAveInsert += (double) allAverages[0];
            totalAveSearch += (double) allAverages[1];
            totalAveDelete += (double) allAverages[2];
        }

        double insertAveAfter30Exp = totalAveInsert/30;
        double searchAveAfter30Exp = totalAveSearch/30;
        double deleteAveAfter30Exp = totalAveDelete/30;

        Object[] allAveAfter30Exp = {insertAveAfter30Exp, searchAveAfter30Exp, deleteAveAfter30Exp};
        return allAveAfter30Exp;
    }

    public static void main(String[] args) {

        double[] p = {0.33, 0.5, 0.75, 0.9};

        // 2.2 ---------------------------------------------------------------------------------------------------------
        System.out.println("-----------------------------------------------------------------------");
        for (double i : p) {
            System.out.println("for p = "+i);
            printTable22(i);
            System.out.println("-----------------------------------------------------------------------");
            System.out.println("");
        }
        //

        // 2.5 ---------------------------------------------------------------------------------------------------------
        int[] xVals = {1000, 2500, 5000, 10000, 15000, 20000, 50000};
        System.out.println("-----------------------------------------------------------------------");
        for (double i : p) {
            System.out.println("for p = "+i);
            printTable23(i);
            System.out.println("-----------------------------------------------------------------------");
            System.out.println("");
        }
        //
    }

    //2.2 Tables
    public static String row22(double p, int x){
        double expectedLevel = 1/p;
        double[] arrOfL = { measureLevels(p, x), measureLevels(p, x), measureLevels(p, x), measureLevels(p, x), measureLevels(p, x)};
        String ans = String.format("| %.3f | %.3f | %.3f | %.3f | %.3f | %.3f | %.3f",
                                    arrOfL[0], arrOfL[1], arrOfL[2], arrOfL[3], arrOfL[4], expectedLevel, averageDelta(arrOfL, expectedLevel));
        return ans;
    }

    public static double averageDelta(double[] arrOfL, double expectedLevel){
        double result = 0;
        for (double l : arrOfL) {
            result += Math.abs(l-expectedLevel);
        }
        return (result/5);
    }

    public static void printTable22(double p){
        String headLine =  "  x  " + " | " + " l1  " + " | " + " l2  " + " | " + " l3  " + " | " + " l4  " + " | " + " l5  " + " | " + "exp l" + " | " + "delta";
        System.out.println(headLine);
        System.out.println("10:   " + row22(p, 10));
        System.out.println("100:  " + row22(p, 100));
        System.out.println("1000: " + row22(p, 1000));
        System.out.println("10000:" + row22(p, 10000));
    }

    //2.3 Tables
    public static String row23(double p, int size) {
        Object[] results = averageAfter30Exp(p, size);
        String ans = String.format("|    %.3f      |    %.3f   |    %.3f     |",
                                        results[0], results[1], results[2]);
        return ans;
    }

    public static void printTable23(double p){
        String headLine =  "  x   " + "|" + "Average Insertion" + "|" + "Average Search" + "|" + "Average Deletion";
        System.out.println(headLine);
        System.out.println("1000: " + row23(p, 1000));
        System.out.println("2500: " + row23(p, 2500));
        System.out.println("5000: " + row23(p, 5000));
        System.out.println("10000:" + row23(p, 10000));
        System.out.println("15000:" + row23(p, 15000));
        System.out.println("20000:" + row23(p, 20000));
        System.out.println("50000:" + row23(p, 50000));
    }

}




