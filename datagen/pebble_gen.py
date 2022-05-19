from common_datagen import *

pebbles = [
    'minecraft:andesite',
    'minecraft:diorite',
    'minecraft:granite',
    'minecraft:stone',
    'minecraft:blackstone',
    'minecraft:basalt',
    'minecraft:deepslate'
]


def generate():
    print('Generating pebble recipes...')
    for pebble in pebbles:
        save_pebble_recipe(pebble)


if __name__ == "__main__":
    print("Running in standalone mode.")
    generate()
