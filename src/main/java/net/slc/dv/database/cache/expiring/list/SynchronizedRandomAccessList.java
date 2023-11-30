package net.slc.dv.database.cache.expiring.list;

import java.util.List;
import java.util.RandomAccess;

public class SynchronizedRandomAccessList<E> extends SynchronizedList<E> implements RandomAccess {
    private static final long serialVersionUID = 1530674583602358482L;

    SynchronizedRandomAccessList(List<E> list) {
        super(list);
    }

    SynchronizedRandomAccessList(List<E> list, Object mutex) {
        super(list, mutex);
    }

    public List<E> subList(int fromIndex, int toIndex) {
        synchronized (mutex) {
            return new SynchronizedRandomAccessList<>(
                    list.subList(fromIndex, toIndex), mutex);
        }
    }

    /**
     * Allows instances to be deserialized in pre-1.4 JREs (which do
     * not have SynchronizedRandomAccessList).  SynchronizedList has
     * a readResolve method that inverts this transformation upon
     * deserialization.
     */
    private Object writeReplace() {
        return new SynchronizedList<>(list);
    }
}
