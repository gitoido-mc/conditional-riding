/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

// By default, this is how your built jars are called
rootProject.name = "conditional-riding"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
        maven("https://maven.architectury.dev/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

includeBuild("gradle/build-logic")
include("x-common")
if ((System.getenv("CI") != null && System.getenv("LOADER") == "fabric") || System.getenv("CI") == null) {
    include("x-fabric")
}
if ((System.getenv("CI") != null && System.getenv("LOADER") == "neoforge") || System.getenv("CI") == null) {
    include("x-neoforge")
}
