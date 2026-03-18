package lol.gito.conditionalRiding.common

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.platform.events.PlatformEvents
import lol.gito.conditionalRiding.common.api.ConditionalRidingImplementation
import lol.gito.conditionalRiding.common.config.ConditionalRidingConfig
import lol.gito.conditionalRiding.common.config.ConfigBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ConditionalRiding {
    const val MOD_ID = "conditional_riding"
    const val CONFIG_NAME = "cobblemon_${MOD_ID}"

    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    lateinit var CONFIG: ConditionalRidingConfig
    lateinit var implementation: ConditionalRidingImplementation
    lateinit var server: MinecraftServer

    fun preInitialize(implementation: ConditionalRidingImplementation) {
        this.implementation = implementation
    }

    fun initialize() {
        ConditionalRidingConfigDataProvider.registerDefaults()

        PlatformEvents.SERVER_STARTED.subscribe(Priority.LOWEST) { event ->
            server = event.server
            LOGGER.info("Conditional Riding :: loading config")
            CONFIG = ConfigBuilder.load(CONFIG_NAME)
        }
    }

    fun modId(name: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, name)

    @JvmStatic
    fun debug(message: String, vararg params: Any) {
        if (CONFIG.debug!!) LOGGER.info(message, *params)
    }


    @JvmStatic
    fun canRide(player: ServerPlayer, target: PokemonEntity): Boolean {
        debug(
            "Conditional Riding :: checking if we allow %s to ride %s".format(
                player.uuid,
                target.pokemon.getDisplayName()
            )
        )

        if (CONFIG.rulesets!!.isEmpty()) return true

        return CONFIG.rulesets!!.any { (_, value) ->
            value.all { it.check(target.pokemon) }
        }
    }
}
