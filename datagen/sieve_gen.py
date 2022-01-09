from common_datagen import *

sieves = {
    'fabricaeexnihilo:oak_sieve': 'minecraft:oak_planks',
    'fabricaeexnihilo:spruce_sieve': 'minecraft:spruce_planks',
    'fabricaeexnihilo:birch_sieve': 'minecraft:birch_planks',
    'fabricaeexnihilo:jungle_sieve': 'minecraft:jungle_planks',
    'fabricaeexnihilo:acacia_sieve': 'minecraft:acacia_planks',
    'fabricaeexnihilo:dark_oak_sieve': 'minecraft:dark_oak_planks',
    'fabricaeexnihilo:rubber_sieve': 'techreborn:rubber_planks'
}


def generate():
    print('Generating sieves...')
    for sieve, material in sieves.items():
        material_namespace, material_path = material.split(':')
        sieve_namespace, sieve_path = sieve.split(':')
        save_block_model(sieve_path, 'fabricaeexnihilo:block/sieve',
                         {'all': f'{material_namespace}:block/{material_path}'})
        save_blockstate(sieve_path, f'{sieve_namespace}:block/{sieve_path}')
        save_block_item_model(sieve_path, f'{sieve_namespace}:block/{sieve_path}')
        name = get_name(sieve_path)
        add_to_lang_file(f'block.{sieve_namespace}.{sieve_path}', name)


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
