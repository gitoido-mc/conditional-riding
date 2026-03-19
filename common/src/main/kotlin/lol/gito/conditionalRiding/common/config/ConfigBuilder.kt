/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.config

import lol.gito.conditionalRiding.common.ConditionalRiding.LOGGER
import lol.gito.conditionalRiding.common.serialization.RidingRequirementAdapter
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

/**
 * Config builder class yoinked and adapted from Tim Core
 * Thank you, Tim!
 * @see (https://github.com/timinc-cobble/cobblemon-tim-core)
 *
 */
class ConfigBuilder private constructor(private val path: String) {
    companion object {
        fun load(path: String): ConditionalRidingConfig {
            return ConfigBuilder(path).load()
        }
    }

    private fun load(): ConditionalRidingConfig {

        var config = ConditionalRidingConfig()
        val configFile = File("config/$path.json")
        configFile.parentFile.mkdirs()

        if (configFile.exists()) {
            try {
                val fileReader = FileReader(configFile)
                config = RidingRequirementAdapter.gson.fromJson(fileReader, ConditionalRidingConfig::class.java)
                LOGGER.info("Found ${config.rulesets.count()} cobblemon riding rulesets")
                fileReader.close()
            } catch (e: Exception) {
                LOGGER.error("Error reading config file: %s, loading default".format(e.cause?.message))
                return config
            }
        }

        try {
            if (!configFile.exists()) {
                configFile.createNewFile()
                val pw = PrintWriter(configFile)
                RidingRequirementAdapter.gson.toJson(ConditionalRidingConfig(), pw)
                pw.close()
            }
        } catch (e: Exception) {
            LOGGER.error("Error writing config file: caught %s".format(e.message))
        }

        return config
    }
}
