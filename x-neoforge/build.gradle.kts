/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

plugins {
    alias(libs.plugins.cr.common)
    alias(libs.plugins.shadow)
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

loom {
    runs {
        getByName("server") {
            runDirectory.set(file("runServer"))
        }
        getByName("client") {
            runDirectory.set(file("runClient"))
            programArguments.addAll(
                "--username=Gitoido",
                "--uuid=23131d78-9edb-48a4-902a-e22e572e9f2b",
            )
        }
    }
}

val shadowCommon: Configuration = configurations.create("shadowCommon") {
    isCanBeResolved = true
    isCanBeConsumed = false
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.neoforged.net/releases")
}

dependencies {
    neoForge(libs.neoforge.loader)
    implementation(libs.neoforge.kotlin) {
        exclude("net.neoforged.fancymodloader", "loader")
    }

    modImplementation(libs.architectury.neoforge)
    modImplementation(libs.cobblemon.neoforge) {
        isTransitive = false
    }

    shadowCommon(project(":x-common", configuration = "transformProductionNeoForge"))
    project(":x-common", configuration = "namedElements").let {
        implementation(it)
        "developmentNeoForge"(it) { isTransitive = false }
    }
}

tasks {
    processResources {

        inputs.property("version", project.version)

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(
                mapOf(
                    "version" to project.version,
                    "mod_id" to project.property("mod_id"),
                    "minecraft_version" to libs.versions.minecraft.get(),
                    "neoforge_version" to libs.versions.neoforge.loader.get(),
                    "cobblemon_version" to libs.versions.cobblemon.get().split("+").first()
                ),
            )
        }
    }

    jar {
        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${rootProject.version}")
        archiveClassifier.set("dev-slim")
    }

    shadowJar {
        exclude("fabric.mod.json")
        exclude("architectury.common.json")

        configurations = listOf(shadowCommon)

        archiveBaseName.set("${rootProject.name}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("shadow")
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
    }
}
