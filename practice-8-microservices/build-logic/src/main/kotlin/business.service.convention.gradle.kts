import edu.mirea.remsely.csad.practice8.conventions.extensions.implementation
import edu.mirea.remsely.csad.practice8.conventions.extensions.libs


plugins {
    id("spring.boot.app.convention")
    id("spring.cloud.convention")
    id("test.commons.convention")
}

dependencies {
    implementation(project(":commons"))
    implementation(libs.bundles.business.service)
    implementation(libs.bundles.spring.cloud.client)

    runtimeOnly(libs.postgresql)

    testImplementation(libs.bundles.testing)
    testRuntimeOnly(libs.h2)
}
