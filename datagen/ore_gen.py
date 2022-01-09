from common_datagen import *

ores = {
    'iron': 'minecraft:raw_iron',
    'gold': 'minecraft:raw_gold',
    'tin': 'techreborn:raw_tin',
    'silver': 'techreborn:raw_silver',
    'lead': 'techreborn:raw_lead',
    'iridium': 'techreborn:raw_iridium',
    'tungsten': 'techreborn:raw_tungsten'
}


def generate():
    print('Generating ores...')
    for material, raw_ore in ores.items():
        save_ore_piece_recipe(material, raw_ore)
        add_to_lang_file(f'item.fabricaeexnihilo.{material}_piece', get_name(material + '_piece'))


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
