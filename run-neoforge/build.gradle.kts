plugins {
    id("net.neoforged.moddev") version("2.0.+")
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

dependencies {
    implementation(project(":")) {
        exclude(group = "net.fabricmc")
        exclude(group = "net.fabricmc.fabric-api")
        exclude(group = "net.neoforged")
    }
}

neoForge {
    version = "26.1.0.0-alpha.0+rc-2.20260321.154221"

    runs {
        create("client") {
            client()
        }
    }
}

tasks.jar { enabled = false }