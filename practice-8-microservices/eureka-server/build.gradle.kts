plugins {
    id("spring.boot.app.convention")
    id("spring.cloud.convention")
}

dependencies {
    implementation(libs.spring.cloud.starter.netflix.eureka.server)
}
