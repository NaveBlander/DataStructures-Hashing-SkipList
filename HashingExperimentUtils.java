import java.util.List;
import java.util.Random;

public class HashingExperimentUtils {
    final private static int k = 16;

    public static Pair<Double, Double> measureOperationsChained(double maxLoadFactor) {
        HashFactory<Integer> hf = new ModularHash();
        ChainedHashTable<Integer, Integer> ht = new ChainedHashTable<>(hf, k, maxLoadFactor); // Create a hash table of size 2^16
        int maxNumToInsert = ((int) (maxLoadFactor * Math.pow(2, k)))-1;    // Compute the maximum number of elements to insert without rehashing

        long totalInsertTime = 0;
        long totalSearchTime = 0;
        int[] randomInts = arrayWithRandomInts(maxNumToInsert);

        for (int i = 0; i < randomInts.length; i = i + 1) {
            long startTime = System.nanoTime();
            ht.insert(randomInts[i], randomInts[i]);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            totalInsertTime += duration;
        }

        int searchCount = (int) (Math.pow(2, k) * maxLoadFactor);
        Random rand = new Random();
        for (int i = 0; i < searchCount / 2; i = i + 1) {

            // Successful search
            long startTime1 = System.nanoTime();
            ht.search(randomInts[i]);
            long endTime1 = System.nanoTime();
            long duration1 = (endTime1 - startTime1);
            totalSearchTime += duration1;

            // Unsuccessful search
            int randomInt = rand.nextInt();
            while (ht.search(randomInt) != null) { // make sure the random int is not in the table
                randomInt = rand.nextInt();
            }

            long startTime2 = System.nanoTime();
            ht.search(randomInt);
            long endTime2 = System.nanoTime();
            long duration2 = (endTime2 - startTime2);
            totalSearchTime += duration2;
        }

        Pair<Double, Double> output = new Pair<>((double) totalInsertTime / maxNumToInsert, (double) totalSearchTime / searchCount);
        return output;
    }

    public static Pair<Double, Double> measureOperationsProbing(double maxLoadFactor) {
        HashFactory<Integer> hf = new ModularHash();
        ProbingHashTable<Integer, Integer> ht = new ProbingHashTable<>(hf, k, maxLoadFactor); // Create a hash table of size 2^16
        int maxNumToInsert = ((int) (maxLoadFactor * Math.pow(2, k)))-1;    // Compute the maximum number of elements to insert without rehashing

        long totalInsertTime = 0;
        long totalSearchTime = 0;
        int[] randomInts = arrayWithRandomInts(maxNumToInsert);

        for (int i = 0; i < randomInts.length; i = i + 1) {
            long startTime = System.nanoTime();
            ht.insert(randomInts[i], randomInts[i]);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            totalInsertTime += duration;
        }

        int searchCount = (int) (Math.pow(2, k) * maxLoadFactor);
        Random rand = new Random();
        for (int i = 0; i < searchCount / 2; i = i + 1) {

            // Successful search
            long startTime1 = System.nanoTime();
            ht.search(randomInts[i]);
            long endTime1 = System.nanoTime();
            long duration1 = (endTime1 - startTime1);
            totalSearchTime += duration1;

            // Unsuccessful search
            int randomInt = rand.nextInt();
            while (ht.search(randomInt) != null) { // make sure the random int is not in the table
                randomInt = rand.nextInt();
            }

            long startTime2 = System.nanoTime();
            ht.search(randomInt);
            long endTime2 = System.nanoTime();
            long duration2 = (endTime2 - startTime2);
            totalSearchTime += duration2;
        }

        Pair<Double, Double> output = new Pair<>((double) totalInsertTime / maxNumToInsert, (double) totalSearchTime / searchCount);
        return output;
    }

