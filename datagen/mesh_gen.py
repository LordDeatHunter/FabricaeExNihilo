from common_datagen import *

meshes = [
    'string',
    'flint',
    'copper',
    'iron',
    'gold',
    'emerald',
    'diamond',
    'netherite',
    'carbon'
]


def generate():
    print('Generating meshes...')
    for material in meshes:
        proper_name = material + '_mesh'
        save_item_model(proper_name, 'fabricaeexnihilo:item/mesh')
        add_to_lang_file(f'item.fabricaeexnihilo.{proper_name}', get_name(proper_name))


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
