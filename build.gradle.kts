plugins {
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.0"
    id("org.jetbrains.compose") version "1.9.3"
    id("maven-publish")
    id("net.fabricmc.fabric-loom") version "1.15-SNAPSHOT"
}

version = "0.5.0"
group = "dev.aperso"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.neoforged.net/releases/")
    maven {
        name = "Maven for PR #2879" // https://github.com/neoforged/NeoForge/pull/2879
        url = uri("https://prmaven.neoforged.net/NeoForge/pr2879")
        content {
            includeModule("net.neoforged", "neoforge")
            includeModule("net.neoforged", "testframework")
        }
    }
}

val composeDeps by configurations.creating

dependencies {
    minecraft("com.mojang:minecraft:26.1-rc-2")

    implementation("net.fabricmc:fabric-loader:0.18.4")
    implementation("net.fabricmc.fabric-api:fabric-api:0.144.0+26.1")
    implementation("net.fabricmc:fabric-language-kotlin:1.13.9+kotlin.2.3.10")

    // https://maven.neoforged.net/#/releases/net/neoforged
    compileOnly("net.neoforged:neoforge:26.1.0.0-alpha.0+rc-2.20260321.154221")
    compileOnly("net.neoforged:bus:8.0.5")
    compileOnly("net.neoforged.fancymodloader:loader:11.0.3")
    compileOnly("net.neoforged.fancymodloader:spi:3.0.9")

    composeDeps(implementation(compose.material3)!!)
    composeDeps(implementation(compose.materialIconsExtended)!!)
    composeDeps(implementation(compose.desktop.windows_x64)!!)
    composeDeps(implementation(compose.desktop.windows_arm64)!!)
    composeDeps(implementation(compose.desktop.macos_x64)!!)
    composeDeps(implementation(compose.desktop.macos_arm64)!!)
    composeDeps(implementation(compose.desktop.linux_x64)!!)
    composeDeps(implementation(compose.desktop.linux_arm64)!!)
    composeDeps(implementation("androidx.collection:collection:1.5.0")!!)
}

tasks.jar {
    val resolvedArtifacts = composeDeps.resolvedConfiguration.resolvedArtifacts
    val natives = resolvedArtifacts.filter { it.moduleVersion.id.group == "org.jetbrains.skiko" }.map { it.file }
    val jars = resolvedArtifacts.map { it.file }.filter { it.name.endsWith(".jar") && it !in natives }
    from(natives.map { zipTree(it) })
    from(jars.map { zipTree(it) })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.processResources {
    filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
        expand("version" to version)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.name
            artifact(tasks["jar"]) {
                classifier = ""
            }
        }
    }
    repositories {
        mavenLocal()
    }
}