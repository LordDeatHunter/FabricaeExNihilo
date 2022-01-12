from common_datagen import *

ores = {
    'iron': ('minecraft:raw_iron', 'NORMAL', 'GRANITE'),
    'gold': ('minecraft:raw_gold', 'FINE', 'STONE'),
    'tin': ('techreborn:raw_tin', 'NORMAL', 'DIORITE'),
    'silver': ('techreborn:raw_silver', 'NORMAL', 'STONE'),
    'lead': ('techreborn:raw_lead', 'COARSE', 'STONE'),
    'iridium': ('techreborn:raw_iridium', 'FINE', 'SAND'),
    'tungsten': ('techreborn:raw_tungsten', 'COARSE', 'ENDSTONE')
}


def generate():
    print('Generating ores...')
    for material, stats in ores.items():
        raw_ore, shape, piece_material = stats
        shape = shape.lower()
        piece_material = piece_material.lower()
        proper_name = material + '_piece'
        save_ore_piece_recipe(material, raw_ore)
        save_item_model(proper_name, 'minecraft:item/generated', {
            'layer0': f'fabricaeexnihilo:item/ore/pieces/{shape}_{piece_material}',
            'layer1': f'fabricaeexnihilo:item/ore/pieces/{shape}_overlay'
        })
        add_to_lang_file(f'item.fabricaeexnihilo.{proper_name}', get_name(proper_name))


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
