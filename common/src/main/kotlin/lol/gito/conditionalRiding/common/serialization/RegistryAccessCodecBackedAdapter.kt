package lol.gito.conditionalRiding.common.serialization

import com.cobblemon.mod.common.util.adapters.RegistryOpsProvider
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import net.minecraft.core.RegistryAccess
import net.minecraft.resources.RegistryOps
import java.lang.reflect.Type

class RegistryAccessCodecBackedAdapter<T>(val codec: Codec<T>, val registryAccess: RegistryAccess.Frozen) :
    JsonDeserializer<T>, JsonSerializer<T> {

    override fun deserialize(
        jElement: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): T {
        val ops = RegistryOps.create(JsonOps.INSTANCE, registryAccess)
        val result = ops.withDecoder(codec).apply(jElement)
        return result.result().orElseThrow {
            IllegalStateException("Failed to deserialize $jElement: ${result.error().orElse(null)}")
        }.first
    }

    override fun serialize(
        src: T,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        val ops = RegistryOps.create(JsonOps.INSTANCE, registryAccess)
        val result = ops.withEncoder(codec).apply(src)
        return result.result().orElseThrow {
            IllegalStateException("Failed to serialize $src: ${result.error().orElse(null)}")
        }
    }
}
