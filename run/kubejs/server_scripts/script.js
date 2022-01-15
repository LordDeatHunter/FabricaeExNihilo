// priority: 0

settings.logAddedRecipes = true
settings.logRemovedRecipes = true
settings.logSkippedRecipes = false
settings.logErroringRecipes = true

console.info('Hello, World! (You will see this line every time server resources reload)')

onEvent('recipes', event => {
    event.recipes.fabricaeexnihilo.sieve("kubejs:test_block", "kubejs:crushed_test_block", "minecraft:empty", {"fabricaeexnihilo:string_mesh": [1, 0.5]})
	event.recipes.fabricaeexnihilo.hammer("kubejs:crushed_test_block", [1.0], "kubejs:test_block")

	event.recipes.fabricaeexnihilo.crucible(Fluid.of("minecraft:lava", 81000), "kubejs:crushed_test_block")
	event.recipes.fabricaeexnihilo.crucible_heat("kubejs:test_block", 10)

	event.recipes.fabricaeexnihilo.alchemy("minecraft:water", "minecraft:oak_sapling").delay(20).toSpawn("minecraft:pig").result({id: "item", stack: "kubejs:test_block"})
	event.recipes.fabricaeexnihilo.compost("kubejs:test_block", "kubejs:crushed_test_block", 4, "AA33AA")
	event.recipes.fabricaeexnihilo.fluid_combination({id: "fluid", fluid: "minecraft:water", amount: 40500}, "#c:blood", "minecraft:lava")
	event.recipes.fabricaeexnihilo.fluid_transformation({id: "item", stack: "kubejs:test_block"}, "#c:blood", "kubejs:crushed_test_block")
	event.recipes.fabricaeexnihilo.leaking("kubejs:test_block", "kubejs:crushed_test_block", "#c:saltwater", 100)
	event.recipes.fabricaeexnihilo.milking("fabricaeexnihilo:milk", "minecraft:creeper", 81000, 1200)

	event.recipes.fabricaeexnihilo.witch_water_entity("minecraft:creeper", "minecraft:cod")
	event.recipes.fabricaeexnihilo.witch_water_world({"minecraft:sponge": 1, "minecraft:wet_sponge": 2}, "minecraft:lava")
})

onEvent('item.tags', event => {
	// Get the #forge:cobblestone tag collection and add Diamond Ore to it
	// event.get('forge:cobblestone').add('minecraft:diamond_ore')

	// Get the #forge:cobblestone tag collection and remove Mossy Cobblestone from it
	// event.get('forge:cobblestone').remove('minecraft:mossy_cobblestone')
})