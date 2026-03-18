package lol.gito.conditionalRiding.common.serialization

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.RegistryAccess
import java.lang.reflect.Type

class ItemConditionWrapperAdapter(registryAccess: RegistryAccess.Frozen) : JsonDeserializer<ItemPredicate>,
    JsonSerializer<ItemPredicate> {
    private val codecAdapter = RegistryAccessCodecBackedAdapter(ItemPredicate.CODEC, registryAccess)

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ItemPredicate =
        this.codecAdapter.deserialize(json, typeOfT, context)

    override fun serialize(src: ItemPredicate, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        this.codecAdapter.serialize(src, typeOfSrc, context)

}
