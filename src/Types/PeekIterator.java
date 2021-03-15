package Types;


import java.util.LinkedList;
import java.util.List;

public class PeekIterator<T> extends LinkedList<T> {
    public static final Object END_OF_LIST = null;

    private int index;

    public PeekIterator(List<T> start)  {
        super(start);
        this.index = 0;
    }

    public T getCurrent() {
        if(index >= size()) return (T)END_OF_LIST;
        return get(index);
    }

    public T next() {
        if(index >= size()-1) {
            index++;
            return (T)END_OF_LIST;
        }
        return get(++index);
    }
}
