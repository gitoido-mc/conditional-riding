/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

plugins {
    id("java")
    alias(libs.plugins.cr.base)
    alias(libs.plugins.cr.publication)
    alias(libs.plugins.architectury.dev)
    alias(libs.plugins.architectury.plugin)
}

loom {
    silentMojangMappingsLicense()
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching(listOf("fabric.mod.json", "META-INF/neoforge.mods.toml")) {
            expand(
                mapOf(
                    "version" to project.version,
                    "mod_id" to project.property("mod_id"),
                    "minecraft_version" to libs.versions.minecraft.get(),
                    "neoforge_loader_version" to libs.versions.neoforge.loader.get(),
                    "neoforge_kotlin_version" to libs.versions.neoforge.kotlin.get(),
                    "fabric_loader_version" to libs.versions.fabric.loader.get(),
                    "fabric_kotlin_version" to libs.versions.fabric.kotlin.get(),
                    "fabric_api_version" to libs.versions.fabric.api.get(),
                    "cobblemon_version" to libs.versions.cobblemon.get().split("+").first()
                ),
            )
        }
    }

    remapJar {
        archiveBaseName.set("${rootProject.name}-${project.property("build.sourceset")}")
        archiveVersion.set("${project.version}")
    }

    remapSourcesJar {
        archiveBaseName.set("${rootProject.name}-${project.property("build.sourceset")}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("sources")
    }
}
