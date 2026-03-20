/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.requirement

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.pokemon.requirement.Requirement
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.conditionalRiding.common.ConditionalRiding.MOD_ID
import lol.gito.conditionalRiding.common.permission.RidingPermission
import net.minecraft.resources.ResourceLocation

class PermissionNodeRequirement(val node: String = "conditional_riding.can_ride") : Requirement {
    override fun check(pokemon: Pokemon): Boolean = when {
        (!pokemon.isPlayerOwned()) -> false
        (pokemon.getOwnerPlayer() == null) -> false
        else -> {
            val permission = RidingPermission(
                ResourceLocation.fromNamespaceAndPath(MOD_ID, node.replace('.', '/')),
                node
            )

            Cobblemon.permissionValidator.hasPermission(pokemon.getOwnerPlayer()!!, permission)
        }
    }

    companion object {
        const val ADAPTER_VARIANT = "permission_node"
    }
}
