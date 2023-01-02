plugins {
    id("java")
    `maven-publish`
}

group = "com.arcanewarrior"
version = "0.0.3"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.CityWideMC.Minestom:Minestom:fe815c61d6")
    compileOnly("com.github.CityWideMC:CityStom:1.4.0")
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