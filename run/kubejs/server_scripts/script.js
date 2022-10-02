// priority: 0

settings.logAddedRecipes = true
settings.logRemovedRecipes = true
settings.logSkippedRecipes = false
settings.logErroringRecipes = true

console.info('Hello, World! (You will see this line every time server resources reload)')

ServerEvents.recipes(event => {
    event.recipes.fabricaeexnihilo.sieve("minecraft:dirt", "minecraft:stone", {"fabricaeexnihilo:string_mesh": [1, 0.5]})
	event.recipes.fabricaeexnihilo.hammer("minecraft:dirt", [1.0], ["minecraft:gravel", "minecraft:sand"])
	event.recipes.fabricaeexnihilo.hammer("minecraft:dirt", [1.0], "#minecraft:sand")

	event.recipes.fabricaeexnihilo.crucible(Fluid.of("minecraft:lava", 81000), "minecraft:emerald_block")
    event.recipes.fabricaeexnihilo.crucible_heat("minecraft:dirt", 10)

	event.recipes.fabricaeexnihilo.alchemy("minecraft:water", "minecraft:oak_sapling").delay(20).toSpawn("minecraft:pig").result({id: "item", stack: "minecraft:dirt"})
	event.recipes.fabricaeexnihilo.compost("minecraft:dirt", "minecraft:emerald_block", 4, "AA33AA")
	event.recipes.fabricaeexnihilo.fluid_combination({id: "fluid", fluid: "minecraft:water", amount: 40500}, "#c:blood", "minecraft:lava")
	event.recipes.fabricaeexnihilo.fluid_transformation({id: "item", stack: "minecraft:dirt"}, "#c:blood", "minecraft:emerald_block")
	event.recipes.fabricaeexnihilo.leaking("minecraft:dirt", "minecraft:emerald_block", "#c:brine", 100)
	event.recipes.fabricaeexnihilo.milking("fabricaeexnihilo:milk", "minecraft:creeper", 81000, 1200)

	event.recipes.fabricaeexnihilo.witch_water_entity("minecraft:creeper", "minecraft:cod")
	event.recipes.fabricaeexnihilo.witch_water_world({"minecraft:sponge": 1, "minecraft:wet_sponge": 2}, "minecraft:lava")
})