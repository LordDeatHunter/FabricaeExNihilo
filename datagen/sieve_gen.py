from common_datagen import *

sieves = [
    ('fabricaeexnihilo:oak_sieve', 'minecraft:oak_planks', 'minecraft:oak_slab'),
    ('fabricaeexnihilo:spruce_sieve', 'minecraft:spruce_planks', 'minecraft:spruce_slab'),
    ('fabricaeexnihilo:birch_sieve', 'minecraft:birch_planks', 'minecraft:birch_slab'),
    ('fabricaeexnihilo:jungle_sieve', 'minecraft:jungle_planks', 'minecraft:jungle_slab'),
    ('fabricaeexnihilo:acacia_sieve', 'minecraft:acacia_planks', 'minecraft:acacia_slab'),
    ('fabricaeexnihilo:dark_oak_sieve', 'minecraft:dark_oak_planks', 'minecraft:dark_oak_slab'),
    ('fabricaeexnihilo:warped_sieve', 'minecraft:warped_planks', 'minecraft:warped_slab'),
    ('fabricaeexnihilo:crimson_sieve', 'minecraft:crimson_planks', 'minecraft:crimson_slab'),
    ('fabricaeexnihilo:rubber_sieve', 'techreborn:rubber_planks', 'techreborn:rubber_plank_slab')
]


def generate():
    print('Generating sieves...')
    for sieve_data in sieves:
        sieve, material, slab = sieve_data
        material_namespace, material_path = material.split(':')
        sieve_namespace, sieve_path = sieve.split(':')
        save_block_model(sieve_path, 'fabricaeexnihilo:block/sieve',
                         {'all': f'{material_namespace}:block/{material_path}'})
        save_blockstate(sieve_path, f'{sieve_namespace}:block/{sieve_path}')
        save_block_item_model(sieve_path, f'{sieve_namespace}:block/{sieve_path}')
        save_sieve_recipe(sieve_path, material, slab)
        name = get_name(sieve_path)
        add_to_lang_file(f'block.{sieve_namespace}.{sieve_path}', name)


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
