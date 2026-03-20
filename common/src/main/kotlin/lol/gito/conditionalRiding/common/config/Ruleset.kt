/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.config

import com.cobblemon.mod.common.api.pokemon.requirement.Requirement
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.google.gson.annotations.Since
import lol.gito.conditionalRiding.common.serialization.RidingRequirementAdapter.VERSION_1_2

data class Ruleset(
    val enabled: Boolean = true,
    @Since(VERSION_1_2)
    val negation: Boolean = false,
    val message: String = "conditional_riding.failed_ruleset.generic",
    val rules: List<Requirement> = emptyList()
) {
    fun check(target: PokemonEntity): Boolean = when (negation) {
        true -> !this.rules.all { requirement -> requirement.check(target.pokemon) }
        else -> this.rules.all { requirement -> requirement.check(target.pokemon) }
    }
}
