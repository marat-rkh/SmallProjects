import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MergedIterator<T extends Comparable<T>> implements Iterator<T> {
    private final Heap<IterElem<T>> heap = new Heap<>();
    private final List<Iterator<T>> sourceIters;

    public MergedIterator(List<Iterator<T>> iters) {
        sourceIters = new ArrayList<>(iters);
        List<IterElem<T>> firstElems = new ArrayList<>();
        for(int i = 0; i < iters.size(); i++) {
            if(iters.get(i).hasNext()) {
                firstElems.add(new IterElem<T>(iters.get(i).next(), i));
            }
        }
        heap.build(firstElems);
    }

    @Override
    public boolean hasNext() {
        return !heap.isEmpty();
    }

    @Override
    public T next() {
        IterElem<T> extracted = heap.extractMax();
        if(sourceIters.get(extracted.sourceIteratorNum).hasNext()) {
            heap.insert(new IterElem<>(sourceIters.get(extracted.sourceIteratorNum).next(), extracted.sourceIteratorNum));
        }
        return extracted.elem;
    }

    @Override
    public void remove() {
        // not implemented
    }
}
