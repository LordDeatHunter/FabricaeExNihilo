from common_datagen import *

ores = {
    'iron': 'minecraft:raw_iron',
    'gold': 'minecraft:raw_gold',
    'tin': 'techreborn:raw_tin',
    'silver': 'techreborn:raw_silver',
    'lead': 'techreborn:raw_lead',
    'aluminum': 'fabricaeexnihilo:aluminum_chunk',
    'iridium': 'techreborn:raw_iridium',
    'zinc': 'fabricaeexnihilo:zinc_chunk',
    'tungsten': 'techreborn:raw_tungsten',
    'platinum': 'fabricaeexnihilo:platinum_chunk',
}


def generate():
    for material, raw_ore in ores.items():
        save_ore_piece_recipe(material, raw_ore)
