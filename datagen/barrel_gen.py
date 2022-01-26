from common_datagen import *

barrels = {
    ('fabricaeexnihilo:oak_barrel', 'minecraft:oak_planks', 'minecraft:oak_slab'),
    ('fabricaeexnihilo:spruce_barrel', 'minecraft:spruce_planks', 'minecraft:spruce_slab'),
    ('fabricaeexnihilo:birch_barrel', 'minecraft:birch_planks', 'minecraft:birch_slab'),
    ('fabricaeexnihilo:jungle_barrel', 'minecraft:jungle_planks', 'minecraft:jungle_slab'),
    ('fabricaeexnihilo:acacia_barrel', 'minecraft:acacia_planks', 'minecraft:acacia_slab'),
    ('fabricaeexnihilo:dark_oak_barrel', 'minecraft:dark_oak_planks', 'minecraft:dark_oak_slab'),
    ('fabricaeexnihilo:warped_barrel', 'minecraft:warped_planks', 'minecraft:warped_slab'),
    ('fabricaeexnihilo:crimson_barrel', 'minecraft:crimson_planks', 'minecraft:crimson_slab'),
    ('fabricaeexnihilo:rubber_barrel', 'techreborn:rubber_planks', 'techreborn:rubber_plank_slab'),
    ('fabricaeexnihilo:stone_barrel', 'minecraft:stone', 'minecraft:stone_slab')
}


def generate():
    print('Generating barrels...')
    for barrel_data in barrels:
        barrel, plank, slab = barrel_data
        material_namespace, material_path = plank.split(':')
        barrel_namespace, barrel_path = barrel.split(':')
        save_block_model(barrel_path, 'fabricaeexnihilo:block/barrel',
                         {'all': f'{material_namespace}:block/{material_path}'})
        save_blockstate(barrel_path, f'{barrel_namespace}:block/{barrel_path}')
        save_block_item_model(barrel_path, f'{barrel_namespace}:block/{barrel_path}')
        save_barrel_recipe(barrel_path, plank, slab)
        name = get_name(barrel_path)
        add_to_lang_file(f'block.{barrel_namespace}.{barrel_path}', name)


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
