from common_datagen import *

strainers = {
    'fabricaeexnihilo:oak_strainer': 'minecraft:oak_log',
    'fabricaeexnihilo:spruce_strainer': 'minecraft:spruce_log',
    'fabricaeexnihilo:birch_strainer': 'minecraft:birch_log',
    'fabricaeexnihilo:jungle_strainer': 'minecraft:jungle_log',
    'fabricaeexnihilo:acacia_strainer': 'minecraft:acacia_log',
    'fabricaeexnihilo:dark_oak_strainer': 'minecraft:dark_oak_log',
    'fabricaeexnihilo:rubber_strainer': 'techreborn:rubber_log',
}


def generate():
    print('Generating strainers...')
    for strainer, material in strainers.items():
        material_namespace, material_path = material.split(':')
        strainer_namespace, strainer_path = strainer.split(':')
        save_block_model(strainer_path, 'fabricaeexnihilo:block/strainer',
                         {'pilar': f'{material_namespace}:block/{material_path}'})
        save_blockstate(strainer_path, f'{strainer_namespace}:block/{strainer_path}')
        save_block_item_model(strainer_path, f'{strainer_namespace}:block/{strainer_path}')
        name = get_name(strainer_path)
        add_to_lang_file(f'block.{strainer_namespace}.{strainer_path}', name)


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
