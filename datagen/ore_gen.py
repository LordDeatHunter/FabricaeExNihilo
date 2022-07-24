from common_datagen import *

ores = [
    ('iron', 'minecraft:raw_iron', 'GRANITE'),
    ('gold', 'minecraft:raw_gold', 'STONE'),
    ('copper', 'minecraft:raw_copper', 'SAND'),
    ('tin', 'techreborn:raw_tin', 'DIORITE'),
    ('silver', 'techreborn:raw_silver', 'STONE'),
    ('lead', 'techreborn:raw_lead', 'STONE'),
    ('iridium', 'techreborn:raw_iridium', 'SAND'),
    ('tungsten', 'techreborn:raw_tungsten', 'ENDSTONE'),
    ('platinum', 'techreborn:raw_platinum', 'ENDSTONE'),

    ('tin', 'indrev:raw_tin', 'DIORITE'),
    ('silver', 'indrev:raw_silver', 'STONE'),
    ('lead', 'indrev:raw_lead', 'STONE'),
    ('tungsten', 'indrev:raw_tungsten', 'ENDSTONE'),

    ('antimony', 'modern_industrialization:raw_antimony', 'ANDESITE'),
    ('iridium', 'modern_industrialization:raw_iridium', 'ANDESITE'),
    ('tin', 'modern_industrialization:raw_tin', 'DIORITE'),
    ('silver', 'modern_industrialization:raw_silver', 'STONE'),
    ('lead', 'modern_industrialization:raw_lead', 'STONE'),
    ('nickel', 'modern_industrialization:raw_nickel', 'ANDESITE'),
    ('platinum', 'modern_industrialization:raw_platinum', 'STONE'),
    ('titanium', 'modern_industrialization:raw_titanium', 'GRANITE'),
    ('tungsten', 'modern_industrialization:raw_tungsten', 'ENDSTONE'),
    ('uranium', 'modern_industrialization:raw_uranium', 'ANDESITE'),

    ('aquarium', 'mythic_metals:raw_aquarium', 'AQUARIUM'),
    ('banglum', 'mythic_metals:raw_banglum', 'BANGLUM'),
    ('kyber', 'mythic_metals:raw_kyber', 'KYBER'),
    ('manganese', 'mythic_metals:raw_manganese', 'MANGANESE'),
    ('midas_gold', 'mythic_metals:raw_midas_gold', 'MIDAS_GOLD'),
    ('osmium', 'mythic_metals:raw_osmium', 'OSMIUM'),
    ('quadrillum', 'mythic_metals:raw_quadrillum', 'QUADRILLUM'),
    ('runite', 'mythic_metals:raw_runite', 'RUNITE'),
    ('stormyx', 'mythic_metals:raw_stormyx', 'STORMYX'),
    ('adamantite', 'mythic_metals:raw_adamantite', 'ADAMANTITE'),
    ('carmot', 'mythic_metals:raw_carmot', 'CARMOT'),
    ('mythril', 'mythic_metals:raw_mythril', 'MYTHRIL'),
    ('orichalcum', 'mythic_metals:raw_orichalcum', 'ORICHALCUM'),
    ('prometheum', 'mythic_metals:raw_prometheum', 'PROMETHEUM'),
    ('palladium', 'mythic_metals:raw_palladium', 'PALLADIUM'),
]

def generate():
    print('Generating ores...')
    for stats in ores:
        material, raw_ore, piece_material = stats
        piece_material = piece_material.lower()
        proper_name = f'raw_{material}_piece'
        save_ore_piece_recipe(material, raw_ore)
        save_item_model(proper_name, 'minecraft:item/generated', {
            'layer0': f'fabricaeexnihilo:item/ore/raw_{piece_material}_piece'
        })
        add_to_lang_file(f'item.fabricaeexnihilo.{proper_name}', get_name(proper_name))


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
