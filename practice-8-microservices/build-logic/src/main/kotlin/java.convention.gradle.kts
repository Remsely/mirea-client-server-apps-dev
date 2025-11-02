import edu.mirea.remsely.csad.practice8.conventions.extensions.libs

plugins {
    id("base.convention")
    id("java")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(libs.versions.java.get())
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
