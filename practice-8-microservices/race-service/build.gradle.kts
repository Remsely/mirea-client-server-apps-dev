import edu.mirea.remsely.csad.practice8.conventions.extensions.implementation
import edu.mirea.remsely.csad.practice8.conventions.extensions.libs

plugins {
    id("business.service.convention")
}

dependencies {
    implementation(libs.spring.cloud.starter.openfeign)
}
