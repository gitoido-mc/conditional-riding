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
import com.cobblemon.mod.common.platform.events.ServerEvent
import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import lol.gito.conditionalRiding.common.api.ConditionalRidingImplementation
import lol.gito.conditionalRiding.common.config.ConditionalRidingConfig
import lol.gito.conditionalRiding.common.config.ConfigBuilder
import lol.gito.conditionalRiding.common.config.Ruleset
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.Commands.literal
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ConditionalRiding {
    object ReloadCommand {
        private const val NAME = "reload"

        fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
            ConditionalRiding.Commands.prefixes.forEach { prefix ->
                val command =
                    literal(prefix).then(
                        literal(NAME)
                            .executes(::execute),
                    )
                dispatcher.register(command)
            }
        }

        fun execute(context: CommandContext<CommandSourceStack>): Int {
            loadConfig(null)
            context.source.player?.displayClientMessage(Component.literal("reloaded conditional riding config!"), true)
            return Command.SINGLE_SUCCESS
        }
    }

    object Commands {
        val prefixes = listOf("conditional_riding", "cr")

        @Suppress("unused")
        fun register(
            dispatcher: CommandDispatcher<CommandSourceStack>,
            registry: CommandBuildContext,
            selection: Commands.CommandSelection,
        ) {
            ReloadCommand.register(dispatcher)
        }
    }

    const val MOD_ID = "conditional_riding"
    const val CONFIG_NAME = "cobblemon_${MOD_ID}"

    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    lateinit var config: ConditionalRidingConfig
    lateinit var implementation: ConditionalRidingImplementation
    lateinit var server: MinecraftServer

    @JvmStatic
    fun info(
        message: String,
        vararg params: Any,
    ) = LOGGER.info("conditional_riding :: $message", *params)

    @JvmStatic
    fun debug(
        message: String,
        vararg params: Any,
    ) = if (config.debug) info(message, *params) else Unit

    fun initialize(implementation: ConditionalRidingImplementation) {
        this.implementation = implementation

        PlatformEvents.SERVER_STARTING.subscribe(Priority.HIGHEST) { event ->
            server = event.server
        }

        PlatformEvents.SERVER_STARTED.subscribe(Priority.LOWEST, ::loadConfig)

        CobblemonEvents.RIDE_EVENT_PRE.subscribe { event ->
            if (!config.enabled) {
                debug("enabled config option is set to false, rulesets will not be checked, skipping...")
                return@subscribe
            }
            if (!canRide(event.player, event.pokemon)) event.cancel()
        }
    }

    @Suppress("unused")
    fun loadConfig(event: ServerEvent.Started?) {
        info("loading config")
        config = ConfigBuilder.load(implementation.config)
        if (config.debug) debug("debug option enabled, expect increased log spam")
        if (config.enabled) {
            if (config.global != null) info("global ruleset loaded")
            info("loaded ${config.custom.count()} custom rulesets")
            if (config.messagesInHotbar) {
                info("the messages will be shown in hotbar, because messagesInHotbar option is set to true")
            } else {
                info("the messages will be shown in chat, because messagesInHotbar option is set to false")
            }
        } else {
            info("enabled config option is set to false, rulesets will not be checked")
        }
    }

    @JvmStatic
    fun canRide(
        player: ServerPlayer,
        target: PokemonEntity,
    ): Boolean {
        val targetSpecies = target.pokemon.species.resourceIdentifier.path
        debug("checking if we allow player %s to ride %s".format(player.uuid, targetSpecies))

        if (config.global != null && config.global!!.enabled && config.global!!.rules.isNotEmpty()) {
            if (!checkRuleset(config.global!!, player, target)) return false
        }

        val applicable =
            config.custom.filter {
                it.key.matches(target) && it.value.enabled && it.value.rules.isNotEmpty()
            }

        return applicable.isEmpty() ||
            applicable.any {
                checkRuleset(it.value, player, target)
            }
    }

    private fun checkRuleset(
        ruleset: Ruleset,
        player: ServerPlayer,
        target: PokemonEntity,
    ): Boolean =
        when {
            !ruleset.enabled -> {
                debug("enabled config option is set to false for $ruleset ruleset, skipping")

                true
            }

            !ruleset.check(player, target) -> {
                debug("$ruleset ruleset check failed for player ${player.uuid}")

                player.sendSystemMessage(
                    Component.translatable(ruleset.message, target.pokemon.getDisplayName(), player.name),
                    config.messagesInHotbar,
                )

                false
            }

            else -> {
                debug("$ruleset ruleset check passed for player ${player.uuid}, allowing them to ride the species")

                true
            }
        }
}
