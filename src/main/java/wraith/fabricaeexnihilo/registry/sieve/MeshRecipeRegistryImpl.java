package wraith.fabricaeexnihilo.registry.sieve;

import com.google.common.reflect.TypeToken;
import net.minecraft.util.registry.Registry;
import wraith.fabricaeexnihilo.api.registry.MeshRecipeRegistry;
import wraith.fabricaeexnihilo.compatibility.modules.MetaModule;
import wraith.fabricaeexnihilo.modules.sieves.MeshProperties;
import wraith.fabricaeexnihilo.registry.AbstractRegistry;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MeshRecipeRegistryImpl extends AbstractRegistry<List<MeshProperties>> implements MeshRecipeRegistry {

    private final List<MeshProperties> registry;

    public MeshRecipeRegistryImpl() {
        this(new ArrayList<>());
    }

    public MeshRecipeRegistryImpl(List<MeshProperties> registry) {
        this.registry = registry;
    }

    @Override
    public void clear() {
        registry.clear();
    }

    @Override
    public boolean register(MeshProperties recipe) {
        return registry.add(recipe);
    }

    @Override
    public List<MeshProperties> getAll() {
        return registry;
    }

    public void registerItems() {
        registry.forEach(recipe -> Registry.register(Registry.ITEM, recipe.getIdentifier(), recipe.getItem()));
    }

    @Override
    protected List<MeshProperties> serializable() {
        return registry;
    }

    @Override
    public void registerJson(File file) {
        if (file.exists()){
            try (var reader = new FileReader(file)) {
                List<MeshProperties> json = gson.fromJson(
                        reader,
                        SERIALIZATION_TYPE
                );
                json.forEach(this::register);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final Type SERIALIZATION_TYPE = new TypeToken<List<MeshProperties>>(){}.getType();

    public static MeshRecipeRegistry fromJson(File file) {
        return fromJson(file, MeshRecipeRegistryImpl::new, MetaModule.INSTANCE::registerMesh);
    }

}