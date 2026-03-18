/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/rad-gyms/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common

import lol.gito.conditionalRiding.common.ConditionalRiding.CONFIG
import lol.gito.conditionalRiding.common.ConditionalRiding.CONFIG_NAME
import lol.gito.conditionalRiding.common.ConditionalRiding.LOGGER
import lol.gito.conditionalRiding.common.ConditionalRiding.modId
import lol.gito.conditionalRiding.common.config.ConfigBuilder
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener

object ConditionalRidingConfigDataProvider {
    fun registerDefaults() {
        ConditionalRiding.implementation.registerResourceReloader(
            modId("data_resources"),
            SimpleResourceReloader(PackType.SERVER_DATA),
            PackType.SERVER_DATA,
            emptyList(),
        )
    }

    private class SimpleResourceReloader(private val type: PackType) : ResourceManagerReloadListener {
        override fun onResourceManagerReload(manager: ResourceManager) {
            if (type == PackType.SERVER_DATA) {
//                LOGGER.info("Conditional Riding :: loading config")
//                CONFIG = ConfigBuilder.load(CONFIG_NAME)
            }
        }
    }
}
