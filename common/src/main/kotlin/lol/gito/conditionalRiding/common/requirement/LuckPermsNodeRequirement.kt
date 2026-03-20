/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

package lol.gito.conditionalRiding.common.requirement

import com.cobblemon.mod.common.api.pokemon.requirement.Requirement
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.conditionalRiding.common.ConditionalRiding.LOGGER

class LuckPermsNodeRequirement(val node: String) : Requirement {
    constructor() : this("conditional_riding.can_ride")

    override fun check(pokemon: Pokemon): Boolean {
        try {
            if (!pokemon.isPlayerOwned()) return false
            if (pokemon.getOwnerPlayer() == null) return false

            var check = false

            // not using the import here to gracefully return if luckperms are not installed
            val luckPerms = net.luckperms.api.LuckPermsProvider.get()

            luckPerms
                .userManager
                .loadUser(pokemon.getOwnerUUID()!!)
                .thenApplyAsync { user ->
                    return@thenApplyAsync user
                        .getInheritedGroups(user.queryOptions)
                        .stream()
                        .anyMatch { it.name == node }
                }
                .thenAcceptAsync { result ->
                    check = result
                }

            return check

        } catch (_: Exception) {
            LOGGER.error("Tried to check LuckPerms $node node presence, but LuckPerms is not installed.")
            LOGGER.error("Because of that, this check will pass")

            return true
        }
    }

    companion object {
        const val ADAPTER_VARIANT = "luckperms_node"
    }
}
