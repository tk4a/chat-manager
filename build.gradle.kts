plugins {
    kotlin("jvm") version Version.KOTLIN
    kotlin("plugin.spring") version Version.KOTLIN apply false
    id("org.springframework.boot") version Version.SPRING_BOOT apply false
    id("io.spring.dependency-management") version Version.SPRING_DEPENDENCY apply false
}

subprojects {
    group = GROUP_NAME
    version = VERSION
}

repositories {
    mavenCentral()
}
