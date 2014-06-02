import java.util.Iterator;

public class IterElem<T extends Comparable> implements Comparable<IterElem<T>> {
    public final T elem;
    public final int sourceIteratorNum;

    public IterElem(T elem, int sourceIteratorNum) {
        this.elem = elem;
        this.sourceIteratorNum = sourceIteratorNum;
    }

    @Override
    public int compareTo(IterElem<T> o) {
        return elem.compareTo(o.elem);
    }
}
