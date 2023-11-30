package net.slc.dv.database.cache.expiring.object;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class ExpiringObject<T> {

    private long created = System.currentTimeMillis();

    @Setter
    private long expirationTime;
    @Setter
    private T object;

    public ExpiringObject(T object, long expirationTime) {
        this.object = object;
        this.expirationTime = expirationTime;
    }


    public boolean isExpired() {
        return System.currentTimeMillis() >= created + expirationTime;
    }

    public long timeLeft() {
        return created + expirationTime - System.currentTimeMillis();
    }

    public void refresh() {
        created = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpiringObject<?> that = (ExpiringObject<?>) o;
        return object.equals(that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(created, object);
    }
}