    public static Pair<Double, Double> measureLongOperations() {
        HashingUtils utils = new HashingUtils();
        MultiplicativeShiftingHash hf = new MultiplicativeShiftingHash();
        ChainedHashTable<Long, Long> ht = new ChainedHashTable(hf, k, 1); // Create a hash table with max load factor = 1
        Long[] randomLongs = (utils.genUniqueLong((int) Math.pow(2, k)-1)); // Generate unique Long values, the size is the maximum number of elements to insert without rehashing (2^16)

        long totalInsertTime = 0;
        long totalSearchTime = 0;

        for (int i = 0; i < randomLongs.length; i++) {
            long startTime = System.nanoTime();
            ht.insert(randomLongs[i], randomLongs[i]);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            totalInsertTime += duration;
        }

        int searchCount = (int) Math.pow(2, k);
        Random rand = new Random();
        for (int i = 0; i < searchCount / 2; i++) {
            // Successful search
            long startTime1 = System.nanoTime();
            ht.search(randomLongs[i]);
            long endTime1 = System.nanoTime();
            long duration1 = (endTime1 - startTime1);
            totalSearchTime += duration1;

            // Unsuccessful search
            long randomLong = rand.nextLong();
            while (ht.search(randomLong) != null) {
                randomLong = rand.nextLong();
            }

            long startTime2 = System.nanoTime();
            ht.search(randomLong);
            long endTime2 = System.nanoTime();
            long duration2 = (endTime2 - startTime2);
            totalSearchTime += duration2;
        }

        Pair<Double, Double> output = new Pair<>((double) totalInsertTime / randomLongs.length, (double) totalSearchTime / searchCount);
        return output;
    }

    public static Pair<Double, Double> measureStringOperations() {
        HashingUtils utils = new HashingUtils();
        HashFactory<String> hf = new StringHash();
        ChainedHashTable<String, String> ht = new ChainedHashTable<>(hf, k, 1); // Create a hash table with max load factor = 1
        List<String> randomStrings = utils.genUniqueStrings((int) Math.pow(2, k)-1, 10, 20); // Generate unique Strings of length 10 to 20

        long totalInsertTime = 0;
        long totalSearchTime = 0;

        for (String str : randomStrings) {
            long startTime = System.nanoTime();
            ht.insert(str, str);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            totalInsertTime += duration;
        }

        int searchCount = (int) Math.pow(2, k);
        Random rand = new Random();
        for (int i = 0; i < searchCount / 2; i++) {
            // Successful search
            long startTime1 = System.nanoTime();
            ht.search(randomStrings.get(i));
            long endTime1 = System.nanoTime();
            long duration1 = (endTime1 - startTime1);
            totalSearchTime += duration1;

            // Unsuccessful search
            String randomString = utils.genUniqueStrings(1, 10, 20).get(0);
            while (ht.search(randomString) != null) {
                randomString = utils.genUniqueStrings(1, 10, 20).get(0);
            }

            long startTime2 = System.nanoTime();
            ht.search(randomString);
            long endTime2 = System.nanoTime();
            long duration2 = (endTime2 - startTime2);
            totalSearchTime += duration2;
        }

        Pair<Double, Double> output = new Pair<>((double) totalInsertTime / randomStrings.size(), (double) totalSearchTime / searchCount);
        return output;
    }

