import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.5.10"
    application
    id("com.github.johnrengelman.shadow") version "7.0.0" // fat jar support
}

group = "soroko.gesrep"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.4")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.12.4")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("soroko.gesrep.MainKt")
}


tasks.withType<ShadowJar> {
    manifest {
        attributes(
            mapOf(
                "ImplementationTitle" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }
}

