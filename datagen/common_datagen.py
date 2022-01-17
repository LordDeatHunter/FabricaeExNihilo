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


def save_model(model_type, file_name, parent_model, textures={}):
    path = block_model_path if model_type.lower() == 'block' else item_model_path
    with open(f'{path}{file_name}.json', 'w') as f:
        data = {
            'parent': parent_model
        }
        if len(textures) > 0:
            data['textures'] = textures
        f.write(json.dumps(data, indent=2))


def save_block_model(file_name, parent_model, textures={}):
    save_model('block', file_name, parent_model, textures)


def save_item_model(file_name, parent_model, textures={}):
    save_model('item', file_name, parent_model, textures)


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
            data = json.loads(f.read())
        except:
            pass
        data[key] = value
    with open(default_lang_file, 'w') as f:
        json.dump(data, f, indent=2)


def get_name(string):
    return ' '.join(capitalize_word(word) for word in string.split('_'))


def capitalize_word(word):
    return word[0].upper() + word[1:]


def item_or_tag(entry):
    if entry.startswith('#'):
        return {'tag': entry[1:]}
    return {'item': entry}


def save_crafting_recipe(file_name, result, pattern=[], key={}, amount=1):
    if not pattern or not key:
        return
    with open(f'{recipes_path}{file_name}.json', 'w') as f:
        f.write(json.dumps({
            'type': 'minecraft:crafting_shaped',
            'pattern': pattern,
            'key': key,
            'result': {
                'item': result,
                'count': amount
            }
        }, indent=2))


def save_smithing_recipe(file_name, result, input='', output=''):
    if not input or not output:
        return
    with open(f'{recipes_path}{file_name}.json', 'w') as f:
        f.write(json.dumps({
            'type': 'minecraft:smithing',
            'base': item_or_tag(input),
            'addition': item_or_tag(output),
            'result': {'item': result}
        }, indent=2))


def save_barrel_recipe(file_name, plank, slab):
    pattern = [
        '# #',
        '# #',
        '#S#'
    ]
    key = {
        '#': item_or_tag(plank),
        'S': item_or_tag(slab)
    }
    save_crafting_recipe(file_name, f'fabricaeexnihilo:{file_name}', pattern, key, 1)


def save_sieve_recipe(file_name, plank, slab):
    pattern = [
        '# #',
        '#S#',
        'I I'
    ]
    key = {
        '#': item_or_tag(plank),
        'S': item_or_tag(slab),
        'I': {'item': 'minecraft:stick'}
    }
    save_crafting_recipe(file_name, f'fabricaeexnihilo:{file_name}', pattern, key, 1)


def save_crucible_recipe(file_name, material, slab):
    pattern = [
        '# #',
        '#S#',
        'I I'
    ]
    key = {
        '#': item_or_tag(material),
        'S': item_or_tag(slab),
        'I': {'item': 'minecraft:stick'}
    }
    save_crafting_recipe(file_name, f'fabricaeexnihilo:{file_name}', pattern, key, 1)


def save_mesh_crafting_recipe(new_mesh, material, old_mesh):
    if material == '' and old_mesh == '':
        return
    pattern = [
        '# #',
        '#O#',
        '# #'
    ]
    key = {
        '#': item_or_tag(material),
        'O': item_or_tag(old_mesh)
    }
    save_crafting_recipe(new_mesh, f'fabricaeexnihilo:{new_mesh}', pattern, key, 1)
