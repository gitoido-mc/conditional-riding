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
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.conditionalRiding.common.api.ConditionalRidingImplementation
import lol.gito.conditionalRiding.common.config.ConditionalRidingConfig
import lol.gito.conditionalRiding.common.config.ConfigBuilder
import lol.gito.conditionalRiding.common.config.Ruleset
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

        if (CONFIG.rulesets.isEmpty()) return true

        val global = CONFIG.rulesets[GLOBAL_RULESET]

        global?.let { ruleset ->
            if (!ruleset.check(target)) {
                debug("Conditional Riding :: global ruleset check failed for player ${player.uuid}")
                sendRulesetMessageToPlayer(ruleset, target.pokemon, player)
                return false
            }
        }

        val targetSpecies = target.pokemon.species.resourceIdentifier.path
        val speciesRuleset = CONFIG.rulesets[targetSpecies] ?: return true


        if (!speciesRuleset.check(target)) {
            debug("Conditional Riding :: $targetSpecies ruleset check failed for player ${player.uuid}")
            sendRulesetMessageToPlayer(speciesRuleset, target.pokemon, player)
            return false
        }

        return true
    }

    private fun sendRulesetMessageToPlayer(ruleset: Ruleset, pokemon: Pokemon, player: ServerPlayer) {
        val messageTranslationKey = when (ruleset.message) {
            null -> "conditional_riding.failed_ruleset.global"
            else -> ruleset.message
        }

        player.sendSystemMessage(Component.translatable(messageTranslationKey, pokemon.getDisplayName(), player.name))
    }

    private fun Ruleset.check(target: PokemonEntity): Boolean = this.rules.all { requirement ->
        requirement.check(target.pokemon)
    }
}
