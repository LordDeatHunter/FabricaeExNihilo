package wraith.fabricaeexnihilo.util;

import java.util.function.Supplier;

/**
 * Evaluates the suppler only once necessary allowing creation of references without resolving the value before it is ready. For example, it can be used to store a reference to a block from another mod that may or may not be loaded after. In such cases it's important to not call the {@link #get} method before all mods are done.
 * @param <T> The type of the eventual value.
 */
public class Lazy<T> {
    private final Supplier<T> supplier;
    private T value = null;
    
    public Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    public T get() {
        return value == null ? (value = supplier.get()) : value;
    }
}
