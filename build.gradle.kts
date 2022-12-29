plugins {
    id("java")
}

group = "com.arcanewarrior"
version = "0.0.3"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("com.github.Minestom:Minestom:809d9516b2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}