from common_datagen import *

meshes = [
    ('string', '', '', ''),
    ('flint', 'minecraft:flint', 'fabricaeexnihilo:string_mesh', 'crafting'),
    ('copper', 'minecraft:copper_ingot', 'fabricaeexnihilo:flint_mesh', 'crafting'),
    ('iron', 'minecraft:iron_ingot', 'fabricaeexnihilo:flint_mesh', 'crafting'),
    ('gold', 'minecraft:gold_ingot', 'fabricaeexnihilo:copper_mesh', 'crafting'),
    ('emerald', 'minecraft:emerald', 'fabricaeexnihilo:gold_mesh', 'crafting'),
    ('diamond', 'minecraft:diamond', 'fabricaeexnihilo:iron_mesh', 'crafting'),
    ('netherite', 'minecraft:netherite_ingot', 'fabricaeexnihilo:diamond_mesh', 'smithing'),
    ('carbon', 'techreborn:carbon_fiber', 'fabricaeexnihilo:emerald_mesh', 'smithing')
]


def generate():
    print('Generating meshes...')
    save_crafting_recipe('string_mesh', 'fabricaeexnihilo:string_mesh', ['###', '###', '###'], {'#': {'item': 'minecraft:string'}})
    for mesh_data in meshes:
        proper_name = mesh_data[0] + '_mesh'
        save_item_model(proper_name, 'fabricaeexnihilo:item/mesh')
        add_to_lang_file(f'item.fabricaeexnihilo.{proper_name}', get_name(proper_name))
        if mesh_data[3] == 'smithing':
            save_smithing_recipe(proper_name, f'fabricaeexnihilo:{proper_name}', mesh_data[2], mesh_data[1])
        else:
            save_mesh_crafting_recipe(proper_name, mesh_data[1], mesh_data[2])


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
