package com.shindoclient

import com.shindoclient.logger.ShindoLogger
import net.minecraft.client.Minecraft

class Shindo {

    private val mc: Minecraft = Minecraft.getMinecraft()

    fun start() {
        ShindoLogger.info("Hello World!")
        mc.updateDisplay()
    }

    fun stop() {

        //Sound.play("shindo/audio/close.wav", true)
    }
    companion object {
        private val INSTANCE: Shindo = Shindo()

        @JvmStatic
        fun getInstance(): Shindo = INSTANCE
        @JvmField var started: Boolean = false
    }
}