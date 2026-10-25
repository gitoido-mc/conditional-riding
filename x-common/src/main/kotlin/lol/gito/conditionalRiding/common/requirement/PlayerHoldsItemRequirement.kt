package lol.gito.conditionalRiding.common.requirement

import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.conditionalRiding.common.api.requirement.PlayerRequesterAwareRequirement
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.server.level.ServerPlayer

class PlayerHoldsItemRequirement(val itemCondition: ItemPredicate): PlayerRequesterAwareRequirement {
    override fun check(
        player: ServerPlayer,
        pokemon: Pokemon
    ): Boolean = itemCondition.test(player.mainHandItem)

    companion object {
        const val ADAPTER_VARIANT = "player_held_item"
    }
}