import java.util.Random;

public class StringHash implements HashFactory<String> {

    public StringHash() {
    }

    @Override
    public HashFunctor<String> pickHash(int k) {
        return new Functor(k);
    }

    public class Functor implements HashFunctor<String> {
        final private HashFunctor<Integer> carterWegmanHash;
        final private int c;
        final private int q;

        public Functor (int k){
            boolean isPrime = false;
            HashingUtils MR = new HashingUtils();
            int temp = 0;
            while (!isPrime) {
                temp = (int) ((Math.random()*(Integer.MAX_VALUE-((Integer.MAX_VALUE)/2)+1))+((Integer.MAX_VALUE)/2));
                isPrime = MR.runMillerRabinTest(temp, 50);
            }

            carterWegmanHash = (HashFunctor<Integer>) new ModularHash().pickHash(k);
            this.q = temp;
            this.c = (int) ((Math.random()*(q-1))+2);
        }

        @Override
        public int hash(String key) {
            HashingUtils HU = new HashingUtils();
            int sum = 0;
            for (int i = 0; i < key.length(); i = i +1) {
                int ans =(int) HU.modPow(c, key.length()-i,q);
                sum = sum + HashingUtils.mod(((int)(key.charAt(i))*ans), q);
            }
            sum = HashingUtils.mod(sum,q);

            return carterWegmanHash.hash(sum);
        }

        public int c() {
            return c;
        }

        public int q() {
            return q;
        }

        public HashFunctor carterWegmanHash() {
            return carterWegmanHash;
        }
    }
}
