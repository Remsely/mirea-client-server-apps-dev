import edu.mirea.remsely.csad.practice8.conventions.extensions.libs

plugins {
    id("spring.lib.convention")
}

apply(plugin = libs.plugins.spring.boot.get().pluginId)
