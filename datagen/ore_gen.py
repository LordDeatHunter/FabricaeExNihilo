from common_datagen import *

ores = [
    ('iron', 'minecraft:raw_iron', 'NORMAL', 'GRANITE'),
    ('gold', 'minecraft:raw_gold', 'FINE', 'STONE'),
    ('copper', 'minecraft:raw_copper', 'FINE', 'SAND'),
    ('tin', 'techreborn:raw_tin', 'NORMAL', 'DIORITE'),
    ('silver', 'techreborn:raw_silver', 'NORMAL', 'STONE'),
    ('lead', 'techreborn:raw_lead', 'COARSE', 'STONE'),
    ('iridium', 'techreborn:raw_iridium', 'FINE', 'SAND'),
    ('tungsten', 'techreborn:raw_tungsten', 'COARSE', 'ENDSTONE'),
    ('platinum', 'techreborn:raw_platinum', 'COARSE', 'ENDSTONE'),

    ('tin', 'indrev:raw_tin', 'NORMAL', 'DIORITE'),
    ('silver', 'indrev:raw_silver', 'NORMAL', 'STONE'),
    ('lead', 'indrev:raw_lead', 'COARSE', 'STONE'),
    ('tungsten', 'indrev:raw_tungsten', 'COARSE', 'ENDSTONE'),

    ('antimony', 'modern_industrialization:raw_antimony', 'NORMAL', 'ANDESITE'),
    ('iridium', 'modern_industrialization:raw_iridium', 'NORMAL', 'ANDESITE'),
    ('tin', 'modern_industrialization:raw_tin', 'NORMAL', 'DIORITE'),
    ('silver', 'modern_industrialization:raw_silver', 'NORMAL', 'STONE'),
    ('lead', 'modern_industrialization:raw_lead', 'COARSE', 'STONE'),
    ('nickel', 'modern_industrialization:raw_nickel', 'COARSE', 'ANDESITE'),
    ('platinum', 'modern_industrialization:raw_platinum', 'NORMAL', 'STONE'),
    ('titanium', 'modern_industrialization:raw_titanium', 'COARSE', 'GRANITE'),
    ('tungsten', 'modern_industrialization:raw_tungsten', 'COARSE', 'ENDSTONE'),
    ('uranium', 'modern_industrialization:raw_uranium', 'COARSE', 'ANDESITE')
]


def generate():
    print('Generating ores...')
    for stats in ores:
        material, raw_ore, shape, piece_material = stats
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
