/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated
import pl.allegro.tech.build.axion.release.domain.properties.VersionProperties
import pl.allegro.tech.build.axion.release.domain.scm.ScmPosition

plugins {
    id("java")
    id("java-library")
    kotlin("jvm") version("2.2.20")
    id("com.gradleup.shadow") version "9.3.1" apply false
    id("dev.architectury.loom") version("1.11-SNAPSHOT") apply false
    id("architectury-plugin") version("3.4-SNAPSHOT") apply false
    id("pl.allegro.tech.build.axion-release") version "1.20.1"
}

scmVersion {
    releaseBranchNames = listOf("main")
    versionCreator("simple")

    tag {
        prefix = "${project.property("cobblemon_version")}+"
        fallbackPrefixes = listOf("1.6.1+", "1.7.0+", "1.7.1+", "1.7.2+")
    }

    branchVersionCreator.put("bugfix/.*", "simple")
    branchVersionCreator.put(
        "feature/.*",
        VersionProperties.Creator { version: String, position: ScmPosition ->
            "$version-${position.branch.split("/").last()}"
        },
    )

    branchVersionIncrementer.putAll(
        mapOf(
            "main" to "incrementPatch",
            "develop" to "incrementPrerelease",
            "feature/.*" to "incrementPrerelease",
            "hotfix/.*" to "incrementPrerelease",
            "refactor/.*" to "incrementPrerelease",
        ),
    )
}

version = scmVersion.version

allprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    version = rootProject.version
    group = project.properties["maven_group"]!!

    repositories {
        mavenCentral()
        maven("https://artefacts.cobblemon.com/releases/")
    }

    tasks {
        test {
            useJUnitPlatform()
        }

        java {
            withSourcesJar()
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

        compileJava {
            options.release = 21
        }

        compileKotlin {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }
    }
}



val buildMod by project.tasks.registering {
    dependsOn(":common:build")
    dependsOn(":fabric:build")
    dependsOn(":neoforge:build")
    mustRunAfter(
        ":fabric:build",
        ":neoforge:build"
    )

    doLast {
        logger.info("Preparing $version jars")

        layout.buildDirectory.file("libs").get().asFile.delete()

        listOf(":common", ":fabric", ":neoforge").forEach { mod ->
            val modProject = project(mod)
            val jars = listOf(
                "${project.name}-${modProject.name}-${modProject.version}.jar",
                "${project.name}-${modProject.name}-${modProject.version}-sources.jar",
            )

            jars.forEach {
                val dest = project.layout.buildDirectory.file("libs/$it").get().asFile
                dest.ensureParentDirsCreated()

                modProject.layout.buildDirectory.file("libs/$it").get().asFile.renameTo(dest)
            }
        }
    }
}
