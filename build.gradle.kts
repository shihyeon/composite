plugins {
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.3.0"
    id("org.jetbrains.compose") version "1.9.3"
    id("maven-publish")
    id("net.fabricmc.fabric-loom-remap") version "1.14-SNAPSHOT"
}

version = "0.2.1"
group = "dev.aperso"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    google()
}

val natives = arrayListOf<File>()

val transitiveInclude by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.1")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:0.16.14")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.92.6+1.20.1")
    modImplementation("net.fabricmc:fabric-language-kotlin:1.13.8+kotlin.2.3.0")

    fun addImplementationInclude(dep: Any) {
        implementation(dep)
        transitiveInclude(dep)
    }

    addImplementationInclude(compose.material3)
    addImplementationInclude(compose.desktop.windows_x64)
    addImplementationInclude(compose.desktop.windows_arm64)
    addImplementationInclude(compose.desktop.macos_x64)
    addImplementationInclude(compose.desktop.macos_arm64)
    addImplementationInclude(compose.desktop.linux_x64)
    addImplementationInclude(compose.desktop.linux_arm64)
}

tasks.jar {
    from({
        transitiveInclude.resolvedConfiguration.resolvedArtifacts.map { artifact ->
            zipTree(artifact.file)
        }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.processResources {
    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = project.name
            artifact(tasks["remapJar"]) {
                classifier = ""
            }
        }
    }
    repositories {
        mavenLocal()
    }
}