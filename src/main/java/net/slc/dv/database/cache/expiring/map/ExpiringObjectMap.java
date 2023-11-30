package net.slc.dv.database.cache.expiring.map;

import net.slc.dv.database.cache.expiring.object.ExpiringObject;

import java.util.HashMap;

public class ExpiringObjectMap<K, V> extends SynchronizedMap<ExpiringObject<K>, V> {

    public ExpiringObjectMap() {
        super(new HashMap<>());
    }

    public void putOrUpdate(ExpiringObject<K> key, V value) {
        synchronized (mutex) {
            ExpiringObject<K> existing = m.keySet().stream().filter(it -> it.getObject().equals(key.getObject())).findFirst().orElse(null);
            if (existing != null) {
                m.remove(existing);

                existing.refresh();
                m.put(existing, value);
            } else {
                m.put(key, value);
            }
        }
    }

    @Override
    public V get(Object key) {
        synchronized (mutex) {
            if (key instanceof ExpiringObject<?>) {
                ExpiringObject<?> expiringObject = (ExpiringObject<?>) key;
                return m.keySet().stream()
                        .filter(it -> it.getObject().equals(expiringObject.getObject()))
                        .findFirst()
                        .map(it -> {
                            if (it.isExpired()) {
                                return null;
                            }
                            return m.get(it);
                        })
                        .orElse(null);
            }

            return m.keySet().stream()
                    .filter(it -> it.getObject().equals(key))
                    .findFirst()
                    .map(it -> {
                        if (it.isExpired()) {
                            return null;
                        }
                        return m.get(it);
                    })
                    .orElse(null);
        }
    }

    @Override
    public V remove(Object key) {
        synchronized (mutex) {
            if (key instanceof ExpiringObject<?>) {
                ExpiringObject<?> expiringObject = (ExpiringObject<?>) key;
                return m.keySet().stream()
                        .filter(it -> it.getObject().equals(expiringObject.getObject()))
                        .findFirst()
                        .map(m::remove)
                        .orElse(null);
            }

            return m.keySet().stream()
                    .filter(it -> it.getObject().equals(key))
                    .findFirst()
                    .map(m::remove)
                    .orElse(null);
        }
    }

    @Override
    public boolean containsKey(Object key) {
        synchronized (mutex) {
            if (key instanceof ExpiringObject<?>) {
                ExpiringObject<?> expiringObject = (ExpiringObject<?>) key;
                return m.keySet().stream().anyMatch(it -> it.getObject().equals(expiringObject.getObject()));
            }
            return this.keySet().stream().anyMatch(it -> it.getObject().equals(key));
        }
    }
}
