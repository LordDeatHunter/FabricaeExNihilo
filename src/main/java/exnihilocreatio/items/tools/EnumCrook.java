package exnihilocreatio.items.tools;

import exnihilocreatio.config.ModConfig;
import lombok.Getter;

public enum EnumCrook {
    WOOD("wood", 64, 0),

    STONE("stone", 128, 1),
    ANDESITE("andesite", 128, 1),
    GRANITE("granite", 128, 1),
    DIORITE("diorite", 128, 1),

    GOLD("gold", 32, 2),
    IRON("iron", 256, 2),
    DIAMOND("diamond", 2048, 3),

    BONE("bone", 256, 1),
    CLAY_UNCOOKED("clay_uncooked", 0, 0),
    CLAY("clay", 256, 0),

    PRISMARINE("prismarine", 512, 2),
    NETHERRACK("netherrack", 512, 2),
    PURPUR("purpur", 512, 2),

    BLAZE("blaze", 1024, 3);

    @Getter
    String registryName;
    @Getter
    int defaultDurability;
    @Getter
    int defaultLevel;

    EnumCrook(String name, int durability, int miningLevel) {
        this.registryName = "crook_"+name;
        this.defaultDurability = durability;
        this.defaultLevel = miningLevel;
    }

    public int getDurability(){
        return ModConfig.crooking.durability.get(this.getRegistryName());
    }
}
