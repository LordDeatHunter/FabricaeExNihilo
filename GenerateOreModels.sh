#!/usr/bin/env bash

for i in "chunk" "piece"; do
    for name in src/main/resources/assets/exnihilofabrico/textures/item/ore/${i}s/*.png; do
        bn=`basename ${name} .png`
        overlay=`echo ${bn} | cut -d_ -f1`
        model="src/main/resources/assets/exnihilofabrico/models/item/ore/${i}/${bn}.json"
        touch ${model}
        cat > ${model} <<EOL
{
  "parent": "item/generated",
  "textures": {
    "layer0": "exnihilofabrico:item/ore/${i}s/${bn}",
    "layer1": "exnihilofabrico:item/ore/${i}s/${overlay}_overlay"
  }
}

EOL
    done
done
