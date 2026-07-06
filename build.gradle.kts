/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("java-library")
    kotlin("jvm") version ("2.3.20")
    id("com.gradleup.shadow") version "9.4.1" apply false
    id("dev.architectury.loom-remap") version ("1.17-SNAPSHOT") apply false
    id("architectury-plugin") version ("3.5-SNAPSHOT") apply false
    id("pl.allegro.tech.build.axion-release") version "1.21.1"
    id("com.diffplug.spotless") version "8.7.0"
}

scmVersion {
    tag {
        prefix.set("v")
    }
}

version = scmVersion.version

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.diffplug.spotless")

    version = rootProject.version
    group = project.properties["maven_group"]!!

    repositories {
        maven("https://artefacts.cobblemon.com/releases/")
    }

    spotless {
        kotlin {
            // version, editorConfigPath, editorConfigOverride and customRuleSets are all optional
            ktlint("1.8.0")
            suppressLintsFor {
                step = "ktlint"
                shortCode = "standard:no-wildcard-imports"
            }
        }
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
