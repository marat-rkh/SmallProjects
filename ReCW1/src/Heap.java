import java.util.ArrayList;
import java.util.List;

public class Heap<T extends Comparable<T>> {
    private List<T> elems = null;

    public void build(List<T> input) {
        elems = new ArrayList<>(input);
        if(input.size() > 1) {
            for (int i = input.size() / 2 - 1; i >= 0; i--) {
                heapifyDown(i);
            }
        }
    }

    public T extractMax() {
        swap(0, elems.size() - 1);
        T extracted = elems.remove(elems.size() - 1);
        heapifyDown(0);
        return extracted;
    }

    public void insert(T e) {
        elems.add(e);
        heapifyUp();
    }

    public boolean isEmpty() {
        return elems == null || elems.size() == 0;
    }

    private void heapifyDown(int index) {
        int maxElemIndex = index;
        if(leftChildIndex(maxElemIndex) != null && elems.get(leftChildIndex(index)) != null &&
           elems.get(maxElemIndex).compareTo(elems.get(leftChildIndex(maxElemIndex))) < 0)
        {
            maxElemIndex = leftChildIndex(maxElemIndex);
        }
        if(rightChildIndex(maxElemIndex) != null && elems.get(rightChildIndex(index)) != null &&
           elems.get(maxElemIndex).compareTo(elems.get(rightChildIndex(maxElemIndex))) < 0)
        {
            maxElemIndex = rightChildIndex(maxElemIndex);
        }
        if(maxElemIndex != index) {
            swap(index, maxElemIndex);
            heapifyDown(maxElemIndex);
        }
    }

    private void heapifyUp() {
        int index = elems.size() - 1;
        while (parentIndex(index) != null &&
               elems.get(index).compareTo(elems.get(parentIndex(index))) > 0)
        {
            swap(index, parentIndex(index));
            index = parentIndex(index);
        }
    }

    private Integer leftChildIndex(int parentIndex) {
        return parentIndex * 2 + 1 < elems.size() ? parentIndex * 2 + 1 : null;
    }

    private Integer rightChildIndex(int parentIndex) {
        return parentIndex * 2 + 2 < elems.size() ? parentIndex * 2 + 2 : null;
    }

    private Integer parentIndex(int childIndex) {
        if(childIndex % 2 == 0) {
            return childIndex / 2 - 1 >= 0 ? childIndex / 2 - 1 : null;
        }
        return childIndex / 2 >= 0 ? childIndex / 2 : null;
    }

    private void swap(int ind1, int ind2) {
        T buf = elems.get(ind1);
        elems.set(ind1, elems.get(ind2));
        elems.set(ind2, buf);
    }
}
