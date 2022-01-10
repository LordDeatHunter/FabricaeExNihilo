from common_datagen import *

leaves = [
    'minecraft:oak_leaves',
    'minecraft:spruce_leaves',
    'minecraft:birch_leaves',
    'minecraft:jungle_leaves',
    'minecraft:acacia_leaves',
    'minecraft:dark_oak_leaves',
    'techreborn:rubber/rubber_leaves',
]


def generate():
    print('Generating barrels...')
    for leaf in leaves:
        namespace, path = leaf.split(':')
        proper_path = 'infested_' + path.split('/')[-1]
        save_blockstate(f'{proper_path}', f'{namespace}:block/{path}')
        save_block_item_model(f'{proper_path}', f'{namespace}:block/{path}')
        add_to_lang_file(f'block.fabricaeexnihilo.{proper_path}', get_name(proper_path))


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
