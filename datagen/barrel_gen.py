from common_datagen import *

barrels = {
    'fabricaeexnihilo:oak_barrel': 'minecraft:oak_planks',
    'fabricaeexnihilo:spruce_barrel': 'minecraft:spruce_planks',
    'fabricaeexnihilo:birch_barrel': 'minecraft:birch_planks',
    'fabricaeexnihilo:jungle_barrel': 'minecraft:jungle_planks',
    'fabricaeexnihilo:acacia_barrel': 'minecraft:acacia_planks',
    'fabricaeexnihilo:dark_oak_barrel': 'minecraft:dark_oak_planks',
    'fabricaeexnihilo:stone_barrel': 'minecraft:stone'
}


def generate():
    for barrel, material in barrels.items():
        material_namespace, material_path = material.split(':')
        barrel_namespace, barrel_path = barrel.split(':')
        save_block_model(barrel_path, 'fabricaeexnihilo:block/barrel',
                         {'all': f'{material_namespace}:block/{material_path}'})
        save_blockstate(barrel_path, f'{barrel_namespace}:block/{barrel_path}')
        save_block_item_model(barrel_path, f'{barrel_namespace}:block/{barrel_path}')
