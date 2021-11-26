package wraith.fabricaeexnihilo.api.registry;

public interface IRegistry<T> {
    void clear();
    boolean register(T recipe);
}