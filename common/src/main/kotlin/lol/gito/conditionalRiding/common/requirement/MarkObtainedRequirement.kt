/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.requirement

import com.cobblemon.mod.common.api.mark.Marks
import com.cobblemon.mod.common.api.pokemon.requirement.Requirement
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.resources.ResourceLocation

class MarkObtainedRequirement(
    val mark: ResourceLocation
): Requirement {
    companion object {
        const val ADAPTER_VARIANT = "mark_obtained"
    }

    override fun check(pokemon: Pokemon): Boolean {
        val thisMark = Marks.getByIdentifier(mark) ?: return false

        return pokemon.marks.contains(thisMark)
    }
}
