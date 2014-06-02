import java.util.Iterator;
import java.util.Random;

public class RandomDecrIterator implements Iterator<Integer> {
    private int current;
    private int rndBound;
    private final Random rndGen = new Random();


    public RandomDecrIterator(int startNumber, int rndMax) {
        current = startNumber;
        rndBound = rndMax;
    }

    @Override
    public boolean hasNext() {
        return  current >= 0;
    }

    @Override
    public Integer next() {
        current = current - rndGen.nextInt(rndBound);
        return current;
    }

    @Override
    public void remove() {
        // not implemented
    }
}
