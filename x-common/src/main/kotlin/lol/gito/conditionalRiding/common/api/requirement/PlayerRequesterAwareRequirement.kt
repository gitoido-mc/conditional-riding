package lol.gito.conditionalRiding.common.api.requirement

import com.cobblemon.mod.common.api.pokemon.requirement.Requirement
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.level.ServerPlayer

interface PlayerRequesterAwareRequirement: Requirement {
    override fun check(pokemon: Pokemon): Boolean = true

    /**
     * Checks if the given [Pokemon] and [ServerPlayer] satisfies the requirement.
     *
     * @param player The [ServerPlayer] being queried.
     * @param pokemon The [Pokemon] being queried.
     * @return If the requirement was satisfied.
     */
    fun check(player: ServerPlayer, pokemon: Pokemon): Boolean
}