package lol.gito.conditionalRiding.fabric

import lol.gito.conditionalRiding.common.api.ConditionalRidingImplementation
import lol.gito.conditionalRiding.common.ConditionalRiding
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object ConditionalRidingFabric : ModInitializer, ConditionalRidingImplementation {
    override fun onInitialize() {
        ConditionalRiding.preInitialize(this)
        ConditionalRiding.initialize()
    }

    override fun registerResourceReloader(
        identifier: ResourceLocation,
        reloader: PreparableReloadListener,
        type: PackType,
        dependencies: Collection<ResourceLocation>
    ) = ResourceManagerHelper
        .get(type)
        .registerReloadListener(ConditionalRidingListener(identifier, reloader, dependencies))

    private class ConditionalRidingListener(
        private val identifier: ResourceLocation,
        private val reloader: PreparableReloadListener,
        private val dependencies: Collection<ResourceLocation>,
    ) : IdentifiableResourceReloadListener {
        override fun reload(
            synchronizer: PreparableReloadListener.PreparationBarrier,
            manager: ResourceManager,
            prepareProfiler: ProfilerFiller,
            applyProfiler: ProfilerFiller,
            prepareExecutor: Executor,
            applyExecutor: Executor,
        ): CompletableFuture<Void> = this.reloader.reload(
            synchronizer,
            manager,
            prepareProfiler,
            applyProfiler,
            prepareExecutor,
            applyExecutor,
        )

        override fun getFabricId(): ResourceLocation = this.identifier

        override fun getName(): String = this.reloader.name

        override fun getFabricDependencies(): MutableCollection<ResourceLocation> = this.dependencies.toMutableList()
    }
}
