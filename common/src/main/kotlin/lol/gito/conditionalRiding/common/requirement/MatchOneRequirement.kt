/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.requirement

import com.cobblemon.mod.common.api.pokemon.requirement.Requirement
import com.cobblemon.mod.common.pokemon.Pokemon

class MatchOneRequirement(
    val rules: List<Requirement>,
) : Requirement {
    companion object {
        const val ADAPTER_VARIANT = "match_one"
    }

    override fun check(pokemon: Pokemon): Boolean = rules.any { it.check(pokemon) }
}
