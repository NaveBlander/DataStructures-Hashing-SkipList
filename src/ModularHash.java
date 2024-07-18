import java.util.Random;

public class ModularHash implements HashFactory<Integer> {

    public ModularHash() {
    }

    @Override
    public HashFunctor<Integer> pickHash(int k) {
        return new Functor(k);
    }

    public class Functor implements HashFunctor<Integer> {
        final private int a;
        final private int b;
        final private long p;
        final private int m;

        public Functor (Integer key) {
            m = (int) Math.pow(2, key);
            a = (int) ((Math.random() * (Integer.MAX_VALUE - 1)) + 15);
            b = (int) ((Math.random() * (Integer.MAX_VALUE)) + 15);
            boolean isPrime = false;
            HashingUtils MR = new HashingUtils();
            long temp = 0;
            while (!isPrime) {
                temp = (long) ((Math.random() * (Long.MAX_VALUE - Integer.MAX_VALUE + 1 + 1)) + Integer.MAX_VALUE + 1);
                isPrime = MR.runMillerRabinTest(temp, 50);
            }
            p = temp;

        }

        @Override
        public int hash(Integer key) {
            return (int) HashingUtils.mod(HashingUtils.mod((a*key+b), p), m);
        }

        public int a() {
            return a;
        }

        public int b() {
            return b;
        }

        public long p() {
            return p;
        }

        public int m() {
            return m;
        }
    }
}
