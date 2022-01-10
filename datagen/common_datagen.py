from pathlib import Path
import json
import os


def create_and_return_path(path):
    folder_path = os.path.dirname(path)
    if not os.path.exists(folder_path):
        os.makedirs(folder_path)
    if path[-1] != '/' and path[-1] != '\\':
        open(path, 'w').close()
    return path


root_path = os.path.abspath(os.path.dirname(os.path.abspath(__file__)))
block_model_path = create_and_return_path(root_path + '/generated/assets/fabricaeexnihilo/models/block/')
item_model_path = create_and_return_path(root_path + '/generated/assets/fabricaeexnihilo/models/item/')
blockstates_path = create_and_return_path(root_path + '/generated/assets/fabricaeexnihilo/blockstates/')
default_lang_file = create_and_return_path(root_path + '/generated/assets/fabricaeexnihilo/lang/en_us.json')
recipes_path = create_and_return_path(root_path + '/generated/data/fabricaeexnihilo/recipes/')
ore_piece_recipes_path = create_and_return_path(root_path + '/generated/data/fabricaeexnihilo/recipes/ore_piece/')


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
                'item': raw_ore,
                'count': 1
            }
        }, indent=2))


def add_to_lang_file(key, value):
    data = {}
    with open(default_lang_file, 'r') as f:
        try:
            data = json.load(f)
        except:
            pass
        data[key] = value
    with open(default_lang_file, 'w') as f:
        json.dump(data, f, indent=2)


def get_name(string):
    return ' '.join(capitalize_word(word) for word in string.split('_'))


def capitalize_word(word):
    return word[0].upper() + word[1:]