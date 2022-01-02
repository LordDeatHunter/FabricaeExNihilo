package wraith.fabricaeexnihilo.api;

import net.minecraft.util.Identifier;

/**
 * Several functions to generate FabricaeExNihilo blocks based on other blocks
 *
 * These functions if called before Fabricae Ex Nihilo registers its blocks will cause Fabricae Ex Nihilo to register them.
 *
 */
public interface BlockGenerator {
    /**
     * Black list a mod from auto generation
     */
    void blacklistMod(String modid);

    /**
     * Generated Identifier = exnihilofabric:infested_<blockID.namespace>_<blockID.path>
     */
    void createInfestedLeavesBlock(Identifier block);
    
}