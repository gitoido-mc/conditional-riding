/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.requirement

import com.cobblemon.mod.common.api.pokemon.requirement.Requirement
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.conditionalRiding.common.api.requirement.PlayerRequesterAwareRequirement
import net.minecraft.server.level.ServerPlayer

class AllRequirement(
    val requirements: List<Requirement>,
) : PlayerRequesterAwareRequirement {
    companion object {
        const val ADAPTER_VARIANT = "match_all"
    }

    override fun check(
        player: ServerPlayer,
        pokemon: Pokemon,
    ): Boolean {
        val forPlayer = requirements.filterIsInstance<PlayerRequesterAwareRequirement>().all { it.check(player, pokemon) }
        val defaults = requirements.filterNot { it is PlayerRequesterAwareRequirement }.all { it.check(pokemon) }
        return forPlayer && defaults
    }
}
