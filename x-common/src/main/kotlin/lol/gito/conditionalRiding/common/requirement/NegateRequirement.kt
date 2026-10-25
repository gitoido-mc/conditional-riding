package lol.gito.conditionalRiding.common.requirement

import com.cobblemon.mod.common.api.pokemon.requirement.Requirement
import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.conditionalRiding.common.api.requirement.PlayerRequesterAwareRequirement
import net.minecraft.server.level.ServerPlayer

class NegateRequirement(val inner: Requirement): PlayerRequesterAwareRequirement {
    override fun check(
        player: ServerPlayer,
        pokemon: Pokemon
    ): Boolean = when (inner) {
        is PlayerRequesterAwareRequirement -> !inner.check(player, pokemon)
        else -> !inner.check(pokemon)
    }

    companion object {
        const val ADAPTER_VARIANT = "negate"
    }
}