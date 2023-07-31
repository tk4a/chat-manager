plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}

tasks.withType<Jar>{
    enabled = true
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar>{
    enabled = false
}