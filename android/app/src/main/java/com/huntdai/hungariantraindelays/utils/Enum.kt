package com.huntdai.hungariantraindelays.utils

fun <T : Enum<T>> T.toInt() = this.ordinal

inline fun <reified T : Enum<T>> fromInt(ordinal: Int): T = enumValues<T>()[ordinal]