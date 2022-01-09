from pathlib import Path
import json
import os


def check_and_return_path(path):
    if not os.path.exists(path):
        os.makedirs(path)
    return path


root_path = os.path.abspath(os.path.dirname(os.path.abspath(__file__)))
block_model_path = check_and_return_path(root_path + '/generated/assets/fabricaeexnihilo/models/block/')
item_model_path = check_and_return_path(root_path + '/generated/assets/fabricaeexnihilo/models/item/')
blockstates_path = check_and_return_path(root_path + '/generated/assets/fabricaeexnihilo/blockstates/')
recipes_path = check_and_return_path(root_path + '/generated/data/fabricaeexnihilo/recipes/')
ore_piece_recipes_path = check_and_return_path(root_path + '/generated/data/fabricaeexnihilo/recipes/ore_piece/')
ore_chunk_recipes_path = check_and_return_path(root_path + '/generated/data/fabricaeexnihilo/recipes/ore_chunk/')


def save_block_item_model(file_name, block_path):
    with open(f'{item_model_path}{file_name}.json', 'w') as f:
        f.write(json.dumps({
            'parent': block_path
        }, indent=2))


def save_blockstate(file_name, model):
    with open(f'{blockstates_path}{file_name}.json', 'w') as f:
        f.write(json.dumps({
            'variants': {
                '': {
                    'model': model
                }
            }
        }, indent=2))


def save_block_model(file_name, parent_model, textures):
    with open(f'{block_model_path}{file_name}.json', 'w') as f:
        f.write(json.dumps({
            'parent': parent_model,
            'textures': textures
        }, indent=2))


def save_ore_piece_recipe(material, raw_ore):
    with open(f'{ore_piece_recipes_path}{material}.json', 'w') as f:
        f.write(json.dumps({
            'type': 'minecraft:crafting_shaped',
            'pattern': ['##', '##'],
            'key': {
                '#': {'item': f'fabricaeexnihilo:{material}_piece'}
            },
            'result': {
                'item': f'fabricaeexnihilo:{raw_ore}',
                'count': 1
            }
        }, indent=2))
