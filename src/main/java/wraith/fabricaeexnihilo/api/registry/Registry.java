package wraith.fabricaeexnihilo.api.registry;

public interface Registry<T> {
    void clear();
    boolean register(T recipe);
}