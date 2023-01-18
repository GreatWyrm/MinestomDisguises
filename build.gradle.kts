plugins {
    id("java")
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.arcanewarrior"
version = "0.0.5"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Minestom.Minestom:Minestom:c995f9c3a9")
    compileOnly("com.github.HypeJet:HypeStom:8041cf5d89")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}


tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
    archiveClassifier.set("")
}