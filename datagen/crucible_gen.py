from common_datagen import *

crucibles = [
    ('fabricaeexnihilo:oak_crucible', 'minecraft:oak_log', 'minecraft:oak_log', 'minecraft:oak_slab'),
    ('fabricaeexnihilo:spruce_crucible', 'minecraft:spruce_log', 'minecraft:spruce_log', 'minecraft:spruce_slab'),
    ('fabricaeexnihilo:birch_crucible', 'minecraft:birch_log', 'minecraft:birch_log', 'minecraft:birch_slab'),
    ('fabricaeexnihilo:jungle_crucible', 'minecraft:jungle_log', 'minecraft:jungle_log', 'minecraft:jungle_slab'),
    ('fabricaeexnihilo:acacia_crucible', 'minecraft:acacia_log', 'minecraft:acacia_log', 'minecraft:acacia_slab'),
    ('fabricaeexnihilo:dark_oak_crucible', 'minecraft:dark_oak_log', 'minecraft:dark_oak_log', 'minecraft:dark_oak_slab'),
    ('fabricaeexnihilo:warped_crucible', 'minecraft:warped_stem', 'minecraft:warped_stem', 'minecraft:warped_slab'),
    ('fabricaeexnihilo:crimson_crucible', 'minecraft:crimson_stem', 'minecraft:crimson_stem', 'minecraft:crimson_slab'),
    ('fabricaeexnihilo:rubber_crucible', 'techreborn:rubber_log_side', 'techreborn:rubber_log', 'techreborn:rubber_plank_slab'),
    ('fabricaeexnihilo:porcelain_crucible', 'minecraft:stone', '', '')
]


def generate():
    print('Generating crucibles...')
    for crucible_data in crucibles:
        crucible, texture, material, slab = crucible_data
        material_namespace, material_path = texture.split(':')
        crucible_namespace, crucible_path = crucible.split(':')
        save_block_model(crucible_path, 'fabricaeexnihilo:block/crucible',
                         {'all': f'{material_namespace}:block/{material_path}'})
        save_blockstate(crucible_path, f'{crucible_namespace}:block/{crucible_path}')
        save_block_item_model(crucible_path, f'{crucible_namespace}:block/{crucible_path}')
        if material and slab:
            save_crucible_recipe(crucible_path, material, slab)
        name = get_name(crucible_path)
        add_to_lang_file(f'block.{crucible_namespace}.{crucible_path}', name)


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
