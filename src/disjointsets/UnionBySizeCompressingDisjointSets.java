package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A quick-union-by-size data structure with path compression.
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    private Map<T, Integer> id;
    private int size;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {
        pointers = new ArrayList<>();
        id = new HashMap<>();
        size = 0;
    }

    @Override
    public void makeSet(T item) {
        if (id.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        pointers.add(-1);
        id.put(item, size);
        size++;
    }

    @Override
    public int findSet(T item) {
        if (!id.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        // int setNum = pointers.get(id.get(item));
        // while (setNum >= 0) {
        //     setNum = pointers.get(setNum);
        // }
        // return setNum;

        return findSetHelper(id.get(item));
    }

    private int findSetHelper(int pointer) {
        if (pointers.get(pointer) >= 0) {
            int result = findSetHelper(pointers.get(pointer));
            pointers.set(pointer, result);
            return result;
        }
        return pointer;
    }

    @Override
    public boolean union(T item1, T item2) {
        if (!id.containsKey(item1) || !id.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int pointer1 = findSet(item1);
        int pointer2 = findSet(item2);
        if (pointer1 == pointer2) {
            return false;
        } else {
            if (Math.abs(pointers.get(pointer1)) >= Math.abs(pointers.get(pointer2))) {
                pointers.set(pointer1, pointers.get(pointer1) + pointers.get(pointer2));
                pointers.set(pointer2, pointer1);
            } else {
                pointers.set(pointer2, pointers.get(pointer1) + pointers.get(pointer2));
                pointers.set(pointer1, pointer2);
            }
            return true;
        }
    }
}
