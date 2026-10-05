package com.shindoclient.injection

import com.shindoclient.ShindoMeta
import com.shindoclient.injection.transformer.LwjglTransformer
import com.shindoclient.logger.ShindoLogger
import com.shindoclient.logger.ShindoLogger.error
import com.shindoclient.logger.ShindoLogger.info
import com.shindoclient.logger.ShindoLogger.warn
import com.sun.jna.Native
import com.sun.jna.win32.StdCallLibrary
import net.minecraft.launchwrapper.ITweaker
import net.minecraft.launchwrapper.Launch
import net.minecraft.launchwrapper.LaunchClassLoader
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.MixinEnvironment
import org.spongepowered.asm.mixin.Mixins
import java.io.File
import java.util.*

class ShindoTweaker : ITweaker {
    private val launchArguments: MutableList<String?> = ArrayList<String?>()

    override fun acceptOptions(args: MutableList<String?>, gameDir: File?, assetsDir: File?, profile: String?) {
        this.makeProcessDpiAware()

        try {
            Class.forName("optifine.Patcher")
            hasOptifine = true
        } catch (e: ClassNotFoundException) {
            if (ShindoMeta.BUILD_TYPE == ShindoMeta.Type.DEV) {
                info("Optifine not present, continuing without Optifine")
            } else {
                warn("Optifine is not present, contact us on discord if you think this is a mistake", e)
            }
        }

        this.launchArguments.addAll(args)

        if (profile != null) {
            launchArguments.add("--version")
            launchArguments.add(profile)
        }

        if (assetsDir != null) {
            launchArguments.add("--assetsDir")
            launchArguments.add(assetsDir.absolutePath)
        }

        if (gameDir != null) {
            launchArguments.add("--gameDir")
            launchArguments.add(gameDir.absolutePath)
        }
    }

    override fun injectIntoClassLoader(classLoader: LaunchClassLoader) {
        classLoader.registerTransformer(LwjglTransformer::class.java.name)

        MixinBootstrap.init()

        val env = MixinEnvironment.getDefaultEnvironment()
        Mixins.addConfiguration("mixins.shindo.json")

        if (env.obfuscationContext == null) {
            env.obfuscationContext = "notch"
        }

        env.setSide(MixinEnvironment.Side.CLIENT)

        this.unlockLwjgl()
    }

    override fun getLaunchTarget(): String {
        return "net.minecraft.client.main.Main"
    }

    override fun getLaunchArguments(): Array<String?> {
        return launchArguments.toTypedArray<String?>()
    }

    private fun unlockLwjgl() {
        try {
            val transformerExceptions = LaunchClassLoader::class.java.getDeclaredField("classLoaderExceptions")
            transformerExceptions.isAccessible = true
            val o = transformerExceptions.get(Launch.classLoader)
            (o as MutableSet<*>).remove("org.lwjgl.")
        } catch (e: NoSuchFieldException) {
            ShindoLogger.error("Could not unlock LWJGL, this could have disastrous impacts on the ability of glide to launch", e)
        } catch (e: IllegalAccessException) {
            ShindoLogger.error("Could not unlock LWJGL, this could have disastrous impacts on the ability of glide to launch", e)
        }
    }

    /**
     * Declares this process DPI-aware to Windows.
     */
    private fun makeProcessDpiAware() {
        if (!System.getProperty("os.name", "").lowercase(Locale.getDefault()).contains("win")) {
            return
        }

        try {
            val success = User32Ext.INSTANCE.SetProcessDPIAware()

            if (!success) {
                warn("SetProcessDPIAware() has failed.")
            }
        } catch (t: Throwable) {
            error("Failed to set process DPI awareness", t)
        }
    }

    private interface User32Ext : StdCallLibrary {
        fun SetProcessDPIAware(): Boolean

        companion object {
            val INSTANCE: User32Ext = Native.load("user32", User32Ext::class.java)
        }
    }

    companion object {
        var hasOptifine: Boolean = false
    }
}