plugins {
    id("spring.boot.app.convention")
    id("spring.cloud.convention")
    id("test.commons.convention")
}

dependencies {
    implementation(projects.commons)

    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)

    implementation(libs.spring.cloud.starter.config)
    implementation(libs.spring.cloud.starter.netflix.eureka.client)
    implementation(libs.spring.cloud.starter.openfeign)

    runtimeOnly(libs.postgresql)

    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.h2)
}
