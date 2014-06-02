import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        List<Integer> lst1 = new ArrayList<>();
//        lst1.add(9);
//        lst1.add(7);
//        lst1.add(1);
//        List<Integer> lst2 = new ArrayList<>();
//        lst2.add(8);
//        lst2.add(3);
//        lst2.add(2);
//        List<Integer> lst3 = new ArrayList<>();
//        lst3.add(8);
//        lst3.add(3);
//        lst3.add(2);
//        List<Iterator<Integer>> iters = new ArrayList<>();
//        iters.add(lst1.iterator());
//        iters.add(lst2.iterator());
//        iters.add(lst3.iterator());
        List<Iterator<Integer>> iters = new ArrayList<>();
        iters.add(new EvensDecrIter(10));
        iters.add(new RandomDecrIterator(25, 5));

        MergedIterator<Integer> mIter = new MergedIterator<>(iters);
        while (mIter.hasNext()) {
            System.out.print(mIter.next() + " ");
        }
    }
}
