package com.shindoclient.injection.mixins.client

import com.shindoclient.Shindo.Companion.getInstance
import net.minecraft.client.Minecraft
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(Minecraft::class)
class MixinMinecraft {

    @Inject(method = ["startGame"], at = [At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;", shift = At.Shift.AFTER)])
    fun preStartGame(ci: CallbackInfo) {
        getInstance().start()
    }
}