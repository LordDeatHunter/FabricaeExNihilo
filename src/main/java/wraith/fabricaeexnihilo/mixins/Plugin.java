package wraith.fabricaeexnihilo.mixins;

import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import wraith.fabricaeexnihilo.util.MixinCompatibility;

public class Plugin implements IMixinConfigPlugin{
    @Override
    public void onLoad(String mixinPackage){
    
    }
    
    @Override
    public String getRefMapperConfig(){
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName){
        return switch(mixinClassName){
            case "wraith.fabricaeexnihilo.mixins.client.FastBlockModelRendererMixin" -> !MixinCompatibility.Modes.BLOCK_COLOR_OVERRIDE.enabled();
            case "wraith.fabricaeexnihilo.mixins.client.SlowBlockModelRendererMixin" -> MixinCompatibility.Modes.BLOCK_COLOR_OVERRIDE.enabled();
            default -> true;
        };
    }
    
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets){}
    
    @Override
    public List<String> getMixins(){
        return List.of();
    }
    
    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo){}
    
    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo){}
}
