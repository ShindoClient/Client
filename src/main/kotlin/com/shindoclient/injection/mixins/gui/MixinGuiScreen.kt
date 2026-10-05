package com.shindoclient.injection.mixins.gui

import net.minecraft.client.gui.GuiScreen
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(GuiScreen::class)
class MixinGuiScreen {


    @Inject(method = ["initGui"], at = [At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiScreen;mc:Lnet/minecraft/client/Minecraft;", shift = At.Shift.AFTER)])
    fun initGui(ci: CallbackInfo) {

    }
}