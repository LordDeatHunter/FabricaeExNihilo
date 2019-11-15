package exnihilofabrico.util

import net.minecraft.state.AbstractPropertyContainer

fun <S : Any?, O : Any?> AbstractPropertyContainer<O, S>.get(name: String) =
    properties.firstOrNull { name == it.getName() }
