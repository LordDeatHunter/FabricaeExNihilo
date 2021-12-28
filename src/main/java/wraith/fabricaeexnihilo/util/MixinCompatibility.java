package wraith.fabricaeexnihilo.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.stream.Stream;

// If a mod ever breaks a mixin you can setup an entry here and in Plugin with a slower, more compatible mixin.
public final class MixinCompatibility{
    private MixinCompatibility(){}
    
    private record ModEntry(
        @NotNull String modId,
        @Nullable SemanticVersion version
    ){
        ModEntry{
            Objects.requireNonNull(modId, "modId can not be null");
        }
    
        private ModEntry(@NotNull String modId){
            this(modId, (SemanticVersion)null);
        }
    
        private ModEntry(@NotNull String modId, @Nullable String version) throws VersionParsingException{
            this(modId, version == null ? null : SemanticVersion.parse(version));
        }
        
        private boolean test(){
            var container = FabricLoader.getInstance().getModContainer(modId());
            if(container.isPresent()){
                if(version == null){
                    return true;
                }
                var metadata = container.get();
                // Is the mod greater than the incompatible version?
                return metadata.getMetadata().getVersion().compareTo(version) > 0;
            }
            return false;
        }
    }
    
    public enum Modes{
        // This is one of those things that is likely to break in another mod...
        BLOCK_COLOR_OVERRIDE(),
        ;
        
        private final boolean enabled;
        
        Modes(ModEntry... mods){
            enabled = Stream.of(mods).anyMatch(ModEntry::test);
        }
        
        public boolean enabled(){
            return enabled;
        }
    }
}
