package net.slc.dv.database.cache.expiring.list;

import net.slc.dv.database.cache.expiring.object.ExpiringObject;

import java.util.ArrayList;

public class ExpiringObjectList<E> extends SynchronizedList<ExpiringObject<E>> {

    public ExpiringObjectList() {
        super(new ArrayList<>());
    }

    public void updateExpiry(ExpiringObject<E> expiringObject) {
        synchronized (mutex) {
            if (this.contains(expiringObject)) {
                this.remove(expiringObject);
                this.add(expiringObject);
            }
        }
    }

    public void removeExpired() {
        synchronized (mutex) {
            this.removeIf(ExpiringObject::isExpired);
        }
    }

    public void removeExpired(long expirationTime) {
        synchronized (mutex) {
            this.removeIf(expiringObject -> expiringObject.isExpired() || expiringObject.getExpirationTime() < expirationTime);
        }
    }

    public ExpiringObject<E> getObject(Object o) {
        synchronized (mutex) {
            return this.stream().filter(it -> it.getObject().equals(o)).findFirst().orElse(null);
        }
    }

    @Override
    public boolean contains(Object o) {
        synchronized (mutex) {
            return this.containsObject(o);
        }
    }

    public boolean containsObject(Object o) {
        synchronized (mutex) {
            if (o instanceof ExpiringObject<?>) {
                ExpiringObject<?> expiringObject = (ExpiringObject<?>) o;
                return this.stream().anyMatch(it -> it.getObject().equals(expiringObject.getObject()));
            }
            return false;
        }
    }
}
