/*
 * Copyright (c) 2026. gitoido-mc
 * This Source Code Form is subject to the terms of the GNU General Public License v3.0.
 * If a copy of the GNU General Public License v3.0 was not distributed with this file,
 * you can obtain one at https://github.com/gitoido-mc/conditional-riding/blob/main/LICENSE.
 */

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
