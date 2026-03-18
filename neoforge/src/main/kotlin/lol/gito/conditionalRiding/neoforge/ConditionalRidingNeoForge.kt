package lol.gito.conditionalRiding.neoforge

import lol.gito.conditionalRiding.common.api.ConditionalRidingImplementation
import lol.gito.conditionalRiding.common.ConditionalRiding
import lol.gito.conditionalRiding.common.ConditionalRiding.MOD_ID
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.event.AddReloadListenerEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(MOD_ID)
object ConditionalRidingNeoForge : ConditionalRidingImplementation {
    private val reloadableResources = arrayListOf<PreparableReloadListener>()

    init {
        with(MOD_BUS) {
            ConditionalRiding.preInitialize(this@ConditionalRidingNeoForge)
            addListener { _: FMLCommonSetupEvent ->
                ConditionalRiding.initialize()
            }
        }

        with(NeoForge.EVENT_BUS) {
            addListener(::onReload)
        }
    }

    override fun registerResourceReloader(
        identifier: ResourceLocation,
        reloader: PreparableReloadListener,
        type: PackType,
        dependencies: Collection<ResourceLocation>
    ) {
        if (type == PackType.SERVER_DATA) {
            this.reloadableResources += reloader
        }
    }

    private fun onReload(e: AddReloadListenerEvent) {
        this.reloadableResources.forEach(e::addListener)
    }
}
