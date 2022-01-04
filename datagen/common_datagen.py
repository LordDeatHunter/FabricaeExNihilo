from pathlib import Path
import json
import os


def check_and_return_path(path):
    if not os.path.exists(path):
        os.makedirs(path)
    return path


root_path = os.path.abspath(os.path.dirname(os.path.abspath(__file__)))


def get_block_model_path():
    return check_and_return_path(root_path + '/assets/fabricaeexnihilo/models/block/')


def get_item_model_path():
    return check_and_return_path(root_path + '/assets/fabricaeexnihilo/models/item/')


def get_blockstates_path():
    return check_and_return_path(root_path + '/assets/fabricaeexnihilo/blockstates/')


def save_block_item_model(file_name, block_path):
    with open(f'{get_item_model_path()}{file_name}.json', 'w') as f:
        f.write(json.dumps({
            'parent': block_path
        }, indent=2))


def save_blockstate(file_name, model):
    with open(f'{get_blockstates_path()}{file_name}.json', 'w') as f:
        f.write(json.dumps({
            'variants': {
                '': {
                    'model': model
                }
            }
        }, indent=2))

def save_block_model(file_name, parent_model, textures):
    with open(f'{get_block_model_path()}{file_name}.json', 'w') as f:
        f.write(json.dumps({
            'parent': parent_model,
            'textures': textures
        }, indent=2))
