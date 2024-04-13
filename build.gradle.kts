plugins {
    id("application")
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

apply(plugin = "kotlin")

repositories {
    mavenCentral()
    maven(url = "https://repo.openrs2.org/repository/openrs2-snapshots/")
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.displee:rs-cache-library:7.1.3")
}

sourceSets {
    main {
        kotlin {
            srcDir("src")
        }
    }
}

