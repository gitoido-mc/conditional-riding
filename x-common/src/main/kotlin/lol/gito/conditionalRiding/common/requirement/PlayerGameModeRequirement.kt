package lol.gito.conditionalRiding.common.requirement

import com.cobblemon.mod.common.pokemon.Pokemon
import lol.gito.conditionalRiding.common.api.requirement.PlayerRequesterAwareRequirement
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.GameType

class PlayerGameModeRequirement(val mode: GameType) : PlayerRequesterAwareRequirement {
    override fun check(
        player: ServerPlayer,
        pokemon: Pokemon
    ): Boolean = player.gameMode.gameModeForPlayer == mode

    companion object {
        const val ADAPTER_VARIANT = "game_mode"
    }
}