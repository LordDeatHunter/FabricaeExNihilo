package wraith.fabricaeexnihilo.api.registry;

public interface RecipeRegistry<T> {
    void clear();
    boolean register(T recipe);
}