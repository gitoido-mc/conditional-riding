/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.requirement

import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.conditionalRiding.common.api.requirement.PlayerRequesterAwareRequirement
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.server.level.ServerPlayer

class PlayerHoldsItemRequirement(
    val itemCondition: ItemPredicate,
) : PlayerRequesterAwareRequirement {
    override fun check(
        player: ServerPlayer,
        pokemon: Pokemon,
    ): Boolean = itemCondition.test(player.mainHandItem)

    companion object {
        const val ADAPTER_VARIANT = "player_held_item"
    }
}
