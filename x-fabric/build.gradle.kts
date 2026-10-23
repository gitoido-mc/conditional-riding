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
    fabric()
}

loom {
    runs {
        getByName("client") {
            runDirectory.set(file("runClient"))
            programArguments.addAll(
                "--username=Gitoido",
                "--uuid=23131d78-9edb-48a4-902a-e22e572e9f2b",
            )
        }
        getByName("server") {
            runDirectory.set(file("runServer"))
        }
    }
}

val shadowCommon = configurations.create("shadowCommon") {
    isCanBeResolved = true
    isCanBeConsumed = false
}

repositories {
    maven("https://maven.fabricmc.net/")
}

dependencies {
    modRuntimeOnly(libs.bundles.fabric.dev)
    minecraftServerLibraries(libs.icu4j)

    modImplementation(libs.architectury.fabric)
    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)
    modImplementation(libs.fabric.kotlin)
    modImplementation(libs.cobblemon.fabric) {
        isTransitive = false
    }

    shadowCommon(project(":x-common", configuration = "transformProductionFabric"))
    project(":x-common", configuration = "namedElements").let {
        implementation(it)
        "developmentFabric"(it) { isTransitive = false }
    }
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand(
                mapOf(
                    "version" to project.version,
                    "mod_id" to project.property("mod_id"),
                    "minecraft_version" to libs.versions.minecraft.get(),
                    "fabric_loader_version" to libs.versions.fabric.loader.get(),
                    "cobblemon_version" to libs.versions.cobblemon.get().split("+").first()
                ),
            )
        }
    }

    shadowJar {
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
