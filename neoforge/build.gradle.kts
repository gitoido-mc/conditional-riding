plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

loom {
    enableTransitiveAccessWideners.set(true)
    silentMojangMappingsLicense()

    val clientConfig = runConfigs.getByName("client")
    clientConfig.runDir = "runClient"
    // clientConfig.vmArg("-Dmixin.debug=true")
    //This is AshKetchum's UUID so you get an Ash Ketchum skin
    clientConfig.programArg("--username=AshKetchum")
    clientConfig.programArg("--uuid=93e4e551-589a-41cb-ab2d-435266c8e035")

    val serverConfig = runConfigs.getByName("server")
    serverConfig.runDir = "runServer"
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

repositories {
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://maven.neoforged.net/releases/")
    maven {
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
        content { includeGroup ("thedarkcolour") }
    }
}

dependencies {
    // Minecraft, mappings and neoforge deps
    minecraft("net.minecraft:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:${property("neoforge_version")}")

    // Kotlin support, also required by cobblemon
    implementation("thedarkcolour:kotlinforforge-neoforge:${property("kotlin_for_forge_version")}") {
        exclude("net.neoforged.fancymodloader", "loader")
    }

    // Cobblemon itself
    modImplementation("com.cobblemon:neoforge:${property("cobblemon_version")}") { isTransitive = false }

    // Common module
    implementation(project(":common", configuration = "namedElements"))
    "developmentNeoForge"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowBundle(project(":common", configuration = "transformProductionFabric"))
}

tasks {
    test {
        useJUnitPlatform()
    }

    val copyMixin by registering(Copy::class) {
        from(project(":common").file("src/resources/${project.property("mod_id")}.mixins.json"))
        into(file("src/resources"))
    }

    processResources {
        mustRunAfter(copyMixin)
        inputs.property("version", project.version)

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(project.properties)
        }
    }

    jar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveClassifier.set("dev-slim")
    }

    shadowJar {
        exclude("fabric.mod.json")
        archiveClassifier.set("dev-shadow")
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        configurations = listOf(shadowBundle)
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${rootProject.version}")
    }
}
