buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.minecraftforge.net")
        maven("https://repo.spongepowered.org/maven")
        maven("https://jitpack.io")
    }

    dependencies {
        classpath("com.github.MikiDevAHM:ForgeGradle:3750acd")
        classpath("com.github.MikiDevAHM:MixinGradle:d2d49df")
    }
}

rootProject.name = "Client"
