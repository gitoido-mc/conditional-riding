/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.requirement

import com.cobblemon.mod.common.api.pokemon.requirement.Requirement
import com.cobblemon.mod.common.pokemon.Pokemon

class MinimalHpPercentRequirement : Requirement {
    val percent = MAX_PERCENT
    override fun check(pokemon: Pokemon) =
        ((pokemon.currentHealth.toDouble() / pokemon.maxHealth.toDouble()) * MAX_PERCENT) >= percent

    companion object {
        const val MAX_PERCENT = 100.0
        const val ADAPTER_VARIANT = "minimal_hp_percent"
    }
}
