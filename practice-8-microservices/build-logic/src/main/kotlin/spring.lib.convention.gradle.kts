import edu.mirea.remsely.csad.practice8.conventions.extensions.libs
import org.springframework.boot.gradle.plugin.SpringBootPlugin

plugins {
    id("java.convention")
}

dependencies {
    implementation(platform(SpringBootPlugin.BOM_COORDINATES))
    annotationProcessor(platform(SpringBootPlugin.BOM_COORDINATES))
    compileOnly(platform(SpringBootPlugin.BOM_COORDINATES))
    runtimeOnly(platform(SpringBootPlugin.BOM_COORDINATES))
    testImplementation(platform(SpringBootPlugin.BOM_COORDINATES))
    testRuntimeOnly(platform(SpringBootPlugin.BOM_COORDINATES))

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
