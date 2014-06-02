import java.util.Iterator;

public class EvensDecrIter implements Iterator<Integer> {
    private int current;

    public EvensDecrIter(int startNum) {
        this.current = startNum;
    }

    @Override
    public boolean hasNext() {
        return current >= 0;
    }

    @Override
    public Integer next() {
        current -= 2;
        return current + 2;
    }

    @Override
    public void remove() {
        // not implemented
    }
}