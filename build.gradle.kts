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

val natives = arrayListOf<File>()

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

    val transitiveInclude by configurations.creating
    transitiveInclude(implementation(compose.material3)!!)
    transitiveInclude(implementation(compose.materialIconsExtended)!!)
    transitiveInclude(implementation(compose.desktop.windows_x64)!!)
    transitiveInclude(implementation(compose.desktop.windows_arm64)!!)
    transitiveInclude(implementation(compose.desktop.macos_x64)!!)
    transitiveInclude(implementation(compose.desktop.macos_arm64)!!)
    transitiveInclude(implementation(compose.desktop.linux_x64)!!)
    transitiveInclude(implementation(compose.desktop.linux_arm64)!!)
    transitiveInclude(implementation("androidx.collection:collection:1.5.0")!!)
    transitiveInclude.resolvedConfiguration.resolvedArtifacts.forEach {
        val id = it.moduleVersion.id
        if (id.group == "org.jetbrains.skiko") {
            natives.add(it.file)
        } else {
            include(id.toString())
        }
    }
}

tasks.jar {
    from(natives.map { zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register("runClientFabric") {
    group = "composite"
    description = "Runs the client with the Fabric version of Composite"
    dependsOn(tasks.named("runClient"))
}

tasks.register("runClientNeoForge") {
    group = "composite"
    description = "Runs the client with the NeoForge version of Composite"
    dependsOn(project(":run-neoforge").tasks.named("runClient"))
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