package wraith.fabricaeexnihilo.api;

import net.minecraft.block.LeavesBlock;
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
    void createInfestedLeavesBlock(LeavesBlock block);

    /**
     * plankID and slabID are used to generate a crafting recipe
     * Default assumption is the texture shares a name with the planks and is under modid:block
     *
     * Generated Identifier = fabricaeexnihilo:<plankID.namespace>_<plankID.path> with "planks" replaced by "sieve"
     */
    void createSieveBlock(Identifier plankID, Identifier slabID, Identifier tex);
    default void createSieveBlock(Identifier plankID, Identifier slabID) {
        this.createSieveBlock(plankID, slabID, new Identifier(plankID.getNamespace(), "block/" + plankID.getPath()));
    }

    /**
     * plankID and slabID are used to generate a crafting recipe
     * Default assumption is the texture shares a name with the planks and is under modid:block
     *
     * Generated Identifier = fabricaeexnihilo:<plankID.namespace>_<plankID.path> with "planks" replaced by "barrel"
     */
    void createWoodBarrelBlock(Identifier plankID, Identifier slabID, Identifier tex);
    default void createWoodBarrelBlock(Identifier plankID, Identifier slabID) {
        this.createWoodBarrelBlock(plankID, slabID, new Identifier(plankID.getNamespace(), "block/" + plankID.getPath()));
    }

    /**
     * Provide the log you want to be used as the crafting ingredient
     * Default assumption is the texture shares a name and is under modid:block
     *
     * Generated Identifier = fabricaeexnihilo:<logID.namespace>_<logID.path> with "log" replaced by "crucible"
     */
    void createWoodCrucibleBlock(Identifier logID, Identifier logTex);
    default void createWoodCrucibleBlock(Identifier logID) {
        this.createWoodCrucibleBlock(logID, new Identifier(logID.getNamespace(), "block/" + logID.getPath()));
    }

}