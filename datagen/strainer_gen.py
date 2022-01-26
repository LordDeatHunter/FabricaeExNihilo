from common_datagen import *

strainers = [
    ('fabricaeexnihilo:oak_strainer', 'minecraft:oak_log', 'minecraft:oak_log'),
    ('fabricaeexnihilo:spruce_strainer', 'minecraft:spruce_log', 'minecraft:spruce_log'),
    ('fabricaeexnihilo:birch_strainer', 'minecraft:birch_log', 'minecraft:birch_log'),
    ('fabricaeexnihilo:jungle_strainer', 'minecraft:jungle_log', 'minecraft:jungle_log'),
    ('fabricaeexnihilo:acacia_strainer', 'minecraft:acacia_log', 'minecraft:acacia_log'),
    ('fabricaeexnihilo:dark_oak_strainer', 'minecraft:dark_oak_log', 'minecraft:dark_oak_log'),
    ('fabricaeexnihilo:warped_strainer', 'minecraft:warped_stem', 'minecraft:warped_stem'),
    ('fabricaeexnihilo:crimson_strainer', 'minecraft:crimson_stem', 'minecraft:crimson_stem'),
    ('fabricaeexnihilo:rubber_strainer', 'techreborn:rubber_log', 'techreborn:rubber_log_side')
]


def generate():
    print('Generating strainers...')
    for strainer_data in strainers:
        strainer, material, texture = strainer_data
        strainer_namespace, strainer_path = strainer.split(':')
        texture_namespace, texture_path = texture.split(':')
        save_block_model(strainer_path, 'fabricaeexnihilo:block/strainer',
                         {'pilar': f'{texture_namespace}:block/{texture_path}'})
        save_blockstate(strainer_path, f'{strainer_namespace}:block/{strainer_path}')
        save_strainer_recipe(strainer_path, material)
        save_block_item_model(strainer_path, f'{strainer_namespace}:block/{strainer_path}')
        name = get_name(strainer_path)
        add_to_lang_file(f'block.{strainer_namespace}.{strainer_path}', name)


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
