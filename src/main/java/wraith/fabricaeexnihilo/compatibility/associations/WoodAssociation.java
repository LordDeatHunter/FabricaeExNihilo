//package wraith.fabricaeexnihilo.compatibility.associations;

/**
 * An association of identifiers to be used for generating Sieves/Crucibles/Barrels
 */
/*
data class WoodAssociation(val base:Identifier,
                      val crucible:Identifier=subId(base, "crucible"),
        val barrel:Identifier=subId(base,"barrel"),
        val sieve:Identifier=subId(base,"sieve"),
        var blocks:WoodBlocks,
        var textures:WoodTextures){
        val crucibleModel=id("block/${crucible.path}")
        val barrelModel=id("block/${barrel.path}")
        val sieveModel=id("block/${sieve.path}")

        fun buildModels(builder:ArtificeResourcePack.ClientResourcePackBuilder){
        builder.addCrucibleModel(this)
        builder.addBarrelModel(this)
        builder.addSieveModel(this)
        }

        fun buildRecipes(builder:ShapedRecipeBuilder){
        builder.addCrucibleRecipe(this)
        builder.addBarrelRecipe(this)
        builder.addSieveRecipe(this)
        }
        }

        fun subId(identifier:Identifier,suffix:String)=
        id(identifier.toString().replace(":","/")+"_"+suffix)

        data

class WoodTextures(var planks:Identifier?, var log:Identifier?, var leaves:Identifier?) {
    fun isCompletelyDefined() =!(planks ==null||log ==null||leaves ==null)

    fun ApeVanilla() {

    }
}

data

class WoodBlocks(var planks:Identifier?, var slab:Identifier?, var log:Identifier?, var leaves:Identifier?) {
    fun isCompletelyDefined() =!(planks ==null||slab ==null||log ==null||leaves ==null)
}

    fun ArtificeResourcePack.ClientResourcePackBuilder.addCrucibleModel(wood:WoodAssociation){
        (wood.textures.log)?.let{texture->
        this.addBlockModel(wood.crucible){model->
        model.parent(id("block/crucible")).texture("all",texture)}
        this.addBlockState(wood.crucible){state->
        state.variant(""){it.model(wood.crucibleModel)}}
        this.addItemModel(wood.crucible){model->
        model.parent(wood.crucibleModel)}
        }?:FabricaeExNihilo.LOGGER.warn("Failed to generate ${wood.crucible} model because log texture is undefined.")
        }

        fun ArtificeResourcePack.ClientResourcePackBuilder.addBarrelModel(wood:WoodAssociation){
        (wood.textures.planks)?.let{texture->
        this.addBlockModel(wood.barrel){model->
        model.parent(id("block/barrel")).texture("all",texture)}
        this.addBlockState(wood.barrel){state->
        state.variant(""){it.model(wood.barrelModel)}}
        this.addItemModel(wood.barrel){model->
        model.parent(wood.barrelModel)}
        }?:FabricaeExNihilo.LOGGER.warn("Failed to generate ${wood.barrel} model because planks texture is undefined.")
        }

        fun ArtificeResourcePack.ClientResourcePackBuilder.addSieveModel(wood:WoodAssociation){
        (wood.textures.planks)?.let{texture->
        this.addBlockModel(wood.sieve){model->
        model.parent(id("block/barrel")).texture("all",texture)}
        this.addBlockState(wood.sieve){state->
        state.variant(""){it.model(wood.sieveModel)}}
        this.addItemModel(wood.sieve){model->
        model.parent(wood.sieveModel)}
        }?:FabricaeExNihilo.LOGGER.warn("Failed to generate ${wood.sieve} model because planks texture is undefined.")
        }

        fun ShapedRecipeBuilder.addCrucibleRecipe(wood:WoodAssociation){
        (wood.blocks.log)?.let{log->
        this.pattern(
        "x x",
        "x x",
        "xxx")
        .ingredientItem('x',log)
        .result(wood.barrel,1)
        }?:FabricaeExNihilo.LOGGER.warn("Failed to generate ${wood.barrel} recipe because log are undefined.")
        }

        fun ShapedRecipeBuilder.addBarrelRecipe(wood:WoodAssociation){
        (wood.blocks.planks)?.let{planks->
        (wood.blocks.slab)?.let{slab->
        this.pattern(
        "x x",
        "x x",
        "xyx")
        .ingredientItem('x',planks)
        .ingredientItem('y',slab)
        .result(wood.barrel,1)
        }?:FabricaeExNihilo.LOGGER.warn("Failed to generate ${wood.barrel} recipe because slab is undefined.")
        }?:FabricaeExNihilo.LOGGER.warn("Failed to generate ${wood.barrel} recipe because planks are undefined.")
        }

        fun ShapedRecipeBuilder.addSieveRecipe(wood:WoodAssociation){
        (wood.blocks.planks)?.let{planks->
        (wood.blocks.slab)?.let{slab->
        this.pattern(
        "x x",
        "xyx",
        "z z")
        .ingredientItem('x',planks)
        .ingredientItem('y',slab)
        .ingredientItem('z',Identifier("stick"))
        .result(wood.sieve,1)
        }?:FabricaeExNihilo.LOGGER.warn("Failed to generate ${wood.sieve} recipe because slab is undefined.")
        }?:FabricaeExNihilo.LOGGER.warn("Failed to generate ${wood.sieve} recipe because planks are undefined.")
        }
 */