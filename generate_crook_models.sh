#!/usr/bin/env bash

modid=exnihilocreatio
texture_dir=src/main/resources/assets/${modid}/textures/items/tools/crook
models_dir=src/main/resources/assets/${modid}/models/item

for i in ${texture_dir}/crook_*.png; do
    echo $i
    asset=`basename $i .png`
    model=${models_dir}/${asset}.json
    touch model
    cat > $model << EOL
{
  "parent": "item/handheld",
  "textures": {
    "layer0": "${modid}:items/tools/crook/${asset}"
  }
}
EOL
done