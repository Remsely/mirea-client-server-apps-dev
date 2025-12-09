import edu.mirea.remsely.csad.practice8.conventions.extensions.implementation
import edu.mirea.remsely.csad.practice8.conventions.extensions.libs

plugins {
    id("spring.boot.app.convention")
    id("spring.cloud.convention")
}

dependencies {
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.oauth2.authorization.server)

    implementation(libs.bundles.spring.cloud.client)
    implementation(libs.bundles.infrastructure.service)

    runtimeOnly(libs.postgresql)
}