    public static int[] arrayWithRandomInts(int size) {
        int[] output = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            output[i] = rand.nextInt();
        }
        return output;
    }

    public static void main(String[] args) {

        //Linear probing table
        double[] a1 = {0.5, 0.75, 0.875, 0.9375};
        double[] insertTimeProbing = new double[4];
        double[] searchTimeProbing = new double[4];
        for (int i = 0; i < 4; i++) {
            double averageInsertProbing = 0;
            double averageSearchProbing = 0;
            for (int j = 0; j < 30; j++) {
                Pair<Double, Double> pair = measureOperationsProbing(a1[i]);
                averageInsertProbing += pair.first();
                averageSearchProbing += pair.second();
            }
            insertTimeProbing[i] = averageInsertProbing / 30;
            searchTimeProbing[i] = averageSearchProbing / 30;
        }
        printProbingTable(insertTimeProbing, searchTimeProbing, a1);



        //Chaining table
        double[] a2 = {0.5, 0.75, 1, 1.5, 2};
        double[] insertTimeChaining = new double[5];
        double[] searchTimeChaining = new double[5];
        for (int i = 0; i < 5; i++) {
            double averageInsertChaining = 0;
            double averageSearchChaining = 0;
            for (int j = 0; j < 30; j++) {
                Pair<Double, Double> pair = measureOperationsChained(a2[i]);
                averageInsertChaining += pair.first();
                averageSearchChaining += pair.second();
            }
            insertTimeChaining[i] = averageInsertChaining / 30;
            searchTimeChaining[i] = averageSearchChaining / 30;
        }
        printChaningTable(insertTimeChaining, searchTimeChaining, a2);



        // Long operations
        double[] insertTimeLong = new double[1];
        double[] searchTimeLong = new double[1];
        double averageInsertLong = 0;
        double averageSearchLong = 0;
        for (int j = 0; j < 10; j++) {
            Pair<Double, Double> pair = measureLongOperations();
            averageInsertLong += pair.first();
            averageSearchLong += pair.second();
        }
        insertTimeLong[0] = averageInsertLong / 10;
        searchTimeLong[0] = averageSearchLong / 10;

        printLongTable(insertTimeLong, searchTimeLong);

        // String operations
        double[] insertTimeString = new double[1];
        double[] searchTimeString = new double[1];

            double averageInsertString = 0;
            double averageSearchString = 0;
            for (int j = 0; j < 10; j++) {
                Pair<Double, Double> pair = measureStringOperations();
                averageInsertString += pair.first();
                averageSearchString += pair.second();
            }
            insertTimeString[0] = averageInsertString / 10;
            searchTimeString[0] = averageSearchString / 10;

        printStringTable(insertTimeString, searchTimeString);

    }

    private static void printChaningTable(double[] insert, double[] search, double[] a) {
        System.out.println("|--------------------------------------|");
        System.out.println("|               Chaining               |");
        System.out.println("|--------------------------------------|");
        System.out.println("|--------------------------------------|");
        System.out.println("|     a      | Ave Insert | Ave Search |");
        System.out.println("|--------------------------------------|");
        for (int i = 0; i < insert.length; i++) {
            System.out.println("|   " + a[i] + "   |   " + insert[i] + "   |   " + search[i] + "   ");
        }
        System.out.println("|--------------------------------------|");
    }

    private static void printProbingTable(double[] insert, double[] search, double[] a) {
        System.out.println("|--------------------------------------|");
        System.out.println("|               Probing                |");
        System.out.println("|--------------------------------------|");
        System.out.println("|--------------------------------------|");
        System.out.println("|     a      | Ave Insert | Ave Search |");
        System.out.println("|--------------------------------------|");
        for (int i = 0; i < insert.length; i++) {
            System.out.println("|   " + a[i] + "   |   " + insert[i] + "   |   " + search[i] + "   ");
        }
        System.out.println("|--------------------------------------|");
    }

    private static void printLongTable(double[] insert, double[] search) {
        System.out.println("|--------------------------------------|");
        System.out.println("|              Long Hashing             |");
        System.out.println("|--------------------------------------|");
        System.out.println("|--------------------------------------|");
        System.out.println("|  Ave Insert | Ave Search |");
        System.out.println("|--------------------------------------|");
        for (int i = 0; i < insert.length; i++) {
            System.out.println("|   " + insert[i] + "   |   " + search[i] + "   ");
        }
        System.out.println("|--------------------------------------|");
    }

    private static void printStringTable(double[] insert, double[] search) {
        System.out.println("|--------------------------------------|");
        System.out.println("|             String Hashing            |");
        System.out.println("|--------------------------------------|");
        System.out.println("|--------------------------------------|");
        System.out.println("|  Ave Insert | Ave Search |");
        System.out.println("|--------------------------------------|");
        for (int i = 0; i < insert.length; i++) {
            System.out.println("|   " + insert[i] + "   |   " + search[i] + "   ");
        }
        System.out.println("|--------------------------------------|");
    }
}



