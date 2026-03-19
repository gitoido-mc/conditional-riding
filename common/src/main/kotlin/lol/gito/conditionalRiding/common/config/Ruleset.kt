/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.config

import com.cobblemon.mod.common.api.pokemon.requirement.Requirement

data class Ruleset(
    val enabled: Boolean? = true,
    val message: String? = null,
    val rules: List<Requirement> = emptyList()
)
