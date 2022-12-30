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
    compileOnly("com.github.CityWideMC:CityStom:8f9625559f")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}