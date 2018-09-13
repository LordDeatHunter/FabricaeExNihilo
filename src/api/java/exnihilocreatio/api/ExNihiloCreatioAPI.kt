package exnihilocreatio.api

import exnihilocreatio.api.registries.*
import exnihilocreatio.registries.manager.ExNihiloRegistryManager

object ExNihiloCreatioAPI {
    //---------------------------------------------------------------
    //                        ALL REGISTRIES
    //---------------------------------------------------------------
    val COMPOST_REGISTRY: ICompostRegistry = ExNihiloRegistryManager.COMPOST_REGISTRY
    val CROOK_REGISTRY: ICrookRegistry = ExNihiloRegistryManager.CROOK_REGISTRY
    val SIEVE_REGISTRY: ISieveRegistry = ExNihiloRegistryManager.SIEVE_REGISTRY
    val HAMMER_REGISTRY: IHammerRegistry = ExNihiloRegistryManager.HAMMER_REGISTRY
    val HEAT_REGISTRY: IHeatRegistry = ExNihiloRegistryManager.HEAT_REGISTRY
    val ORE_REGISTRY: IOreRegistry = ExNihiloRegistryManager.ORE_REGISTRY
    val BARREL_LIQUID_BLACKLIST_REGISTRY: IBarrelLiquidBlacklistRegistry = ExNihiloRegistryManager.BARREL_LIQUID_BLACKLIST_REGISTRY
    val FLUID_ON_TOP_REGISTRY: IFluidOnTopRegistry = ExNihiloRegistryManager.FLUID_ON_TOP_REGISTRY
    val FLUID_TRANSFORM_REGISTRY: IFluidTransformRegistry = ExNihiloRegistryManager.FLUID_TRANSFORM_REGISTRY
    val FLUID_BLOCK_TRANSFORMER_REGISTRY: IFluidBlockTransformerRegistry = ExNihiloRegistryManager.FLUID_BLOCK_TRANSFORMER_REGISTRY
    val FLUID_ITEM_FLUID_REGISTRY: IFluidItemFluidRegistry = ExNihiloRegistryManager.FLUID_ITEM_FLUID_REGISTRY
    val CRUCIBLE_STONE_REGISTRY: ICrucibleRegistry = ExNihiloRegistryManager.CRUCIBLE_STONE_REGISTRY
    val CRUCIBLE_WOOD_REGISTRY: ICrucibleRegistry = ExNihiloRegistryManager.CRUCIBLE_WOOD_REGISTRY
    val MILK_ENTITY_REGISTRY: IMilkEntityRegistry = ExNihiloRegistryManager.MILK_ENTITY_REGISTRY
}