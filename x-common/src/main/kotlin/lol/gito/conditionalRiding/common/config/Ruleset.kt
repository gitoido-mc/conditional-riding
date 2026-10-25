/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.config

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.requirement.Requirement
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.google.gson.annotations.Since
import lol.gito.conditionalRiding.common.api.requirement.PlayerRequesterAwareRequirement
import lol.gito.conditionalRiding.common.serialization.RidingRequirementAdapter.VERSION_1_2
import lol.gito.conditionalRiding.common.serialization.RidingRequirementAdapter.VERSION_1_5
import net.minecraft.server.level.ServerPlayer

data class Ruleset(
    @Since(VERSION_1_5)
    val species: PokemonProperties? = null,
    val enabled: Boolean = true,
    @Since(VERSION_1_2)
    val negation: Boolean = false,
    val message: String = "conditional_riding.failed_ruleset.generic",
    val rules: List<Requirement> = emptyList(),
) {
    fun check(
        player: ServerPlayer,
        target: PokemonEntity,
    ): Boolean =
        when (negation) {
            true -> !(checkPlayerAware(player, target) && checkCommon(target))
            false -> checkPlayerAware(player, target) && checkCommon(target)
        }

    private fun checkPlayerAware(
        player: ServerPlayer,
        target: PokemonEntity,
    ): Boolean =
        this.rules
            .filterIsInstance<PlayerRequesterAwareRequirement>() // smart cast
            .all { requirement -> requirement.check(player, target.pokemon) }

    private fun checkCommon(target: PokemonEntity): Boolean =
        this.rules
            .filterNot { it is PlayerRequesterAwareRequirement }
            .all { requirement -> requirement.check(target.pokemon) }
}
