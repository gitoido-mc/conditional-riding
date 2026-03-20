/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.platform.events.PlatformEvents
import lol.gito.conditionalRiding.common.api.ConditionalRidingImplementation
import lol.gito.conditionalRiding.common.config.ConditionalRidingConfig
import lol.gito.conditionalRiding.common.config.ConfigBuilder
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ConditionalRiding {
    const val MOD_ID = "conditional_riding"
    const val GLOBAL_RULESET = "global"
    const val CONFIG_NAME = "cobblemon_${MOD_ID}"

    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)


    lateinit var CONFIG: ConditionalRidingConfig
    lateinit var implementation: ConditionalRidingImplementation
    lateinit var server: MinecraftServer

    fun initialize(implementation: ConditionalRidingImplementation) {
        this.implementation = implementation

        PlatformEvents.SERVER_STARTING.subscribe(Priority.HIGHEST) { event ->
            server = event.server
        }

        PlatformEvents.SERVER_STARTED.subscribe(Priority.LOWEST) {
            LOGGER.info("Conditional Riding :: loading config")
            CONFIG = ConfigBuilder.load(CONFIG_NAME)
        }

        CobblemonEvents.RIDE_EVENT_PRE.subscribe { event ->
            if (!canRide(event.player, event.pokemon)) event.cancel()
        }
    }

    @JvmStatic
    fun debug(message: String, vararg params: Any) {
        if (CONFIG.debug) LOGGER.info(message, *params)
    }


    @JvmStatic
    fun canRide(player: ServerPlayer, target: PokemonEntity): Boolean {
        debug(
            "Conditional Riding :: checking if we allow %s to ride %s".format(
                player.uuid,
                target.pokemon.getDisplayName()
            )
        )

        val targetSpecies = target.pokemon.species.resourceIdentifier.path
        return when {
            (CONFIG.rulesets.isEmpty()) -> true
            (CONFIG.rulesets.contains(GLOBAL_RULESET)) -> checkRuleset(GLOBAL_RULESET, player, target)
            (CONFIG.rulesets.contains(targetSpecies)) -> checkRuleset(targetSpecies, player, target)
            else -> true
        }
    }

    private fun checkRuleset(ruleset: String, player: ServerPlayer, target: PokemonEntity): Boolean {
        val ruleset = CONFIG.rulesets[ruleset]!!
        return when {
            (!ruleset.enabled) -> true
            (!ruleset.check(target)) -> {
                debug("Conditional Riding :: $ruleset ruleset check failed for player ${player.uuid}")

                player.sendSystemMessage(
                    Component.translatable(ruleset.message, target.pokemon.getDisplayName(), player.name),
                    CONFIG.messagesInHotbar
                )

                false
            }

            else -> true
        }
    }
}
