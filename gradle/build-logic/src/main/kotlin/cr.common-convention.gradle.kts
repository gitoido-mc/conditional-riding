/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

plugins {
    id("java")
    alias(libs.plugins.cr.base)
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
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
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
