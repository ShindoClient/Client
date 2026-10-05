package com.shindoclient

import com.shindoclient.utils.annotations.Immutable

object ShindoMeta {

    const val CLIENT_NAME = "Shindo"
    const val VERSION_NUMBER = "6.0"
    const val VERSION_IDENTIFIER = 6000
    @JvmField @Immutable val BUILD_TYPE: Type = Type.DEV

    enum class Type {
        DEV,
        BETA,
        STABLE
    }
}


