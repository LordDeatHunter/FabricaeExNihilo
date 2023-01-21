![FEN](/assets/FEN.png)\
[![curseforge](https://cf.way2muchnoise.eu/596911.svg?badge_style=for_the_badge)](https://www.curseforge.com/minecraft/mc-mods/fabricae-ex-nihilo)
[![modrinth](https://img.shields.io/modrinth/dt/fabricae-ex-nihilo?color=00AF5C&label=Modrinth&logo=modrinth&style=for-the-badge)](https://modrinth.com/mod/fabricae-ex-nihilo)
[![discord](https://img.shields.io/discord/760524772189798431?label=wraith%20coding%20sesh&logo=discord&logoColor=white&color=5662f6&style=for-the-badge)](https://discord.gg/e5r7kuKRpY)

Ex Nihilo for Fabric (Minecraft 1.18.2 onwards)

Fabricae Ex Nihilo is a fork of Ex Nihilo Fabrico,\
which was a fork of Ex Nihilo Creatio,\
which was a fork of Ex Nihilo Adscensio for 1.12,\
which was itself a continuation of the Ex Nihilo mod from 1.7.10, rewritten from the ground up.

## Ex Nihilo History

There have been many mods with the name "Ex Nihilo", below is the short lineage of mods with this name:

* 1.18.1 Fabric - Fabricae Ex Nihilo (LordDeatHunter, MattiDragon)
* 1.15 - 1.18 Forge - [Ex Nihilo Sequentia](https://github.com/NovaMachina-Mods/ExNihiloSequentia)
* 1.14.4 Ex Nihilo Fabrico (SirLyle)
* 1.12.* Ex Nihilo Creatio, direct fork of Adscensio (BloodWorkXGaming, SirLyle)
* 1.10.* Ex Nihilo Adscensio (insaneau)
* 1.10.* Ex Nihilo Omnia (jozufozu)
* 1.8 Ex Nihilo 2 (Erasmus_Crowley)
* 1.7.10 Ex Nihilo (Erasmus_Crowley)

## Features

* Barrels
  * Alchemy
  * Bleeding
  * Leaking
  * Milking
  * Transforming
* Crucibles
* Strainers
* Tools
  * Crooks
  * Hammers

## License
Fabricae Ex Nihilo is licensed under the MIT license (see the license file)

## Development
Some notes for people who want to develop this mod:
 * Change the `dev_settings.properties` file to toggle dependencies. An intellij refresh is required to apply the changes. If the file is missing, run any gradle command to generate it.
 * We use fabric datagen to generate most of our assets. 
   * To change something you need to modify the classes under `wraith.fabricaeexnihilo.datagen` and run the `Data Generation` run configuration or the `runDatagen` gradle task.
   * Data generation gets stuck if KubeJS is enabled in the dependencies due to a bug
