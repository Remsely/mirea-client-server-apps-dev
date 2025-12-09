import edu.mirea.remsely.csad.practice8.conventions.extensions.implementation
import edu.mirea.remsely.csad.practice8.conventions.extensions.libs

plugins {
    id("spring.boot.app.convention")
    id("spring.cloud.convention")
}

dependencies {
    implementation(libs.spring.cloud.starter.netflix.eureka.server)
    implementation(libs.bundles.infrastructure.service)
}
